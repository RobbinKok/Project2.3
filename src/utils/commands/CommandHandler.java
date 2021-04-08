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
                buffer.clear();
                Future<Integer> rawResult = socketChannel.read(this.buffer);
                rawResult.get();

                String[] results = new String(this.buffer.array()).trim().split("\r\n");
                for (String result : results)
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

        String[] explodedString = response.split("\\s+");
        String data = "";

        System.out.println(response);

        switch (explodedString[0]) {
            case "OK":
                break;
            case "ERR":
                explodedString = ArrayUtil.removeFromStringArray(explodedString,  0);
                data = ArrayUtil.stringFromStringArray(explodedString);
                commands.get(CommandType.Error).execute(data);
                break;
            case "SVR":
                explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);

                switch (explodedString[0]) {
                    case "GAME":
                        explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);

                        switch (explodedString[0]) {
                            case "CHALLENGE":
                                explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);

                                if (explodedString[0].equals("CANCELLED")) {
                                    explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);
                                    data = ArrayUtil.stringFromStringArray(explodedString);
                                    commands.get(CommandType.ChallengeCancelled).execute(data);
                                    break;
                                }

                                data = ArrayUtil.stringFromStringArray(explodedString);
                                commands.get(CommandType.Challenge).execute(data);
                                break;
                            case "MATCH":
                                explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);
                                data = ArrayUtil.stringFromStringArray(explodedString);
                                commands.get(CommandType.Match).execute(data);
                                break;
                            case "YOURTURN":
                                explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);
                                data = ArrayUtil.stringFromStringArray(explodedString);
                                commands.get(CommandType.MyTurn).execute(data);
                                break;
                            case "MOVE":
                                explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);
                                data = ArrayUtil.stringFromStringArray(explodedString);
                                commands.get(CommandType.Move).execute(data);
                                break;
                        }
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
        Move,
        Match,
        MyTurn,
        GameList,
        PlayerList,
        Error,
        Challenge,
        ChallengeCancelled
    }
}