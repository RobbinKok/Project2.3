package utils.commands;

import utils.ArrayUtil;
import utils.Callback;

import java.lang.reflect.Array;
import java.util.HashMap;

public class ResultCommand extends Command {
    public ResultCommand(Callback<HashMap<String, String>> callback) {
        this.callback = callback;
    }

    @Override
    public void execute(String data) {
        this.callback.call(ArrayUtil.stringToHashMap(data));
    }
}
