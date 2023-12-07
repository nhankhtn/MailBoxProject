package MailBox.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JEditorPane;

import MailBox.view.home;

public class handleHome implements ActionListener, MouseListener {
    private home home;

    public handleHome(home home) {
        this.home = home;
    }

    /*
     * Handle button click events in the main interface
     * 
     * When clicking the All, Important, Work, Project, Spam
     * buttons, we will reset the pageCurrent variable, then
     * delete the mails on the old page and reset the current mail list.
     * Finally, display that mail list again on the interface
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("All")) {
            this.home.setPageCurrent("home");
            this.home.clear();
            this.home.setListMailsCurrent("home");
            this.home.displayMails();
        } else if (command.equals("Important")) {
            this.home.setPageCurrent("important");
            this.home.clear();
            this.home.setListMailsCurrent("important");
            this.home.displayMails();
        } else if (command.equals("Work")) {
            this.home.setPageCurrent("work");
            this.home.clear();
            this.home.setListMailsCurrent("work");
            this.home.displayMails();
        } else if (command.equals("Project")) {
            this.home.setPageCurrent("project");
            this.home.clear();
            this.home.setListMailsCurrent("project");
            this.home.displayMails();
        } else if (command.equals("Spam")) {
            this.home.setPageCurrent("spam");
            this.home.clear();
            this.home.setListMailsCurrent("spam");
            this.home.displayMails();
        } else if (command.equals("reload")) {
            this.home.getMailBox().cloneEmail();
            this.home.clear();
            this.home.setListMailsCurrent(this.home.getPageCurrent());
            this.home.displayMails();
        } else if (command.equals("newMail")) {
            this.home.displaySendMail();
        }  else if (command.equals("Auto Save")) {
            this.home.getMailBox().setAutoSaveFile(!this.home.getAutoSave());
        } 
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof JEditorPane) {
            JEditorPane clickedEditorPane = (JEditorPane) e.getSource();
            String idPanel = clickedEditorPane.getName();
            home.renderMailReading(idPanel);
            home.changeStatus(idPanel, false);
            home.getMailBox().setStatus(idPanel, false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
