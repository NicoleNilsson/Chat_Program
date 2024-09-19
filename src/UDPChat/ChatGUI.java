package UDPChat;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ChatGUI extends JFrame {
    final private JPanel panel = new JPanel();
    final private JTextArea chatTextArea = new JTextArea(15,30);
    final private JTextArea usersTextArea = new JTextArea("Online:\n", 5,10);
    final private JTextField textField = new JTextField();
    final private JButton button = new JButton("Disconnect");
    final private Border border = BorderFactory.createLineBorder(Color.GRAY, 1);

    public ChatGUI() {}

    public void addToChat(String str) { chatTextArea.append(str + "\n"); }

    public JTextField getTextField() { return textField; }

    public JButton getButton(){ return button; }

    public void setUp(Chat chat) {
        this.setTitle("Chat " + chat.getUser().getName());
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

        textField.addActionListener(chat);
        button.addActionListener(chat);
    }

    public void addUser(String user) {
        if(!usersTextArea.getText().contains(user)){
            usersTextArea.append(user + "\n");
        }
    }

    public void removeUser(String user) {
        String text = usersTextArea.getText();
        String[] lines = text.split("\n");
        usersTextArea.setText("");

        for (String line : lines) {
            if (!line.trim().equalsIgnoreCase(user)) {
                usersTextArea.append(line + "\n");
            }
        }

    }
}
