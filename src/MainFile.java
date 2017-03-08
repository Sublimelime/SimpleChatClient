
import java.util.InputMismatchException;
import javax.swing.JOptionPane;

public class MainFile {

    public static void main(String[] args) {
        String username = "";
        while (username.equals("")) {
            try {
                username = JOptionPane.showInputDialog("Please provide a valid username.");
            } catch (InputMismatchException e) {
                System.out.println("Username is invalid.");
            }
        }
        //todo code to check if already taken
        new ChatFrame(username);
    }
}
