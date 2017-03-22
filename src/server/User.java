package server;

import java.io.*;

/**
 * This is a class to hold an active connection.
 *
 * @author Noah Morton
 *
 * Date created: Mar 9, 2017
 *
 * Part of project: SimpleChatClient
 */
public class User implements Runnable {

    private final String username;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public User(String username, ObjectInputStream input, ObjectOutputStream output) {
        this.username = username;
        this.input = input;
        this.output = output;

        Thread t = new Thread(this);
        t.start();
    }

    public String getUsername() {
        return username;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    @Override
    public void run() {
        try {
            String received = input.readObject().toString();

            String[] receivedItems = received.split("[`]"); //creates an array with the type of message, user, and message
            switch (receivedItems[0]) {
                case "M":
                    for (User u : ChatServer.getCurrentUsers()) {
                        //send the received message to everyone
                        u.output.writeObject("M`" + receivedItems[1] + "`" + receivedItems[2]);
                        u.output.reset();
                    }
                    break;
                case "L":
                    for (User u : ChatServer.getCurrentUsers()) {
                        //send the received message to everyone
                        u.output.writeObject("L`" + receivedItems[1] + "`" + "none");
                        u.output.reset();
                    }
                    break;
                case "J":
                    for (User u : ChatServer.getCurrentUsers()) {
                        //send the received message to everyone
                        u.output.writeObject("J`" + receivedItems[1] + "`" + "none");
                        u.output.reset();
                    }
                    break;
                default:
                    break;
            }
        } catch (IOException | ClassNotFoundException ignored) {
        }
    }
}
