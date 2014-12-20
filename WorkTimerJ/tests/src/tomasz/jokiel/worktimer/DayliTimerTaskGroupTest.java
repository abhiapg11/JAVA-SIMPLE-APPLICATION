package tomasz.jokiel.worktimer;

import static org.junit.Assert.*;

import javax.swing.JTextPane;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DayliTimerTaskGroupTest {
    DayliTimerTaskGroup mDayliTimerTaskGroup;

    @Before
    public void setUp() throws Exception {
        mDayliTimerTaskGroup = new DayliTimerTaskGroup(null);
        mDayliTimerTaskGroup.setDayliTimerValueJTextPane(new JTextPane());
    }

    @After
    public void tearDown() throws Exception {
        mDayliTimerTaskGroup.reset();
    }

    @Test (expected = RuntimeException.class)
    public void testExpectedException() {
        throw new NullPointerException();
    }

    @Test
    public void testCallback() {
        // given
        final String expectedTimerText = "00:00:01";
        
        //when
        mDayliTimerTaskGroup.getDayliTimer().start();
        waitAfterStartTimer();
        String text = mDayliTimerTaskGroup.getDayliTimerValueJTextPane().getText();
        
        // then
        assertEquals(expectedTimerText, text);
    }

    private void waitAfterStartTimer() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
