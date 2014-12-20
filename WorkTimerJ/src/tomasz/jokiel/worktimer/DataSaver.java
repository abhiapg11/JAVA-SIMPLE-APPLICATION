package tomasz.jokiel.worktimer;

import java.util.ArrayList;

public interface DataSaver {
    void storeReleasedTimersValues(boolean isEndOfWeek, boolean isEndOfMonth, ArrayList<DayliTimerTaskGroup> dayliTimerTaskGroupContainerList, DayliTimerTaskGroup mSumatrDayliTimerTaskGroupContainer);
}
