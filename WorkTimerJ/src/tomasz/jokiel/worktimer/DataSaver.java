package tomasz.jokiel.worktimer;

import java.util.ArrayList;

public interface DataSaver {
    void storeTimersValues(ArrayList<DayliTimerTaskGroup> dayliTimerTaskGroupContainerList, DayliTimerTaskGroup sumatrDayliTimerTaskGroupContainer);
    void storeReleasedTimersValues(boolean isEndOfWeek, boolean isEndOfMonth, ArrayList<DayliTimerTaskGroup> dayliTimerTaskGroupContainerList, DayliTimerTaskGroup sumatrDayliTimerTaskGroupContainer);
}
