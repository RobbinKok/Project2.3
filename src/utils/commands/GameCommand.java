package utils.commands;

import utils.Callback;

import java.util.HashMap;

public class GameCommand extends Command {
    public GameCommand(Callback<HashMap<String, String>> callback) {
        this.callback = callback;
    }

    @Override
    public void execute(String data) {
        this.callback.call(data);
    }
}