package xyz.its_me.alfresco;

import xyz.its_me.TomcatLauncher;

import java.util.Arrays;

public class Launcher {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new RuntimeException("need at least 1 argument: docBase preResources...");
        }
        final String docBase = args[0];
        System.out.printf("docBase: %s\n", docBase);
        System.setProperty("tomcat.util.scan.StandardJarScanFilter.jarsToSkip", "*.jar");

        final String[] preRessources = Arrays.copyOfRange(args, 1, args.length);
        TomcatLauncher.start(8080, "/alfresco", docBase, preRessources);
        TomcatLauncher.waitforever();
    }
}
