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

    private static final ArrayList<User> currentUsers = new ArrayList<>();
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
            ObjectOutputStream os;
            try {
                incomingSocket = serverSocket.accept();
                os = new ObjectOutputStream(incomingSocket.getOutputStream());
                is = new ObjectInputStream(incomingSocket.getInputStream());
                String username = is.readObject().toString();
                System.out.println("Got connection, name is " + username);

                if (!currentUsers.isEmpty()) { //build a list of usernames and send it
                    ArrayList<String> currentUsernames = new ArrayList<>();
                    currentUsers.forEach((u) -> {
                        currentUsernames.add(u.getUsername());
                    });
                    os.writeObject(currentUsernames);
                    System.out.println("Sent list of users to new user.");

                    for (User u : currentUsers) {
                        //tell all users that a user just joined
                        u.getOutput().writeObject("J`" + username + "`" + "none");
                        u.getOutput().reset();
                    }

                    System.out.println("Told all existing users about the new connection.");
                }
                currentUsers.add(new User(username, is, os));
                System.out.println("Added the user to the list of users.");
            } catch (IOException | ClassNotFoundException t) {
                t.printStackTrace();
            }
        }
    }

    public static ArrayList<User> getCurrentUsers() {
        return currentUsers;
    }

}
