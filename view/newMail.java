package MailBox.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class newMail extends JDialog {
    JPanel contentPanel;
    private JTextField fromTextField;
	private JTextField ccTextField;
	private JTextField toTextField;
	private JTextField bccTextField;
	private JTextField subjectTextField;
    private JTextField fileTextField;

    newMail() {
        this.display();
		showSend(true);
    }

    public void display() {
        setBounds(100, 100, 569, 384);
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
			fromTextField.setBounds(59, 10, 188, 13);
			header.add(fromTextField);
			fromTextField.setColumns(10);
			
			ccTextField = new JTextField();
            ccTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			ccTextField.setBounds(59, 26, 188, 13);
			header.add(ccTextField);
			ccTextField.setColumns(10);
			
			subjectTextField = new JTextField();
            subjectTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			subjectTextField.setBounds(59, 44, 431, 13);
			header.add(subjectTextField);
			subjectTextField.setColumns(10);
			
			toTextField = new JTextField();
            toTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			toTextField.setBounds(302, 10, 188, 13);
			header.add(toTextField);
			toTextField.setColumns(10);
			
			bccTextField = new JTextField();
            bccTextField.setFont(new Font("Tahoma", Font.PLAIN, 8));
			bccTextField.setBounds(302, 26, 188, 13);
			header.add(bccTextField);
			bccTextField.setColumns(10);
		}
        {
			JPanel body = new JPanel();
			body.setBounds(0, 67, 555, 210);
			contentPanel.add(body);
			body.setLayout(new BorderLayout(0, 0));
			
			JTextArea textArea = new JTextArea();
			
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
			buttonPane.add(send);
			JButton cancelButton = new JButton("Cancel");
			buttonPane.add(cancelButton);
		}
    }

    public void showSend(boolean visible) {
        this.setVisible(visible);
    }
	
	public static void main(String[] args) {
		new newMail();
	}
}
