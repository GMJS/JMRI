package jmri.jmrix.easydcc;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import jmri.ProgrammingMode;
import jmri.jmrix.AbstractProgrammer;

/**
 * Implements the jmri.Programmer interface via commands for the EasyDCC
 * powerstation.
 *
 * @author Bob Jacobsen Copyright (C) 2001
 */
public class EasyDccProgrammer extends AbstractProgrammer implements EasyDccListener {

    public EasyDccProgrammer(EasyDccSystemConnectionMemo memo) {
        tc = memo.getTrafficController();
        // need a longer LONG_TIMEOUT
        LONG_TIMEOUT = 180000;
    }

    protected EasyDccTrafficController tc = null;

    /** 
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public List<ProgrammingMode> getSupportedModes() {
        List<ProgrammingMode> ret = new ArrayList<ProgrammingMode>();
        ret.add(ProgrammingMode.PAGEMODE);
        ret.add(ProgrammingMode.REGISTERMODE);
        return ret;
    }

    // members for handling the programmer interface
    int progState = 0;
    static final int NOTPROGRAMMING = 0;// is notProgramming
    static final int COMMANDSENT = 2;  // read/write command sent, waiting reply
    boolean _progRead = false;
    int _val; // remember the value being read/written for confirmative reply
    int _cv; // remember the cv being read/written

    /** 
     * {@inheritDoc}
     */
    @Override
    public synchronized void writeCV(String CVname, int val, jmri.ProgListener p) throws jmri.ProgrammerException {
        final int CV = Integer.parseInt(CVname);
        if (log.isDebugEnabled()) {
            log.debug("writeCV {} listens {}", CV, p);
        }
        useProgrammer(p);
        _progRead = false;
        // set commandPending state
        progState = COMMANDSENT;
        _val = val;
        _cv = CV;

        try {
            // start the error timer
            startLongTimer();

            // format and send the write message
            tc.sendEasyDccMessage(progTaskStart(getMode(), _val, _cv), this);
        } catch (jmri.ProgrammerException e) {
            progState = NOTPROGRAMMING;
            throw e;
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public synchronized void confirmCV(String CV, int val, jmri.ProgListener p) throws jmri.ProgrammerException {
        readCV(CV, p);
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public synchronized void readCV(String CVname, jmri.ProgListener p) throws jmri.ProgrammerException {
        final int CV = Integer.parseInt(CVname);
        if (log.isDebugEnabled()) {
            log.debug("readCV {} listens {}", CV, p);
        }
        useProgrammer(p);
        _progRead = true;

        progState = COMMANDSENT;
        _cv = CV;

        try {
            // start the error timer
            startLongTimer();

            // format and send the write message
            tc.sendEasyDccMessage(progTaskStart(getMode(), -1, _cv), this);
        } catch (jmri.ProgrammerException e) {
            progState = NOTPROGRAMMING;
            throw e;
        }

    }

    private jmri.ProgListener _usingProgrammer = null;

    // internal method to remember who's using the programmer
    protected void useProgrammer(jmri.ProgListener p) throws jmri.ProgrammerException {
        // test for only one!
        if (_usingProgrammer != null && _usingProgrammer != p) {
            if (log.isDebugEnabled()) {
                log.debug("programmer already in use by {}", _usingProgrammer);
            }
            throw new jmri.ProgrammerException("programmer in use");
        } else {
            _usingProgrammer = p;
            return;
        }
    }

    /**
     * Internal method to create the EasyDccMessage for programmer task start.
     * @param mode Programming mode to iniate
     * @param val Value to program
     * @param cvnum CV number to address
     * @return formatted message for layout
     * @throws jmri.ProgrammerException if programmer or mode not available
     */
    protected EasyDccMessage progTaskStart(ProgrammingMode mode, int val, int cvnum) throws jmri.ProgrammerException {
        // val = -1 for read command; mode is direct, etc
        if (val < 0) {
            // read
            if (getMode().equals(ProgrammingMode.PAGEMODE)) {
                return EasyDccMessage.getReadPagedCV(cvnum);
            } else {
                return EasyDccMessage.getReadRegister(registerFromCV(cvnum));
            }
        } else {
            // write
            if (getMode().equals(ProgrammingMode.PAGEMODE)) {
                return EasyDccMessage.getWritePagedCV(cvnum, val);
            } else {
                return EasyDccMessage.getWriteRegister(registerFromCV(cvnum), val);
            }
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void message(EasyDccMessage m) {
        log.error("message received unexpectedly: {}", m.toString());
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    synchronized public void reply(EasyDccReply m) {
        if (progState == NOTPROGRAMMING) {
            // we get the complete set of replies now, so ignore these
            if (log.isDebugEnabled()) {
                log.debug("reply in NOTPROGRAMMING state");
            }
            return;
        } else if (progState == COMMANDSENT) {
            if (log.isDebugEnabled()) {
                log.debug("reply in COMMANDSENT state");
            }
            // operation done, capture result, then have to leave programming mode
            progState = NOTPROGRAMMING;
            // check for errors
            if (m.match("--") >= 0) {
                if (log.isDebugEnabled()) {
                    log.debug("handle error reply {}", m);
                }
                // perhaps no loco present? Fail back to end of programming
                notifyProgListenerEnd(-1, jmri.ProgListener.NoLocoDetected);
            } else {
                // see why waiting
                if (_progRead) {
                    // read was in progress - get return value
                    _val = m.value();
                }
                // if this was a read, we retreived the value above.  If its a
                // write, we're to return the original write value
                notifyProgListenerEnd(_val, jmri.ProgListener.OK);
            }
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    synchronized protected void timeout() {
        if (progState != NOTPROGRAMMING) {
            // we're programming, time to stop
            if (log.isDebugEnabled()) {
                log.debug("timeout!");
            }
            // perhaps no loco present? Fail back to end of programming
            progState = NOTPROGRAMMING;
            cleanup();
            notifyProgListenerEnd(_val, jmri.ProgListener.FailedTimeout);
        }
    }

    /**
     * Internal method to send a cleanup message (if needed) on timeout.
     * <p>
     * Here, it sends a request to exit from programming mode. But subclasses,
     * e.g. ops mode, may redefine that.
     */
    void cleanup() {
        tc.sendEasyDccMessage(EasyDccMessage.getExitProgMode(), this);
    }

    // internal method to notify of the final result
    protected void notifyProgListenerEnd(int value, int status) {
        if (log.isDebugEnabled()) {
            log.debug("notifyProgListenerEnd value {} status {}", value, status);
        }
        // the programmingOpReply handler might send an immediate reply, so
        // clear the current listener _first_
        jmri.ProgListener temp = _usingProgrammer;
        _usingProgrammer = null;
        notifyProgListenerEnd(temp,value,status);
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EasyDccProgrammer.class);

}
