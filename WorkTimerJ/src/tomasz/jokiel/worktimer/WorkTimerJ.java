package tomasz.jokiel.worktimer;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JButton;

import tomasz.jokiel.worktimer.DataReader.WorkTimmerSummary;
import tomasz.jokiel.worktimer.DayliTimerTaskGroup.OnUpdateDayliTimerValueListener;
import tomasz.jokiel.worktimer.OnMouseClickListenerEmptyTimeUnitBlocksContainer.OnRemoveAllTimeUnitBlocksFromCointainerListener;
import tomasz.jokiel.worktimer.TimeUnitBlock.OnTimeUnitBlockDroppedListener;
import tomasz.jokiel.worktimer.TimeUnitBlocksContainer.OnConvertAddRemoveTimeUnitBlockListener;
import tomasz.jokiel.worktimer.WindowCloseListener.OnWindowCloseListener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.Font;

public class WorkTimerJ {
    private static final int FIVE_MINUTES = 5  * 60;
    private static final int TEN_MINUTES  = 10 * 60;
    private static final int HALF_HOUR    = 30 * 60;
    private static final int ONE_HOUR     = 60 * 60;
    private static final int TUB_WIDTH = 20;
    private static final int TUB_HEIGHT = 14;
    private static final int EIGHT_HOURS_IN_SECONDS = 8 * ONE_HOUR;
    private JFrame frame;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;
    private JTextPane mGlbalDeltaTextPane;

    private DataSaver mDataSaver = new DataSaverImpl();
    private WorkTimmerSummary mWorkTimmerSummary;

    private final TimeUnitBlock mFiveMinuteTimeUnitBlock = new TimeUnitBlock(FIVE_MINUTES);
    private final TimeUnitBlock mTenMinutesTimeUnitBlock  = new TimeUnitBlock(TEN_MINUTES);
    private final TimeUnitBlock mHalfHourTimeUnitBlock   = new TimeUnitBlock(HALF_HOUR);
    private final TimeUnitBlock mOneHourTimeUnitBlock    = new TimeUnitBlock(ONE_HOUR);
    
    private ArrayList<TimeUnitBlocksContainer> mTimeUnitBlocksCointainers = new ArrayList<TimeUnitBlocksContainer>(6);
    private ArrayList<DayliTimerTaskGroup> mDayliTimerTaskGroupContainer = initDayliTimerTaskGroupContainer();
    private DayliTimerTaskGroup mSumatrDayliTimerTaskGroupContainer = new DayliTimerTaskGroup(null);

    protected int firstTimeUnitBoxZIndex;

    private final ImageIcon trashIcon = new ImageIcon(getClass().getResource("/delete_icon.png"));
    private final ImageIcon trashIconSml1 = new ImageIcon(getClass().getResource("/delete_icon_sml.png"));
    private final ImageIcon trashIconSml2 = new ImageIcon(getClass().getResource("/delete_icon_sml.png"));
    private final ImageIcon trashIconSml3 = new ImageIcon(getClass().getResource("/delete_icon_sml.png"));
    private final ImageIcon trashIconSml4 = new ImageIcon(getClass().getResource("/delete_icon_sml.png"));
    private final ImageIcon trashIconSml5 = new ImageIcon(getClass().getResource("/delete_icon_sml.png"));
    private final ImageIcon trashIconSml6 = new ImageIcon(getClass().getResource("/delete_icon_sml.png"));

    private OnRemoveAllTimeUnitBlocksFromCointainerListener mOnRemoveAllTimeUnitBlocksFromCointainerListener = new OnRemoveAllTimeUnitBlocksFromCointainerListener() {
        @Override
        public void onRemove(TimeUnitBlocksContainer timeUnitBlocksContainer) {
            handleRemoveClearAllTimeUnitBlockContainerContent(timeUnitBlocksContainer);
        }
    };
    private TimeUnitBlocksContainer mDaySummatorTimeUnitBlocksContainer;
    private TimeUnitBlocksContainer mTrashTimeUnitBlocksContainer;
    private ButtonGroup mRadioButtonGroup = new ButtonGroup();
    private JToggleButton mTglbtnStartpause;
    private OnConvertAddRemoveTimeUnitBlockListener mAddRemoveTimeUnitBlockListener = new OnConvertAddRemoveTimeUnitBlockListener() {
        
        @Override
        public void onAfterRemovedTimeUnitBlockFromContainer(
                TimeUnitBlock timeUnitBlock) {
            handleOnAfterRemovedTimeUnitBlockFromContainer(timeUnitBlock);
        }

        @Override
        public void onAddFiveMinutesTimeUnitBlockRequested(
                TimeUnitBlocksContainer timeUnitBlocksContainer) {
            addFiveMinuteTimeUnitBlockToContainer(timeUnitBlocksContainer);
        }

        @Override
        public void onAddTenMinutesTimeUnitBlockRequested(
                TimeUnitBlocksContainer timeUnitBlocksContainer) {
            addTenMinutesTimeUnitBlockToContainer(timeUnitBlocksContainer);
        }

        @Override
        public void onAddHalfHourTimeUnitBlockRequested(
                TimeUnitBlocksContainer timeUnitBlocksContainer) {
            addHalfHourTimeUnitBlockToContainer(timeUnitBlocksContainer);
        }

        @Override
        public void onAddOneHourTimeUnitBlockRequested(
                TimeUnitBlocksContainer timeUnitBlocksContainer) {
            addOneHourTimeUnitBlockToContainer(timeUnitBlocksContainer);
        }
    };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    WorkTimerJ window = new WorkTimerJ();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public WorkTimerJ() {
        initialize();
    }

