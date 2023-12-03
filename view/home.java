package MailBox.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

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
import javax.swing.border.EmptyBorder;

import MailBox.model.mail;

public class home extends JFrame {
	JPanel contentPane;
	JEditorPane[] editorPane;
	newMail sendMail;
	private JPanel displayMail;

	home() {
		this.display();
		this.setVisible(true);
	}

	public void display() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 846, 560);
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
			panel.add(clone);

			ImageIcon cloneImg = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\clone.png");
			Image cloneImg1 = cloneImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			clone.setIcon(new ImageIcon(cloneImg1));

			JButton newMail = new JButton("");
			newMail.setBounds(680, 10, 50, 50);
			panel.add(newMail);

			ImageIcon newMailImg = new ImageIcon("D:\\Workspace\\Socket\\MailBox\\view\\Image\\newMail.jpg");
			Image newMailImg1 = newMailImg.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			newMail.setIcon(new ImageIcon(newMailImg1));

			JMenuBar menuBar = new JMenuBar();
			menuBar.setBounds(10, 10, 101, 47);
			panel.add(menuBar);

			JMenu menuHome = new JMenu("Home");
			menuBar.add(menuHome);
 
			JMenuItem home = new JMenuItem("All");
			menuHome.add(home);

			JMenuItem project = new JMenuItem("Project");
			menuHome.add(project);

			JMenuItem important = new JMenuItem("Important");
			menuHome.add(important);

			JMenuItem work = new JMenuItem("Work");
			menuHome.add(work);

			JMenuItem spam = new JMenuItem("Spam");
			menuHome.add(spam);

			JMenu settings = new JMenu("Settings");
			menuBar.add(settings);

			JCheckBoxMenuItem autoSave = new JCheckBoxMenuItem("Auto Save");
			settings.add(autoSave);
		}
		{// body home
			JPanel body = new JPanel();
			body.setBounds(10, 77, 812, 446);
			contentPane.add(body);
			body.setLayout(null);

			JTextArea contentMail = new JTextArea();
			contentMail.setBounds(330, 0, 482, 446);
			contentMail.setEditable(false);
			body.add(contentMail);

			displayMail = new JPanel();

			displayMail.setLayout(new BoxLayout(displayMail, BoxLayout.Y_AXIS));

			// Hiển thị mail
			render();

			JScrollPane mainScrollPane = new JScrollPane(displayMail);
			mainScrollPane.setBounds(0, 0, 322, 446);
			mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		
			body.add(mainScrollPane);

		}
	}

	//Hiển thị các mail của user
	public void render() {
		editorPane = new JEditorPane[9]; 
		for (int i = 0; i < 9; i++) {
			editorPane[i] = new JEditorPane();
			editorPane[i].setPreferredSize(new Dimension(332, 50));
			editorPane[i].setContentType("text/html"); // Đặt loại nội dung, ví dụ HTML
			editorPane[i].setEditable(false);
			// Hiển thị mail

			displayMail.add(editorPane[i]);

			editorPane[i].setBorder(BorderFactory.createTitledBorder(""));
		}
	}

	public static void main(String[] args) {
		new home();
	}
}
