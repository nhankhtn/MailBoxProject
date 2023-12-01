package MailBoxProject.model;

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
    }
}
