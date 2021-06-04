import javax.swing.*;
import java.awt.*;

public class MainClass extends JFrame {

    public MainClass(String title) {
        super(title);
    }

    public static void main(String[] args) {


        MainClass gameScreen = new MainClass("Flying Chicken");
        gameScreen.setBounds(400, 100, 500, 500);
        gameScreen.setFocusable(false);
        //gameScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //gameScreen.setUndecorated(true);

        CardLayout cardLayout = new CardLayout(); 
        Container container = gameScreen.getContentPane();
        container.setLayout(cardLayout);

        GameLoginScreen loginScreen = new GameLoginScreen(gameScreen, cardLayout);
        container.add("loginScreen", loginScreen);
        loginScreen.requestFocus();
        loginScreen.setFocusable(true);
        loginScreen.setFocusTraversalKeysEnabled(false);

        cardLayout.first(container);
        gameScreen.setVisible(false);
        gameScreen.setVisible(true);
    }

}
