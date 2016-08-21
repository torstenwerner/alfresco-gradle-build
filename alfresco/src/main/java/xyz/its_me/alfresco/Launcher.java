package xyz.its_me.alfresco;

import xyz.its_me.TomcatLauncher;

public class Launcher {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new RuntimeException("need exactly 1 argument: docBase");
        }
        final String docBase = args[0];
        System.out.printf("docBase: %s\n", docBase);
        System.setProperty("tomcat.util.scan.StandardJarScanFilter.jarsToSkip", "*.jar");

        final TomcatLauncher launcher = TomcatLauncher.create(8080, "/alfresco", docBase);
        launcher.start();
        launcher.waitForStop();
    }
}
