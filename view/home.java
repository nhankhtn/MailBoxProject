package MailBox.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.TextArea;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import MailBox.controller.control;
import MailBox.model.MailBox;
import MailBox.model.mail;

public class home extends JFrame {
	private JPanel contentPane;
	private JEditorPane[] editorPane;
	private newMailUI sendMailUI;
	private JPanel displayMail;
	private MailBox mailBox;
	private control control;
	private JTextPane contentMail;
	private ArrayList<mail> listMailsCurrent;

	public MailBox getMailBox() {
		return mailBox;
	}

	public home() {
		control = new control(this);
		this.display();
		this.setVisible(true);

		mailBox = new MailBox();

		listMailsCurrent = this.mailBox.getMails();
		displayMails();
	}

	public ArrayList<mail> getListMailsCurrent() {
		return listMailsCurrent;
	}

	public void setListMailsCurrent(String pageCurrent) {
		if (pageCurrent.equals("home")) {
			this.listMailsCurrent = this.mailBox.getMails();
		} else if (pageCurrent.equals("important")) {
			this.listMailsCurrent = this.mailBox.getMailImportant();
		} else if (pageCurrent.equals("work")) {
			this.listMailsCurrent = this.mailBox.getMailWork();
		} else if (pageCurrent.equals("project")) {
			this.listMailsCurrent = this.mailBox.getMailProject();
		} else if (pageCurrent.equals("spam")) {
			this.listMailsCurrent = this.mailBox.getMailSpam();
		}
	}

