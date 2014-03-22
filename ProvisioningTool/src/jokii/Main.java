package jokii;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.JComboBox;
import com.thoughtworks.xstream.XStream;
import java.awt.event.ActionListener;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class Main {

    private JFrame              mFrame;
    private JTextField          mEmailTextField;
    private JTextField          mOtaPinTextField;
    private JTextPane           mConsoleOutputJTextPane;
    private JComboBox<String>   mHistoryComboBox;
    private JTextField          mDescriptionTextField;
    private JButton             mBtnSave;

    private ProvisioningData    mStorageData;
    private XStream             mXstream                = new XStream();


    /**
     * Launch the application.
     */
    public static void main(String[] args) {


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.mFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Create the application.
     */
    public Main() {
        initialize();
    }


    private void readData() {
        try {
            mStorageData = (ProvisioningData)mXstream.fromXML(new FileInputStream(ProjectConst.DATA_FILE_NAME));
        } catch (FileNotFoundException e) {
            System.out.println("No data to load - " + ProjectConst.DATA_FILE_NAME + "doesn't exist in current path.");
        } catch (Exception e) {
            System.out.println("Error on start application: " + e.toString());
        }
    }


    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        readData();

        mFrame = new JFrame();
        mFrame.setTitle("Provisioning Tool");
        mFrame.setResizable(false);
        mFrame.setBounds(100, 100, 718, 430);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.getContentPane().setLayout(null);

        JButton btnProvision = new JButton("Provision");
        btnProvision.addActionListener(mProvisionActionListener);
        btnProvision.setBounds(10, 357, 89, 23);
        mFrame.getContentPane().add(btnProvision);

        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(mExitActionListener);
        btnExit.setBounds(109, 357, 89, 23);
        mFrame.getContentPane().add(btnExit);

        mEmailTextField = new JTextField();
        mEmailTextField.setBounds(10, 25, 266, 20);
        mFrame.getContentPane().add(mEmailTextField);
        mEmailTextField.setColumns(10);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(10, 11, 46, 14);
        mFrame.getContentPane().add(lblEmail);

        mOtaPinTextField = new JTextField();
        mOtaPinTextField.setBounds(286, 25, 222, 20);
        mFrame.getContentPane().add(mOtaPinTextField);
        mOtaPinTextField.setColumns(10);

        JLabel lblOta = new JLabel("OTA PIN");
        lblOta.setBounds(286, 11, 46, 14);
        mFrame.getContentPane().add(lblOta);

        mConsoleOutputJTextPane = new JTextPane();
        mConsoleOutputJTextPane.setEditable(false);
        mConsoleOutputJTextPane.setBounds(10, 141, 692, 173);
        mFrame.getContentPane().add(mConsoleOutputJTextPane);

        JLabel lblConsoleOutput = new JLabel("Console output");
        lblConsoleOutput.setBounds(10, 124, 89, 14);
        mFrame.getContentPane().add(lblConsoleOutput);

        mHistoryComboBox = new JComboBox<String>();
        mHistoryComboBox.setBounds(518, 54, 181, 23);

        fillComboBox();

        mHistoryComboBox.addItemListener(mComboItemListener);
        mFrame.getContentPane().add(mHistoryComboBox);

        mDescriptionTextField = new JTextField();
        mDescriptionTextField.setBounds(518, 25, 181, 20);
        mFrame.getContentPane().add(mDescriptionTextField);
        mDescriptionTextField.setColumns(10);

        JLabel lblDescription = new JLabel("Description");
        lblDescription.setBounds(518, 11, 74, 14);
        mFrame.getContentPane().add(lblDescription);

        mBtnSave = new JButton("Save");
        mBtnSave.addActionListener(mSaveActionListener);
        mBtnSave.setBounds(286, 54, 89, 23);
        mFrame.getContentPane().add(mBtnSave);

        JButton mRemoveBtn = new JButton("Remove");
        mRemoveBtn.addActionListener(mRemoveActionListener);
        mRemoveBtn.setBounds(419, 54, 89, 23);
        mFrame.getContentPane().add(mRemoveBtn);

        JButton button = new JButton("?");
        button.addActionListener(mAboutActionListener);
        button.setBounds(696, 378, 16, 23);
        mFrame.getContentPane().add(button);
        mFrame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{mEmailTextField, mOtaPinTextField, mDescriptionTextField, mBtnSave, mRemoveBtn, mHistoryComboBox, btnProvision, btnExit}));

        if(mStorageData != null && !mStorageData.getList().isEmpty()) {
            ProvisioningItem firstElement = mStorageData.getList().get(0);
            fillDisplayFields(firstElement.email, firstElement.otaPin, firstElement.description);
        }
    }


    private ItemListener mComboItemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent arg0) {
            int selectedItemIndex = mHistoryComboBox.getSelectedIndex();
            if(selectedItemIndex != -1) {
                ProvisioningItem chosenConfiguration = mStorageData.getList().get(selectedItemIndex);

                mConsoleOutputJTextPane.setText("Selected:\n" + chosenConfiguration.description);

                fillDisplayFields(chosenConfiguration.email, chosenConfiguration.otaPin, chosenConfiguration.description);
            }
        }
    };


    protected void fillDisplayFields(String email, String otaPin, String description) {
        mEmailTextField.setText(email);
        mOtaPinTextField.setText(otaPin);
        mDescriptionTextField.setText(description);
    }


    protected void fillInitialDisplayFields() {
        if(mStorageData != null && !mStorageData.getList().isEmpty()) {
            ProvisioningItem firstElement = mStorageData.getList().get(0);
            fillDisplayFields(firstElement.email, firstElement.otaPin, firstElement.description);
        }
    }


    private void fillComboBox() {
        if(mStorageData != null) {
            if(mHistoryComboBox.getItemCount() > 0) {
                mHistoryComboBox.removeAllItems();
            }

            for(ProvisioningItem item : mStorageData.getList()) {
                mHistoryComboBox.addItem(item.description);
            }
        }
    }


    private ActionListener mProvisionActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                String email  = mEmailTextField.getText();
                String otaPin = mOtaPinTextField.getText();

                if(validateProvisioningData(email, otaPin)) {
                    executeProvision(email, otaPin);
                }

            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    };


    private void showInfoDialog(String title, String body) {
        JOptionPane.showMessageDialog(mFrame,
                body,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }


    private void showAboutDialog(){
        final int OK_OPTION              = 0;
        final int XSTREAM_LICENSE_OPTION = 1;

        Object[] options = {"OK",
                            "XStream license"};
        int selectedOption = JOptionPane.showOptionDialog(mFrame,
                        "Author: Tomasz Jokiel\n" +
                        "version: " + ProjectConst.APP_VERSION,
        "Application info",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,                   //do not use a custom Icon
        options,                //the titles of buttons
        options[OK_OPTION]);    //default button title
        
        if(selectedOption == XSTREAM_LICENSE_OPTION) {
            showInfoDialog("XStream license", readFileContentFromResource(ProjectConst.XSTREAM_LICENSE_FILE_NAME));
        }
    }


    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(mFrame, message);
    }


    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(mFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


    private boolean showConfirmDialog(String message) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, message , "Confirm", dialogButton);
        return (dialogResult == 0);
    }


    private ActionListener mExitActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };


    private ActionListener mAboutActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            showAboutDialog();
