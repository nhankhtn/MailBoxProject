package MailBox.model;

public class autoLoadMail extends Thread {
    MailBox mailBox;
    int autoLoad;

    autoLoadMail(MailBox mailBox, int autoLoad) {
        this.mailBox = mailBox;
        this.autoLoad = autoLoad;
    }

    @Override
    public void run() {
        while(true) { 
            try {
                Thread.sleep(autoLoad*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mailBox.cloneEmail();
        }
    }
    
}
