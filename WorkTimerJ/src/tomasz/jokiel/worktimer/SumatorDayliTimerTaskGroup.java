package tomasz.jokiel.worktimer;


public class SumatorDayliTimerTaskGroup extends DayliTimerTaskGroup{

    private static final int EIGHT_HOURS_IN_SECONDS = 8 * 60 * 60;

    public SumatorDayliTimerTaskGroup(OnUpdateDayliTimerValueListener onOnUpdateDayliTimerValueListener) {
        super(onOnUpdateDayliTimerValueListener, "sumator");
    }

    @Override
    protected boolean isTimeUnitBlockAddedManuallyToContainer() {
        return mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds() > (EIGHT_HOURS_IN_SECONDS + mDayliTimer.getCounterValue());
    }
}
