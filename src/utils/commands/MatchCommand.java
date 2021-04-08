package utils.commands;

import utils.ArrayUtil;
import utils.Callback;

import java.util.HashMap;

public class MatchCommand extends Command {
    public MatchCommand(Callback<HashMap<String, String>> callback) {
        this.callback = callback;
    }

    @Override
    public void execute(String data) {
        this.callback.call(ArrayUtil.stringToHashMap(data));
    }
}
