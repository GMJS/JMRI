package jmri.jmrit.display;

import jmri.*;
import jmri.util.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;

import junit.framework.*;
import junit.extensions.jfcunit.*;
import junit.extensions.jfcunit.finder.*;

/**
 * PositionableLabelTest.java
 *
 * Description:
 *
 * @author	Bob Jacobsen
 * @version	$Revision$
 */
public class PositionableLabelTest extends jmri.util.SwingTestCase {

    PositionableLabel to = null;
    jmri.jmrit.display.panelEditor.PanelEditor panel
            = new jmri.jmrit.display.panelEditor.PanelEditor("PositionableLabel Test Panel");

    public void testShow() {
        JFrame jf = new JFrame();
        JPanel p = new JPanel();
        jf.getContentPane().add(p);
        p.setPreferredSize(new Dimension(200, 200));
        p.setLayout(null);

        // test button in upper left
        JButton whereButton = new JButton("where");
        whereButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                whereButtonPushed();
            }
        });
        whereButton.setBounds(0, 0, 70, 40);
        p.add(whereButton);

        to = new PositionableLabel("here", panel);
        to.setBounds(80, 80, 40, 40);
        panel.putItem(to);
        to.setDisplayLevel(jmri.jmrit.display.Editor.LABELS);
        assertEquals("Display Level ", to.getDisplayLevel(), jmri.jmrit.display.Editor.LABELS);

        p.add(to);
        panel.addLabel("There");

        jf.pack();
        jf.setVisible(true);
    }

    // animate the visible frame
    public void whereButtonPushed() {
    }
    
    // Load labels with backgrounds and make sure they have right color
    // The file used was written with 4.0.1, and behaves as expected from panel names
    public void testBackgroundColors() throws Exception {
        if (!System.getProperty("jmri.headlesstest","false").equals("false")) {
            return;
        }
        // make four windows
        InstanceManager.configureManagerInstance()
                .load(new java.io.File("java/test/jmri/jmrit/display/configurexml/verify/backgrounds.xml"));
        flushAWT();

        // Find color in label by frame name
        checkColor("F Bkg none, label Bkg none", 0xffffffff); // white background

        checkColor("F Bkg blue, label Bkg none", 0xff0000ff); // white background

        checkColor("F Bkg none, label Bkg yellow", 0xffffff00); // yellow

        checkColor("F Bkg blue, label Bkg yellow", 0xffffff00); // yellow

    }
    
    void checkColor(String name, int value) {

        flushAWT();

        // Find window by name
        JmriJFrame ft = JmriJFrame.getFrame(name);
        Assert.assertNotNull("frame: "+name, ft);
        
        // find label within that
        JLabelFinder finder = new JLabelFinder("....");
        java.util.List list = finder.findAll(ft);
        Assert.assertNotNull("list: "+name, list);
        Assert.assertTrue("length: "+name+": "+list.size(), list.size()>0);
        
        // find a point in upper left of label
        Point p = SwingUtilities.convertPoint(((JComponent)list.get(0)),1,1,ft);
        
        // check pixel color (from http://stackoverflow.com/questions/13307962/how-to-get-the-color-of-a-point-in-a-jpanel )
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = image.createGraphics();
        ft.paint(g2);
        
        int color = image.getRGB(p.x,p.y);

        g2.dispose();
        
        // test for match
        Assert.assertEquals(name, String.format("0x%8s", Integer.toHexString(value)).replace(' ', '0'),
                                  String.format("0x%8s", Integer.toHexString(color)).replace(' ', '0'));
        
        // Ask to close table window
        ft.setVisible(false);
        //TestHelper.disposeWindow(ft, this);
        return;
    }
    
    // from here down is testing infrastructure

    public PositionableLabelTest(String s) {
        super(s);
    }

    // Main entry point
    static public void main(String[] args) {
        String[] testCaseName = {"-noloading", PositionableLabelTest.class.getName()};
        junit.swingui.TestRunner.main(testCaseName);
    }

    // test suite from all defined tests
    public static Test suite() {
        TestSuite suite = new TestSuite(PositionableLabelTest.class);
        return suite;
    }

    // The minimal setup for log4J
    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    protected void tearDown() {
        // now close panel window
        java.awt.event.WindowListener[] listeners = panel.getTargetFrame().getWindowListeners();
        for (int i = 0; i < listeners.length; i++) {
            panel.getTargetFrame().removeWindowListener(listeners[i]);
        }
        junit.extensions.jfcunit.TestHelper.disposeWindow(panel.getTargetFrame(), this);
        apps.tests.Log4JFixture.tearDown();
    }

	// static private Logger log = LoggerFactory.getLogger(TurnoutIconTest.class.getName());
}
