package MailBox.model;

import javax.swing.plaf.SliderUI;

public class autoLoadMail extends Thread {
    receiveHandler receiveHandler;
    int autoLoad;

    autoLoadMail(receiveHandler receiveHandler, int autoLoad) {
        this.receiveHandler = receiveHandler;
        this.autoLoad = autoLoad;
    }

    @Override
    public void run() {
        while(true) {
            this.receiveHandler.cloneEmail();
            try {
                Thread.sleep(autoLoad*60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
