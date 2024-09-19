package TCPChat.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread{
    final private ServerListener serverListener;
    final private ObjectInputStream in;
    final private ObjectOutputStream out;
    final private Protocol protocol = new Protocol();
    private boolean running = true;

    public ServerThread(Socket clientSocket, ServerListener serverListener) throws IOException {
        this.serverListener = serverListener;
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());

        try {out.writeObject(protocol.handleInput(null));}
        catch (IOException e) {throw new RuntimeException(e);}
    }

    public synchronized void sendObject(Object obj) throws IOException {
        out.writeObject(obj);
    }

    public void run() {
        while (running) {
            Object obj;
            try {
                if ((obj = in.readObject()) != null) {
                    serverListener.broadcast(protocol.handleInput(obj));
                }
            } catch (Exception e) {
                running = false;
                serverListener.removeThread(this);
            }
        }
    }
}
