package TCPChat.Util;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class User implements Serializable {
    private final String name;
    private final int ID;
    private boolean online = false;

    public User(String name){
        this.name = name;
        ID = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }

    public String getName() {return name;}
    public int getID() {return ID;}
    public boolean isOnline(){return online;}
    public void setOnline(boolean online){this.online = online;}
}
