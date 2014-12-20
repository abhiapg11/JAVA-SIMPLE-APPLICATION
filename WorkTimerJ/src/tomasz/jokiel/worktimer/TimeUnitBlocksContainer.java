package tomasz.jokiel.worktimer;

import java.awt.Canvas;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class TimeUnitBlocksContainer extends Canvas {
    private static final long serialVersionUID = -7832898888093474767L;

    private static final int BLOCK_INNER_MARGIN_X = 3;
    static final int FIVE_MINUTES_IN_SECONDS       = 5  * 60;
    static final int TEN_MINUTES_IN_SECONDS        = 10 * 60;
    static final int HALF_HOUR_IN_SECONDS          = 30 * 60;
    static final int ONE_HOUR_MINUTES_IN_SECONDS   = 60 * 60;

    private ArrayList<TimeUnitBlock> mTimeUnitBlocks = new ArrayList<TimeUnitBlock>();

    private boolean mIsSummable = true;

    private OnConvertAddRemoveTimeUnitBlockListener mOnConvertAddRemoveTimeUnitBlockListener;
    private OnAddRemoveTimeUnitBlockListener mOnAddRemoveTimeUnitBlockListener;
    
    public void setConvertOnAddRemoveTimeUnitBlockListener(OnConvertAddRemoveTimeUnitBlockListener convertAddRemoveTimeUnitBlockListener) {
        mOnConvertAddRemoveTimeUnitBlockListener = convertAddRemoveTimeUnitBlockListener;
    }

    public void setOnAddRemoveTimeUnitBlockListener(OnAddRemoveTimeUnitBlockListener addRemoveTimeUnitBlockListener) {
        mOnAddRemoveTimeUnitBlockListener = addRemoveTimeUnitBlockListener;
    }

    public void addTimeUnitBlock(TimeUnitBlock timeUnitBlock) {
        if(!contains(timeUnitBlock)) {
            placeInTimeUnitBlockContainer(timeUnitBlock);
            mTimeUnitBlocks.add(timeUnitBlock);
            timeUnitBlock.setInSideTimeUnitBlocksContainer(this);

            if(mOnAddRemoveTimeUnitBlockListener != null) {
                mOnAddRemoveTimeUnitBlockListener.onAddedTimeUnitBlock(timeUnitBlock);
            }
        }
    }

    public void removeTimeUnitBlock(TimeUnitBlock timeUnitBlock) {
        removeTimeUnitBlock(timeUnitBlock, false);
    }

    public boolean contains(TimeUnitBlock timeUnitBlock) {
        for (TimeUnitBlock tub : mTimeUnitBlocks) {
            if(tub == timeUnitBlock) {
                return true;
            }
        }
        return false;
    }

    public boolean containCloneOf(TimeUnitBlock timeUnitBlock) {
        return getCloneOf(timeUnitBlock) != null;
    }

    public TimeUnitBlock getCloneOf(TimeUnitBlock timeUnitBlock) {
        for (TimeUnitBlock tub : mTimeUnitBlocks) {
            if(tub.isCloneOf(timeUnitBlock)) {
                return tub;
            }
        }
        return null;
    }

    int sumAllTimeUnitBlockInSeconds() {
        int sum = 0;

        for (TimeUnitBlock timeUnitBlock : mTimeUnitBlocks) {
            sum += timeUnitBlock.getValue();
        }

        return sum;
    }
    
    public boolean isSummable() {
        return mIsSummable;
    }

    public void setSummable(boolean summable) {
        mIsSummable = summable;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "["+hashCode()+"] size: "+mTimeUnitBlocks.size()+" current value = " + sumAllTimeUnitBlockInSeconds();
    }

    protected void placeInTimeUnitBlockContainer(TimeUnitBlock timeUnitBlock) {
        Point timeUnitBlocksNextSlootPositionRelative = getNextSlotlocation(timeUnitBlock.getWidth(), timeUnitBlock.getHeight());
        Rectangle timeUnitBlockBoundsRelative = timeUnitBlock.getBounds();
        timeUnitBlockBoundsRelative.x = timeUnitBlocksNextSlootPositionRelative.x;
        timeUnitBlockBoundsRelative.y = timeUnitBlocksNextSlootPositionRelative.y;
        timeUnitBlock.setBounds(timeUnitBlockBoundsRelative);
    }

    public Point getNextSlotlocation(int blockWidth, int blockHeight) {
        Rectangle bounds = getBounds();
        int offsetX = BLOCK_INNER_MARGIN_X + mTimeUnitBlocks.size() * (blockWidth + BLOCK_INNER_MARGIN_X);
        int offsetY = (bounds.height - blockHeight) / 2;

        return new Point(bounds.x + offsetX, bounds.y + offsetY);
    }

    public void reorderTimeUnitBlocks() {
        ArrayList<TimeUnitBlock> timeUnitBlocksTmp = new ArrayList<TimeUnitBlock>(mTimeUnitBlocks);

        for (TimeUnitBlock timeUnitBlock : timeUnitBlocksTmp) {
            removeTimeUnitBlock(timeUnitBlock);
        }
        for (TimeUnitBlock timeUnitBlock : timeUnitBlocksTmp) {
            addTimeUnitBlock(timeUnitBlock);
        }
    }

    public void removeAllTimeUnitBlocks() {
        removeAllTimeUnitBlocks(false);
    }

    public ArrayList<TimeUnitBlock> getAllTimeUnitBlocks() {
        return mTimeUnitBlocks;
    }

    public void convertTimeUnitBlocksFromSmallerToBigger() {
        int sumTime = sumAllTimeUnitBlockInSeconds();

        int maxCountOfOneHourTimeUnitBlocks = sumTime / ONE_HOUR_MINUTES_IN_SECONDS;
        int maxCountOfHalfHourTimeUnitBlocks = (sumTime % ONE_HOUR_MINUTES_IN_SECONDS) / HALF_HOUR_IN_SECONDS;
        int maxCountOfTenMinutesTimeUnitBlocks = (sumTime % HALF_HOUR_IN_SECONDS) / TEN_MINUTES_IN_SECONDS;
        int maxCountOfFiveMinutesTimeUnitBlocks = (sumTime % TEN_MINUTES_IN_SECONDS) / FIVE_MINUTES_IN_SECONDS;

        removeAllTimeUnitBlocks(true);

        triggerRequireAddTimeUnitBlockByAmountType(maxCountOfOneHourTimeUnitBlocks,
                                                   maxCountOfHalfHourTimeUnitBlocks,
                                                   maxCountOfTenMinutesTimeUnitBlocks,
                                                   maxCountOfFiveMinutesTimeUnitBlocks);
    }


    private void removeAllTimeUnitBlocks(boolean removeFromPane) {
        ArrayList<TimeUnitBlock> timeUnitBlocksTmp = new ArrayList<TimeUnitBlock>(mTimeUnitBlocks);
        for (TimeUnitBlock timeUnitBlock : timeUnitBlocksTmp) {
            removeTimeUnitBlock(timeUnitBlock, removeFromPane);
        }
    }

    private void removeTimeUnitBlock(TimeUnitBlock timeUnitBlock, boolean removeFromPane) {
        if(timeUnitBlock != null) {
            mTimeUnitBlocks.remove(timeUnitBlock);
            timeUnitBlock.setInSideTimeUnitBlocksContainer(null);
    
            if(removeFromPane) {
                if(mOnConvertAddRemoveTimeUnitBlockListener != null) {
                    mOnConvertAddRemoveTimeUnitBlockListener.onAfterRemovedTimeUnitBlockFromContainer(timeUnitBlock);
                }
            } else {
                if(mOnAddRemoveTimeUnitBlockListener != null) {
                    mOnAddRemoveTimeUnitBlockListener.onRemovedTimeUnitBlock(timeUnitBlock);
                }
            }
            
        }
    }

    private void triggerRequireAddTimeUnitBlockByAmountType(int maxCountOfOneHourTimeUnitBlocks,
                                                            int maxCountOfHalfHourTimeUnitBlocks,
                                                            int maxCountOfTenMinutesTimeUnitBlocks,
                                                            int maxCountOfFiveMinutesTimeUnitBlocks) {

        for (int i = 0; i < maxCountOfOneHourTimeUnitBlocks; i++) {
            if(mOnConvertAddRemoveTimeUnitBlockListener != null) {
                mOnConvertAddRemoveTimeUnitBlockListener.onAddOneHourTimeUnitBlockRequested(this);
            }
        }

        for (int i = 0; i < maxCountOfHalfHourTimeUnitBlocks; i++) {
            if(mOnConvertAddRemoveTimeUnitBlockListener != null) {
                mOnConvertAddRemoveTimeUnitBlockListener.onAddHalfHourTimeUnitBlockRequested(this);
            }
        }

        for (int i = 0; i < maxCountOfTenMinutesTimeUnitBlocks; i++) {
            if(mOnConvertAddRemoveTimeUnitBlockListener != null) {
                mOnConvertAddRemoveTimeUnitBlockListener.onAddTenMinutesTimeUnitBlockRequested(this);
            }
        }

        for (int i = 0; i < maxCountOfFiveMinutesTimeUnitBlocks; i++) {
            if(mOnConvertAddRemoveTimeUnitBlockListener != null) {
                mOnConvertAddRemoveTimeUnitBlockListener.onAddFiveMinutesTimeUnitBlockRequested(this);
            }
        }
    }
    
    public interface OnConvertAddRemoveTimeUnitBlockListener {
        public void onAddFiveMinutesTimeUnitBlockRequested(TimeUnitBlocksContainer timeUnitBlocksContainer);
        public void onAddTenMinutesTimeUnitBlockRequested(TimeUnitBlocksContainer timeUnitBlocksContainer);
        public void onAddHalfHourTimeUnitBlockRequested(TimeUnitBlocksContainer timeUnitBlocksContainer);
        public void onAddOneHourTimeUnitBlockRequested(TimeUnitBlocksContainer timeUnitBlocksContainer);
        public void onAfterRemovedTimeUnitBlockFromContainer(TimeUnitBlock timeUnitBlock);
    }

    public interface OnAddRemoveTimeUnitBlockListener {
        public void onAddedTimeUnitBlock(TimeUnitBlock addedTimeUnitBlock);
        public void onRemovedTimeUnitBlock(TimeUnitBlock removedTimeUnitBlock);
    }
}
