package utils.commands;

import utils.Callback;

public class ErrorCommand extends Command {
    public ErrorCommand(Callback<String> callback) {
        this.callback = callback;
    }

    @Override
    public void execute(String data) {
        this.callback.call(data);
    }
}
