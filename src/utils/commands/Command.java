package utils.commands;

import utils.Callback;

public abstract class Command {
    protected String data;
    protected Callback callback;

    public abstract void execute(String data);
}
