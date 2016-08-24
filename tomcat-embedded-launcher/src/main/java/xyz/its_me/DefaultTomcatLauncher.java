package xyz.its_me;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DefaultTomcatLauncher implements TomcatLauncher {
    private final int port;
    private final String contextPath;
    private final String docBase;
    private StandardContext standardContext;

    DefaultTomcatLauncher(int port, String contextPath, String docBase) {
        this.port = port;
        this.contextPath = contextPath;
        this.docBase = docBase;
    }

    private final Tomcat tomcat = new Tomcat();
    private final CompletableFuture<Void> startFuture = new CompletableFuture<>();
    private final CompletableFuture<Void> stopFuture = new CompletableFuture<>();

    @Override
    public void triggerStart() {
        try {
            final Path basePath = Files.createTempDirectory("tomcat-base-dir");
            System.out.printf("basePath: %s\n", basePath);
            Files.createDirectory(basePath.resolve("webapps"));
            tomcat.setBaseDir(basePath.toString());

            tomcat.setPort(port);

            standardContext = (StandardContext) tomcat.addWebapp(contextPath, docBase);

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

    @Override
    public void waitForStart() {
        wait(startFuture);
    }

    @Override
    public void triggerStop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new RuntimeException("failed", e);
        }
    }

    @Override
    public void waitForStop() {
        wait(stopFuture);
    }

    @Override
    public ServletContext getServletContext() {
        return standardContext.getServletContext();
    }

    private void wait(CompletableFuture<Void> future) {
        try {
            future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException("failed", e);
        }
    }
}
