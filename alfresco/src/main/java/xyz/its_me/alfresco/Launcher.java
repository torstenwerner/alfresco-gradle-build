package xyz.its_me.alfresco;

import xyz.its_me.TomcatLauncher;

public class Launcher {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new RuntimeException("need 1 argument: docBase");
        }
        System.out.printf("docBase: %s\n", args[0]);
        System.setProperty("tomcat.util.scan.StandardJarScanFilter.jarsToSkip", "*.jar");

        TomcatLauncher.start(8080, "/alfresco", args[0]);
        TomcatLauncher.waitforever();
    }
}
