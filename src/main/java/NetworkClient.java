package main.java;

import main.java.utils.Observable;
import main.java.utils.commands.CommandHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class NetworkClient extends Observable {
    private CommandHandler commandHandler;

    private AsynchronousSocketChannel client;
    private final InetSocketAddress hostAddress;

    public NetworkClient(String host, int port) {
        this.hostAddress = new InetSocketAddress(host, port);
    }

    public void connect() {
        if (client == null || !client.isOpen())
            try {
                this.client = AsynchronousSocketChannel.open();
            }
            catch (Exception e) {
                System.out.println(e);
            }

        client.connect(this.hostAddress, null, new CompletionHandler<>() {
            @Override
            public void completed(Void unused, Object o) {
                System.out.println("Connected to Server");

                commandHandler = new CommandHandler(client);
                Thread thread = new Thread(commandHandler);
                thread.start();
            }

            @Override
            public void failed(Throwable throwable, Object o) {
                System.out.println("Could not connect to server.");
            }
        });
    }

    public void disconnect() {
        commandHandler.halt();

        try  {
            client.close();
        }
        catch (Exception e) {
            if (e instanceof AsynchronousCloseException) {
                System.out.println("Closed network client");
            }
        }
    }

    public void login(String username) {
        sendMessage("login " + username);
    }

    public void logout() {
        sendMessage("logout");
    }

    public void getList(ListType type) {
        sendMessage("get " + type.toString().toLowerCase());
    }

    public void subscribe(GameType gameType) {
        sendMessage("subscribe " + gameType.toString());
    }

    public void move(int x, int y) {
        sendMessage("move " + localToNetworkCoordinates(x, y));
    }

    public void challengePlayer(String name, GameType gameType) {
        sendMessage("challenge " + name + " " + gameType.toString());
    }

    public void acceptChallenge(int challengeId) {
        sendMessage("challenge accept " + challengeId);
    }

    public void forfeit() {
        sendMessage("forfeit");
    }

    public void sendRawMessage(String message) {
        sendMessage(message);
    }

    private void sendMessage(String message) {
        byte[] byteMessage = (message + "\n").getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(byteMessage);
        Future<Integer> result = client.write(buffer);

        try {
            result.get();
        }
        catch (Exception e) {
            System.out.println(e);
        }
//        client.write(buffer, null, new CompletionHandler<Integer, Object>() {
//            @Override
//            public void completed(Integer integer, Object o) {
//                System.out.println("Message send to server: " + message);
//            }
//
//            @Override
//            public void failed(Throwable throwable, Object o) {
//                System.out.println("Error in writing to server");
//            }
//        });
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public enum ListType {
        Gamelist,
        Playerlist
    }

    public enum GameType {
        Reversi,
        TicTacToe
    }

    private int localToNetworkCoordinates(int x, int y) {
        return 0;
    }
}
