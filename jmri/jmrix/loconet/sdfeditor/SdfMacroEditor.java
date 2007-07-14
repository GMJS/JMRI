// SdfMacroEditor.java

package jmri.jmrix.loconet.sdfeditor;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JLabel;

import jmri.jmrix.loconet.sdf.*;

/**
 * Common base for all the SDF macro editors.
 *
 * @author		Bob Jacobsen  Copyright (C) 2007
 * @version             $Revision: 1.2 $
 */

public abstract class SdfMacroEditor extends JPanel {

    public SdfMacroEditor(SdfMacro inst) {
        this.inst = inst;
        add(new JLabel("test default"));
    }
    
    SdfMacro inst;
    
    public String toString() {
        return inst.toString();
    }
    
    public String oneInstructionString() {
        return inst.oneInstructionString();
    }
    
    public String allInstructionString(String indent) {
        return inst.allInstructionString(indent);
    }
    
    /**
     * Return an editor object for a SdfMacro type.
     */
    static public SdfMacroEditor attachEditor(SdfMacro inst) {

            // full 1st byte decoder
            if (inst instanceof ChannelStart) return new ChannelStartEditor(inst); else
            if (inst instanceof SdlVersion) return new SdlVersionEditor(inst); else
            if (inst instanceof SkemeStart) return new SkemeStartEditor(inst); else
            if (inst instanceof GenerateTrigger) return new GenerateTriggerEditor(inst); else
            if (inst instanceof EndSound) return new EndSoundEditor(inst); else

            // 7 bit decode
            if (inst instanceof DelaySound) return new DelaySoundEditor(inst); else
                            
            // 6 bit decode
            if (inst instanceof SkipOnTrigger) return new SkipOnTriggerEditor(inst); else
            
            // 5 bit decode
            if (inst instanceof InitiateSound) return new InitiateSoundEditor(inst); else
            if (inst instanceof MaskCompare) return new MaskCompareEditor(inst); else
            
            // 4 bit decode
            if (inst instanceof LoadModifier) return new LoadModifierEditor(inst); else
            if (inst instanceof BranchTo) return new BranchToEditor(inst); else

            // 2 bit decode
            if (inst instanceof Play) return new PlayEditor(inst); else
            
            // generics
            if (inst instanceof FourByteMacro) return new FourByteMacroEditor(inst); else
            if (inst instanceof TwoByteMacro) return new TwoByteMacroEditor(inst);
            
            log.error("PANIC");
            return null;
    }

    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(SdfMacroEditor.class.getName());

}
/* @(#)SdfMacroEditor.java */
