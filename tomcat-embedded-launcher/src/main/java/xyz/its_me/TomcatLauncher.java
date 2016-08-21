package xyz.its_me;

public interface TomcatLauncher {
    static TomcatLauncher create(int port, String contextPath, String docBase) {
        return new DefaultTomcatLauncher(port, contextPath, docBase);
    }

    static TomcatLauncher create() {
        final int port = Integer.parseInt(System.getProperty("tomcat.port", "8080"));
        final String contextPath = System.getProperty("tomcat.context-path", "/");
        final String docBase = System.getProperty("tomcat.doc-base", "src/main/webapp");
        return create(port, contextPath, docBase);
    }

    void start();

    void waitForStart();

    void stop();

    void waitForStop();
}
