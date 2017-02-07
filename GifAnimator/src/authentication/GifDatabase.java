package authentication;

import database.Database;
import gif.Animator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GifDatabase extends JFrame{
	private static final long serialVersionUID = 1L;
	private final Database gifInfo;
	private final Dimension textFieldSize = new Dimension(250, 22);
	
        //Constructs the JFrame where the user logs into the data base that stores the users info and images
	public GifDatabase(){
		super("Gif Project");
		
		gifInfo = Database.getInstance();
		
		JLabel titleLabel = new JLabel("Gif Login", SwingConstants.CENTER);
		
		JPanel inputPanel = new JPanel();
		
		JLabel usernameLabel = new JLabel("Username", SwingConstants.CENTER);
		final JTextField usernameBox = new JTextField("", SwingConstants.CENTER);
		usernameBox.setPreferredSize(textFieldSize);
		usernameBox.setMaximumSize( usernameBox.getPreferredSize() );
		
		JLabel passwordLabel = new JLabel("Password");
		final JPasswordField passwordBox = new JPasswordField("", SwingConstants.CENTER);
		passwordBox.setPreferredSize(textFieldSize);
		passwordBox.setMaximumSize( passwordBox.getPreferredSize() );
		
		JButton loginButton = new JButton("Login");
		
		ActionListener al;
            al = new ActionListener(){
                @SuppressWarnings("deprecation")
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getActionCommand().equalsIgnoreCase("Login")){
                        if(!usernameBox.getText().isEmpty() && !passwordBox.getText().isEmpty()){
                            String uname = usernameBox.getText();
                            String password = passwordBox.getText();
                            boolean inCorrectCreds = gifInfo.testCreds(uname, password);
                            if(inCorrectCreds){
                                showMessage(1);
                            }else{
                                dispose();
                                String[] args = {uname};
                                Animator.main(args);
                            }
                        }else{
                            showMessage(2);
                        }
                    }
                }
            };
		
		loginButton.addActionListener(al);
		
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.add(new JLabel(" "));
		inputPanel.add(usernameLabel);
		inputPanel.add(usernameBox);
		inputPanel.add(new JLabel(" "));
		inputPanel.add(passwordLabel);
		inputPanel.add(passwordBox);
		inputPanel.add(new JLabel(" "));
		
		add(titleLabel, BorderLayout.NORTH);
		add(inputPanel, BorderLayout.EAST);
		add(loginButton, BorderLayout.SOUTH);
	
		setSize(350, 210);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
        //Debug messages show in console
	private void showMessage(int message){
		switch(message){
		case 0:
			JOptionPane.showMessageDialog(this, "Login Successful");
			break;
		case 1: 
			JOptionPane.showMessageDialog(this, "Incorrect username or password.");
			break;
		case 2:
			JOptionPane.showMessageDialog(this, "Please enter a username and a password.");
			break;
		default: 
			System.out.println("JOptionPane message number is out of range. \nLogin: showMessage(int message)\n\n");
		}
	}
}
