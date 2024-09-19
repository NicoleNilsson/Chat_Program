package TCPChat.Util;

import java.io.Serializable;

public class RequestToAddUser implements Serializable {
    private User user;

    public RequestToAddUser(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
