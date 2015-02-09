package tomasz.jokiel.worktimer;

import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import tomasz.jokiel.worktimer.DayliTimer.OnCounterValueChangedListener;
import tomasz.jokiel.worktimer.TimeUnitBlocksContainer.OnAddRemoveTimeUnitBlockListener;

public class DayliTimerTaskGroup {
    private static final int MINIMAL_TIME_UNIT_BLOCK_IN_SECONDS = TimeUnitBlocksContainer.FIVE_MINUTES_IN_SECONDS;

    private JRadioButton mRadioButton; 
    private JTextField   mTaskDescription; 
    private JTextPane    mDayliTimerValue;
    private OnUpdateDayliTimerValueListener mOnUpdateDayliTimerValueListener;

    final private OnCounterValueChangedListener mOnTimerRepeatListener = new OnCounterValueChangedListener() {
        @Override
        public void onCounterValueChanged(int counterValue) {
            if(mDayliTimerValue != null) {
                updateDayliTimerValue();
            }

            notifyTimerValueChanged();
            if(isFiveMinutesMultiply(counterValue)) {
                notifyFiveMinutesMultiplyExpires();
            }
        }

        @Override
        public void onCounterManuallyChanged(int counterValue) {
            if(mDayliTimerValue != null) {
                updateDayliTimerValue();
            }
        }
    };

    private OnAddRemoveTimeUnitBlockListener mOnAddRemoveTimeUnitBlockListener = new OnAddRemoveTimeUnitBlockListener() {

        @Override
        public void onRemovedTimeUnitBlock(TimeUnitBlock removedTimeUnitBlock) {
            mDayliTimer.addToCounterValue(-removedTimeUnitBlock.getValue());
        }

        @Override
        public void onAddedTimeUnitBlock(TimeUnitBlock addedTimeUnitBlock) {
            if(isTimeUnitBlockAddedManuallyToContainer()) {
                mDayliTimer.addToCounterValue(addedTimeUnitBlock.getValue());
            }
        }
    };
    private String mName;
    protected DayliTimer mDayliTimer = new DayliTimer(mOnTimerRepeatListener);
    protected TimeUnitBlocksContainer mTimeUnitBlocksContainer;


    public DayliTimerTaskGroup(OnUpdateDayliTimerValueListener onOnUpdateDayliTimerValueListener) {
        mOnUpdateDayliTimerValueListener =  onOnUpdateDayliTimerValueListener;
    }

    public DayliTimerTaskGroup(OnUpdateDayliTimerValueListener onOnUpdateDayliTimerValueListener, String name) {
        this(onOnUpdateDayliTimerValueListener);
        mName = name;
        mDayliTimer = new DayliTimer(mOnTimerRepeatListener, name);
    }

    public JRadioButton getRadioButton() {
        return mRadioButton;
    }

    public void setRadioButton(JRadioButton radioButton) {
        mRadioButton = radioButton;
    }

    public JTextField getTaskDescriptionJTextField() {
        return mTaskDescription;
    }

    public void setTaskDescriptionJTextField(JTextField taskDescription) {
        mTaskDescription = taskDescription;
    }

    public JTextPane getDayliTimerValueJTextPane() {
        return mDayliTimerValue;
    }

    public void setDayliTimerValueJTextPane(JTextPane dayliTimerValue) {
        mDayliTimerValue = dayliTimerValue;
    }

    public DayliTimer getDayliTimer() {
        return mDayliTimer;
    }

    public TimeUnitBlocksContainer getTimeUnitBlocksContainer() {
        return mTimeUnitBlocksContainer;
    }

    public void setTimeUnitBlocksContainer(TimeUnitBlocksContainer timeUnitBlocksContainer) {
        mTimeUnitBlocksContainer = timeUnitBlocksContainer;
        mTimeUnitBlocksContainer.setOnAddRemoveTimeUnitBlockListener(mOnAddRemoveTimeUnitBlockListener);
    }

    public void reset() {
        mDayliTimer.reset();
        updateDayliTimerValue();
    }

    protected boolean isTimeUnitBlockAddedManuallyToContainer() {
        return mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds() > mDayliTimer.getCounterValue();
    }

    private void updateDayliTimerValue() {
        if(mDayliTimerValue != null) {
            int counterValue = mDayliTimer.getCounterValue();
            mDayliTimerValue.setText(Utils.formatTimerFromSeconds(counterValue, "", "-"));
        }
    }

    private void notifyFiveMinutesMultiplyExpires() {
        if (mOnUpdateDayliTimerValueListener != null) {
            mOnUpdateDayliTimerValueListener.onUpdateDayliTimerValueByFiveMinutes(mTimeUnitBlocksContainer);
        }
    }

    private void notifyTimerValueChanged() {
        if (mOnUpdateDayliTimerValueListener != null) {
            mOnUpdateDayliTimerValueListener.onUpdateDayliTimerValue();
        }
    }

    private boolean isFiveMinutesMultiply(int counterValue) {
        return counterValue % 300 == 0;
    }

    public interface OnUpdateDayliTimerValueListener {
        public void onUpdateDayliTimerValueByFiveMinutes(TimeUnitBlocksContainer timeUnitBlocksContainer);
        public void onUpdateDayliTimerValue();
    }
    
    @Override
    public String toString() {
        return super.toString() + ", name: " + mName;
    }
}
