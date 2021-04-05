package utils;

import java.util.List;

public class Observable {
    private List<Observer> observers;

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers)
            observer.update();
    }
}