//            showInfoDialog("Application info",  "Author: Tomasz Jokiel\n" +
//                    "version: " + ProjectConst.APP_VERSION);
        }
    };

    //	private void executeCommand(final String command) throws IOException {
    //		
    //		// define a SwingWorker to run in background  
    //		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()  
    //				{  
    //			public Void doInBackground() throws IOException  
    //			{  
    //				StringBuilder sb = new StringBuilder();
    //				String printline;
    //				Process process = Runtime.getRuntime().exec(command);
    //				
    //				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
    //				
    //				while ((printline = input.readLine()) != null) {
    //					System.out.println(printline);
    //					sb.append(printline).append("\n");
    //					mConsoleOutputJTextPane.setText(sb.toString());
    //				}
    //				input.close(); 
    //				return null;  
    //			}  
    //				};  
    //				
    //				// execute the background thread  
    //				worker.execute();
    //	}


    private void executeProvision(final String email, final String otaPin) throws IOException {

        // define a SwingWorker to run in background  
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()  
                {  
            private void runCommandAndPrintOutput(String command) throws IOException {
                StringBuilder sb = new StringBuilder();
                String printline;

                Process process = Runtime.getRuntime().exec(command);
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

                while ((printline = input.readLine()) != null) {
                    System.out.println(printline);
                    sb.append(printline).append("\n");
                    mConsoleOutputJTextPane.setText(sb.toString());
                }
                input.close(); 
            }

            public Void doInBackground() throws IOException, InterruptedException  
            {  
                runCommandAndPrintOutput("adb shell input text " + email);
                runCommandAndPrintOutput("adb shell input keyevent 66");

                String[] otaPinArray = otaPin.split("-");
                runCommandAndPrintOutput("adb shell input text " + otaPinArray[0]);
                Thread.sleep(1000);
                runCommandAndPrintOutput("adb shell input text " + otaPinArray[1]);
                Thread.sleep(1000);
                runCommandAndPrintOutput("adb shell input text " + otaPinArray[2]);

                runCommandAndPrintOutput("adb shell input keyevent 66");

                return null;  
            }  
                };  

                // execute the background thread  
                worker.execute();
    }


    private ActionListener mSaveActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String emailText  = mEmailTextField.getText();
            String otaPinText = mOtaPinTextField.getText();

            if(emailText.isEmpty() 
                || otaPinText.isEmpty()
                || mDescriptionTextField.getText().isEmpty()) {
                showMessageDialog("Please fill all fields");
                return;
            }

            if(validateProvisioningData(emailText, otaPinText) && updateStorage()) {
                fillComboBox();
                showMessageDialog("Saved");
            }
        }
    };


    public boolean updateStorage() {
        if(mStorageData == null) {
            mStorageData = new ProvisioningData();
        }

        ProvisioningItem data = new ProvisioningItem();
        data.email 		 = mEmailTextField.getText();
        data.otaPin 	 = mOtaPinTextField.getText();
        data.description = mDescriptionTextField.getText();

        if(!mStorageData.getList().contains(data)) {
            mStorageData.add(data);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(ProjectConst.DATA_FILE_NAME);
                mXstream.toXML(mStorageData, fos);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                showErrorDialog("Storage access denny");
            } finally {
                try {
                    if(fos != null) {
                        fos.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            showErrorDialog("Entry already exist");
            return false;
        }

        return true;
    }

    ActionListener mRemoveActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            int currentItem = mHistoryComboBox.getSelectedIndex();

            if(currentItem != -1) {
                String itemDescription = mStorageData.getList().get(currentItem).description;
                boolean result = showConfirmDialog("Are you sure you want to remove\n\n\"" + itemDescription + "\"\n\n");

                if(result) {
                    mHistoryComboBox.removeItemAt(currentItem);

                    mStorageData.getList().remove(currentItem);

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(ProjectConst.DATA_FILE_NAME);
                        mXstream.toXML(mStorageData, fos);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                        showErrorDialog("Storage access denny");
                    } finally {
                        try {
                            if(fos != null) {
                                fos.close();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    fillInitialDisplayFields();
                }
            }
        }
    };


//    private String readFileContent(String filePath) throws IOException {
//        String fileContentString = "";
//        BufferedReader br = new BufferedReader(new FileReader(filePath));
//        try {
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                sb.append(line);
//                sb.append(System.lineSeparator());
//                line = br.readLine();
//            }
//            fileContentString = sb.toString();
//        } finally {
//            br.close();
//        }
//
//        return fileContentString;
//    }


    private String readFileContentFromResource(String pathToResouceTextFile) {
        String fileContentString = "";

        InputStream stream = Main.class.getResourceAsStream("/"+pathToResouceTextFile);

        if (stream != null) {
            Scanner input = null;
            try {
                input = new Scanner (stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();

            while (input.hasNextLine()) {
                sb.append(input.nextLine());
                sb.append(System.lineSeparator());
            }

            fileContentString = sb.toString();
        }

        return fileContentString;
    }


    private boolean validateProvisioningData(String emailText, String otaPinText) {
        boolean isValid = false;

        if(!validateEmail(emailText)) {
            showErrorDialog("Provide proper email");
        } else if(!validateOtaPin(otaPinText)) {
            showErrorDialog("Provide proper PIN\nformat: xxx-xxx-xxx");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private boolean validateEmail(String emailText) {
        if(emailText == null) {
            return false;
        }
        final String EMAIL_PATTERN = 
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailText);
        return matcher.matches();
    }


    private boolean validateOtaPin(String otaPinText) {
        if(otaPinText == null) {
            return false;
        }
//        final String OTA_PIN_PATTERN = "^[\\w]+\\-[\\w]+\\-[\\w]+\\-[\\w]+$";
        final String OTA_PIN_PATTERN = "^[\\w]+\\-[\\w]+\\-[\\w]+$";
        
        Pattern pattern = Pattern.compile(OTA_PIN_PATTERN);
        Matcher matcher = pattern.matcher(otaPinText);
        return matcher.matches();
    }
}