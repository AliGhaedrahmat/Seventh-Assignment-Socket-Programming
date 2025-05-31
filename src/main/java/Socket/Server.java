package Socket;

import Shared.Message;
import Utils.Console;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private int port;
    private String ip;
    private final List<Client> clients = new CopyOnWriteArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Console.print("Server started on port " + port, Console.Color.GREEN);

            this.ip = InetAddress.getLocalHost().getHostAddress();
            Console.print("Server IP: " + ip, Console.Color.GREEN);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Console.print("New client connected: " + clientSocket.getRemoteSocketAddress(), Console.Color.GREEN);

                Client handler = new Client(clientSocket);
                handler.setMessageHandler(message -> {
                    try {

                        Message received = (Message) message;
                        if (received.hasFile()){
                            Console.print("Received FILE client: " + received.getUsername() + ": " + received.getMessage(), Console.Color.CYAN);

                        }
                        else {
                            Console.print("Received text client: " + received.getUsername() + ": " + received.getMessage(), Console.Color.CYAN);

                        }
                        broadcast(received);
                    } catch (Exception e) {
                        Console.print(e.getMessage(), Console.Color.RED);
                    }
                });
                clients.add(handler);
            }

        } catch (IOException e) {
            Console.print("Server error: " + e.getMessage(), Console.Color.RED);
        }
    }

    public String getIp() {
        return ip;
    }

    public void broadcast(Message message) {
        for (Client client : clients) {
            try {
                client.sendObject(message);
            } catch (Exception e) {
                Console.print("Broadcast error: " + e.getMessage(), Console.Color.RED);
            }
        }
    }
}
