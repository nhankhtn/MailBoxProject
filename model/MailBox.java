package MailBox.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MailBox {
    private String user, password, mailServer;
    private int SMTPport, POPport, autoLoad;
    private receiveHandler receiveHandler;
    private sendHandler sendHandler;
    private String pathSaveFile;
    private boolean autoSaveFile;

    public MailBox() {
        this.configure();
    }

    public String getUser() {
        return user;
    }

    public int getAutoLoad(){
        return autoLoad;
    }

    public void setAutoSaveFile(boolean autoSaveFile) {
        this.autoSaveFile = autoSaveFile;
    }

    public void configure() {
        try {
            // Get the relative path of the config.xml file
            File xmlFile = new File(Paths.get("").toAbsolutePath().toString() + "\\MailBox\\config.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Read basic information for mailbox
            NodeList nodeList_1 = doc.getElementsByTagName("General");
            Element element_1 = (Element) nodeList_1.item(0);
            user =element_1.getElementsByTagName("Username").item(0).getTextContent();
            password =element_1.getElementsByTagName("Password").item(0).getTextContent();
            mailServer =element_1.getElementsByTagName("MailServer").item(0).getTextContent();
            SMTPport = Integer.parseInt(element_1.getElementsByTagName("SMTP").item(0).getTextContent());
            POPport = Integer.parseInt(element_1.getElementsByTagName("POP3").item(0).getTextContent());
            autoLoad = Integer.parseInt(element_1.getElementsByTagName("Autoload").item(0).getTextContent());

            NodeList nodeList_2 = doc.getElementsByTagName("Configure");
            Element element_2 = (Element) nodeList_2.item(0);
            pathSaveFile = element_2.getElementsByTagName("PathDefault").item(0).getTextContent();
            autoSaveFile = Boolean.parseBoolean(element_2.getElementsByTagName("AutoSave").item(0).getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Get mail from the server and save it to file
     */
    public void cloneEmail() {
        try {
            this.receiveHandler = new receiveHandler(mailServer, POPport, user, password, totalMail());
            receiveHandler.cloneEmail(pathSaveFile, autoSaveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<mail> newMails = receiveHandler.getMails();
        this.saveMailsToFile(newMails);
    }

    public void sendMail(ArrayList<String> to, String subject, String msg, ArrayList<String> cc, ArrayList<String> bcc,
            ArrayList<String> pathFiles) throws IOException {
        this.sendHandler = new sendHandler(mailServer, SMTPport);
        sendHandler.sendEmail(user, to, subject, msg, cc, bcc, pathFiles);
    }

    /*
     * Get the current path of the project
     */
    public String getPathCurrent() {
        return Paths.get("").toAbsolutePath().toString()+"\\MailBox";
    }

    public void saveMailsToFile(ArrayList<mail> newMails)  {
       for (mail mail : newMails) 
           if(!mail.checkEmpty()) {
               Path filePath = Paths.get(getPathCurrent() + "\\storeMail\\"+mail.getId()+".xml");
               if(!Files.exists(filePath)) {
                   mail.saveMailToFile(getPathCurrent() + "\\storeMail\\"+mail.getId()+".xml");
               }
           }
    }

    /*
     * Count the number of mails in the directory storeMail
     */
    public int totalMail() {
        File storeMail = new File(getPathCurrent()+"\\storeMail");

        FilenameFilter filterFile = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        };
        File[] listMails = storeMail.listFiles(filterFile);

        if (listMails != null) {
            return listMails.length;
        } else {
            return 0;
        }
    }

    /*
     * Change the read mail status back to the file
     */
    public void setStatus(String idMail, boolean status) {
        try {
            File xmlFile = new File(getPathCurrent() + "\\storeMail\\" + idMail + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            NodeList statusList = doc.getElementsByTagName("Status");

            if (statusList.getLength() > 0) {
                Element statusElement = (Element) statusList.item(0);

                statusElement.setTextContent("false");
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Get mail list from directory storeMail
     */
    public ArrayList<mail> getMails() {
        ArrayList<mail> mails = new ArrayList<mail>();
        try {
            File storeMail = new File(getPathCurrent()+"\\storeMail");
            FilenameFilter filterFile = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".xml");
                }
            };
            File[] listMails = storeMail.listFiles(filterFile);

            for (File file : listMails) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);

                String name = file.getName();
                String id = name.substring(name.lastIndexOf("\\")+1, name.length() - 4);
                String from = doc.getElementsByTagName("From").item(0).getTextContent();
                String to = doc.getElementsByTagName("To").item(0).getTextContent();
                String cc = doc.getElementsByTagName("Cc").item(0).getTextContent();
                String time = doc.getElementsByTagName("Time").item(0).getTextContent();
                String subject = doc.getElementsByTagName("Subject").item(0).getTextContent();
                String content = doc.getElementsByTagName("Content").item(0).getTextContent();
                String files = doc.getElementsByTagName("Files").item(0).getTextContent();
                Boolean status = Boolean.parseBoolean(doc.getElementsByTagName("Status").item(0).getTextContent());

                mail mail = new mail(id , from, to, cc, subject, content, time, files, status);
                mails.add(mail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mails;
    }

    public ArrayList<mail> getMailImportant(){
        ArrayList<mail> mailList = this.getMails();
        ArrayList<mail> mailImportant = new ArrayList<mail>();
        for(int i=0; i<mailList.size(); i++) {
            if(mailList.get(i).getTypeMail().indexOf("important") >= 0) 
                mailImportant.add(mailList.get(i));
        }
        return mailImportant;
    }

    public ArrayList<mail> getMailProject(){
        ArrayList<mail> mailsList = this.getMails();
        ArrayList<mail> mailsImportant = new ArrayList<mail>();
        for(int i=0; i<mailsList.size(); i++) 
            if(mailsList.get(i).getTypeMail().indexOf("project") >= 0) 
                mailsImportant.add(mailsList.get(i));
        return mailsImportant;
    }

    public ArrayList<mail> getMailWork(){
        ArrayList<mail> mailList = this.getMails();
        ArrayList<mail> mailImportant = new ArrayList<mail>();
        for(int i=0; i<mailList.size(); i++) 
            if(mailList.get(i).getTypeMail().indexOf("work") >= 0) 
                mailImportant.add(mailList.get(i));
        return mailImportant;
    }

    public ArrayList<mail> getMailSpam(){
        ArrayList<mail> mailList = this.getMails();
        ArrayList<mail> mailImportant = new ArrayList<mail>();
        for(int i=0; i<mailList.size(); i++) 
            if(mailList.get(i).getTypeMail().indexOf("spam") >= 0) 
                mailImportant.add(mailList.get(i));
        return mailImportant;
    }

}
