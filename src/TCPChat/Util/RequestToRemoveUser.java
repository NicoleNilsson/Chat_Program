package TCPChat.Util;

import java.io.Serializable;

public class RequestToRemoveUser implements Serializable {
    private User user;

    public RequestToRemoveUser(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
