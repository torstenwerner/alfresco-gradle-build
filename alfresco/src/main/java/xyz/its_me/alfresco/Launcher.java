package xyz.its_me.alfresco;

import xyz.its_me.TomcatLauncher;

public class Launcher {
    public static void main(String[] args) throws Exception {
        final TomcatLauncher launcher = TomcatLauncher.create();
        launcher.start();
        launcher.waitForStop();
    }
}