    @SuppressWarnings("serial")
    private ArrayList<DayliTimerTaskGroup> initDayliTimerTaskGroupContainer() {
        return new ArrayList<DayliTimerTaskGroup>(6) {
            {
                OnUpdateDayliTimerValueListener listener = new OnUpdateDayliTimerValueListener() {

                    @Override
                    public void onUpdateDayliTimerValueByFiveMinutes(
                            TimeUnitBlocksContainer timeUnitBlocksContainer) {
                        addFiveMinuteTimeUnitBlockToContainer(timeUnitBlocksContainer);

                        if(isConversionTimeUnitBlocksFromSmallerToBiggerAllowed()) {
                            timeUnitBlocksContainer.convertTimeUnitBlocksFromSmallerToBigger();
                        }
                    }

                    @Override
                    public void onUpdateDayliTimerValue() {
                        mSumatrDayliTimerTaskGroupContainer.getDayliTimer().addToCounterValue(1);
                    }

                };

                add(new DayliTimerTaskGroup(listener));
                add(new DayliTimerTaskGroup(listener));
                add(new DayliTimerTaskGroup(listener));
                add(new DayliTimerTaskGroup(listener));
                add(new DayliTimerTaskGroup(listener));
                add(new DayliTimerTaskGroup(listener));
            }
        };
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 640, 312);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.addWindowListener(new WindowCloseListener(getOnApplicationCloseListener()));

        mWorkTimmerSummary = DataReader.readDataBase();

