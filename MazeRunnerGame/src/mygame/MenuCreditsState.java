package mygame;

import de.lessvoid.nifty.Nifty;

public class MenuCreditsState implements MenuStateInterface{

    private StartScreenState startScreenState;
    public MainGame main;
    public Nifty nifty1;
   
   
   public MenuCreditsState(StartScreenState sc){
        startScreenState = sc;
    }
    
   public int getUpScreen() {
        return 2;
    }

    public int getDownScreen() {
        return 4;
    }

    public int getEnterScreen() {
        return 6;
    }
}