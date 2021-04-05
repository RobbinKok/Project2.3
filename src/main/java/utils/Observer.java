package utils;

public abstract class Observer {
    protected Observable observable;

    public abstract void update();
}
