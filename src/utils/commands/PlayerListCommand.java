package utils.commands;

import utils.ArrayUtil;
import utils.Callback;

import java.util.ArrayList;

public class PlayerListCommand extends Command {
    public PlayerListCommand(Callback<ArrayList<String>> callback) {
        this.callback = callback;
    }

    @Override
    public void execute(String data) {
        callback.call(ArrayUtil.stringToArray(data));
    }
}
