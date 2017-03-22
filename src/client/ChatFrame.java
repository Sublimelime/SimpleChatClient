package client;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import javax.swing.*;

/**
 * This is the frame for my simple chat client.
 *
 * @author Noah Morton
 *
 * Date created: Mar 9, 2017
 *
 * Part of project: SimpleChatClient
 */
public class ChatFrame extends JFrame implements Runnable {

    private JButton btn_exit;
    private JButton btn_send;

    private JLabel lbl_users;
    private JList list_users;

    private JLabel lbl_chatBox;
    private JScrollPane scr_chatBox;
    private JTextArea txt_chatBox;

    private JLabel lbl_message;
    private JTextArea txt_message;

    private final String userName;
    private ArrayList<String> users;

    ObjectInputStream input;
    ObjectOutputStream output;
    Socket socket;

    public ChatFrame() {
        super("Chat Client");
        this.users = new ArrayList<>();
        this.btn_send = new JButton("Send");
        this.btn_exit = new JButton("Exit");
        this.list_users = new JList();
        this.lbl_users = new JLabel("Users:");
        this.lbl_chatBox = new JLabel("Messages:");
        this.scr_chatBox = null;
        this.txt_chatBox = new JTextArea();
        this.txt_message = new JTextArea();
        this.lbl_message = new JLabel("Enter Message:");
        //make connection and ping server
        System.out.println("Making connection...");
        try {
            this.socket = new Socket("127.0.0.1", 1337);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
        } catch (IOException c) {
        }
        System.out.println("Made connection.");
        String username = "";
        while (username.isEmpty()) {
            try {
                System.out.println("Getting username.");
                username = JOptionPane.showInputDialog("Please provide a valid, and unique username.");
            } catch (InputMismatchException e) {
                System.out.println("Username is invalid.");
                continue;
            }
            //code to check if the username is already used
            for (String user : users) {
                if (user.equals(username)) {
                    System.out.println("Username is taken. Try again.");
                } else {
                    username = "";
                }
            }
        }
        userName = username;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);

        users.add(userName); //add ourselves to the list
        try { //give the server our username
            output.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        txt_chatBox.append(username + " joined.\n");

        list_users.setListData(users.toArray());
        list_users.setEnabled(false);
        lbl_users.setBounds(640, 30, 130, 20);
        list_users.setBounds(640, 50, 130, 550);

        scr_chatBox = new JScrollPane(txt_chatBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scr_chatBox.setBounds(20, 50, 600, 550);
        lbl_chatBox.setBounds(20, 30, 100, 20);
        txt_chatBox.setEditable(false);

        txt_message.setBounds(20, 650, 600, 80);
        lbl_message.setBounds(20, 630, 100, 20);

        btn_send.setBounds(640, 650, 130, 30);
        btn_exit.setBounds(640, 700, 130, 30);

        setLayout(null);
        add(txt_message);
        add(lbl_message);
        add(lbl_users);
        add(lbl_chatBox);
        add(scr_chatBox);
        add(list_users);
        add(btn_send);
        add(btn_exit);

        btn_exit.addActionListener((ActionEvent e) -> {
            try {
                output.writeObject("L`" + userName + "`none");
            } catch (IOException g) {
                System.err.println("Unable to send leaving message, sorry.");
                System.exit(0);
            }
            System.exit(0);
        });

        btn_send.addActionListener((ActionEvent e) -> {
            sendtxt_message();
        });

        setVisible(true);
    }

    private void sendtxt_message() {
        String m = userName + ": " + txt_message.getText();
        txt_chatBox.append(m + "\n");
        //todo send the message back to the server
        try {
            output.writeObject(m);
            txt_message.setText(""); //empty the chat box
        } catch (IOException e) {
            System.out.println("Failed to send message.");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //continually listen
        while (true) {
            try {
                Object received = input.readObject();
                if (received instanceof String) {
                    String[] receivedItems = received.toString().split("[`]"); //creates an array with the type of message, user, and message
                    switch (receivedItems[0]) {
                        case "J":
                            users.add(receivedItems[1]);
                            list_users.setListData(users.toArray());
                            txt_chatBox.append(receivedItems[1] + " joined.\n");
                            System.out.println(receivedItems[1] + " joined.");
                            break;
                        case "L":
                            users.remove(receivedItems[1]);
                            list_users.setListData(users.toArray());
                            txt_chatBox.append(receivedItems[1] + " left.\n");
                            System.out.println(receivedItems[1] + " left.");
                            break;
                        case "M":
                            txt_chatBox.append(receivedItems[1] + ": " + receivedItems[2] + "\n");
                            break;
                        default:
                            break;
                    }
                } else if (received instanceof ArrayList) {
                    users = (ArrayList<String>) received;
                    list_users.setListData(users.toArray());
                }
            } catch (IOException | ClassNotFoundException r) {
                r.printStackTrace();
            }
        }
    }
}
