package tomasz.jokiel.worktimer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getCurrentDateFormatted() {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

    public static String formatTimerFromSeconds(int timeInSeconds) {
        return formatTimerFromSeconds(timeInSeconds, "+", "-");
    }

    public static String formatTimerFromSeconds(int timeInSeconds, String plusSign, String minusSign) {
        String sign = (timeInSeconds >= 0) ? plusSign : minusSign;

        timeInSeconds = Math.abs(timeInSeconds);

        int hours   = getHoursFromSecondsCounter(timeInSeconds);
        int minutes = getMinuterFromSecondsCounter(timeInSeconds);
        int seconds = getSecondsFromSecondsCounter(timeInSeconds);

        return String.format("%s%02d:%02d:%02d", sign, hours, minutes, seconds);
    }

    private static int getHoursFromSecondsCounter(int counterValue) {
        return counterValue / (60 * 60);
    }

    private static int getMinuterFromSecondsCounter(int counterValue) {
        return (counterValue % (60 * 60)) / 60;
    }

    private static int getSecondsFromSecondsCounter(int counterValue) {
        return counterValue % 60;
    }

}
