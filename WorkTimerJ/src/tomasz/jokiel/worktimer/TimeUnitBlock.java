package tomasz.jokiel.worktimer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class TimeUnitBlock extends JPanel {
    private static final long serialVersionUID = 5084602705848206159L;

    private final static Map<Color, Color> COLORS_MAP = new HashMap<Color, Color>(){{
        put(Color.BLACK,    Color.WHITE);
        put(Color.WHITE,    Color.BLACK);
        put(Color.MAGENTA,  Color.CYAN);
        put(Color.CYAN,     Color.MAGENTA);
        put(Color.YELLOW,   Color.MAGENTA);
        put(Color.ORANGE,   Color.MAGENTA);
        put(Color.GREEN,    Color.MAGENTA);
        put(Color.BLUE,     Color.GREEN);
    }};

    final int mTimeAnountInSeconds;
    protected Point mMousePositinInsideRelWhenPressed = new Point();
    protected Point mLocationWhenPressed = new Point();
    private OnTimeUnitBlockDroppedListener mOnTimeUnitBlockDroppedListener;
    private TimeUnitBlocksContainer mTimeUnitBlocksContainerWhichInside;
    private Color mTimeTextColor = Color.BLACK;
    private TimeUnitBlock mCloneOf;
    private boolean mIsManuallyAddedToDayTimeSumator;
    private Color mColorOriginal;

    public TimeUnitBlock(int initialTimeAnountInSeconds) {
        mTimeAnountInSeconds = initialTimeAnountInSeconds;

        addMouseMotionListener(mouseMotionListener);
        addMouseListener(mouseListener);
    }

    public int getValue() {
        return mTimeAnountInSeconds;
    }

    public void setOnTimeUnitBlockDroppedListener(OnTimeUnitBlockDroppedListener listener) {
        mOnTimeUnitBlockDroppedListener = listener;
    }

    public void setInSideTimeUnitBlocksContainer(TimeUnitBlocksContainer timeUnitBlocksContainerWhichInside) {
        mTimeUnitBlocksContainerWhichInside = timeUnitBlocksContainerWhichInside;
    }

    public TimeUnitBlocksContainer getTimeUnitBlocksContainerInWhich() {
        return mTimeUnitBlocksContainerWhichInside;
    }

    public boolean isInsideTimeUnitBlocksContainer() {
        return getTimeUnitBlocksContainerInWhich() != null;
    }

    public void bringBackToPreviousPositionInContainer() {
        setLocation(mLocationWhenPressed);
    }

    public void setTimeTextColor(Color color) {
        mTimeTextColor = color;
    }

    public boolean isCloneOf(TimeUnitBlock timeUnitBlock) {
        return mCloneOf == timeUnitBlock;
    }
    
    public void setIsManuallyAddedToDayTimeSummator(boolean value) {
        mIsManuallyAddedToDayTimeSumator = value;
    }

    @Override
    public void setBackground(Color bg) {
        mColorOriginal = new Color(bg.getRGB());
        super.setBackground(bg);
    }
    // *******************************************
    private MouseMotionListener mouseMotionListener = new MouseMotionListener() {

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            handleMouseDragged(TimeUnitBlock.this, mouseEvent);
        }
    };

    private MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseReleased(MouseEvent e) {
            TimeUnitBlock.super.setBackground(mColorOriginal);
            if(mOnTimeUnitBlockDroppedListener != null) {
                mOnTimeUnitBlockDroppedListener.onTimeUnitBlockDropped(TimeUnitBlock.this);
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            TimeUnitBlock.super.setBackground(getBackground().darker());
            mMousePositinInsideRelWhenPressed.x = mouseEvent.getX();
            mMousePositinInsideRelWhenPressed.y = mouseEvent.getY();
            mLocationWhenPressed = getLocation();

            if(mOnTimeUnitBlockDroppedListener != null) {
                mOnTimeUnitBlockDroppedListener.onTimeUnitBlockDragged(TimeUnitBlock.this);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
        }
    };
    // *********************************************

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(mTimeTextColor);
        g.drawString(String.format("'%02d", mTimeAnountInSeconds/60), 0, 11);
        Rectangle bounds = getBounds();

        if (mIsManuallyAddedToDayTimeSumator) {
            g.setColor(getOpositeColor(getBackground()));
            g.drawRect(0, 0, bounds.width-1, bounds.height-1);
        }
    };

    private Color getOpositeColor(Color originalColor) {
            Color opositeColor = COLORS_MAP.get(originalColor);

            if(opositeColor == null) {
                opositeColor = Color.WHITE;
            }
//            opositeColor = calculateOpositeColor(originalColor);
            return opositeColor;
        }

