package MailBox.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
* Defines an email to display
*/

public class mail {
    private String id;
    private String from;
    private String to, cc;
    private String files;
    private String subject, content, time;
    private boolean status; // true if the mail has not been read
    private ArrayList<String> typeMail;

    public mail(String id, String from, String to, String cc, String subject, String content,
            String time, String files, Boolean status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.content = content;
        this.time = time;
        this.files = files;
        this.status = status;
        this.setTypeMail();
    }

    public boolean checkEmpty() {
        return this.from.equals("");
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getCc() {
        return cc;
    }

    public String getFiles() {
        return files;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
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
        if (subject.indexOf("virus") != -1 || subject.indexOf("hack") != -1 || subject.indexOf("crack") != -1
                || content.indexOf("virus") != -1 || content.indexOf("hack") != -1 || content.indexOf("crack") != -1)
            typeMail.add("spam");
        else {
            if (from.endsWith("@testing.com"))
                typeMail.add("project");
            if (subject.equals("urgent") || subject.equals("ASAP"))
                typeMail.add("important");
            if (content.indexOf("report") != -1 || content.indexOf("meeting") != -1)
                typeMail.add("work");
        }
    }

    public void saveMailToFile(String pathFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("Mails");
            doc.appendChild(rootElement);

            Element fromElement = doc.createElement("From");
            fromElement.appendChild(doc.createTextNode(from));
            rootElement.appendChild(fromElement);

            Element toElement = doc.createElement("To");
            toElement.appendChild(doc.createTextNode(to));
            rootElement.appendChild(toElement);

            Element ccElement = doc.createElement("Cc");
            ccElement.appendChild(doc.createTextNode(cc));
            rootElement.appendChild(ccElement);

            Element timeElement = doc.createElement("Time");
            timeElement.appendChild(doc.createTextNode(time));
            rootElement.appendChild(timeElement);

            Element subjectElement = doc.createElement("Subject");
            subjectElement.appendChild(doc.createTextNode(subject));
            rootElement.appendChild(subjectElement);

            Element contentElement = doc.createElement("Content");
            contentElement.appendChild(doc.createTextNode(content));
            rootElement.appendChild(contentElement);

            Element filesElement = doc.createElement("Files");
            filesElement.appendChild(doc.createTextNode(files));
            rootElement.appendChild(filesElement);

            Element statusElement = doc.createElement("Status");
            statusElement.appendChild(doc.createTextNode(String.valueOf(status)));
            rootElement.appendChild(statusElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(pathFile));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
