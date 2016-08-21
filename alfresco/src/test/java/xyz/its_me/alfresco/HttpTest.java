package xyz.its_me.alfresco;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import xyz.its_me.TomcatLauncher;

public class HttpTest {
    private static final TomcatLauncher launcher;

    static {
        // The test port should be a little bit random to avoid conflicts with other programs.
        System.setProperty("tomcat.port", "9274");
        launcher = TomcatLauncher.create();
    }

    @BeforeClass
    public static void setupClass() {
        launcher.start();
        launcher.waitForStart();
    }

    @AfterClass
    public static void teardownClass() {
        launcher.stop();
        launcher.waitForStop();
    }

    @Test
    public void serverIsUp() throws Exception {

    }
}
