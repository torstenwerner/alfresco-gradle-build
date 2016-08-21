package xyz.its_me;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TomcatLauncher {

    private static final Tomcat tomcat = new Tomcat();
    private static final CompletableFuture<Void> startFuture = new CompletableFuture<>();
    private static final CompletableFuture<Void> stopFuture = new CompletableFuture<>();

    public static void start(int port, String contextPath, String docBase) {
        try {
            final Path basePath = Files.createTempDirectory("tomcat-base-dir");
            System.out.printf("basePath: %s\n", basePath);
            Files.createDirectory(basePath.resolve("webapps"));
            tomcat.setBaseDir(basePath.toString());

            tomcat.setPort(port);

            final StandardContext ctx = (StandardContext) tomcat.addWebapp(contextPath, docBase);

            // does not work, sorry
            ctx.setReloadable(true);

            tomcat.getServer().addLifecycleListener((event) -> {
                if ("after_start".equals(event.getType())) {
                    startFuture.complete(null);
                } else if ("after_stop".equals(event.getType())) {
                    stopFuture.complete(null);
                }
            });

            tomcat.start();
        } catch (IOException | LifecycleException | ServletException e) {
            throw new RuntimeException("failed", e);
        }
    }

    public static void waitForStart() {
        wait(startFuture);
    }

    public static void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new RuntimeException("failed", e);
        }
    }

    public static void waitForStop() {
        wait(stopFuture);
    }

    private static void wait(CompletableFuture<Void> future) {
        try {
            future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException("failed", e);
        }
    }

    public static void waitforever() {
        tomcat.getServer().await();
    }
}
