package MailBox.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

public class receiveHandler {
    private Socket socket;
    private String user, password;

    private BufferedReader in;
    private PrintWriter writer;
    private ArrayList<mail> mails;
    private int mailLoaded; /*
                             * The number of mails that have been downloaded.
                             * This number helps the system avoid re-downloading previously downloaded mails
                             */

    public receiveHandler(String mailServer, int port, String user, String password, int mailLoaded)
            throws IOException {
        try {
            this.user = user;
            this.password = password;

            socket = new Socket(mailServer, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            mails = new ArrayList<>();
            this.mailLoaded = mailLoaded;
        } catch (Exception e) {
            throw new IOException("Can't connect to server: " + port);
        }
    }

    public ArrayList<mail> getMails() {
        return mails;
    }

    public void cloneEmail(String pathSaveFile, boolean autoSaveFile) throws IOException {
        writer.println("CAPA");
        readData();
        writer.println("USER " + user);
        writer.println("PASS " + password);
        writer.println("STAT");
        writer.println("LIST");
        readData();
        writer.println("UIDL");
        readData();

        // Download new mail on mailbox server
        while (true) {
            mail mail = readMail(mailLoaded + 1, pathSaveFile, autoSaveFile);
            mailLoaded++;
            if (mail.checkEmpty())
                break;
            mails.add(mail);
        }

        writer.println("QUIT");
        this.close();
    }

    // Returns mail information if mail exists, otherwise returns empty mail
    mail readMail(int index, String pathSaveFile, boolean autoSaveFile) throws IOException {
        writer.println("RETR " + index);
        String id = "", from = "", cc = "", to = "", subject = "", content = "", time = "", boundary;
        ArrayList<String> files = new ArrayList<>();
        String nameFiles = "";
        ArrayList<String> codeFiles = new ArrayList<>();

        try {
            String line;

            String response = in.readLine();
            if (response.equals("-ERR Invalid message number")) {
                return new mail(id, from, to, cc, subject, content, time, nameFiles, true);
            } else if (response.startsWith("+OK")) {
                line = in.readLine();
                if (line.startsWith("Message-ID")) {
                    // Reader the header of the mail
                    id = line.substring(13, line.length() - 1);
                    id = id.replace("-", "_").replace("@", "_");
                    while ((line = in.readLine()) != null && !line.startsWith("Content-Transfer-Encoding:")) {
                        if (line.startsWith("Date: ")) {
                            time = line.substring(6, line.length()).trim();
                        } else if (line.startsWith("To: ")) {
                            to = line.substring(4, line.length()).trim();
                        } else if (line.startsWith("From: ")) {
                            from = line.substring(6, line.length()).trim();
                        } else if (line.startsWith("Subject: ")) {
                            subject = line.substring(9, line.length()).trim();
                        } else if (line.startsWith("Cc: ")) {
                            cc = line.substring(4, line.length()).trim();
                        }
                    }
                    // Read body of the mail
                    while ((line = in.readLine()) != null && !line.equals("."))
                        content += (line + "\n");
                    content = content.trim();
                } else { // Read mail if have file attached
                    boundary = "--" + line.substring(line.indexOf("boundary") + 10, line.indexOf("boundary") + 46);

                    // Read header of mail
                    while ((line = in.readLine()) != null && !line.equals(boundary)) {
                        if (line.startsWith("Message-ID: ")) {
                            id = "_" + line.substring(12, line.length()).trim();
                        } else if (line.startsWith("Date: ")) {
                            time = line.substring(6, line.length()).trim();
                        } else if (line.startsWith("To: ")) {
                            to = line.substring(4, line.length()).trim();
                        } else if (line.startsWith("From: ")) {
                            from = line.substring(6, line.length()).trim();
                        } else if (line.startsWith("Subject: ")) {
                            subject = line.substring(9, line.length()).trim();
                        } else if (line.startsWith("Cc: ")) {
                            cc = line.substring(4, line.length()).trim();
                        }
                    }

                    // Read body of mail
                    while ((line = in.readLine()) != null && !line.startsWith(boundary)) {
                        if (line.startsWith("Content"))
                            continue;
                        content += (line + "\n");
                    }
                    content = content.trim();

                    // Read the file if there is one
                    while (!line.equals(boundary + "--")) {
                        String codeFile = "";
                        while (!(line = in.readLine()).equals("boundary") && !line.equals(boundary + "--")) {
                            if (line.startsWith("Content-Type")) {
                                files.add(line.substring(line.indexOf("name") + 6, line.length() - 1));
                                continue;
                            } else if (line.startsWith("Content"))
                                continue;

                            codeFile += line;
                        }
                        codeFile = codeFile.trim();

                        if (autoSaveFile)
                            saveFile(codeFile, files.get(files.size() - 1), pathSaveFile);
                        codeFiles.add(codeFile);
                    }

                    for (int i = 0; i < files.size(); i++) {
                        nameFiles += files.get(i);
                        if (i != files.size() - 1)
                            nameFiles += ", ";
                    }

                    in.readLine();
                }
            }
        } catch (IOException e) {
            throw new IOException("Reading file failed: " + e.getMessage(), e);
        }
        return new mail(id, from, to, cc, subject, content, time, nameFiles, true);
    }

    public void saveFile(String codeFile, String nameFile, String pathSaveFile) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(codeFile);

        // Default path to save files
        File directory = new File(pathSaveFile);

        // Check if the folder already exists, if not, create it
        if (!directory.exists())
            directory.mkdir();

        Path filePath = Paths.get(pathSaveFile, nameFile);
        if (Files.exists(filePath)) {
            int i = 1;
            // Create a new file name if that file already exists on your computer
            do {
                String newName = nameFile.substring(0, nameFile.indexOf(".")) + " (" + i + ")."
                        + nameFile.substring(nameFile.indexOf(".") + 1, nameFile.length());
                i++;
                filePath = Paths.get(pathSaveFile, newName);
            } while (Files.exists(filePath));
        }

        try {
            Files.write(filePath, decodedBytes);
        } catch (IOException e) {
            throw new IOException("Writing file failed", e);
        }
    }

    /*
     * Read mail from the server
     */
    public String readData() throws IOException {
        String msg, res = "";
        try {
            while ((msg = in.readLine()) != null && !msg.equals(".") && !msg.equals("-ERR Invalid message number")) {
                res += msg + "\n";
            }
        } catch (IOException e) {
            throw new IOException("Reading data failed", e);
        }
        return res;
    }

    public void close() throws IOException {
        try {
            socket.close();
            in.close();
            writer.close();
        } catch (IOException e) {
            throw new IOException("Failed to close", e);
        }
    }

}
