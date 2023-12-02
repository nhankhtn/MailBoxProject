package MailBox.model;

import java.util.Arrays;
import java.util.Date;

/*
* Defines an email to display
*/
public class mail {
    String from;
    String to[],cc[];
    int numTo = 0, numCc = 0;
    String subject, content, time;
    int numFiles; 
    String[] nameFiles;
    boolean status; // true nếu mail chưa được đọc

    public mail(String from, String[] to, int numTo, String[] cc, int numCc, String subject, String content, String time, int numFiles, String[] nameFiles) {
        this.from = from;
        this.to = new String[numTo];
        System.arraycopy(to, 0, this.to, 0, numTo);
        this.numTo = numTo;
        this.cc = new String[numCc];
        System.arraycopy(cc, 0, this.cc, 0, numCc);
        this.numCc = numCc;
        this.subject = subject;
        this.content = content;
        this.time = time;
        this.numFiles = numFiles;
        this.nameFiles = new String[numFiles];
        System.arraycopy(nameFiles, 0, this.nameFiles, 0, numFiles);
        status = true;
    }
    
    public boolean checkEmpty() {
        return this.from.equals("");
    }

    @Override
    public String toString() {
        return "mail [from=" + from + ", to=" + Arrays.toString(to) + ", subject=" + subject + ", content=" + content
                + ", numFiles=" + numFiles + ", nameFiles=" + Arrays.toString(nameFiles) + "]";
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
