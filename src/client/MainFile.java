package client;

import java.util.InputMismatchException;
import javax.swing.JOptionPane;

public class MainFile {

    public static void main(String[] args) {
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
    }
}
