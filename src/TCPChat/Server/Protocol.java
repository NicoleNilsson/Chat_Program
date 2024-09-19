package TCPChat.Server;

import TCPChat.Util.Initiator;
import TCPChat.Util.RequestToAddUser;
import TCPChat.Util.RequestToRemoveUser;
import TCPChat.Util.RequestUsersOnline;

public class Protocol {
    private static final int BEFORE_INIT = 0;
    private static final int WAITING_FOR_REQUEST = 1;

    private int state = BEFORE_INIT;

    public Object handleInput(Object input){
        Object output = null;

        if(state == BEFORE_INIT){
            output = new Initiator();
            state = WAITING_FOR_REQUEST;
        }else if(state == WAITING_FOR_REQUEST) {
            switch (input){
                case RequestUsersOnline ignored -> output = new RequestUsersOnline();
                case String message -> output = message;
                case RequestToRemoveUser request -> output = new RequestToRemoveUser(request.getUser());
                case RequestToAddUser request -> output = new RequestToAddUser(request.getUser());
                default -> throw new IllegalStateException("Unexpected value: " + input);
            }
        }

        return output;
    }
}
