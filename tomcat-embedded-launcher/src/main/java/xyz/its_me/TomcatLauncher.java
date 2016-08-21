package xyz.its_me;

public interface TomcatLauncher {
    static TomcatLauncher create(int port, String contextPath, String docBase) {
        return new DefaultTomcatLauncher(port, contextPath, docBase);
    }

    void start();

    void waitForStart();

    void stop();

    void waitForStop();
}
