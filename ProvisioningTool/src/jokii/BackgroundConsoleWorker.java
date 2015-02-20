package jokii;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

public class BackgroundConsoleWorker extends SwingWorker<Void, Void> {

    private final BackgroundConsoleWorkerExecutor mExecutor;

    public BackgroundConsoleWorker(BackgroundConsoleWorkerExecutor executor) {
        if(executor == null) {
            throw new IllegalArgumentException("BackgroundConsoleWorkerExecutor cannot be null");
        }
        mExecutor = executor;
    }

    public void runConsoleCommand(String command) {
        String printline;

        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((printline = input.readLine()) != null) {
                mExecutor.printOutputLine(printline);
            }

            input.close();
        } catch (IOException e) {
            mExecutor.printOutputLine(e.getMessage());
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        mExecutor.doInBackground(this);
        return null;
    }

    public interface BackgroundConsoleWorkerExecutor {
        public void doInBackground(BackgroundConsoleWorker backgroundConsoleWorker);
        public void printOutputLine(String printline);
    }
}