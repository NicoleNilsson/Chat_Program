package TCPChat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerListener {
    private final List<ServerThread> threads = new ArrayList<>();

    public ServerListener(){
        final int port = 55556;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, this);
                threads.add(serverThread);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(Object obj) throws IOException {
        for(ServerThread st : threads){
            st.sendObject(obj);
        }
    }

    public void removeThread(ServerThread serverThread) {
        threads.remove(serverThread);
        serverThread.interrupt();
    }

    public static void main(String[] args) {
        ServerListener s = new ServerListener();
    }
}
