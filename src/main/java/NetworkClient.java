package main.java;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

public class NetworkClient {
    private AsynchronousSocketChannel client;
    private InetSocketAddress hostAddress;

    public NetworkClient(String host, int port) {
        this.hostAddress = new InetSocketAddress(host, 11);

        try {
            this.client = AsynchronousSocketChannel.open();
        }
        catch (Exception e) { }

        listen();
    }

    public void connect() {
        Future<Void> future = client.connect(this.hostAddress);
    }

    public void login(String username) {
        sendMessage("login " + username);
    }

    public void logout() {
        sendMessage("logout");
    }

    public void getList(ListType type) {
        sendMessage("get " + type.toString()); //TODO: change enum values to gamelist or playerlist
    }

    public void subscribe(GameType gameType) {
        sendMessage("subscribe " + gameType.toString());
    }

    public void move(String position) {
        sendMessage("move " + position);
    }

    public void challengePlayer(String name, GameType gameType) {
        sendMessage("challenge " + name + " " + gameType.toString());
    }

    public void acceptChallenge(int challengeId) {
        sendMessage("challenge accept " + challengeId);
    }

    public void forfeit() {

    }

    private void sendMessage(String message) {
        byte[] byteMessage = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(byteMessage);
        Future<Integer> writeResult = client.write(buffer);

        try {
            writeResult.get();
            buffer.flip();
            Future<Integer> readResult = client.read(buffer);

            readResult.get();
            String echo = new String(buffer.array()).trim();
            buffer.clear();
        }
        catch (Exception e) {}
    }

    private void listen() {

    }

    public enum ListType {
        Game,
        Player
    }

    public enum GameType {
        Reversi,
        TicTacToe
    }
}
