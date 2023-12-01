package MailBoxProject.model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;



public class MailBox {
    mail[] mails;
    int totalMails;

    String user, password, mailServer;
    int SMTPport, POPport, autoload;


    MailBox(){
        this.configure();

 

    }

    void configure() {
        try {
            //Lấy đường dẫn tương đối của file config.xml 
            Path currentPath = Paths.get("");
            Path pathToC = currentPath.resolveSibling("MailBoxProject").resolve("config.xml");
            File xmlFile = new File(pathToC.toAbsolutePath().toString());
        
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("General");
           
            Element element = (Element) nodeList.item(0);
            user = element.getElementsByTagName("Username").item(0).getTextContent();
            password = element.getElementsByTagName("Password").item(0).getTextContent();
            mailServer = element.getElementsByTagName("MailServer").item(0).getTextContent();
            SMTPport = Integer.parseInt(element.getElementsByTagName("SMTP").item(0).getTextContent());
            POPport = Integer.parseInt(element.getElementsByTagName("POP3").item(0).getTextContent());
            autoload = Integer.parseInt(element.getElementsByTagName("Autoload").item(0).getTextContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
       MailBox mb = new MailBox();
       
    }
}
