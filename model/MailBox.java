package MailBox.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
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
            // Lấy đường dẫn tương đối của file config.xml
            File xmlFile = new File(Paths.get("").toAbsolutePath().toString() + "\\MailBox\\config.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            // Đọc thông tin cơ bản
            NodeList nodeList = doc.getElementsByTagName("General");

            Element element = (Element) nodeList.item(0);
            user = element.getElementsByTagName("Username").item(0).getTextContent();
            password = element.getElementsByTagName("Password").item(0).getTextContent();
            mailServer = element.getElementsByTagName("MailServer").item(0).getTextContent();
            SMTPport = Integer.parseInt(element.getElementsByTagName("SMTP").item(0).getTextContent());
            POPport = Integer.parseInt(element.getElementsByTagName("POP3").item(0).getTextContent());
            autoLoad = Integer.parseInt(element.getElementsByTagName("Autoload").item(0).getTextContent());

            // Đọc các cấu hình cho mailbox
            nodeList = doc.getElementsByTagName("Configuration");
            element = (Element) nodeList.item(0);
            pathSaveFile = element.getElementsByTagName("PathDefault").item(0).getTextContent();
            autoSaveFile = Boolean.parseBoolean(element.getElementsByTagName("AutoSave").item(0).getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Get mail from the server and save it to file
     */
    public void cloneEmail() {
        this.receiveHandler = new receiveHandler(mailServer, POPport, user, password, totalMail());
        receiveHandler.cloneEmail(pathSaveFile, autoSaveFile);
        ArrayList<mail> newMails = receiveHandler.getMails();
        this.saveMailsToFile(newMails);
    }

    public void sendMail(ArrayList<String> to, String subject, String msg, ArrayList<String> cc, ArrayList<String> bcc,
            ArrayList<String> pathFiles) throws Exception {
        this.sendHandler = new sendHandler(mailServer, SMTPport);
        sendHandler.sendEmail(user, to, subject, msg, cc, bcc, pathFiles);
    }

    /*
     * Get the current path of the project
     */
    public String getPathCurrent() {
        return Paths.get("").toAbsolutePath().toString()+"\\MailBox";
    }

    public void saveMailsToFile(ArrayList<mail> newMails) {
       for (mail mail : newMails) 
           mail.saveMailToFile(getPathCurrent() + "\\storeMail\\mails.xml");
    }

    /*
     * Count the number of mails in the mails.xml file
     */
    public int totalMail() {
        int countMails = 0;
        try {
            // Tạo đối tượng Document
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(getPathCurrent() + "\\storeMail\\mails.xml");

            // Lấy phần tử cha (trong trường hợp này là Mail)
            Element mailElement = (Element) doc.getElementsByTagName("Mails").item(0);

            // Lấy danh sách các phần tử con trực tiếp của Mail
            NodeList childElements = mailElement.getChildNodes();

            // Đếm số lượng phần tử con
            for (int i = 0; i < childElements.getLength(); i++)
                if (childElements.item(i) instanceof Element)
                    countMails++;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return countMails;
    }

    /*
     * Change the read mail status back to the file
     */
    public void setStatus(String idMail, boolean status) {
        try {
            // Đọc tệp XML
            File xmlFile = new File(getPathCurrent() + "\\storeMail\\mails.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Lấy danh sách các phần tử subject trong phần tử Mail
            NodeList mailList = doc.getElementsByTagName(idMail);

            // Kiểm tra xem có phần tử subject nào hay không
            if (mailList.getLength() > 0) {
                Element mailElement = (Element) mailList.item(0);
                Element statusElement = (Element) mailElement.getElementsByTagName("Status").item(0);

                statusElement.setTextContent("false");
            }

            // Ghi tệp XML sau khi đã thay đổi
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
     * Get mail list from mails.xml file
     */
    public ArrayList<mail> getMails() {
        ArrayList<mail> mails = new ArrayList<mail>();
        // Đọc tệp XML
        try {
            File xmlFile = new File(getPathCurrent() + "\\storeMail\\mails.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            Element rootElement = (Element) doc.getElementsByTagName("Mails").item(0);
            NodeList listMail = rootElement.getChildNodes();

            for (int i = 0; i < listMail.getLength(); i++) {
                Node mailNode = listMail.item(i);

                if (mailNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element mailElement = (Element) mailNode;
                    String id = mailElement.getTagName();
                    String from = mailElement.getElementsByTagName("From").item(0).getTextContent();
                    String to = mailElement.getElementsByTagName("To").item(0).getTextContent();
                    String cc = mailElement.getElementsByTagName("Cc").item(0).getTextContent();
                    String time = mailElement.getElementsByTagName("Time").item(0).getTextContent();
                    String subject = mailElement.getElementsByTagName("Subject").item(0).getTextContent();
                    String content = mailElement.getElementsByTagName("Content").item(0).getTextContent();
                    String files = mailElement.getElementsByTagName("Files").item(0).getTextContent();
                    Boolean status = Boolean.parseBoolean(mailElement.getElementsByTagName("Status").item(0).getTextContent());
                    mail mail = new mail(id , from, to, cc, subject, content, time, files, status);
                    mails.add(mail);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
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
