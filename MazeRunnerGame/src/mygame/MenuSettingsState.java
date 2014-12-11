package mygame;

import de.lessvoid.nifty.Nifty;

public class MenuSettingsState implements MenuStateInterface{
  private StartScreenState startScreenState;
    public MainGame main;
    public Nifty nifty1;
   
   
   public MenuSettingsState(StartScreenState sc)
    {
        startScreenState = sc;
    }
    
   public int getUpScreen() {
        return 1;
    }

    public int getDownScreen() {
        return 3;
    }

    public int getEnterScreen() {
     return 0;
    }
}
