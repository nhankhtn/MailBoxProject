package MailBox.model;

import java.util.ArrayList;

/*
* Defines an email to display
*/

public class mail {
    String from;
    ArrayList<String> to, cc, files;
    String subject, content, time;
    boolean status; // true nếu mail chưa được đọc
    ArrayList<String> typeMail;

    public mail(String from, ArrayList<String> to, ArrayList<String> cc, String subject, String content, String time, ArrayList<String> files) {
        this.from = from;
        this.to = new ArrayList<>();
        this.to.addAll(to);
        this.cc = new ArrayList<>();
        this.cc.addAll(cc);
        this.subject = subject;
        this.content = content;
        this.time = time;
        this.files = new ArrayList<>();
        this.files.addAll(files);
        status = true;
        this.setTypeMail();
    }
    
    public boolean checkEmpty() {
        return this.from.equals("");
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<String> getTypeMail() {
        return typeMail;
    }

    public void setTypeMail() {
        typeMail = new ArrayList<>();
        if(from.endsWith("@testing.com")) 
            typeMail.add("project");
        if(subject.equals("urgent") || subject.equals("ASAP")) 
            typeMail.add("important");
        if(content.indexOf("report" ) != -1 || content.indexOf("meeting" ) != -1)
            typeMail.add("work");
        if(subject.indexOf("virus") != -1 || subject.indexOf("hack") != -1 || subject.indexOf("crack") != -1
        || content.indexOf("virus") != -1 || content.indexOf("hack") != -1 || content.indexOf("crack") != -1) 
           typeMail.add("spam");
    }
    
}
