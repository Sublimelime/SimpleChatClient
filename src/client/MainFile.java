package client;

import java.util.InputMismatchException;
import javax.swing.JOptionPane;

/**
 * This is the client for my simple chat client.
 *
 * @author Noah Morton
 *
 * Date created: Mar 9, 2017
 *
 * Part of project: SimpleChatClient
 */
public class MainFile {

    public static void main(String[] args) {
        //make connection and ping server

        String username = "";
        while (true) {
            try {
                username = JOptionPane.showInputDialog("Please provide a valid, and unique username.");
            } catch (InputMismatchException e) {
                System.out.println("Username is invalid.");
            }
            //todo code to check if the username is already used
            break;
        }

        new ChatFrame(username);
        //send join event to server
    }
}
