package mygame;
import de.lessvoid.nifty.Nifty;

public class MenuStartState implements MenuStateInterface  {
    private StartScreenState startScreenState;
    public MainGame main;
    public Nifty nifty1;
   
   
   public MenuStartState(StartScreenState sc){
        startScreenState = sc;
    }
    
   public int getUpScreen() {
        return 4;
   }

    public int getDownScreen() {
        return 1;
    }

    public int getEnterScreen() {
        return 0;
    }
}