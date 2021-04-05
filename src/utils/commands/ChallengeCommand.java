package utils.commands;

import utils.Callback;

import java.util.HashMap;

public class ChallengeCommand extends Command {
    public ChallengeCommand(Callback<HashMap<String, String>> callback) {
        this.callback = callback;
    }

    @Override
    public void execute(String data) {
        callback.call(data);
    }
}