//    private Color getTimeTextColor() {
//        double backgroundColorLuminance = calculateLuminance(getBackground());
//        return (backgroundColorLuminance < 325) ? Color.BLACK : Color.WHITE;
//    }
//

//    private Color calculateOpositeColor(Color originalColor) {
//        calculateSimilarity(originalColor, colorFromPalet);
//        return null;
//    }

//    private static double calculateSimilarity(Color color1, Color color2) {
//        double luminance1 = calculateLuminance(color1);
//        double luminance2 = calculateLuminance(color2);
//
//        return Math.abs(luminance1 - luminance2);
//    }

//    private static double calculateLuminance(Color color) {
//        int red = color.getRed();
//        int green = color.getGreen();
//        int blue = color.getBlue();
//
//        return Math.sqrt(Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2));
//    }

    private void handleMouseDragged(final TimeUnitBlock timeUnitBlock, MouseEvent mouseEvent) {
        Point parentLocation = timeUnitBlock.getParent().getLocationOnScreen();
        Rectangle parentTimeUnitBlockBounds = timeUnitBlock.getParent().getBounds();
        Rectangle timeUnitBlockBounds = timeUnitBlock.getBounds();

        Rectangle timeUnitBlockBoundsAfterMove = calculateTimeUnitBlockBoundsAfterMove(mouseEvent, parentLocation, parentTimeUnitBlockBounds, timeUnitBlockBounds);

        timeUnitBlock.setBounds(timeUnitBlockBoundsAfterMove);
    }
    
    private Rectangle calculateTimeUnitBlockBoundsAfterMove(MouseEvent mouseEvent, Point parentLocation,
            Rectangle parentTimeUnitBlockBounds, final Rectangle timeUnitBlockBounds) {
        Rectangle timeUnitBlockBoundsAfterMove = (Rectangle) timeUnitBlockBounds.clone();
        int timeUnitBlockXAfter = mouseEvent.getXOnScreen() - parentLocation.x - mMousePositinInsideRelWhenPressed.x;
        int timeUnitBlockYAfter = mouseEvent.getYOnScreen() - parentLocation.y - mMousePositinInsideRelWhenPressed.y;
        
        if((timeUnitBlockXAfter + timeUnitBlockBounds.width) > parentTimeUnitBlockBounds.width) {
            timeUnitBlockXAfter = parentTimeUnitBlockBounds.width - timeUnitBlockBounds.width;
        }
        if(timeUnitBlockXAfter < 0) {
            timeUnitBlockXAfter = 0;
        }
        if((timeUnitBlockYAfter + timeUnitBlockBounds.height) > parentTimeUnitBlockBounds.height) {
            timeUnitBlockYAfter = parentTimeUnitBlockBounds.height - timeUnitBlockBounds.height;
        }
        if(timeUnitBlockYAfter < 0) {
            timeUnitBlockYAfter = 0;
        }

        timeUnitBlockBoundsAfterMove.x = timeUnitBlockXAfter;
        timeUnitBlockBoundsAfterMove.y = timeUnitBlockYAfter;
        
        return timeUnitBlockBoundsAfterMove;
    }
    
    // **********************************
    public interface OnTimeUnitBlockDroppedListener {
        public void onTimeUnitBlockDropped(TimeUnitBlock timeUnitBlock);
        public void onTimeUnitBlockDragged(TimeUnitBlock timeUnitBlock);
    }

    protected TimeUnitBlock clone(){
        TimeUnitBlock clone = new TimeUnitBlock(mTimeAnountInSeconds);
        clone.mCloneOf = this;
        clone.mMousePositinInsideRelWhenPressed.x = mMousePositinInsideRelWhenPressed.x;
        clone.mMousePositinInsideRelWhenPressed.y = mMousePositinInsideRelWhenPressed.y;
        clone.mTimeTextColor = mTimeTextColor;
        clone.setBackground(mColorOriginal);
        clone.setBounds(getBounds());
        clone.setOnTimeUnitBlockDroppedListener(mOnTimeUnitBlockDroppedListener);
        return clone;
    }

    
}