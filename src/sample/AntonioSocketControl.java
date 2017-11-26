package sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class AntonioSocketControl implements AntonioControl {

    private static final String MAP = "1";
    private String host = "192.168.1.8";
    private int port = 4420;

    @Override
    public void startMapping() throws IOException {
        sendString(MAP);
    }

    private void sendString(String command) throws IOException {
        System.out.println("Sending command:\n" + command);
        Socket clientSocket = new Socket(host, port);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(command);
        clientSocket.close();
    }
}
