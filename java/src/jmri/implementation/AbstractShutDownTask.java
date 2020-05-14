package jmri.implementation;

import java.beans.PropertyChangeEvent;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import jmri.ShutDownTask;

/**
 * Abstract ShutDownTask implementation.
 * <p>
 * This implementation provides a "doRun" property with a protected getter and
 * setter to allow subclasses to set the "doRun" property to true inside
 * {@link #call()} so that the property can be checked inside {@link #run()} to
 * determine if anything should be done during shut down.
 *
 * @author Bob Jacobsen Copyright (C) 2008
 * @author Randall Wood Copyright 2020
 */
public abstract class AbstractShutDownTask implements ShutDownTask {

    private final String mName;
    private boolean doRun = false;

    /**
     * Constructor specifies the name
     *
     * @param name Name to give this task
     */
    public AbstractShutDownTask(String name) {
        this.mName = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean isShutdownAllowed() {
        return call();
    }

    /**
     * {@inheritDoc}
     * 
     * This implementation merely sets the "doRun" property to true, and should
     * be overridden for any real checking. Note that overriding implementations
     * should call {@link #setDoRun(boolean)} correctly.
     */
    @Override
    public Boolean call() {
        doRun = true;
        return doRun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("deprecation")
    public final boolean execute() {
        run();
        return true;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isParallel() {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isComplete() {
        return !this.isParallel();
    }
    
    /**
     * {@inheritDoc}
     * 
     * Note that overriding implementations should call this implementation to set
     * the doRun property correctly.
     */
    @OverridingMethodsMustInvokeSuper
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("shuttingDown".equals(evt.getPropertyName()) && Boolean.FALSE.equals(evt.getNewValue())) {
            doRun = false;
        }
    }
    
    /**
     * Check if action should be taken in {@link #run()} method.
     * @return 
     */
    public boolean isDoRun() {
        return doRun;
    }

    public void setDoRun(boolean flag) {
        doRun = flag;
    }
}
