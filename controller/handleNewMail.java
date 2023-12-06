package MailBox.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import MailBox.view.newMailUI;

public class handleNewMail implements ActionListener {
    private newMailUI newMailUI;

    public handleNewMail(newMailUI newMailUI) {
        this.newMailUI = newMailUI;
    }

    /*
     * Handle the event click the button on the newMailUI
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Send")) {
            this.newMailUI.sendMail();
            this.newMailUI.setVisible(false);
        } else if (command.equals("Cancel")) {
            this.newMailUI.setVisible(false);
        } else if (command.equals("Attach")) {
            this.newMailUI.setPathFiles(this.newMailUI.getPathFilesSelected());
            this.newMailUI.renderFilesSelector();
        }
    }

}