	public void display() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 100, 846, 572);
		ImageIcon icon = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\mail.png");
		this.setIconImage(icon.getImage());
		this.setTitle("Mailbox");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		{ // header home
			JPanel panel = new JPanel();
			panel.setBackground(new Color(128, 128, 128));
			panel.setBounds(10, 0, 812, 67);
			contentPane.add(panel);
			panel.setLayout(null);

			JButton clone = new JButton("");
			clone.setBounds(743, 10, 50, 50);
			clone.setActionCommand("clone");
			clone.addActionListener(control);
			panel.add(clone);

			ImageIcon cloneImg = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\clone.png");
			Image cloneImg1 = cloneImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			clone.setIcon(new ImageIcon(cloneImg1));

			JButton newMail = new JButton("");
			newMail.setBounds(680, 10, 50, 50);
			newMail.setActionCommand("newMail");
			newMail.addActionListener(control);
			panel.add(newMail);

			ImageIcon newMailImg = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\newMail.jpg");
			Image newMailImg1 = newMailImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			newMail.setIcon(new ImageIcon(newMailImg1));

			JMenuBar menuBar = new JMenuBar();
			menuBar.setBounds(10, 10, 101, 47);
			panel.add(menuBar);

			JMenu menuHome = new JMenu("Home");
			menuHome.addActionListener(control);
			menuBar.add(menuHome);

			JMenuItem home = new JMenuItem("All");
			home.addActionListener(control);
			menuHome.add(home);

			JMenuItem project = new JMenuItem("Project");
			project.addActionListener(control);
			menuHome.add(project);

			JMenuItem important = new JMenuItem("Important");
			important.addActionListener(control);
			menuHome.add(important);

			JMenuItem work = new JMenuItem("Work");
			work.addActionListener(control);
			menuHome.add(work);

			JMenuItem spam = new JMenuItem("Spam");
			spam.addActionListener(control);
			menuHome.add(spam);

			JMenu settings = new JMenu("Settings");
			menuBar.add(settings);

			JCheckBoxMenuItem autoSave = new JCheckBoxMenuItem("Auto Save");
			autoSave.addActionListener(control);
			settings.add(autoSave);
		}
		{// body home
			JPanel body = new JPanel();
			body.setBounds(10, 77, 812, 456);
			contentPane.add(body);
			body.setLayout(null);

			contentMail = new JTextPane();
			contentMail.setBounds(330, 0, 482, 456);
			contentMail.setEditable(false);
			body.add(contentMail);

			displayMail = new JPanel();
			displayMail.setLayout(new BoxLayout(displayMail, BoxLayout.Y_AXIS));

			JScrollPane mainScrollPane = new JScrollPane(displayMail);
			mainScrollPane.setBounds(0, 0, 322, 456);
			mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			body.add(mainScrollPane);

		}
	}

	// Display all mail in listMailCurrent
	public void displayMails() {
        int lenNeed = 0; // Độ dài cần thiết để xin cấp phát
        boolean flag = true; // Đánh dấu xem có cần cấp phát thêm không 
	    
		// Xác định độ dài cần cấp phát thêm 
		int length = listMailsCurrent.size();
		if (editorPane != null) {
			int sizeCurrent = editorPane.length;
			if (length > 9 && length > sizeCurrent) {
				editorPane = new JEditorPane[length - sizeCurrent];
				lenNeed = length - sizeCurrent;
			}else {
				lenNeed = length;
				flag = false;
			}
		} else {
			if (length > 9) {
				editorPane = new JEditorPane[length];
				lenNeed = length;
			}
			else {
				editorPane = new JEditorPane[9];
				lenNeed = 9;
			}
		}

		for (int i = 0; i < lenNeed; i++) {
			if(flag)
		    	editorPane[i] = new JEditorPane();
			editorPane[i].setPreferredSize(new Dimension(332, 50));
			editorPane[i].setContentType("text/html"); // Đặt loại nội dung, ví dụ HTML
			editorPane[i].addMouseListener(control);
			editorPane[i].setEditable(false);
			// Hiển thị mail
			if (listMailsCurrent.get(i).getStatus())
				editorPane[i].setText("<html><body style='font-size: 12px;'><b>" + listMailsCurrent.get(i).getTime()
						+ "</b><br><b>" + listMailsCurrent.get(i).getSubject() + "</b><br>"
						+ listMailsCurrent.get(i).getContent()
						+ "</body></html>");
			else
				editorPane[i].setText("<html><body style='font-size: 12px;'>" + listMailsCurrent.get(i).getTime()
						+ "<br>" + listMailsCurrent.get(i).getSubject() + "<br>" + listMailsCurrent.get(i).getContent()
						+ "</body></html>");

			editorPane[i].setName(listMailsCurrent.get(i).getId());
			if(flag)
	    		displayMail.add(editorPane[i]);

			editorPane[i].setBorder(BorderFactory.createTitledBorder(""));
		}
	}

	public void changeStatus(String id, boolean status) {
		ArrayList<mail> listMails = this.getMailBox().getMails();

		for (int i = 0; i < listMails.size(); i++) {
			if (listMails.get(i).getId().equals(id)) {
				editorPane[i].setText("<html><body style='font-size: 12px;'>" + listMailsCurrent.get(i).getTime()
						+ "<br>" + listMailsCurrent.get(i).getSubject() + "<br>" + listMailsCurrent.get(i).getContent()
						+ "</body></html>");
				break;
			}
		}
	}

	// This function takes the index of the mail being read and displays it
	public void renderMailReading(String id) {
		ArrayList<mail> listMails = this.getMailBox().getMails();

		for (int i = 0; i < listMails.size(); i++) {
			if (listMails.get(i).getId().equals(id)) {
				SimpleAttributeSet attributeSet = new SimpleAttributeSet();
				StyleConstants.setFontSize(attributeSet, 16);
				contentMail.setCharacterAttributes(attributeSet, true);

				contentMail.setText(
						"Date: " + listMails.get(i).getTime()
								+ "\nFrom: " + listMails.get(i).getFrom()
								+ "\nTo: " + listMails.get(i).getTo()
								+ "\nCc: " + listMails.get(i).getCc()
								+ "\n\nSubject: " + listMails.get(i).getSubject()
								+ "\n\n" + listMails.get(i).getContent()
								+ "\n\nAttach:" + listMails.get(i).getFiles());
				break;
			}
		}
	}

	public void clear(int numberPanel) {
		numberPanel = (numberPanel > 9) ? numberPanel : 9;
		for (int i = 0; i < numberPanel; i++) {
			if (editorPane[i] != null) {
				editorPane[i].setText("");
			}
		}
	}

	public void sendMail() {
		this.sendMailUI = new newMailUI();
		this.sendMailUI.setVisible(true);
	}

	public static void main(String[] args) {
		home home = new home();

	}
}
