package utils.commands;

import utils.Callback;

import java.util.concurrent.Callable;

public abstract class Command {
    protected String data;
    protected Callback callback;

    public abstract void execute(String data);
}
