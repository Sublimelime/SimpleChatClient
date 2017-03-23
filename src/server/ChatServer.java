package server;

import java.io.*;
import java.net.*;
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
                    ArrayList<User> usersToDrop = new ArrayList<>();
                    for (User u : currentUsers) {
                        //tell all users that a user just joined
                        try {
                            u.getOutput().writeObject("J`" + username + "`" + "none");
                            u.getOutput().reset();
                        } catch (SocketException t) {
                            System.err.println("Unable to reach " + u.getUsername() + ", dropping.");
                            usersToDrop.add(u);
                        }
                    }
                    System.out.println("Told all existing users about the new connection.");

                    usersToDrop.forEach((u) -> { //drop all dead users
                        currentUsers.remove(u);
                    });

                    ArrayList<String> currentUsernames = new ArrayList<>();
                    currentUsers.forEach((u) -> {
                        currentUsernames.add(u.getUsername());
                    });
                    os.writeObject(currentUsernames);
                    System.out.println("Sent list of users to new user.");

                }
                currentUsers.add(new User(username, is, os));
                System.out.println("Added the user to the list of users.");
            } catch (IOException | ClassNotFoundException t) {
                t.printStackTrace();
            }
        }
    }

    public synchronized static ArrayList<User> getCurrentUsers() {
        return currentUsers;
    }

    protected static User resolveUserFromUsername(String un) {
        for (User u : currentUsers) {
            if (u.getUsername().equals(un)) {
                return u;
            }
        }
        return null;
    }

}
