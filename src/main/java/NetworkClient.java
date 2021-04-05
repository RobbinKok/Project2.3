//package main.java;

import utils.Observable;
import utils.commands.CommandHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class NetworkClient extends Observable {
    private CommandHandler commandHandler;

    private AsynchronousSocketChannel client;
    private InetSocketAddress hostAddress;

    public NetworkClient(String host, int port) {
        this.hostAddress = new InetSocketAddress(host, port);

    }

    public void connect() {
        if (client == null || !client.isOpen())
            try {
                this.client = AsynchronousSocketChannel.open();
            }
            catch (Exception e) {}

        client.connect(this.hostAddress, null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void unused, Object o) {
                System.out.println("Connected to Server");
                //onResponse();

                commandHandler = new CommandHandler(client);
                Thread thread = new Thread(commandHandler);
                thread.start();
            }

            @Override
            public void failed(Throwable throwable, Object o) {

            }
        });
    }

//    private void onResponse() {
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        Future<Integer> result = client.read(buffer);
//
//        try {
//            result.get();
//            String echo = new String(buffer.array()).trim();
//            System.out.println(echo);
//        }
//        catch (Exception e) {
//            System.out.println(e);
//        }

//        client.read(buffer, null, new CompletionHandler<Integer, ByteBuffer>() {
//            @Override
//            public void completed(Integer integer, ByteBuffer byteBuffer) {
//                System.out.println(new String(buffer.array()).trim());
//            }
//
//            @Override
//            public void failed(Throwable throwable, ByteBuffer byteBuffer) {
//
//            }
//        });
//    }

    public void disconnect() {
        try  {
            client.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            commandHandler.halt();
        }
    }

    public void login(String username) {
        sendMessage("login " + username);
    }

    public void logout() {
        sendMessage("logout");
    }

    public void getList(ListType type) {
        sendMessage("get " + type.toString().toLowerCase()); //TODO: change enum values to gamelist or playerlist
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
        sendMessage("forfeit");
    }

    public void sendRawMessage(String message) {
        sendMessage(message);
    }

    private void sendMessage(String message) {
        byte[] byteMessage = (message + "\n").getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(byteMessage);
        client.write(buffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer integer, Object o) {
                System.out.println("Message send to server: " + message);
//                onResponse();
            }

            @Override
            public void failed(Throwable throwable, Object o) {
                System.out.println("Error in writing to server");
            }
        });
    }

    public enum ListType {
        Gamelist,
        Playerlist
    }

    public enum GameType {
        Reversi,
        TicTacToe
    }
}
