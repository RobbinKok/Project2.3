package utils.commands;

import utils.ArrayUtil;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Future;

public class CommandHandler implements Runnable {
    private final AsynchronousSocketChannel socketChannel;
    private boolean shouldExit;

    private final HashMap<CommandType, Command> commands;

    private ByteBuffer buffer;

    public CommandHandler(AsynchronousSocketChannel socketChannel) {
        this.buffer = ByteBuffer.allocate(256);
        this.socketChannel = socketChannel;
        this.commands = new HashMap<>();
    }

    @Override
    public void run() {
        if (socketChannel == null) {
            shouldExit = true;
        }

        while (!shouldExit) {
            try {
                Future<Integer> rawResult = socketChannel.read(this.buffer);
                rawResult.get();

                String result = new String(this.buffer.array()).trim();
                unpackResponse(result);

                //TODO: Zero memory of buffer -> find a fix this is ugly.
                for (int i = 0;  i < this.buffer.limit(); i++)
                    this.buffer.put(i, (byte)0);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void halt() {
        shouldExit = true;
    }

    public void addCommand(CommandType type, Command command) {
        synchronized (this.commands) {
            this.commands.put(type, command);
        }
    }

    private void unpackResponse(String response) {
        if (response.isBlank())
            return;

        System.out.println("Server: " + response);

        String[] explodedString = response.split("\\s+");
        String data = "";

        switch (explodedString[0]) {
            case "OK":
                explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);
                unpackResponse(ArrayUtil.stringFromStringArray(explodedString));
                break;
            case "ERR":
                commands.get(CommandType.Error).execute("");
                break;
            case "SVR":
                explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);

                switch (explodedString[0]) {
                    case "GAME":
                        commands.get(CommandType.Game).execute("");
                        break;
                    case "GAMELIST":
                        explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);
                        data = ArrayUtil.stringFromStringArray(explodedString);
                        commands.get(CommandType.GameList).execute(data);
                        break;
                    case "PLAYERLIST":
                        explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);
                        data = ArrayUtil.stringFromStringArray(explodedString);
                        commands.get(CommandType.PlayerList).execute(data);
                        break;
                }
        }
    }

    public enum CommandType {
        Game,
        GameList,
        PlayerList,
        Error,
        Challenge
    }
}