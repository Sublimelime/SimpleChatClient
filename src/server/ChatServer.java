package server;

import java.util.ArrayList;

/**
 * This is the server for my simple chat client.
 *
 * @author Noah Morton
 *
 * Date created: Mar 9, 2017
 *
 * Part of project: SimpleChatClient
 */
public class ChatServer implements Runnable {

    ArrayList<User> currentUsers = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Init server, waiting for connections.");

    }

    @Override
    public void run() {
        while (true) { //keeps getting connections, adds them to the list of users

        }
    }

}
