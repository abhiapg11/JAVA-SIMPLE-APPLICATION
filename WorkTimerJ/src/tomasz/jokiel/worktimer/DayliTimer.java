package tomasz.jokiel.worktimer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class DayliTimer implements ActionListener {

    private static final int REPEAT_TIME = 1000;

    private int mCounterValue;
    private Timer mTimer;
    private OnCounterValueChangedListener mOnCounterValueChangedListener;
    private String mName;

    public DayliTimer(OnCounterValueChangedListener listener) {
        mOnCounterValueChangedListener = listener;
        mTimer = new Timer(REPEAT_TIME, this);
        mTimer.setInitialDelay(REPEAT_TIME);
    }

    public DayliTimer(OnCounterValueChangedListener listener, String name) {
        this(listener);
        mName = name;
    }

    public void start() {
        System.out.println(hashCode() +" start: " + mCounterValue);
        mTimer.start();
    }

    public void pause() {
        System.out.println(hashCode() +" stop: " + mCounterValue);
        mTimer.stop();
    }

    public void reset() {
        System.out.println(hashCode() +" reset: " + mCounterValue);
        mTimer.stop();
        mCounterValue = 0;
    }
    
    public int getCounterValue() {
        System.out.println(hashCode() +" getCounterValue: " + mCounterValue);
        return mCounterValue;
    }

    synchronized public void addToCounterValue(int value) {
        mCounterValue += value;
        System.out.println(hashCode() +" addToCounterValue: " + value + ", counter after: " + mCounterValue);
        notyfyCounerValueChangedManually();
    }

    @Override
    synchronized public void actionPerformed(ActionEvent e) {
        mCounterValue++;
        System.out.println(hashCode() +" actionPerformed: " + mCounterValue);
        notyfyCounerValueChanged();
    }

    private void notyfyCounerValueChangedManually() {
        if(mOnCounterValueChangedListener != null) {
            mOnCounterValueChangedListener.onCounterManuallyChanged(mCounterValue);
        }
    }

    private void notyfyCounerValueChanged() {
        if(mOnCounterValueChangedListener != null) {
            mOnCounterValueChangedListener.onCounterValueChanged(mCounterValue);
        }
    }
    
    public interface OnCounterValueChangedListener {
        public void onCounterValueChanged(int counterValue);
        public void onCounterManuallyChanged(int counterValue);
    }
    
    @Override
    public String toString() {
        return super.toString() + ", name: " + mName + ", counter calue: " + getCounterValue();
    }
}
