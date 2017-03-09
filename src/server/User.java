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
public class User {

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
    }

    public String getUsername() {
        return username;
    }

    public InputStream getInput() {
        return input;
    }

    public OutputStream getOutput() {
        return output;
    }

}
