package MailBox.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
    private int mailLoaded; // Số mail đã được tải, số này giúp hệ thống tránh tải lại các mail đã được tải trước đó

    receiveHandler(String mailServer, int port, String user, String password,int mailLoaded) {
        try {
            this.user = user;
            this.password = password;

            socket = new Socket(mailServer, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            mails = new ArrayList<>();
            this.mailLoaded = mailLoaded;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<mail> getMails() {
        return mails;
    }

    public void cloneEmail(String pathSaveFile, boolean autoSaveFile) {
        writer.println("CAPA");
        readData();
        writer.println("USER " + user);
        writer.println("PASS " + password);
        writer.println("STAT");
        writer.println("LIST");
        readData();
        writer.println("UIDL");
        readData();

        // Tải các mail mới trên mailbox server
        while (true) {
            mail mail = readMail(mailLoaded + 1, pathSaveFile, autoSaveFile);
            mailLoaded++;
            if (mail.checkEmpty())
                break;
            mails.add(mail);
        }

        // Gửi lệnh QUIT để đóng kết nối
        writer.println("QUIT");
        close();
    }

    // Trả về thông tin mail nếu tồn tại mail, ngược lại trả về mail rỗng
    mail readMail(int index, String pathSaveFile, boolean autoSaveFile) {
        writer.println("RETR " + index);
        String id="", from = "",cc="", to="", subject = "", content = "", time = "", boundary;
        ArrayList<String> files = new ArrayList<>();
        String nameFiles="";
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
                    id = line.substring(13, line.length()-1);
                    id = id.replace("-", "_").replace("@", "_");
                    while((line = in.readLine()) != null && !line.startsWith("Content-Transfer-Encoding:")) {
                        if (line.startsWith("Date: ")) {
                            time = line.substring(6, line.length());
                        } else if (line.startsWith("To: ")) {
                            to = line.substring(4, line.length());
                        } else if (line.startsWith("From: ")) {
                            from = line.substring(6, line.length());
                        } else if (line.startsWith("Subject: ")) {
                            subject = line.substring(9, line.length());
                        } else if (line.startsWith("Cc: ")) {
                            cc = line.substring(4, line.length());
                        }
                    }
                    // Read body of the mail 
                    while((line = in.readLine()) != null && !line.equals(".")) 
                        content += (line + "\n");
                    content = content.trim();
                } else { // Read mail if have file attached
                    boundary = "--" + line.substring(line.indexOf("boundary") + 10, line.indexOf("boundary") + 46);

                    // Read header of mail
                    while ((line = in.readLine()) != null && !line.equals(boundary)) {
                        if(line.startsWith("Message-ID: ")) {
                            id = line.substring(12, line.length());
                        } else if (line.startsWith("Date: ")) {
                            time = line.substring(6, line.length());
                        } else if (line.startsWith("To: ")) {
                            to = line.substring(4, line.length());
                        } else if (line.startsWith("From: ")) {
                            from = line.substring(6, line.length());
                        } else if (line.startsWith("Subject: ")) {
                            subject = line.substring(9, line.length());
                        } else if (line.startsWith("Cc: ")) {
                            cc = line.substring(4, line.length());
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
            e.printStackTrace();
        }
        return new mail(id, from, to, cc, subject, content, time, nameFiles, true);
    }

    void saveFile(String codeFile, String nameFile, String pathSaveFile) {
        byte[] decodedBytes = Base64.getDecoder().decode(codeFile);

        // Đường dẫn mặc định để lưu file
        File directory = new File(pathSaveFile);

        // Kiểm tra xem thư mục đã tồn tại chưa, nếu chưa thì tạo thư mục đó
        if (!directory.exists())
            directory.mkdir();

        Path filePath = Paths.get(pathSaveFile, nameFile);
        if (Files.exists(filePath)) {
            int i = 1;
            // Tạo tên file mới nếu đã tồn tại file đó trong máy
            do {
                String newName = nameFile.substring(0, nameFile.indexOf(".")) + " (" + i + ")."
                        + nameFile.substring(nameFile.indexOf(".") + 1, nameFile.length());
                i++;
                filePath = Paths.get(pathSaveFile, newName);
            } while (Files.exists(filePath));
        }

        try {
            Files.write(filePath, decodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Read mail from the server
     */
    public String readData() {
        String msg, res = "";
        try {
            while ((msg = in.readLine()) != null && !msg.equals(".") && !msg.equals("-ERR Invalid message number")) {
                res += msg + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    void close() {
        try {
            socket.close();
            in.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
