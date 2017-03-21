package server;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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

    private Socket socket;
    private String username;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public User(String username, Socket socket) {
        try {
            this.socket = socket;
            this.username = username;
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException h) {
            System.out.println("Unknown host exception.");
            h.printStackTrace();
        } catch (IOException i) {
            System.out.println("Cannot connect to server.");
            i.printStackTrace();
        }
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
        } catch (Exception ignored) {
        }
    }
}
