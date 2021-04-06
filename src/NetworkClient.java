//package java;

import utils.Callback;
import utils.Observable;
import utils.commands.CommandHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class NetworkClient extends Observable {
    private CommandHandler commandHandler;
    private Thread commandHandlerThread;

    private AsynchronousSocketChannel client;
    private final InetSocketAddress hostAddress;

    public NetworkClient(String host, int port) {
        this.hostAddress = new InetSocketAddress(host, port);
    }

    public void connect(Callback<Void> callback) {
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
                ByteBuffer buffer = ByteBuffer.allocate(256);
                Future<Integer> result = client.read(buffer);

                try { result.get(); } // Remove the server welcome message.
                catch (Exception ignored) {}

                commandHandler = new CommandHandler(client);

                callback.call(null);
            }

            @Override
            public void failed(Throwable throwable, Object o) {
                System.out.println("Could not connect to server.");
            }
        });
    }

    public void disconnect() {
        this.commandHandler.halt();

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
        sendMessage("challenge " + "\"" + name + "\"" + " " + "\"" + gameType.toString() + "\"");
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
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void startCommandHandler() {
        this.commandHandlerThread = new Thread(commandHandler);
        this.commandHandlerThread.start();
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
