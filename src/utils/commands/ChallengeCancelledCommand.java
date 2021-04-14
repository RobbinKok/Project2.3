package utils.commands;

import utils.ArrayUtil;
import utils.Callback;

import java.util.HashMap;

public class ChallengeCancelledCommand extends Command {

    public ChallengeCancelledCommand(Callback<HashMap<String, String>> callback) {
        this.callback = callback;
    }

    @Override
    public void execute(String data) {
        this.callback.call(ArrayUtil.stringToHashMap(data));
    }
}
