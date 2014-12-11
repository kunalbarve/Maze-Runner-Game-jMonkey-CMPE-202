package mygame;

import de.lessvoid.nifty.Nifty;
public class MenuExitState implements MenuStateInterface{

    private StartScreenState startScreenState;
    public MainGame main;
    public Nifty nifty1;
   
   
   public MenuExitState(StartScreenState sc){
        startScreenState = sc;
    }
   
    public int getUpScreen() {
      return 3;
    }

    public int getDownScreen() {
        return 0;
    }

    public int getEnterScreen() {
        return 8;
    }
}
