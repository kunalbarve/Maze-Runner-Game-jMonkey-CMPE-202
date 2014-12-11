package mygame;

import de.lessvoid.nifty.Nifty;

public class MenuControlState implements MenuStateInterface{

    private StartScreenState startScreenState;
    public MainGame main;
    public Nifty nifty1;
   
   
   public MenuControlState(StartScreenState sc){
        startScreenState = sc;
    }
    
   public int getUpScreen() {
        return 0;
    }

    public int getDownScreen() {
        return 2;
    }

    public int getEnterScreen() {
        return 5;
    }
}
