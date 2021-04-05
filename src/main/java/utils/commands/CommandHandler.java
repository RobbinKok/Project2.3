package main.java.utils.commands;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.concurrent.Future;

public class CommandHandler implements Runnable {
    private final AsynchronousSocketChannel socketChannel;
    private boolean shouldExit;

    private HashMap<String, Command> commands;

    public CommandHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.commands = new HashMap<>();

        commands.put("ERR", new ErrorCommand());
        commands.put("GAME", new GameCommand());
        commands.put("GAMELIST", new GameListCommand());
        commands.put("PLAYERLIST", new PlayerListCommand());
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(256);

        if (socketChannel == null) {
            shouldExit = true;
        }

        int number = 0;

        while (!shouldExit) {
            try {
                buffer.clear();
                Future<Integer> rawResult = socketChannel.read(buffer);
                rawResult.get();

                buffer.flip();
                String result = new String(buffer.array()).trim();
                unpackResponse(result);

                //TODO: Zero memory of buffer -> find a fix this is ugly.
                for (int i = 0;  i < buffer.limit(); i++)
                    buffer.put(i, (byte)0);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void halt() {
        shouldExit = true;
    }

    private void unpackResponse(String response) {
        System.out.println("Unpacking response: " + response);

        String[] explodedString = response.split("\\s+");

        switch (explodedString[0]) {
            case "ERR":
                commands.get("ERR").execute();
                break;
            case "SVR":
                switch (explodedString[1]) {
                    case "GAME":
                        commands.get("GAME").execute();
                        break;
                    case "GAMELIST":
                        commands.get("GAMELIST").execute();
                        break;
                    case "PLAYERLIST":
                        commands.get("PLAYERLIST").execute();
                        break;
                }
                break;
        }
    }
}