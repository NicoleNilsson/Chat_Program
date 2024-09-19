package TCPChat.Client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ClientGUI extends JFrame {
    final private JPanel panel = new JPanel();
    final private JTextArea chatTextArea = new JTextArea(15,30);
    final private JTextArea usersTextArea = new JTextArea("Online:\n", 5,10);
    final private JTextField textField = new JTextField();
    final private JButton button = new JButton("Disconnect");
    final private Border border = BorderFactory.createLineBorder(Color.GRAY, 1);

    public ClientGUI() {}

    public void addToChat(String str) {chatTextArea.append(str + "\n");}
    public JTextField getTextField() {return textField;}
    public JButton getButton(){return button;}
    public JTextArea getUsersTextArea() {return usersTextArea;}

    public void setUp(Client client) {
        this.setTitle("Chat " + client.getUser().getName());
        this.add(panel);
        panel.setLayout(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);
        panel.add(new JScrollPane(chatTextArea), BorderLayout.CENTER);

        panel.add(usersTextArea, BorderLayout.EAST);
        panel.add(textField, BorderLayout.SOUTH);

        chatTextArea.setBorder(border);
        usersTextArea.setBorder(border);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textField.addActionListener(client);
        button.addActionListener(client);
    }
}
