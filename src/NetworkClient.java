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

    private String playerName;
    private String firstPlayer;
    private String opponentName;

    private boolean playAsAI;

    private boolean isInMatch;

    public boolean getIsInMatch() {
        return this.isInMatch;
    }

    public void setIsInMatch(boolean value) {
        this.isInMatch = value;
    }

    public NetworkClient(String host, int port) {
        this.hostAddress = new InetSocketAddress(host, port);
    }

    public void connect(InetSocketAddress address, Callback<Void> callback) {
        if (client == null || !client.isOpen())
            try {
                this.client = AsynchronousSocketChannel.open();
            }
            catch (Exception e) {
                System.out.println(e);
            }

        client.connect(address, null, new CompletionHandler<>() {
            @Override
            public void completed(Void unused, Object o) {
                ByteBuffer buffer = ByteBuffer.allocate(256);
                Future<Integer> result = client.read(buffer);

                try { result.get(); } // Remove the server welcome message.
                catch (Exception e) {
                    System.out.println(e);
                }

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

    public void subscribe(String gameType) {
        sendMessage("subscribe " + gameType);
    }

    public void move(int x, int y, GameType type) {
        sendMessage("move " + localToNetworkCoordinates(x, y, type));
    }

    public void challengePlayer(String name, String gameType) {
        sendMessage("challenge " + "\"" + name + "\"" + " " + "\"" + gameType + "\"");
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

    public boolean isConnected() {
        return this.client.isOpen();
    }

    public enum ListType {
        Gamelist,
        Playerlist
    }

    public enum GameType {
        Reversi,
        TicTacToe
    }

    public static int localToNetworkCoordinates(int x, int y, GameType type) {
        int dimension = 0;

        if (type.equals(GameType.Reversi))
            dimension = 8;
        else if (type.equals(GameType.TicTacToe))
            dimension = 3;

        return y * dimension + x;
    }

    public static int[] networkToLocalCoordinates(int coord, GameType type) {
        int dimension = 0;

        if (type.equals(GameType.Reversi))
            dimension = 8;
        else if (type.equals(GameType.TicTacToe))
            dimension = 3;

        int[] coords = new int[2];

        coords[0] = Math.floorMod(coord, dimension);
        coords[1] = (int)Math.floor(coord / dimension);

        return coords;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getFirstPlayer() {
        return this.firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setPlayAsAI(boolean value) {
        this.playAsAI = value;
    }

    public boolean getPlayAsAI() {
        return this.playAsAI;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }
}