        ititLabels();
        initRadioButtons();
        initDayliTimersTasksDescriptions();
        initTimeControlButtons();
        initTimeUnitBlocks();
        initTimeUnitBlocksContainers();
        initDayliTimerValues();
        initGlobalTimeralues();

    }

    private void ititLabels() {
        JLabel lblTimeControll = new JLabel("Time Controll");
        lblTimeControll.setBounds(37, 11, 88, 14);
        frame.getContentPane().add(lblTimeControll);
    
        JLabel lblGlobalDelta = new JLabel("Global delta:");
        lblGlobalDelta.setBounds(10, 236, 100, 14);
        frame.getContentPane().add(lblGlobalDelta);
    
        JLabel lblTodayLeft = new JLabel("Today left:");
        lblTodayLeft.setBounds(462, 9, 77, 14);
        frame.getContentPane().add(lblTodayLeft);
    }

    private void initRadioButtons() {
        JRadioButton radioButton1 = new JRadioButton("");
        radioButton1.setBounds(10, 66, 21, 23);
        radioButton1.setSelected(true);
        radioButton1.addActionListener(onRadioButtonActionListener(radioButton1));
        frame.getContentPane().add(radioButton1);

        JRadioButton radioButton2 = new JRadioButton("");
        radioButton2.setBounds(10, 92, 21, 23);
        radioButton2.addActionListener(onRadioButtonActionListener(radioButton2));
        frame.getContentPane().add(radioButton2);

        JRadioButton radioButton3 = new JRadioButton("");
        radioButton3.setBounds(10, 118, 21, 23);
        radioButton3.addActionListener(onRadioButtonActionListener(radioButton3));
        frame.getContentPane().add(radioButton3);

        JRadioButton radioButton4 = new JRadioButton("");
        radioButton4.setBounds(10, 144, 21, 23);
        radioButton4.addActionListener(onRadioButtonActionListener(radioButton4));
        frame.getContentPane().add(radioButton4);

        JRadioButton radioButton5 = new JRadioButton("");
        radioButton5.setBounds(10, 170, 21, 23);
        radioButton5.addActionListener(onRadioButtonActionListener(radioButton5));
        frame.getContentPane().add(radioButton5);

        JRadioButton radioButton6 = new JRadioButton("");
        radioButton6.setBounds(10, 196, 21, 23);
        radioButton6.addActionListener(onRadioButtonActionListener(radioButton6));
        frame.getContentPane().add(radioButton6);

        mRadioButtonGroup.add(radioButton1);
        mRadioButtonGroup.add(radioButton2);
        mRadioButtonGroup.add(radioButton3);
        mRadioButtonGroup.add(radioButton4);
        mRadioButtonGroup.add(radioButton5);
        mRadioButtonGroup.add(radioButton6);

        mDayliTimerTaskGroupContainer.get(0).setRadioButton(radioButton1);
        mDayliTimerTaskGroupContainer.get(1).setRadioButton(radioButton2);
        mDayliTimerTaskGroupContainer.get(2).setRadioButton(radioButton3);
        mDayliTimerTaskGroupContainer.get(3).setRadioButton(radioButton4);
        mDayliTimerTaskGroupContainer.get(4).setRadioButton(radioButton5);
        mDayliTimerTaskGroupContainer.get(5).setRadioButton(radioButton6);
    }

    private void initDayliTimersTasksDescriptions() {
        textField_1 = new JTextField();
        textField_1.setBounds(37, 66, 263, 20);
        frame.getContentPane().add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(37, 92, 263, 20);
        frame.getContentPane().add(textField_2);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(37, 121, 263, 20);
        frame.getContentPane().add(textField_3);

        textField_4 = new JTextField();
        textField_4.setColumns(10);
        textField_4.setBounds(37, 147, 263, 20);
        frame.getContentPane().add(textField_4);

        textField_5 = new JTextField();
        textField_5.setColumns(10);
        textField_5.setBounds(37, 173, 263, 20);
        frame.getContentPane().add(textField_5);

        textField_6 = new JTextField();
        textField_6.setColumns(10);
        textField_6.setBounds(37, 199, 263, 20);
        frame.getContentPane().add(textField_6);

        if(!mWorkTimmerSummary.timersDescriptions.isEmpty()) {
            textField_1.setText(mWorkTimmerSummary.timersDescriptions.get(0));
            textField_2.setText(mWorkTimmerSummary.timersDescriptions.get(1));
            textField_3.setText(mWorkTimmerSummary.timersDescriptions.get(2));
            textField_4.setText(mWorkTimmerSummary.timersDescriptions.get(3));
            textField_5.setText(mWorkTimmerSummary.timersDescriptions.get(4));
            textField_6.setText(mWorkTimmerSummary.timersDescriptions.get(5));
        }

        mDayliTimerTaskGroupContainer.get(0).setTaskDescriptionJTextField(textField_1);
        mDayliTimerTaskGroupContainer.get(1).setTaskDescriptionJTextField(textField_2);
        mDayliTimerTaskGroupContainer.get(2).setTaskDescriptionJTextField(textField_3);
        mDayliTimerTaskGroupContainer.get(3).setTaskDescriptionJTextField(textField_4);
        mDayliTimerTaskGroupContainer.get(4).setTaskDescriptionJTextField(textField_5);
        mDayliTimerTaskGroupContainer.get(5).setTaskDescriptionJTextField(textField_6);
    }

    private void initTimeControlButtons() {
        mTglbtnStartpause = new JToggleButton("Start/Pause");
        mTglbtnStartpause.setFont(new Font("Tahoma", Font.PLAIN, 10));
        mTglbtnStartpause.setBounds(37, 36, 100, 23);
        mTglbtnStartpause.addItemListener(getStartPauseItemListener());
        frame.getContentPane().add(mTglbtnStartpause);

        JButton btnEndDay = new JButton("End day");
        btnEndDay.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnEndDay.setBounds(142, 36, 77, 23);
        btnEndDay.addActionListener(getOnEndDayButtonClickActionListener());
        frame.getContentPane().add(btnEndDay);

        JButton btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnReset.setBounds(229, 36, 71, 23);
        btnReset.addActionListener(getOnResetButtonClickActionListener());
        frame.getContentPane().add(btnReset);
    }

    private void initTimeUnitBlocks() {

        OnTimeUnitBlockDroppedListener onTimeUnitBlockDroppedListener = new OnTimeUnitBlockDroppedListener() {
            @Override
            public void onTimeUnitBlockDropped(TimeUnitBlock timeUnitBlock) {
                handleTimeUnitBlockDropped(timeUnitBlock);
            }

            @Override
            public void onTimeUnitBlockDragged(TimeUnitBlock timeUnitBlock) {
                setAtTheTop(timeUnitBlock);
                if(!timeUnitBlock.isInsideTimeUnitBlocksContainer()) {
                    addCloneInSamePlace(timeUnitBlock);
                }
            }

            private void setAtTheTop(TimeUnitBlock timeUnitBlock) {
                frame.getContentPane().setComponentZOrder(timeUnitBlock, firstTimeUnitBoxZIndex);
            }

            private Component addCloneInSamePlace(TimeUnitBlock timeUnitBlock) {
                return frame.getContentPane().add(timeUnitBlock.clone());
            }
        };

        mFiveMinuteTimeUnitBlock.setBackground(Color.MAGENTA);
        mFiveMinuteTimeUnitBlock.setBounds(306, 236, TUB_WIDTH, TUB_HEIGHT);
        mFiveMinuteTimeUnitBlock.setOnTimeUnitBlockDroppedListener(onTimeUnitBlockDroppedListener);

        mTenMinutesTimeUnitBlock.setBackground(Color.ORANGE);
        mTenMinutesTimeUnitBlock.setBounds(326, 236, TUB_WIDTH, TUB_HEIGHT);
        mTenMinutesTimeUnitBlock.setOnTimeUnitBlockDroppedListener(onTimeUnitBlockDroppedListener);

        mHalfHourTimeUnitBlock.setBackground(Color.GREEN);
        mHalfHourTimeUnitBlock.setBounds(346, 236, TUB_WIDTH, TUB_HEIGHT);
        mHalfHourTimeUnitBlock.setOnTimeUnitBlockDroppedListener(onTimeUnitBlockDroppedListener);

        mOneHourTimeUnitBlock.setBackground(Color.BLUE);
        mOneHourTimeUnitBlock.setTimeTextColor(Color.WHITE);
        mOneHourTimeUnitBlock.setBounds(366, 236, TUB_WIDTH, TUB_HEIGHT);
        mOneHourTimeUnitBlock.setOnTimeUnitBlockDroppedListener(onTimeUnitBlockDroppedListener);

        frame.getContentPane().add(mFiveMinuteTimeUnitBlock);
        frame.getContentPane().add(mTenMinutesTimeUnitBlock);
        frame.getContentPane().add(mHalfHourTimeUnitBlock);
        frame.getContentPane().add(mOneHourTimeUnitBlock);

        firstTimeUnitBoxZIndex = frame.getContentPane().getComponentZOrder(mFiveMinuteTimeUnitBlock);
    }

    private void initSumatrDayliTimerTaskGroupContainer() {
        mSumatrDayliTimerTaskGroupContainer.reset();
        int initialCounterValue = -EIGHT_HOURS_IN_SECONDS;

        if(mWorkTimmerSummary.dayliTimerValue != null) {
            initialCounterValue = mWorkTimmerSummary.dayliTimerValue;
        }

        mSumatrDayliTimerTaskGroupContainer.getDayliTimer().addToCounterValue(initialCounterValue);
    }

    private void initDayliTimerValues() {
        JTextPane timerTextPane1 = new JTextPane();
        timerTextPane1.setEditable(false);
        timerTextPane1.setBackground(SystemColor.menu);
        timerTextPane1.setText("00:00:00");
        timerTextPane1.setBounds(300, 66, 65, 20);
        frame.getContentPane().add(timerTextPane1);

        JTextPane timerTextPane2 = new JTextPane();
        timerTextPane2.setEditable(false);
        timerTextPane2.setBackground(SystemColor.menu);
        timerTextPane2.setText("00:00:00");
        timerTextPane2.setBounds(300, 92, 65, 20);
        frame.getContentPane().add(timerTextPane2);

        JTextPane timerTextPane3 = new JTextPane();
        timerTextPane3.setEditable(false);
        timerTextPane3.setBackground(SystemColor.menu);
        timerTextPane3.setText("00:00:00");
        timerTextPane3.setBounds(300, 121, 65, 20);
        frame.getContentPane().add(timerTextPane3);

        JTextPane timerTextPane4 = new JTextPane();
        timerTextPane4.setEditable(false);
        timerTextPane4.setBackground(SystemColor.menu);
        timerTextPane4.setText("00:00:00");
        timerTextPane4.setBounds(300, 147, 65, 20);
        frame.getContentPane().add(timerTextPane4);

        JTextPane timerTextPane5 = new JTextPane();
        timerTextPane5.setEditable(false);
        timerTextPane5.setBackground(SystemColor.menu);
        timerTextPane5.setText("00:00:00");
        timerTextPane5.setBounds(300, 173, 65, 20);
        frame.getContentPane().add(timerTextPane5);

        JTextPane timerTextPane6 = new JTextPane();
        timerTextPane6.setEditable(false);
        timerTextPane6.setBackground(SystemColor.menu);
        timerTextPane6.setText("00:00:00");
        timerTextPane6.setBounds(300, 199, 65, 20);
        frame.getContentPane().add(timerTextPane6);

        mDayliTimerTaskGroupContainer.get(0).setDayliTimerValueJTextPane(timerTextPane1);
        mDayliTimerTaskGroupContainer.get(1).setDayliTimerValueJTextPane(timerTextPane2);
        mDayliTimerTaskGroupContainer.get(2).setDayliTimerValueJTextPane(timerTextPane3);
        mDayliTimerTaskGroupContainer.get(3).setDayliTimerValueJTextPane(timerTextPane4);
        mDayliTimerTaskGroupContainer.get(4).setDayliTimerValueJTextPane(timerTextPane5);
        mDayliTimerTaskGroupContainer.get(5).setDayliTimerValueJTextPane(timerTextPane6);
    }

    private void initGlobalTimeralues() {
        mGlbalDeltaTextPane = new JTextPane();
        mGlbalDeltaTextPane.setBackground(SystemColor.menu);
        mGlbalDeltaTextPane.setEditable(false);
        String globalDeltatimerDisplay = Utils.formatTimerFromSeconds(mWorkTimmerSummary.globalDelta);
        mGlbalDeltaTextPane.setText(globalDeltatimerDisplay);
        mGlbalDeltaTextPane.setBounds(105, 232, 77, 20);
        frame.getContentPane().add(mGlbalDeltaTextPane);

        JTextPane todayLeftTimerTextPane = new JTextPane();
        todayLeftTimerTextPane.setBackground(SystemColor.menu);
        todayLeftTimerTextPane.setEditable(false);
        
        String todayLeftTimerDisplay = "-08:00:00";

        if(mWorkTimmerSummary.dayliTimerValue != null) {
            todayLeftTimerDisplay = Utils.formatTimerFromSeconds(mWorkTimmerSummary.dayliTimerValue);
        }

        todayLeftTimerTextPane.setText(todayLeftTimerDisplay);
        todayLeftTimerTextPane.setBounds(542, 5, 66, 20);
        frame.getContentPane().add(todayLeftTimerTextPane);
        mSumatrDayliTimerTaskGroupContainer.setDayliTimerValueJTextPane(todayLeftTimerTextPane);
    }

    private void initTimeUnitBlocksContainers() {
        TimeUnitBlocksContainer timeUnitBlocksContainer1 = new TimeUnitBlocksContainer();
        timeUnitBlocksContainer1.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        timeUnitBlocksContainer1.setBackground(Color.GRAY);
        timeUnitBlocksContainer1.setBounds(368, 66, 223, 20);
        frame.getContentPane().add(timeUnitBlocksContainer1);
        mTimeUnitBlocksCointainers.add(timeUnitBlocksContainer1);
    
        JLabel trashLabelIconTimer1 = new JLabel("", trashIconSml1, JLabel.CENTER);
        trashLabelIconTimer1.setBounds(590, 66, 24, 24);
        trashLabelIconTimer1.addMouseListener(
                new OnMouseClickListenerEmptyTimeUnitBlocksContainer(timeUnitBlocksContainer1, 
                        mOnRemoveAllTimeUnitBlocksFromCointainerListener));
        frame.getContentPane().add(trashLabelIconTimer1);
    
        TimeUnitBlocksContainer timeUnitBlocksContainer2 = new TimeUnitBlocksContainer();
        timeUnitBlocksContainer2.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        timeUnitBlocksContainer2.setBackground(Color.GRAY);
        timeUnitBlocksContainer2.setBounds(368, 95, 223, 20);
        frame.getContentPane().add(timeUnitBlocksContainer2);
        mTimeUnitBlocksCointainers.add(timeUnitBlocksContainer2);
    
        JLabel trashLabelIconTimer2 = new JLabel("", trashIconSml2, JLabel.CENTER);
        trashLabelIconTimer2.setBounds(590, 91, 24, 24);
        trashLabelIconTimer2.addMouseListener(
                new OnMouseClickListenerEmptyTimeUnitBlocksContainer(timeUnitBlocksContainer2, 
                        mOnRemoveAllTimeUnitBlocksFromCointainerListener));
        frame.getContentPane().add(trashLabelIconTimer2);
    
        TimeUnitBlocksContainer timeUnitBlocksContainer3 = new TimeUnitBlocksContainer();
        timeUnitBlocksContainer3.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        timeUnitBlocksContainer3.setBackground(Color.GRAY);
        timeUnitBlocksContainer3.setBounds(368, 121, 223, 20);
        frame.getContentPane().add(timeUnitBlocksContainer3);
        mTimeUnitBlocksCointainers.add(timeUnitBlocksContainer3);
    
        JLabel trashLabelIconTimer3 = new JLabel("", trashIconSml3, JLabel.CENTER);
        trashLabelIconTimer3.setBounds(590, 118, 24, 24);
        trashLabelIconTimer3.addMouseListener(
                new OnMouseClickListenerEmptyTimeUnitBlocksContainer(timeUnitBlocksContainer3, 
                        mOnRemoveAllTimeUnitBlocksFromCointainerListener));
        frame.getContentPane().add(trashLabelIconTimer3);
    
        TimeUnitBlocksContainer timeUnitBlocksContainer4 = new TimeUnitBlocksContainer();
        timeUnitBlocksContainer4.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        timeUnitBlocksContainer4.setBackground(Color.GRAY);
        timeUnitBlocksContainer4.setBounds(368, 147, 223, 20);
        frame.getContentPane().add(timeUnitBlocksContainer4);
        mTimeUnitBlocksCointainers.add(timeUnitBlocksContainer4);
    
        JLabel trashLabelIconTimer4 = new JLabel("", trashIconSml4, JLabel.CENTER);
        trashLabelIconTimer4.setBounds(590, 144, 24, 24);
        trashLabelIconTimer4.addMouseListener(
                new OnMouseClickListenerEmptyTimeUnitBlocksContainer(timeUnitBlocksContainer4, 
                        mOnRemoveAllTimeUnitBlocksFromCointainerListener));
        frame.getContentPane().add(trashLabelIconTimer4);
    
        TimeUnitBlocksContainer timeUnitBlocksContainer5 = new TimeUnitBlocksContainer();
        timeUnitBlocksContainer5.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        timeUnitBlocksContainer5.setBackground(Color.GRAY);
        timeUnitBlocksContainer5.setBounds(368, 173, 223, 20);
        frame.getContentPane().add(timeUnitBlocksContainer5);
        mTimeUnitBlocksCointainers.add(timeUnitBlocksContainer5);
    
        JLabel trashLabelIconTimer5 = new JLabel("", trashIconSml5, JLabel.CENTER);
        trashLabelIconTimer5.setBounds(590, 170, 24, 24);
        trashLabelIconTimer5.addMouseListener(
                new OnMouseClickListenerEmptyTimeUnitBlocksContainer(timeUnitBlocksContainer5, 
                        mOnRemoveAllTimeUnitBlocksFromCointainerListener));
        frame.getContentPane().add(trashLabelIconTimer5);
    
        TimeUnitBlocksContainer timeUnitBlocksContainer6 = new TimeUnitBlocksContainer();
        timeUnitBlocksContainer6.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        timeUnitBlocksContainer6.setBackground(Color.GRAY);
        timeUnitBlocksContainer6.setBounds(368, 199, 223, 20);
        frame.getContentPane().add(timeUnitBlocksContainer6);
        mTimeUnitBlocksCointainers.add(timeUnitBlocksContainer6);
    
        JLabel trashLabelIconTimer6 = new JLabel("", trashIconSml6, JLabel.CENTER);
        trashLabelIconTimer6.setBounds(590, 196, 24, 24);
        trashLabelIconTimer6.addMouseListener(
                new OnMouseClickListenerEmptyTimeUnitBlocksContainer(timeUnitBlocksContainer6, 
                        mOnRemoveAllTimeUnitBlocksFromCointainerListener));
        frame.getContentPane().add(trashLabelIconTimer6);
    
        mDayliTimerTaskGroupContainer.get(0).setTimeUnitBlocksContainer(timeUnitBlocksContainer1);
        mDayliTimerTaskGroupContainer.get(1).setTimeUnitBlocksContainer(timeUnitBlocksContainer2);
        mDayliTimerTaskGroupContainer.get(2).setTimeUnitBlocksContainer(timeUnitBlocksContainer3);
        mDayliTimerTaskGroupContainer.get(3).setTimeUnitBlocksContainer(timeUnitBlocksContainer4);
        mDayliTimerTaskGroupContainer.get(4).setTimeUnitBlocksContainer(timeUnitBlocksContainer5);
        mDayliTimerTaskGroupContainer.get(5).setTimeUnitBlocksContainer(timeUnitBlocksContainer6);
    
        mTrashTimeUnitBlocksContainer = new TimeUnitBlocksContainer();
        mTrashTimeUnitBlocksContainer.setSummable(false);
        mTrashTimeUnitBlocksContainer.setBackground(Color.DARK_GRAY);
        mTrashTimeUnitBlocksContainer.setBounds(399, 225, 192, 30);
        frame.getContentPane().add(mTrashTimeUnitBlocksContainer);
        mTimeUnitBlocksCointainers.add(mTrashTimeUnitBlocksContainer);
    
        JLabel trashLabelIcon = new JLabel("", trashIcon, JLabel.CENTER);
        trashLabelIcon.setBounds(590, 227, 24, 24);
        trashLabelIcon.addMouseListener(
                new OnMouseClickListenerEmptyTimeUnitBlocksContainer(mTrashTimeUnitBlocksContainer, 
                        mOnRemoveAllTimeUnitBlocksFromCointainerListener));
        frame.getContentPane().add(trashLabelIcon);
    
        mDaySummatorTimeUnitBlocksContainer = new TimeUnitBlocksContainer();
        mDaySummatorTimeUnitBlocksContainer.setConvertOnAddRemoveTimeUnitBlockListener(mAddRemoveTimeUnitBlockListener);
        mDaySummatorTimeUnitBlocksContainer.setSummable(false);
        mDaySummatorTimeUnitBlocksContainer.setBackground(Color.DARK_GRAY);
        mDaySummatorTimeUnitBlocksContainer.setBounds(320, 29, 288, 23);
        frame.getContentPane().add(mDaySummatorTimeUnitBlocksContainer);
        mTimeUnitBlocksCointainers.add(mDaySummatorTimeUnitBlocksContainer);
        mSumatrDayliTimerTaskGroupContainer.setTimeUnitBlocksContainer(mDaySummatorTimeUnitBlocksContainer);
        initSumatrDayliTimerTaskGroupContainer();
    }
    
    private OnWindowCloseListener getOnApplicationCloseListener() {
        return new OnWindowCloseListener() {
            @Override
            public void onWindowClose() {
                mDataSaver.storeTimersValues(mDayliTimerTaskGroupContainer, mSumatrDayliTimerTaskGroupContainer);
            }
        };
    }

    private ItemListener getStartPauseItemListener() {
        return new ItemListener() {
            
            @Override
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    startSelectedDayliTimer();
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    pauseAllDayliTimers();
                }
            }
        };
    }

    private ActionListener getOnEndDayButtonClickActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isHoursReleased = releaseHours();

                if(isHoursReleased) {
                    resetApplicationTimersStates();
                }
            }
        };
    }

    private ActionListener getOnResetButtonClickActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetApplicationTimersStates();
            }

        };
    }

    private void resetApplicationTimersStates() {
        removeAllTimeUnitBlockContainersContent();
        resetAllDayliTimers();
        mTglbtnStartpause.setSelected(false);
    }

    private void pauseAllDayliTimers() {
        for (DayliTimerTaskGroup group : mDayliTimerTaskGroupContainer) {
            group.getDayliTimer().pause();
        }
    }

    private void pauseNotSelectedDayliTimers() {
        DayliTimerTaskGroup selectedDayliTimerTaskGroup  = getSelectedDayliTimerTaskGroup();

        for (DayliTimerTaskGroup group : mDayliTimerTaskGroupContainer) {

            if(group != selectedDayliTimerTaskGroup) {
                group.getDayliTimer().pause();
            }
        }
    }

    private void startSelectedDayliTimer() {
        DayliTimerTaskGroup dayliTimerTaskGroup  = getSelectedDayliTimerTaskGroup();
        dayliTimerTaskGroup.getDayliTimer().start();
    }

    private void resetAllDayliTimers() {
        for (DayliTimerTaskGroup group : mDayliTimerTaskGroupContainer) {
            group.reset();
        }

        initSumatrDayliTimerTaskGroupContainer();
    }

    private DayliTimerTaskGroup getSelectedDayliTimerTaskGroup() {
        ButtonModel selectedModel = mRadioButtonGroup.getSelection();

        for (DayliTimerTaskGroup group : mDayliTimerTaskGroupContainer) {
            if(group.getRadioButton().getModel() == selectedModel) {
                return group;
            }
        }
        return null;
    }

    private ActionListener onRadioButtonActionListener(final JRadioButton radioButton) {
            return new ActionListener() {
                JRadioButton mRadioButton = radioButton;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(mRadioButton.isSelected()) {
                        System.out.println(mRadioButton.getModel().hashCode() + " Selected");
                        if(isWorkTimerStarted()) {
                            startSelectedDayliTimer();
                            pauseNotSelectedDayliTimers();
                        }
                    }
                }
            };
        }

    private boolean isWorkTimerStarted() {
        return mTglbtnStartpause.isSelected();
    }

    private void handleRemoveClearAllTimeUnitBlockContainerContent(
            TimeUnitBlocksContainer timeUnitBlock) {
        System.out.println("Empty " + timeUnitBlock);
        removeAllTimeUnitBlocksFromCointainer(timeUnitBlock);
    }
    
    private void removeAllTimeUnitBlocksFromCointainer(TimeUnitBlocksContainer timeUnitBlocksContainer) {
        ArrayList<TimeUnitBlock> tubs = timeUnitBlocksContainer.getAllTimeUnitBlocks();
        for(TimeUnitBlock tub : tubs) {
            frame.remove(tub);
            removeCloneFromDayTimeSumator(tub);
        }
        timeUnitBlocksContainer.removeAllTimeUnitBlocks();
        frame.repaint();
    }

    private void handleOnAfterRemovedTimeUnitBlockFromContainer(TimeUnitBlock timeUnitBlock) {
        frame.remove(timeUnitBlock);
        removeCloneFromDayTimeSumator(timeUnitBlock);
        frame.repaint();
    }

    private void removeAllTimeUnitBlockContainersContent() {
        for (TimeUnitBlocksContainer tubc : mTimeUnitBlocksCointainers) {
            removeAllTimeUnitBlocksFromCointainer(tubc);
        }
    }

    private void handleTimeUnitBlockDropped(final TimeUnitBlock timeUnitBlock) {
        TimeUnitBlocksContainer previousTimeUnitBlocksContainer = timeUnitBlock.getTimeUnitBlocksContainerInWhich();
        TimeUnitBlocksContainer nextTimeUnitBlocksContainer = getContainerInWhichTimeUnitBlockIfAny(timeUnitBlock);

        if(previousTimeUnitBlocksContainer != null) {
            previousTimeUnitBlocksContainer.removeTimeUnitBlock(timeUnitBlock);
        }

        if (isAllovedMoveDirection(previousTimeUnitBlocksContainer, nextTimeUnitBlocksContainer) ) {

            if(isTimer(nextTimeUnitBlocksContainer) && isNewOrFromTrash(previousTimeUnitBlocksContainer)) {
                addCloneToDaySummatorTimeUnitBlocksContainerIfNotAlreadyInsideAndNotInTrash(timeUnitBlock, nextTimeUnitBlocksContainer);
            }

            if(isMovedFromTimerToTrash(previousTimeUnitBlocksContainer, nextTimeUnitBlocksContainer)) {
                removeCloneFromDayTimeSumator(timeUnitBlock);
            }

            boolean isAddedDirectlyToDayTimeSumator = nextTimeUnitBlocksContainer == mDaySummatorTimeUnitBlocksContainer;
            timeUnitBlock.setIsManuallyAddedToDayTimeSummator(isAddedDirectlyToDayTimeSumator);

            nextTimeUnitBlocksContainer.addTimeUnitBlock(timeUnitBlock);

            if(previousTimeUnitBlocksContainer != null) {
                previousTimeUnitBlocksContainer.reorderTimeUnitBlocks();
            }
            System.out.println("Dropped inside: " + nextTimeUnitBlocksContainer);
        } else {
            if(previousTimeUnitBlocksContainer != null) {
                previousTimeUnitBlocksContainer.addTimeUnitBlock(timeUnitBlock);
                timeUnitBlock.bringBackToPreviousPositionInContainer();
            } else {
                frame.remove(timeUnitBlock);
                frame.repaint();
            }
        }
    }

    private boolean isMovedFromTimerToTrash(
            TimeUnitBlocksContainer previousTimeUnitBlocksContainer,
            TimeUnitBlocksContainer nextTimeUnitBlocksContainer) {
        return isTimer(previousTimeUnitBlocksContainer) && isTrash(nextTimeUnitBlocksContainer);
    }

    private boolean isTrash(
            TimeUnitBlocksContainer previousTimeUnitBlocksContainer) {
        return previousTimeUnitBlocksContainer == mTrashTimeUnitBlocksContainer;
    }

    private boolean isTimer(
            TimeUnitBlocksContainer timeUnitBlocksContainer) {
        return timeUnitBlocksContainer != null && timeUnitBlocksContainer.isSummable();
    }

    private boolean isNewOrFromTrash(
            TimeUnitBlocksContainer timeUnitBlocksContainer) {
        return timeUnitBlocksContainer == null || isTrash(timeUnitBlocksContainer);
    }

    private boolean isAllovedMoveDirection(
            TimeUnitBlocksContainer previousTimeUnitBlocksContainer,
            TimeUnitBlocksContainer nextTimeUnitBlocksContainer) {

        boolean isMovedToDifferentAsCurrently = nextTimeUnitBlocksContainer != null && !nextTimeUnitBlocksContainer.equals(previousTimeUnitBlocksContainer);
        boolean isMovedFromTimerToDayTimeSumator = isTimer(previousTimeUnitBlocksContainer) && nextTimeUnitBlocksContainer == mDaySummatorTimeUnitBlocksContainer;
        boolean isMovedFromDayTimeSumator = previousTimeUnitBlocksContainer == mDaySummatorTimeUnitBlocksContainer;
        boolean isMovedToTrash = isTrash(nextTimeUnitBlocksContainer);

        return (isMovedToDifferentAsCurrently && !isMovedFromDayTimeSumator && !isMovedFromTimerToDayTimeSumator) || isMovedToTrash;
    }

    private void removeCloneFromDayTimeSumator(final TimeUnitBlock timeUnitBlock) {
        TimeUnitBlock cloneOf = mDaySummatorTimeUnitBlocksContainer.getCloneOf(timeUnitBlock);

        if(cloneOf != null) {
            mDaySummatorTimeUnitBlocksContainer.removeTimeUnitBlock(cloneOf);
            mDaySummatorTimeUnitBlocksContainer.reorderTimeUnitBlocks();
            frame.remove(cloneOf);
            frame.repaint();
        }
    }
    
    private void addCloneToDaySummatorTimeUnitBlocksContainerIfNotAlreadyInsideAndNotInTrash(final TimeUnitBlock timeUnitBlock, TimeUnitBlocksContainer nextTimeUnitBlocksContainer) {
        if(!mDaySummatorTimeUnitBlocksContainer.containCloneOf(timeUnitBlock) && !mTrashTimeUnitBlocksContainer.containCloneOf(timeUnitBlock)) {
            TimeUnitBlock cloneTubc = timeUnitBlock.clone();
            frame.getContentPane().add(cloneTubc);
            frame.getContentPane().setComponentZOrder(cloneTubc, firstTimeUnitBoxZIndex);
            mDaySummatorTimeUnitBlocksContainer.addTimeUnitBlock(cloneTubc);
        }
    }

    private void addClonedTimeUnitBlockToContainerAndDayliSumator(final TimeUnitBlock timeUnitBlock, final TimeUnitBlocksContainer timeUnitBlocksContainer) {
        TimeUnitBlock cloneTubc = timeUnitBlock.clone();
        frame.getContentPane().add(cloneTubc);
        frame.getContentPane().setComponentZOrder(cloneTubc, firstTimeUnitBoxZIndex);
        timeUnitBlocksContainer.addTimeUnitBlock(cloneTubc);

        addCloneToDaySummatorTimeUnitBlocksContainerIfNotAlreadyInsideAndNotInTrash(cloneTubc, timeUnitBlocksContainer);
    }

    private void addFiveMinuteTimeUnitBlockToContainer (final TimeUnitBlocksContainer timeUnitBlocksContainer) {
        addClonedTimeUnitBlockToContainerAndDayliSumator(mFiveMinuteTimeUnitBlock, timeUnitBlocksContainer);
    }

    private void addTenMinutesTimeUnitBlockToContainer (final TimeUnitBlocksContainer timeUnitBlocksContainer) {
        addClonedTimeUnitBlockToContainerAndDayliSumator(mTenMinutesTimeUnitBlock, timeUnitBlocksContainer);
    }

    private void addHalfHourTimeUnitBlockToContainer (final TimeUnitBlocksContainer timeUnitBlocksContainer) {
        addClonedTimeUnitBlockToContainerAndDayliSumator(mHalfHourTimeUnitBlock, timeUnitBlocksContainer);
    }

    private void addOneHourTimeUnitBlockToContainer (final TimeUnitBlocksContainer timeUnitBlocksContainer) {
        addClonedTimeUnitBlockToContainerAndDayliSumator(mOneHourTimeUnitBlock, timeUnitBlocksContainer);
    }

    private boolean istimeUnitBlockInsideContainer(TimeUnitBlocksContainer timeUnitBlocksContainer, TimeUnitBlock timeUnitBlock) {
        Rectangle timeUnitBlockBounds = timeUnitBlock.getBounds();
        Rectangle timeUnitBlocksContainerBounds = timeUnitBlocksContainer.getBounds();

        return (((timeUnitBlockBounds.x > timeUnitBlocksContainerBounds.x) && (timeUnitBlockBounds.x + timeUnitBlockBounds.width < timeUnitBlocksContainerBounds.x + timeUnitBlocksContainerBounds.width)) &&
                ((timeUnitBlockBounds.y > timeUnitBlocksContainerBounds.y) && (timeUnitBlockBounds.y + timeUnitBlockBounds.height < timeUnitBlocksContainerBounds.y + timeUnitBlocksContainerBounds.height)));
    }

    private TimeUnitBlocksContainer getContainerInWhichTimeUnitBlockIfAny(TimeUnitBlock timeUnitBlock) {
        for(TimeUnitBlocksContainer timeUnitBlockContainer : mTimeUnitBlocksCointainers) {
            if (istimeUnitBlockInsideContainer(timeUnitBlockContainer, timeUnitBlock)) {
                return timeUnitBlockContainer;
            }
        }
        return null;
    }

    private boolean isConversionTimeUnitBlocksFromSmallerToBiggerAllowed() {
        // TODO Auto-generated method stub. Implement:  return true when none of TUB is in move
        return true;
    }
    
    private boolean showConformDialog(String message) {
        int dialogButton = JOptionPane.OK_CANCEL_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, message , "Confirm", dialogButton);
        return (dialogResult == 0);
    }

    private boolean releaseHours() {
        final int OK_OPTION = 0; 

        JCheckBox endOfWeekCheckBox = new JCheckBox();
        JCheckBox endOfMonthCheckBox = new JCheckBox();
        endOfWeekCheckBox.setText("End of week");
        endOfMonthCheckBox.setText("End of month");

        JComponent[] inputs = new JComponent[] {
                new JLabel("Release hours ?"),
                endOfWeekCheckBox,
                endOfMonthCheckBox
        };

        int dialogResult = JOptionPane.showConfirmDialog(null, inputs, "End day", JOptionPane.OK_CANCEL_OPTION);
        boolean isReleaseHoursConfirmed = (dialogResult == OK_OPTION);

        if (isReleaseHoursConfirmed) {
            mDataSaver.storeReleasedTimersValues(endOfWeekCheckBox.isSelected(), 
                                                 endOfMonthCheckBox.isSelected(), 
                                                 mDayliTimerTaskGroupContainer, 
                                                 mSumatrDayliTimerTaskGroupContainer);
            
            updateGlobalDeltaTimerAtHoursRelease();
        }

        System.out.println("End of week: " + endOfWeekCheckBox.isSelected() + ", End of month: " + endOfMonthCheckBox.isSelected());

        return isReleaseHoursConfirmed;
    }

    private void updateGlobalDeltaTimerAtHoursRelease() {
        String globalDeltatimerDisplay = Utils.formatTimerFromSeconds(mWorkTimmerSummary.globalDelta + 
                                             mSumatrDayliTimerTaskGroupContainer.getDayliTimer().getCounterValue());
        mGlbalDeltaTextPane.setText(globalDeltatimerDisplay);
    }
}
