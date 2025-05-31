package Socket;

import java.io.*;
import java.net.Socket;
import Utils.Console;

public class Client {
    private Socket socket = null;
    private ObjectOutputStream output = null;
    private ObjectInputStream input = null;

    public interface MessageHandler {
        void handle(Object message);
    }

    private MessageHandler messageHandler;

    public Client(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());

            startMessageListener();
        } catch (IOException e) {
            Console.print("ERROR sendObject(): " + e.getMessage(), Console.Color.RED);
        }
    }

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());

            startMessageListener();
        } catch (IOException e) {
            Console.print("ERROR sendObject(): " + e.getMessage(), Console.Color.RED);
        }
    }

    public void sendObject(Object object) {
        synchronized (output) {
            try {
                output.writeObject(object);
                output.flush();
            } catch (IOException e) {
                Console.print("ERROR sendObject(): " + e.getMessage(), Console.Color.RED);
            }
        }
    }

    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
    }

    void handleIncomingObject(Object object) {
        if (messageHandler != null) {
            messageHandler.handle(object);
        } else {
            Console.print("Received object but no handler set.", Console.Color.YELLOW);
        }
    }

    private void startMessageListener() {
        new Thread(() -> {
            try {
                while (isConnected()) {
                    Object received = input.readObject();
                    handleIncomingObject(received);
                }
            } catch (Exception e) {
                if (isConnected()) {
                    Console.print("ERROR in startMessageListener(): " + e.getMessage(), Console.Color.RED);
                }
            } finally {
                disconnect();
            }
        }).start();
    }

    public boolean isConnected() {
        return !socket.isClosed();
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
