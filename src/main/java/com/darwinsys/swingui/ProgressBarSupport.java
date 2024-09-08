package com.darwinsys.swingui;

import javax.swing.*;
import java.awt.*;

public class ProgressBarSupport {

    final JProgressBar progressBar;
    final JDialog progressDialog;

    public ProgressBarSupport(Window frame, String title) {
        // Progress bar for generate operations
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressDialog = new JDialog(frame, title);
        progressDialog.add(progressBar);
        JPanel cancelPanel = new JPanel();
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> progressDialog.setVisible(false));
        cancelPanel.add(cancelButton);

        progressDialog.add(cancelPanel, BorderLayout.SOUTH);
        progressDialog.pack(); // Default is not wide enough to show title.
        progressDialog.setSize(progressDialog.getWidth() * 2, (int)(progressDialog.getHeight()*1.5));
        UtilGUI.center(progressDialog);
    }

    /**
     * runWithProgressBar - run one runnable in a background thread and another
     * on the UI (EDT) thread, using a SwingWorker.
     * @param r The Runnable to run on the background thread
     * @param r2 The Runnable to run on the UI/EDT thread
     */
    public void runWithProgressBar(final Runnable r, final Runnable r2) {
        System.out.println("runWithProgressBar");
        progressDialog.setVisible(true);
        new SwingWorker() {
            protected Void doInBackground() {
                System.out.println("ProgressBarSupport.doInBackground");
                r.run();
                return null;
            }
            protected void done() {
                System.out.println("ProgressBarSupport.done");
                r2.run();
                progressDialog.setVisible(false);
            }
        }.execute();
    }
}
