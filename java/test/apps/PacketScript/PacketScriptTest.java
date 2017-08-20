package apps.PacketScript;

import java.awt.GraphicsEnvironment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 */
public class PacketScriptTest {

    @Test
    @Ignore("Causes Exception")
    public void testCTor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        PacketScript t = new PacketScript();
        Assert.assertNotNull("exists", t);
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        apps.tests.Log4JFixture.setUp();
        jmri.util.JUnitUtil.resetInstanceManager();
    }

    @After
    public void tearDown() {
        jmri.util.JUnitUtil.resetInstanceManager();
        apps.tests.Log4JFixture.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(PacketScriptTest.class.getName());
}
