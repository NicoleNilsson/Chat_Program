package UDPChat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

public class Chat extends JFrame implements ActionListener {
    private static final String REQUEST_USERS = "RequestUsers:";
    private static final String USER_PREFIX = "User:";
    private static final String DISCONNECT_PREFIX = "Disconnected:";

    private final String ip = "myIP";
    private final int port = 55555;
    private final InetAddress inetAddress = InetAddress.getByName(ip);
    private final MulticastSocket socket = new MulticastSocket(port);

    private final ChatGUI gui = new ChatGUI();
    private final User user = new User();

    public Chat() throws IOException {
        String name = JOptionPane.showInputDialog(null, "Welcome!\nTo join chat, please enter your name:");
        if (name == null || name.isEmpty()){System.exit(0);}

        user.setName(name);
        gui.setUp(this);

        InetSocketAddress group = new InetSocketAddress(inetAddress, port);
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
        socket.joinGroup(group, networkInterface);

        sendMessage(user.getName() + " joined the chat");
        sendMessage(REQUEST_USERS);

        byte[] data = new byte[256];
        DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);

        while (true) {
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            handleMessage(message);
        }
    }

    public User getUser(){return user;}

    private void sendMessage(String message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
        socket.send(packet);
    }

    private void handleMessage(String toUser) throws IOException {
        if(toUser.equalsIgnoreCase(REQUEST_USERS)){
            sendMessage(USER_PREFIX + user.getName());
        }else if(toUser.startsWith(USER_PREFIX)){
            String remoteUser = toUser.substring(USER_PREFIX.length());
            gui.addUser(remoteUser);
        }else if(toUser.startsWith(DISCONNECT_PREFIX)){
            String remoteUser = toUser.substring(DISCONNECT_PREFIX.length());
            gui.removeUser(remoteUser);
        }else gui.addToChat(toUser);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == gui.getTextField()) {
            try {
                sendMessage(user.getName() + ": " + gui.getTextField().getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gui.getTextField().setText("");
        }else if(ae.getSource() == gui.getButton()){
            try {
                sendMessage(user.getName() + " left the chat");
                sendMessage(DISCONNECT_PREFIX + user.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException {
        Chat chat = new Chat();
    }
}
