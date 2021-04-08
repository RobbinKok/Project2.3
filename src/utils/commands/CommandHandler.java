package utils.commands;

import utils.ArrayUtil;

import java.lang.reflect.Array;
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



    public CommandHandler(AsynchronousSocketChannel socketChannel) {

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
                ByteBuffer buffer = ByteBuffer.allocate(256);

                Future<Integer> rawResult = socketChannel.read(buffer);
                rawResult.get();

                String[] results = new String(buffer.array()).trim().split("\r\n");
                for (String result : results)
                    unpackResponse(result);

                //TODO: Zero memory of buffer -> find a fix this is ugly.
                for (int i = 0;  i < buffer.limit(); i++)
                    buffer.put(i, (byte)0);
            }
            catch (Exception e) {
                for (int i = 0; i < e.getStackTrace().length; i++) {
                    System.out.println(e.getStackTrace()[i]);
                }
            }
        }
    }

    public void halt() {
        shouldExit = true;
    }

    public void addCommand(CommandType type, Command command) {
        if (commands.containsKey(type))
            return;

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
                            case "WIN":
                            case "LOSS":
                            case "DRAW":
                                explodedString = ArrayUtil.removeFromStringArray(explodedString, 0);
                                data = ArrayUtil.stringFromStringArray(explodedString);
                                commands.get(CommandType.Result).execute(data);
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
        Result,
        MyTurn,
        GameList,
        PlayerList,
        Error,
        Challenge,
        ChallengeCancelled
    }
}