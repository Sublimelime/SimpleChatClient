package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The server.
 *
 * @author Noah Morton
 *
 * Date created: Mar 9, 2017
 *
 * Part of project: SimpleChatClient
 */
public class ChatServer implements Runnable {

    private static ArrayList<User> currentUsers = new ArrayList<>();
    private ServerSocket serverSocket;

    public ChatServer() {

        try {
            serverSocket = new ServerSocket(1337);
        } catch (IOException e) {
            System.out.println("Cannot create server connection, terminating.");
            System.exit(-2);
        }

        System.out.println("Init server, waiting for connections.");
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (true) { //keeps getting connections, adds them to the list of users
            System.out.println("Waiting for incoming connections.");
            Socket incomingSocket;
            ObjectInputStream is;
            try {
                incomingSocket = serverSocket.accept();
                ObjectOutputStream os = new ObjectOutputStream(incomingSocket.getOutputStream()); //not used, just let client move
                is = new ObjectInputStream(incomingSocket.getInputStream());
                String username = is.readObject().toString();
                System.out.println("Got connection, name is " + username);

                for (User u : ChatServer.getCurrentUsers()) { //tell all users that a user just joined
                    u.getOutput().writeObject("J`" + username + "`" + "none");
                    u.getOutput().reset();
                }
                currentUsers.add(new User(username, incomingSocket));
                System.out.println("Added the user to the list of users.");
            } catch (IOException | ClassNotFoundException ignored) {
            }
        }
    }

    public static ArrayList<User> getCurrentUsers() {
        return currentUsers;
    }

}
