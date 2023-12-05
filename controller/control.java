package MailBox.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JEditorPane;

import MailBox.view.home;

public class control implements ActionListener, MouseListener {
    private home home;
    private String pageCurrent;

    public control(home home) {
        this.home = home;
        pageCurrent = "home";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("All")) {
            pageCurrent = "home";
            this.home.clear(this.home.getListMailsCurrent().size());
            this.home.setListMailsCurrent(pageCurrent);
            this.home.displayMails();
        } else if (command.equals("Important")) {
            pageCurrent = "important";
            this.home.clear(this.home.getListMailsCurrent().size());
            this.home.setListMailsCurrent(pageCurrent);
            this.home.displayMails();
        } else if (command.equals("Work")) {
            pageCurrent = "work";
            this.home.clear(this.home.getListMailsCurrent().size());
            this.home.setListMailsCurrent(pageCurrent);
            this.home.displayMails();
        } else if (command.equals("Project")) {
            pageCurrent = "project";
            this.home.clear(this.home.getListMailsCurrent().size());
            this.home.setListMailsCurrent(pageCurrent);
            this.home.displayMails();
        } else if (command.equals("Spam")) {
            pageCurrent = "spam";
            this.home.clear(this.home.getListMailsCurrent().size());
            this.home.setListMailsCurrent(pageCurrent);
            this.home.displayMails();
        } else if (command.equals("clone")) {

        } else if (command.equals("newMail")) {
            this.home.sendMail();
        } 
        System.out.println(command);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() instanceof JEditorPane) {
            JEditorPane clickedEditorPane = (JEditorPane) e.getSource();
            String idPanel = clickedEditorPane.getName();
            home.renderMailReading(idPanel);
            home.changeStatus(idPanel, false);
            home.getMailBox().setStatus(idPanel, false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
