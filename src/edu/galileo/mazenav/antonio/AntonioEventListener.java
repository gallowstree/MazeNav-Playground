package edu.galileo.mazenav.antonio;

import edu.galileo.mazenav.MappingListener;
import edu.galileo.mazenav.common.Direction;
import edu.galileo.mazenav.common.Tile;
import edu.galileo.mazenav.common.Vec2;
import io.vavr.collection.Set;
import io.vavr.collection.SortedSet;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.galileo.mazenav.antonio.EventType.DISCOVERED;
import static edu.galileo.mazenav.antonio.EventType.LOCATION;
import static edu.galileo.mazenav.common.Utils.tile;

public class AntonioEventListener {
    ServerSocket welcomeSocket;
    private boolean running;

    public MappingListener mappingListener;

    public AntonioEventListener(int port) throws IOException {
        welcomeSocket = new ServerSocket(port);
        welcomeSocket.setSoTimeout(1000);
    }

    public void run() throws IOException {
        running = true;
        Socket connectionSocket;

        while (running) {
            try {
                connectionSocket = welcomeSocket.accept();
            } catch (SocketTimeoutException e) {
                continue;
            }

            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            String msg = inFromClient.readLine();
            System.out.println("Received: " + msg);
            onEvent(msg);
            //DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            //outToClient.writeBytes(capitalizedSentence);
        }
    }

    private void onEvent(String body) {

        String[] params = body.split("\\|");
        String prefix = params[0];

        if (LOCATION.prefix.equals(prefix)) {
            System.out.println("location");
            onLocationCommand(params);
        } else if (DISCOVERED.prefix.equals(prefix)) {
            System.out.println("discovered");
            onDiscoveredCommand(body);
        }
    }

    private void onDiscoveredCommand(String body) {

    }

    private void onLocationCommand(String[] params) {
        String[] coordsAsText = params[1].split(",");
        Object[] coords = Stream.of(coordsAsText).map(Integer::parseInt).map(Integer::intValue).collect(Collectors.toList()).toArray();

        Vec2 location = new Vec2((int)coords[1], (int)coords[0]);

        Direction direction = parseDirection(params[2]);
        System.out.println("visited tile " + location + " facing " + direction.name());
        mappingListener.tileVisited(location, direction);
        mappingListener.tileDiscovered(location, tile().canMoveTo);
    }

    private Direction parseDirection(String str) {
        return Direction.fromString(str).orElseThrow(() -> new IllegalArgumentException("No direction corresponds to string <" + str + ">"));
    }
}
