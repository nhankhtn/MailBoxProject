package MailBox.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import MailBox.controller.handleNewMail;
import MailBox.model.MailBox;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class newMailUI extends JDialog {
	private JPanel contentPanel;
	private JTextField fromTextField;
	private JTextField ccTextField;
	private JTextField toTextField;
	private JTextField bccTextField;
	private JTextField subjectTextField;
	private JTextField fileTextField;
	private JTextArea textArea;
	private handleNewMail handleNewMail;
	private MailBox mailBox;
	private boolean sendFile; // Kiểm tra xem có gửi kèm file không 
	private ArrayList<String> pathFiles;

	public newMailUI() {
		handleNewMail = new handleNewMail(this);
		mailBox = new MailBox();
  
		pathFiles = new ArrayList<>();
		sendFile = false;

		this.display();
	}

	public void setSendFile(boolean sendFile) {
		this.sendFile = sendFile;
	}

	public void setPathFiles(ArrayList<String> pathFiles) {
		this.pathFiles = pathFiles;
	}

	public void display() {
		setBounds(600, 200, 569, 384);
		getContentPane().setLayout(new BorderLayout());
		ImageIcon icon = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\send.png");
		this.setIconImage(icon.getImage());
		this.setTitle("Send mail");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel header = new JPanel();
			header.setBounds(0, 0, 555, 67);
			contentPanel.add(header);
			header.setLayout(null);

			JButton attachBtn = new JButton("");
			attachBtn.setBounds(508, 10, 16, 47);
			attachBtn.setActionCommand("Attach");
			attachBtn.addActionListener(handleNewMail);
			header.add(attachBtn);

			ImageIcon attachImg = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\attach.png");
			Image attachImg1 = attachImg.getImage().getScaledInstance(16, 47, Image.SCALE_SMOOTH);
			attachBtn.setIcon(new ImageIcon(attachImg1));

			JLabel fromLabel = new JLabel("From");
			fromLabel.setBounds(10, 10, 45, 13);
			header.add(fromLabel);

			JLabel ccLabel = new JLabel("Cc");
			ccLabel.setBounds(10, 26, 45, 13);
			header.add(ccLabel);

			JLabel subjectLabel = new JLabel("Subject");
			subjectLabel.setBounds(10, 44, 45, 13);
			header.add(subjectLabel);

			JLabel toLabel = new JLabel("To");
			toLabel.setBounds(257, 10, 45, 13);
			header.add(toLabel);

			JLabel bccLabel = new JLabel("Bcc");
			bccLabel.setBounds(257, 26, 45, 13);
			header.add(bccLabel);

			fromTextField = new JTextField();
			fromTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			fromTextField.setBounds(59, 10, 188, 18);
			fromTextField.setText(mailBox.getUser());
			fromTextField.setEditable(false);
			header.add(fromTextField);
			fromTextField.setColumns(10);

			ccTextField = new JTextField();
			ccTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			ccTextField.setBounds(59, 28, 188, 18);
			header.add(ccTextField);
			ccTextField.setColumns(10);

			subjectTextField = new JTextField();
			subjectTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			subjectTextField.setBounds(59, 46, 431, 18);
			header.add(subjectTextField);
			subjectTextField.setColumns(10);

			toTextField = new JTextField();
			toTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			toTextField.setBounds(302, 10, 188, 18);
			header.add(toTextField);
			toTextField.setColumns(10);

			bccTextField = new JTextField();
			bccTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			bccTextField.setBounds(302, 28, 188, 18);
			header.add(bccTextField);
			bccTextField.setColumns(10);
		}
		{
			JPanel body = new JPanel();
			body.setBounds(0, 67, 555, 210);
			contentPanel.add(body);
			body.setLayout(new BorderLayout(0, 0));

			textArea = new JTextArea();

			JScrollPane scrollPane = new JScrollPane(textArea);
			body.add(scrollPane);
		}
		{
			JPanel footer = new JPanel();
			footer.setBackground(new Color(192, 192, 192));
			footer.setBounds(0, 277, 555, 39);
			contentPanel.add(footer);
			footer.setLayout(null);

			JLabel fileLabel = new JLabel("File");
			fileLabel.setBounds(10, 10, 45, 19);
			footer.add(fileLabel);

			fileTextField = new JTextField();
			fileTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			fileTextField.setBounds(65, 10, 480, 19);
			fileTextField.setEditable(false);
			footer.add(fileTextField);
			fileTextField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JButton send = new JButton("Send");
			send.addActionListener(handleNewMail);
			buttonPane.add(send);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(handleNewMail);
			buttonPane.add(cancelButton);
		}
	}

	public void sendMail() {
		String to = toTextField.getText().trim();
		String[] toArr = to.split(" ");
		ArrayList<String> recipients = new ArrayList<>(Arrays.asList(toArr));

		String cc = ccTextField.getText().trim();
		String[] ccArr = cc.split(" ");
		ArrayList<String> recipientCC = new ArrayList<>(Arrays.asList(ccArr));

		String bcc = bccTextField.getText().trim();
		String[] bccArr = bcc.split(" ");
		ArrayList<String> recipientBCC = new ArrayList<>(Arrays.asList(bccArr));

		String subject = subjectTextField.getText().trim();
		String content = textArea.getText().trim();

		ArrayList<String> pathFiles = this.pathFiles;
	
		this.mailBox.sendMail(recipients, subject, content, recipientCC, recipientBCC, pathFiles);
	}

	public ArrayList<String> getPathFilesSelected() {
		ArrayList<String> paths = new ArrayList<>();
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);

		FileFilter ff = new FileFilter() {
			@Override
			public boolean accept(File file) {
				// Chỉ chấp nhận các file có dung lượng nhỏ hơn 5 MB
				return file.isFile() && file.length() < 5 * 1024 * 1024; // 5 MB
			}

			@Override
			public String getDescription() {
				return "Files (size < 5 MB)";
			}
		};
		fc.setFileFilter(ff);
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = fc.getSelectedFiles();
			for (File file : selectedFiles) {
				String filePath = file.getAbsolutePath();
				paths.add(filePath);
			}
		}
		return paths;
	}

	public void renderFilesSelector() {
        String nameFiles="";
		for(int i = 0; i < this.pathFiles.size(); i++) {
			nameFiles+=pathFiles.get(i).substring(pathFiles.get(i).lastIndexOf("\\")+1, pathFiles.get(i).length());
			if(i != this.pathFiles.size() - 1) nameFiles += ",";
		}
		this.fileTextField.setText(nameFiles);
	}

}
