package MailBox.model;

import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;
import java.util.Random;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;

public class sendHandler {
	PrintWriter writer;
	Socket socket;
	final int SOCKET_TIMEOUT = 15*1000;

	public sendHandler(String mailServer, int port) {
		try {
			socket = new Socket(mailServer, port);
			socket.setSoTimeout(SOCKET_TIMEOUT);
			writer = new PrintWriter(
					socket.getOutputStream(), true);
		} catch (Exception e) {
			System.out.println("Can't connect to server");
		}
	}

	public void sendEmail(String from, ArrayList<String> to, String subject, String msg, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> pathFiles) {
		String boundary = randomBound(24);

		writer.println("EHLO [127.0.0.1]");
		writer.println("MAIL FROM:<" + from + ">");
		for (String string : to) 
			writer.println("RCPT TO:<" + string + ">");
		for (String string : cc) 
			writer.println("RCPT TO:<" + string + ">");
		for (String string : bcc) 
			writer.println("RCPT TO:<" + string + ">");

		writer.println("DATA");
		writer.println("Content-Type: multipart/mixed; boundary=\"------------" + boundary + "\""); // Khi có gửi file
		writer.println("Message-ID: " + System.currentTimeMillis());
		writer.println("Date: " + currentTimeFormat());
		writer.println("Content-Language: vi-x-KieuCu.[Chuan]");
		writer.println("To: " + unionEmail(to));
		if (cc.size() != 0)
			writer.println("Cc: " + unionEmail(cc));

		writer.println("From: " + from);
		writer.println("Subject: " + subject);
		writer.println("");

		writer.println("This is a multi-part message in MIME format.");
		// Body
		writer.println("--------------" + boundary);
		writer.println("Content-Type: text/plain; charset=UTF-8; format=flowed");
		writer.println("Content-Transfer-Encoding: 7bit");
		writer.println("");

		writer.println(msg);

		// Send file
		for (String pathFile : pathFiles) 
			attachFile(writer, pathFile,  boundary);

		// Footer
		writer.println("--------------" + boundary + "--");
		writer.println(".");
	}

	void attachFile(PrintWriter writer, String pathFile, String boundary) {
		File f = new File(pathFile);
		writer.println("");
		writer.println("--------------" + boundary);
		writer.println("Content-Type: " + typeOfFile(f.getName()) + "; name=\"" + f.getName() + "\"");
		writer.println("Content-Disposition: attachment; filename=\"" + f.getName() + "\"");
		writer.println("Content-Transfer-Encoding: base64");
		writer.println("");

		try {
			byte[] fileBytes = Files.readAllBytes(f.toPath());
			String contentEncode = Base64.getEncoder().encodeToString(fileBytes);
			int sizeLine = 72;
			int len = contentEncode.length() / sizeLine;
			String line;

			for (int i = 0; i <= len; i++) {
				if (i == len)
					line = contentEncode.substring(i * sizeLine, contentEncode.length());
				else
					line = contentEncode.substring(i * sizeLine, (i + 1) * sizeLine);

				writer.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		writer.println("");
	}

	public String currentTimeFormat() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		return sdf.format(date);
	}

	public String unionEmail(ArrayList<String> emails) {
		String res = "";
		for (int i = 0; i < emails.size(); i++) {
			res += emails.get(i);
			if (i != emails.size() - 1)
				res += ", ";
		}
		return res;
	}

	public String randomBound(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder randomString = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			randomString.append(randomChar);
		}

		return randomString.toString();
	}

	public String typeOfFile(String fileName) {
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		switch (suffix) {
			case "txt":
			    return "text/plain";
			case "pdf":
				return"application/pdf";
			case "docx":
				return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			case "ipg":
				return "image/ipeg";
			case "zip":
				return "application/x-zip-compressed";
			case "xlsx":
			    return  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			case "xls":
                return "application/vnd.ms-excel";
            case ".xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "doc":
                return "application/msword";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "rar":
                return "application/vnd.rar";
			default:
			    return "";
		}
	}

	public static void main(String[] args) {
		sendHandler sendHandler = new sendHandler("127.0.0.1", 2225);
		

	}
}
