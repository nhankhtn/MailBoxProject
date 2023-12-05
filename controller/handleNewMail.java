package MailBox.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import MailBox.view.newMailUI;

public class handleNewMail implements ActionListener{
    private newMailUI newMailUI;
    
    public handleNewMail(newMailUI newMailUI) {
        this.newMailUI = newMailUI;
    }
     
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println(command);
    }
    
}
