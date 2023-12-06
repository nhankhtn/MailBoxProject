package MailBox.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
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
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import MailBox.controller.handleHome;
import MailBox.model.MailBox;
import MailBox.model.autoLoadMail;
import MailBox.model.mail;

public class home extends JFrame {
	private MailBox mailBox;
	private autoLoadMail autoLoadMail;
	private newMailUI sendMailUI;
	private handleHome handleHome;

	private JPanel contentPane;
	private JEditorPane[] editorPane; // Where to display the mail list
	private JPanel displayMail;
	private JTextPane contentMail; // Where to display the content of mail reading
	private JCheckBoxMenuItem autoSave;
	private JScrollPane mainScrollPane;

	private ArrayList<mail> listMailsCurrent; // Saves the current mail list
	private final int maxPanel = 100;  // The maximum number of pages to display
	private String pageCurrent; // The current page is  rendering

	public home() {
		handleHome = new handleHome(this);
		this.display(); // Displays the mailbox interface

		mailBox = new MailBox();
		listMailsCurrent = this.mailBox.getMails(); //Loads list mail from the file mails.xml

		autoLoadMail = new autoLoadMail(this); //Automatically download emails and display them on the interface
		autoLoadMail.start();


		editorPane = new JEditorPane[maxPanel];
		for (int i = 0; i < 9; i++) {
			editorPane[i] = new JEditorPane();
			this.displayMail.add(editorPane[i]);
		}

		this.displayMails(); // Display mail list on the interface
		this.pageCurrent = "home";
	}

	public MailBox getMailBox() {
		return mailBox;
	}

	public ArrayList<mail> getListMailsCurrent() {
		return listMailsCurrent;
	}

	public boolean getAutoSave() {
		return autoSave.getState();
	}

	public String getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(String pageCurrent) {
		this.pageCurrent = pageCurrent;
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

			JButton reload = new JButton("");
			reload.setBounds(743, 10, 50, 50);
			reload.setActionCommand("reload");
			reload.addActionListener(handleHome);
			panel.add(reload);

			ImageIcon reloadImg = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\reload.png");
			reload.setIcon(new ImageIcon(reloadImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

			JButton newMail = new JButton("");
			newMail.setBounds(680, 10, 50, 50);
			newMail.setActionCommand("newMail");
			newMail.addActionListener(handleHome);
			panel.add(newMail);

			ImageIcon newMailImg = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\newMail.jpg");
			newMail.setIcon(new ImageIcon(newMailImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

			JMenuBar menuBar = new JMenuBar();
			menuBar.setBounds(10, 10, 101, 47);
			panel.add(menuBar);

			JMenu menuHome = new JMenu("Home");
			menuHome.addActionListener(handleHome);
			menuBar.add(menuHome);

			JMenuItem home = new JMenuItem("All");
			home.addActionListener(handleHome);
			menuHome.add(home);

			JMenuItem project = new JMenuItem("Project");
			project.addActionListener(handleHome);
			menuHome.add(project);

			JMenuItem important = new JMenuItem("Important");
			important.addActionListener(handleHome);
			menuHome.add(important);

			JMenuItem work = new JMenuItem("Work");
			work.addActionListener(handleHome);
			menuHome.add(work);

			JMenuItem spam = new JMenuItem("Spam");
			spam.addActionListener(handleHome);
			menuHome.add(spam);

			JMenu settings = new JMenu("Settings");
			menuBar.add(settings);

			autoSave = new JCheckBoxMenuItem("Auto Save");
			autoSave.setSelected(true);
			autoSave.addActionListener(handleHome);
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

			mainScrollPane = new JScrollPane(displayMail);
			mainScrollPane.setBounds(0, 0, 322, 456);
			mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			body.add(mainScrollPane);

		}

		this.setVisible(true);
	}

	// Display all mail in listMailCurrent
	public void displayMails() {
		int length = listMailsCurrent.size();
		/* 
		 * Variable used to mark whether editorPane already exists outside the interface,
		 * if not, create and add it, otherwise just add content to it
		*/
		boolean flag = false;

		for (int i = 0; i < length; i++) {
			if (editorPane[i] == null) {
				editorPane[i] = new JEditorPane();
				flag = true;
			}
			editorPane[i].setPreferredSize(new Dimension(332, 50));
			editorPane[i].setContentType("text/html"); // Đặt loại nội dung, ví dụ HTML
			editorPane[i].addMouseListener(handleHome);
			editorPane[i].setEditable(false);
			// If the email has not been read, it will be displayed in bold text
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
			if (flag) {
				this.displayMail.add(editorPane[i]);
				flag = false;
			}

			editorPane[i].setBorder(BorderFactory.createTitledBorder(""));
		}
        
		this.mainScrollPane.setAlignmentX(0);
		this.mainScrollPane.setAlignmentY(0);
		
	}

	/*
	 * This function receives the mail id as a parameter and 
	 * changes the read status outside the main interface
	 */
	public void changeStatus(String id, boolean status) {
		ArrayList<mail> listMails = this.getMailBox().getMails();

		for (int i = 0; i < listMails.size(); i++) {
			if (listMails.get(i).getId().equals(id)) {
				editorPane[i].setText(
						"<html>"
								+ "<body style='font-size: 12px;'>"
								+ listMailsCurrent.get(i).getTime()
								+ "<br>" + listMailsCurrent.get(i).getSubject()
								+ "<br>" + listMailsCurrent.get(i).getContent()
								+ "</body>"
								+ "</html>");
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
 
    /*
	 * This function is used to delete content contained in editorPane
	 */
	public void clear() {
		for (JEditorPane jEditorPane : this.editorPane) {
			if(jEditorPane!=null) jEditorPane.setText("");
		}
	}

	/*
	 * Displays the interface so users can send mail
	 */
	public void displaySendMail() {
		this.sendMailUI = new newMailUI();
		this.sendMailUI.setVisible(true);
	}

	public void autoRender() {
		this.mailBox.cloneEmail(); // Pull mail from the server and save it to the mails.xml file
		this.setListMailsCurrent(pageCurrent); // Change the mail list again after updating
		this.displayMails(); // Display the mail list again
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			home home = new home();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
