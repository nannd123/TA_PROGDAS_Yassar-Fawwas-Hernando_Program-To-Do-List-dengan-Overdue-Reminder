import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {

    private JTextField userText;
    private JPasswordField passText;
    private JButton submitButton;
    private JButton cancelButton;

    public LoginForm() {
        setTitle("Login Form");
        setSize(720, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {

        //Background Image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/gambar 1.jpg")); // Path gambar
        Image scaledImage = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setLayout(new GridBagLayout());

        //Main Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin antar komponen




        // Username
        JLabel userLabel = new JLabel("Username :");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        background.add(userLabel, gbc);

        userText = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        background.add(userText, gbc);

        //Password
        JLabel passLabel = new JLabel("Password :");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        background.add(passLabel, gbc);

        passText = new JPasswordField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        background.add(passText, gbc);

        //Submit dan Cancel
        submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        background.add(submitButton, gbc);

        cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        background.add(cancelButton, gbc);

        //Quotes
        JLabel footerLabel = new JLabel("'Organize Your Task!!'");
        footerLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        background.add(footerLabel, gbc);

        add(background);

        // Action listener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passText.getPassword());

                // Pengecekan username dan password
                if (username.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(LoginForm.this, "Login successful!");
                    new Todo().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginScreen = new LoginForm();
            loginScreen.setVisible(true);
        });
    }
}
