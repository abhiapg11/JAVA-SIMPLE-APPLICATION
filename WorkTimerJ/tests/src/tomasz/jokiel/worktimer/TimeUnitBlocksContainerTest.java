package tomasz.jokiel.worktimer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tomasz.jokiel.worktimer.TimeUnitBlocksContainer.OnConvertAddRemoveTimeUnitBlockListener;

public class TimeUnitBlocksContainerTest {
    private final TimeUnitBlock mFiveMinuteTimeUnitBlock  = new TimeUnitBlock(TimeUnitBlocksContainer.FIVE_MINUTES_IN_SECONDS);
    private final TimeUnitBlock mTenMinutesTimeUnitBlock  = new TimeUnitBlock(TimeUnitBlocksContainer.TEN_MINUTES_IN_SECONDS);
    private final TimeUnitBlock mHalfHourTimeUnitBlock    = new TimeUnitBlock(TimeUnitBlocksContainer.HALF_HOUR_IN_SECONDS);
    private final TimeUnitBlock mOneHourTimeUnitBlock     = new TimeUnitBlock(TimeUnitBlocksContainer.ONE_HOUR_MINUTES_IN_SECONDS);
    private final TimeUnitBlock mTwoHoursTimeUnitBlock     = new TimeUnitBlock(TimeUnitBlocksContainer.TWO_HOURS_MINUTES_IN_SECONDS);

    TimeUnitBlocksContainer mTimeUnitBlocksContainer;
    private OnConvertAddRemoveTimeUnitBlockListener mAddRemoveTimeUnitBlockListener = new OnConvertAddRemoveTimeUnitBlockListener() {

        @Override
        public void onAfterRemovedTimeUnitBlockFromContainer(TimeUnitBlock timeUnitBlock) { }

        @Override
        public void onAddFiveMinutesTimeUnitBlockRequested(TimeUnitBlocksContainer timeUnitBlocksContainer) {
            mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock.clone());
        }

        @Override
        public void onAddTenMinutesTimeUnitBlockRequested(TimeUnitBlocksContainer timeUnitBlocksContainer) {
            mTimeUnitBlocksContainer.addTimeUnitBlock(mTenMinutesTimeUnitBlock.clone());
        }

        @Override
        public void onAddHalfHourTimeUnitBlockRequested(TimeUnitBlocksContainer timeUnitBlocksContainer) {
            mTimeUnitBlocksContainer.addTimeUnitBlock(mHalfHourTimeUnitBlock.clone());
        }

        @Override
        public void onAddOneHourTimeUnitBlockRequested(TimeUnitBlocksContainer timeUnitBlocksContainer) {
            mTimeUnitBlocksContainer.addTimeUnitBlock(mOneHourTimeUnitBlock.clone());
        }

