package server;

import java.io.ObjectInputStream;

/**
 * Description
 *
 * @author Noah Morton
 *
 * Date created: Mar 10, 2017
 *
 * Part of project: SimpleChatClient
 */
public class UserListener implements Runnable {

    private ObjectInputStream input;

    public UserListener(ObjectInputStream input) {
        this.input = input;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            String received = input.readObject().toString();
        } catch (Exception ignored) {
        }

        //todo interpret and send to everyone, use static arraylist in server
    }

}
