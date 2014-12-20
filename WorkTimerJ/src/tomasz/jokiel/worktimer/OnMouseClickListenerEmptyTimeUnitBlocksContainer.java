package tomasz.jokiel.worktimer;

public class OnMouseClickListenerEmptyTimeUnitBlocksContainer extends OnMouseClickListener {

    private TimeUnitBlocksContainer mTimeUnitBlocksContainer;
    private OnRemoveAllTimeUnitBlocksFromCointainerListener mOnRemoveAllTimeUnitBlocksFromCointainerListener;

    public OnMouseClickListenerEmptyTimeUnitBlocksContainer(
            TimeUnitBlocksContainer timeUnitBlocksContainer,
            OnRemoveAllTimeUnitBlocksFromCointainerListener onRemoveAllTimeUnitBlocksFromCointainerListener) {
        mTimeUnitBlocksContainer = timeUnitBlocksContainer;
        mOnRemoveAllTimeUnitBlocksFromCointainerListener = onRemoveAllTimeUnitBlocksFromCointainerListener;
    }

  @Override
  public void onMouseClick() {
      mOnRemoveAllTimeUnitBlocksFromCointainerListener.onRemove(mTimeUnitBlocksContainer);
  }

  public interface OnRemoveAllTimeUnitBlocksFromCointainerListener {
      public void onRemove(TimeUnitBlocksContainer timeUnitBlocksContainer);
  }
}
