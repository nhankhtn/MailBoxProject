package model;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilderFactory;
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
    private ArrayList<ArrayList<String>> keywords;

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
        this.setKeywords();

        typeMail = new ArrayList<>();
        for (String spam : keywords.get(3)) {
            if (subject.indexOf(spam) != -1 || content.indexOf(spam) != -1) {
                typeMail.add("spam");
                return;
            }
        }

        for (String project : keywords.get(0)) {
            if (from.indexOf(project) != -1) {
                typeMail.add("project");
                break;
            }
        }

        for (String important : keywords.get(1)) {
            if (subject.indexOf(important) != -1) {
                typeMail.add("important");
                break;
            }
        }

        for (String work : keywords.get(2)) {
            if (content.indexOf(work) != -1) {
                typeMail.add("work");
                break;
            }
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

    private void setKeywords() {
        try {
            File xmlFile = new File(Paths.get("").toAbsolutePath().toString() + "\\config.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList_3 = doc.getElementsByTagName("Filter");
            Element element_3 = (Element) nodeList_3.item(0);
            keywords = new ArrayList<>();

            // Read keywords used to categorize messages
            String project = element_3.getElementsByTagName("Project").item(0).getTextContent();
            keywords.add(new ArrayList<>(Arrays.asList(project.trim().split("\\s*,\\s*"))));
            String important = element_3.getElementsByTagName("Important").item(0).getTextContent();
            keywords.add(new ArrayList<>(Arrays.asList(important.trim().split("\\s*,\\s*"))));
            String work = element_3.getElementsByTagName("Work").item(0).getTextContent();
            keywords.add(new ArrayList<>(Arrays.asList(work.trim().split("\\s*,\\s*"))));
            String spam = element_3.getElementsByTagName("Spam").item(0).getTextContent();
            keywords.add(new ArrayList<>(Arrays.asList(spam.trim().split("\\s*,\\s*"))));
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
