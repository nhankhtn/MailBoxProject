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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class receiveHandler {
    private Socket socket;
    private String user, password;

    private BufferedReader in;
    private PrintWriter writer;
    private int sizeMails; // Số mail tối đa có thể lưu trữ
    private int numMailRead; // Số mail đã được tải về 
    private mail[] mails;

    receiveHandler(String mailServer, int port, String user, String password) {
        try {
            this.user = user;
            this.password = password;
            numMailRead = 0;
            sizeMails = 100;


            socket = new Socket(mailServer, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            mails = new mail[sizeMails];
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getNumMails() {
        return numMailRead;
    }

    public mail[] getMails(){
        return mails;
    }

    public void cloneEmail() {
        writer.println("CAPA");
        readData();
        writer.println("USER " + user);
        writer.println("PASS " + password);
        writer.println("STAT");
        writer.println("LIST");
        readData();
        writer.println("UIDL");
        readData();

        //Tải các mail mới trên mailbox server 
        while(true){
            mail mail = readMail(numMailRead+1);
            if(mail.checkEmpty()) break;
            mails[numMailRead++]=mail;
        }

        // Gửi lệnh QUIT để đóng kết nối
        writer.println("QUIT");
        close();
    }

    //Trả về thông tin mail nếu tồn tại mail, ngược lại trả về mail rỗng 
    mail readMail(int index) {
        writer.println("RETR " + index);
        String from = "", subject = "", content = "", time = "", boundary;
        int maxReceiver = 100;
        String to[] = new String[maxReceiver], cc[] = new String[maxReceiver];
        int numTo = 0, numCc = 0;

        int numberFiles = 0;
        String codeFiles[] = new String[100];// Stores the Base64 code of the file
        String nameFiles[] = new String[100];

        try {
            String line;

            String response = in.readLine();
            if (response.equals("-ERR Invalid message number")) {
                return new mail(from, to, numTo, cc, numCc, subject, content, time, numberFiles, nameFiles);
            } else if (response.startsWith("+OK")) {
                line = in.readLine();
                boundary = "--" + line.substring(line.indexOf("boundary") + 10, line.indexOf("boundary") + 46);

                // Read header of mail
                while ((line = in.readLine()) != null && !line.equals(boundary)) {
                    if (line.startsWith("Date: ")) {
                        time = line.substring(6, line.length());
                    } else if (line.startsWith("To: ")) {
                        int begin = 4;
                        int end;
                        while ((end = line.indexOf("@gmail.com", begin)) != -1) {
                            to[numTo++] = line.substring(begin, end + 10);
                            begin = end + 12;
                        }
                    } else if (line.startsWith("From: ")) {
                        from = line.substring(6, line.length());
                    } else if (line.startsWith("Subject: ")) {
                        subject = line.substring(9, line.length());
                    } else if (line.startsWith("Cc: ")) {
                        int begin = 4;
                        int end;
                        while ((end = line.indexOf("@gmail.com", begin)) != -1) {
                            cc[numCc++] = line.substring(begin, end + 10);
                            begin = end + 12;
                        }
                    }
                }

                // Read body of mail
                while ((line = in.readLine()) != null && !line.startsWith(boundary)) {
                    if (line.startsWith("Content"))
                        continue;
                    content += (line + "\n");
                }
                // Remove 2 characters \n while sending
                content = content.substring(content.indexOf("\n") + 1, content.lastIndexOf("\n") - 1);

                // Read the file if there is one
                while (!line.equals(boundary + "--")) {
                    codeFiles[numberFiles] = "";
                    while (!(line = in.readLine()).equals("boundary") && !line.equals(boundary + "--")) {
                        if (line.startsWith("Content-Type")) {
                            nameFiles[numberFiles] = line.substring(line.indexOf("name") + 6, line.length() - 1);
                            continue;
                        } else if (line.startsWith("Content"))
                            continue;

                        codeFiles[numberFiles] += line;
                    }
                    codeFiles[numberFiles] = codeFiles[numberFiles].trim();

                    saveFile(codeFiles[numberFiles], nameFiles[numberFiles]);

                    numberFiles++;
                }
                in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new mail(from, to, numTo, cc, numCc, subject, content, time, numberFiles, nameFiles);
    }

    void saveFile(String codeFile, String nameFile) {
        byte[] decodedBytes = Base64.getDecoder().decode(codeFile);

        String directoryPath = "D:\\mailClient"; // Đường dẫn mặc định để lưu file
        File directory = new File(directoryPath);

        // Kiểm tra xem thư mục đã tồn tại chưa, nếu chưa thì tạo thư mục đó
        if (!directory.exists())
            directory.mkdir();

        Path filePath = Paths.get(directoryPath, nameFile);
        if(Files.exists(filePath)){
             int i = 1;
             // Tạo tên file mới nếu đã tồn tại file đó trong máy 
             do {
                String newName = nameFile.substring(0, nameFile.indexOf(".")) + " (" + i + ")." 
                + nameFile.substring(nameFile.indexOf(".")+1,nameFile.length());
                filePath = Paths.get(directoryPath, newName);
             } while(Files.exists(filePath));
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

    public static void main(String[] args) {
        receiveHandler pop = new receiveHandler("127.0.0.1", 3335, "abc@gmail.com", "1234");
        pop.cloneEmail();
        pop.print();
    }
}
