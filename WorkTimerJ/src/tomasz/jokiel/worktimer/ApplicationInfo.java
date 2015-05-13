package tomasz.jokiel.worktimer;

public class ApplicationInfo {
    public static final String APPLICATION_NAME = "WorkTimerJ";
    public static final String APPLICATION_VERSION = "0.10.0";

    public static String getApplicationHeader() {
        return APPLICATION_NAME + " v" + APPLICATION_VERSION;
    }
}
