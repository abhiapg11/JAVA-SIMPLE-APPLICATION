package tomasz.jokiel.worktimer;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowCloseListener implements WindowListener {

    private final OnWindowCloseListener mOnWindowCloseListener;
    
    public WindowCloseListener(OnWindowCloseListener onWindowCloseListener) {
        mOnWindowCloseListener = onWindowCloseListener;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("windowClosing");
        mOnWindowCloseListener.onWindowClose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public interface OnWindowCloseListener {
        public void onWindowClose();
    }
}
