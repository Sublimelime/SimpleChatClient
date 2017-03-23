package server;

import java.io.*;
import java.util.ArrayList;

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
        ArrayList<User> usersToDrop = new ArrayList<>();
        while (true) {
            try {
                String received = input.readObject().toString();

                String[] receivedItems = received.split("[`]"); //creates an array with the type of message, user, and message
                switch (receivedItems[0]) {
                    case "M":
                        ChatServer.getCurrentUsers().forEach((u) -> {
                            //send the received message to everyone
                            try {
                                u.output.writeObject("M`" + receivedItems[1] + "`" + receivedItems[2]);
                                u.output.reset();
                            } catch (IOException mes) {
                                System.err.println("Unable to reach " + username + ", dropping.");
                                //if it fails to send, invalid user
                                usersToDrop.add(u);
                            }
                        });
                        break;
                    case "L":
                        ChatServer.getCurrentUsers().forEach((u) -> {
                            //send the received message to everyone
                            try {
                                u.output.writeObject("L`" + receivedItems[1] + "`" + "none");
                                u.output.reset();
                            } catch (IOException mes) {
                                System.err.println("Unable to reach " + username + ", dropping.");
                                //if it fails to send, invalid user
                                usersToDrop.add(u);
                            }
                        });
                        break;
                    case "J":
                        ChatServer.getCurrentUsers().forEach((u) -> {
                            //send the received message to everyone
                            try {
                                u.output.writeObject("J`" + receivedItems[1] + "`" + "none");
                                u.output.reset();
                            } catch (IOException mes) {
                                System.err.println("Unable to reach " + username + ", dropping.");
                                //if it fails to send, invalid user
                                usersToDrop.add(u);
                            }
                        });
                        break;
                    default:
                        break;
                }
                usersToDrop.forEach((u) -> {
                    ChatServer.getCurrentUsers().remove(u);
                });
            } catch (IOException | ClassNotFoundException ig) {
                System.err.println("Unable to reach " + username + ", dropping.");
                //if it fails to send, invalid user
                ChatServer.getCurrentUsers().remove(ChatServer.resolveUserFromUsername(username));
                break; //end the thread
            }
        }
    }
}
