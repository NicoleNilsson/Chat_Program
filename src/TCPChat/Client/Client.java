package TCPChat.Client;

import TCPChat.Util.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client implements ActionListener {
    private final int port = 55556;
    private final String ip = "myIP";
    private final Socket socket = new Socket(ip, port);
    private final ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

    private final User user;
    private final ClientGUI gui = new ClientGUI();
    private final List<User> onlineUsers = new CopyOnWriteArrayList<>();

    private boolean running = true;

    public Client() throws IOException, ClassNotFoundException {
        String name = JOptionPane.showInputDialog(null, "Welcome!\nTo join chat, please enter your name:");
        if (name == null || name.isEmpty()){System.exit(0);}

        user = new User(name);
        gui.setUp(this);
        Object toClient;

        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        while(running && ((toClient = in.readObject())) != null){
            switch (toClient) {
                case Initiator ignored -> {
                    user.setOnline(true);
                    out.writeObject(new RequestUsersOnline());
                    out.writeObject(user.getName() + " joined the chat!");
                }
                case RequestUsersOnline ignored -> out.writeObject(new RequestToAddUser(user));
                case RequestToAddUser remoteUser -> addUser(remoteUser.getUser());
                case String message -> gui.addToChat(message);
                case RequestToRemoveUser remoteUser ->removeUser(remoteUser.getUser());
                default -> throw new IllegalStateException("Unexpected value: " + toClient);
            }
        }
    }

    public User getUser() {return user;}

    private void addUser(User remoteUser){
        for(User u : onlineUsers){
            if(u.getID() == remoteUser.getID()){return;}
        }
        onlineUsers.add(remoteUser);
        gui.getUsersTextArea().append(remoteUser.getName() + "\n");
    }

    private void removeUser(User remoteUser){
        onlineUsers.removeIf(u -> u.getID() == remoteUser.getID());
        gui.getUsersTextArea().setText("");
        gui.getUsersTextArea().setText("Online:\n");
        for(User u : onlineUsers){
            gui.getUsersTextArea().append(u.getName() + "\n");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        Object obj = null;

        if(ae.getSource() == gui.getTextField()) {
            obj = user.getName() + ": " + gui.getTextField().getText();
            gui.getTextField().setText("");
        }else if(ae.getSource() == gui.getButton()){
            try {out.reset(); out.writeObject(user.getName() + " left the chat");}
            catch (IOException e) {throw new RuntimeException(e);}

            obj = new RequestToRemoveUser(user);
            user.setOnline(false);
        }

        try {out.reset(); out.writeObject(obj);}
        catch (IOException e) {throw new RuntimeException(e);}

        if(!user.isOnline()){running = false; System.exit(0);}

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, RuntimeException {
        Client c = new Client();
    }
}