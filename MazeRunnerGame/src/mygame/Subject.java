package mygame;

public interface Subject{
    public abstract void add(Observer obj);
    public abstract void remove(Observer obj);
    public abstract void notifyObservers();
}
 
