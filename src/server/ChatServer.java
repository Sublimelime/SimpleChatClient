package server;

import java.io.IOException;
import java.io.ObjectInputStream;
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

    ArrayList<User> currentUsers = new ArrayList<>();
    ServerSocket serverSocket;

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

        while (true) {

        }
    }

    @Override
    public void run() {
        while (true) { //keeps getting connections, adds them to the list of users
            System.out.println("Waiting for incoming connections.");
            Socket socket1;
            ObjectInputStream is;
            try {
                socket1 = serverSocket.accept();
                is = new ObjectInputStream(socket1.getInputStream());
                currentUsers.add(new User(is.readObject().toString(), socket1));
            } catch (Exception ignored) {
            }

        }
    }
}