        @Override
        public void onAddTwoHoursTimeUnitBlockRequested(
                TimeUnitBlocksContainer timeUnitBlocksContainer) {
            mTimeUnitBlocksContainer.addTimeUnitBlock(mTwoHoursTimeUnitBlock.clone());
        }
    };


    @Before
    public void setUp() throws Exception {
        mTimeUnitBlocksContainer = new TimeUnitBlocksContainer();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAutoConvertTimeUnitBlocksFromSmallerToBigger() {
        // given
        final int fiveMinutesIntervalsCount = 3 * (TimeUnitBlocksContainer.ONE_HOUR_MINUTES_IN_SECONDS / TimeUnitBlocksContainer.FIVE_MINUTES_IN_SECONDS)
                + 9;
        mTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);

        // when
        for (int i = 0; i < fiveMinutesIntervalsCount; i++) {
            mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock.clone());
        }

        mTimeUnitBlocksContainer.convertTimeUnitBlocksFromSmallerToBigger();

        // then
        assertEquals(fiveMinutesIntervalsCount * mFiveMinuteTimeUnitBlock.getValue(), 
                     mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
        boolean isAfterConvertLessTimeUnitBlocksThanBefore = mTimeUnitBlocksContainer.getAllTimeUnitBlocks().size() < fiveMinutesIntervalsCount;
        assertTrue(isAfterConvertLessTimeUnitBlocksThanBefore);
    }

    @Test
    public void testMultipleAddSameTimeUnitBlocks() {
        // given

        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock);

        // then
        assertEquals(mFiveMinuteTimeUnitBlock.getValue(), mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }

    @Test
    public void testAddMultipleCloneOfSameTimeUnitBlocks() {
        // given
        TimeUnitBlock firstCloneTimeUnitBlock  = mFiveMinuteTimeUnitBlock.clone();
        TimeUnitBlock secondCloneTimeUnitBlock = mFiveMinuteTimeUnitBlock.clone();
        TimeUnitBlock thirdCloneTimeUnitBlock  = mFiveMinuteTimeUnitBlock.clone();

        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(firstCloneTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(secondCloneTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(thirdCloneTimeUnitBlock);

        // then
        assertEquals(firstCloneTimeUnitBlock.getValue() + secondCloneTimeUnitBlock.getValue() + thirdCloneTimeUnitBlock.getValue(), 
                     mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }

    @Test
    public void testSummingTimeUnitBlocks() {
        // given

        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTenMinutesTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mHalfHourTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mOneHourTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTwoHoursTimeUnitBlock);

        // then
        assertEquals(mFiveMinuteTimeUnitBlock.getValue() + mTenMinutesTimeUnitBlock.getValue() 
                    + mHalfHourTimeUnitBlock.getValue() + mOneHourTimeUnitBlock.getValue()
                    + mTwoHoursTimeUnitBlock.getValue(), 
                         mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }

    @Test
    public void testRemoveTimeUnitBlock() {
        // given
        TimeUnitBlock timeUnitBloctToTestRemove = mTenMinutesTimeUnitBlock;

        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(timeUnitBloctToTestRemove);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mHalfHourTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mOneHourTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTwoHoursTimeUnitBlock);

        mTimeUnitBlocksContainer.removeTimeUnitBlock(timeUnitBloctToTestRemove);

        // then
        assertFalse(mTimeUnitBlocksContainer.contains(timeUnitBloctToTestRemove));
        assertEquals(mFiveMinuteTimeUnitBlock.getValue() + mHalfHourTimeUnitBlock.getValue() + mOneHourTimeUnitBlock.getValue()
                     + mTwoHoursTimeUnitBlock.getValue(), 
                    mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }

    @Test
    public void testConteinsCloneTimeUnitBlock() {
        // given
        TimeUnitBlock firstTimeUnitBlock    = mFiveMinuteTimeUnitBlock;
        TimeUnitBlock oryginalTimeUnitBlock = mTenMinutesTimeUnitBlock;
        TimeUnitBlock clonedTimeUnitBlock   = oryginalTimeUnitBlock.clone();

        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(firstTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(clonedTimeUnitBlock);

        // then
        assertTrue(mTimeUnitBlocksContainer.containCloneOf(oryginalTimeUnitBlock));
        assertFalse(mTimeUnitBlocksContainer.contains(oryginalTimeUnitBlock));
        assertEquals(clonedTimeUnitBlock, mTimeUnitBlocksContainer.getCloneOf(oryginalTimeUnitBlock));
    }

    @Test
    public void testVerifyTimeUnitBlockAfterReorder() {
        // given

        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTenMinutesTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mHalfHourTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mOneHourTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTwoHoursTimeUnitBlock);

        mTimeUnitBlocksContainer.reorderTimeUnitBlocks();

        // then
        assertTrue(mTimeUnitBlocksContainer.contains(mFiveMinuteTimeUnitBlock));
        assertTrue(mTimeUnitBlocksContainer.contains(mTenMinutesTimeUnitBlock));
        assertTrue(mTimeUnitBlocksContainer.contains(mHalfHourTimeUnitBlock));
        assertTrue(mTimeUnitBlocksContainer.contains(mOneHourTimeUnitBlock));
        assertTrue(mTimeUnitBlocksContainer.contains(mTwoHoursTimeUnitBlock));
    }
    
    @Test
    public void testRemoveAllUnitBlock() {
        // given
        
        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTenMinutesTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mHalfHourTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mOneHourTimeUnitBlock);
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTwoHoursTimeUnitBlock);
        
        mTimeUnitBlocksContainer.removeAllTimeUnitBlocks();
        
        // then
        assertFalse(mTimeUnitBlocksContainer.contains(mFiveMinuteTimeUnitBlock));
        assertFalse(mTimeUnitBlocksContainer.contains(mTenMinutesTimeUnitBlock));
        assertFalse(mTimeUnitBlocksContainer.contains(mHalfHourTimeUnitBlock));
        assertFalse(mTimeUnitBlocksContainer.contains(mOneHourTimeUnitBlock));
        assertFalse(mTimeUnitBlocksContainer.contains(mTwoHoursTimeUnitBlock));
        assertEquals(0, mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }
    
    @Test
    public void testSplitTwoHoursTimeUnitBlock() {
        // given
        mTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        
        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTwoHoursTimeUnitBlock);
        
        mTimeUnitBlocksContainer.onMouseDoubleClick(mTwoHoursTimeUnitBlock);
        
        // then
        assertFalse(mTimeUnitBlocksContainer.contains(mTwoHoursTimeUnitBlock));
        assertEquals(mTwoHoursTimeUnitBlock.getValue(), mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }
    
    @Test
    public void testSplitOneHourTimeUnitBlock() {
        // given
        mTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        
        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mOneHourTimeUnitBlock);
        
        mTimeUnitBlocksContainer.onMouseDoubleClick(mOneHourTimeUnitBlock);
        
        // then
        assertFalse(mTimeUnitBlocksContainer.contains(mOneHourTimeUnitBlock));
        assertEquals(mOneHourTimeUnitBlock.getValue(), mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }
    
    @Test
    public void testSplitHalfHourTimeUnitBlock() {
        // given
        mTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        
        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mHalfHourTimeUnitBlock);
        
        mTimeUnitBlocksContainer.onMouseDoubleClick(mHalfHourTimeUnitBlock);
        
        // then
        assertFalse(mTimeUnitBlocksContainer.contains(mHalfHourTimeUnitBlock));
        assertEquals(mHalfHourTimeUnitBlock.getValue(), mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }

    @Test
    public void testSplitTenMinutesTimeUnitBlock() {
        // given
        mTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        
        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTenMinutesTimeUnitBlock);
        
        mTimeUnitBlocksContainer.onMouseDoubleClick(mTenMinutesTimeUnitBlock);
        
        // then
        assertFalse(mTimeUnitBlocksContainer.contains(mTenMinutesTimeUnitBlock));
        assertEquals(mTenMinutesTimeUnitBlock.getValue(), mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }

    @Test
    public void testNotSplitTenMinutesTimeUnitBlockWhenCointainerNotSummable() {
        // given
        mTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        mTimeUnitBlocksContainer.setSummable(false);
        
        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mTenMinutesTimeUnitBlock);
        
        mTimeUnitBlocksContainer.onMouseDoubleClick(mTenMinutesTimeUnitBlock);
        
        // then
        assertTrue(mTimeUnitBlocksContainer.contains(mTenMinutesTimeUnitBlock));
        assertEquals(mTenMinutesTimeUnitBlock.getValue(), mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }

    @Test
    public void testNotSplitFiveMinutesTimeUnitBlock() {
        // given
        mTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        
        // when
        mTimeUnitBlocksContainer.addTimeUnitBlock(mFiveMinuteTimeUnitBlock);
        
        mTimeUnitBlocksContainer.onMouseDoubleClick(mFiveMinuteTimeUnitBlock);
        
        // then
        assertTrue(mTimeUnitBlocksContainer.contains(mFiveMinuteTimeUnitBlock));
        assertEquals(mFiveMinuteTimeUnitBlock.getValue(), mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }

    @Test
    public void testFillWithTimeUnitBlocksFromTime() {
        // given
        mTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);

        // when
        final int timeSum = mFiveMinuteTimeUnitBlock.getValue()
                + mTenMinutesTimeUnitBlock.getValue()
                + mHalfHourTimeUnitBlock.getValue()
                + mOneHourTimeUnitBlock.getValue()
                + mTwoHoursTimeUnitBlock.getValue();

        mTimeUnitBlocksContainer.fillWithTimeUnitBlocksFromTime(timeSum);

        // then
        assertEquals(timeSum, mTimeUnitBlocksContainer.sumAllTimeUnitBlockInSeconds());
    }
}
