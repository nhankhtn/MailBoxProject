package model;

import view.home;

public class autoLoadMail extends Thread {
    private home home;

    public autoLoadMail(home home) {
        this.home = home;
    }

    @Override
    public void run() {
        while(true) { 
            try {
                Thread.sleep(home.getMailBox().getAutoLoad()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.home.autoRender();
        }
    }
    
}
