package ClientServer;

import GUI.*;

import java.awt.*;
import java.io.FileReader;
import java.net.Socket;
import java.io.IOException;
import java.util.Properties;

// The client class.
public class WorkerRunnable implements Runnable {
    protected Socket clientSocket;
    // CTR
    public WorkerRunnable(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try { // Start the full game flow.
                    WelcomePage welcomePage = new WelcomePage();
                    welcomePage.setTitle("Welcome Page For Match4Sport");
                    welcomePage.setVisible(true);
                    //loginFrame.setVisible(true);
                    //UserRegistration userRegistration = new UserRegistration();
                    //userRegistration.setVisible(true);
                    //JoinGame joinGame = new JoinGame("yasofer");
                    //joinGame.setVisible(true);
                    //FindGame findGame = new FindGame();
                    //findGame.setVisible(true);
                    //ChangePassword changePassword = new ChangePassword("yasofer");
                    //changePassword.setVisible(true);
                    //UserHome userHome = new UserHome("yasofer");
                    //userHome.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void main(String[] args) throws IOException {
        String[] transportProps = Utils.PropertiesReaders.getTransportProperties();
        System.out.println("server.host: " + transportProps[0]); // Debug print
        System.out.println("server.port: " + Integer.parseInt(transportProps[1])); // Debug print
        Socket clientSocket = new Socket(transportProps[0], Integer.parseInt(transportProps[1]));
        WorkerRunnable workerRunnable = new WorkerRunnable(clientSocket);
        workerRunnable.run();
    }
}
