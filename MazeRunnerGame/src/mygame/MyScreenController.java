package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MyScreenController extends AbstractAppState implements ScreenController
{
    protected InputManager inputManager;
     protected AppStateManager stateManager;
     public MenuStartState menuStartState;
     public MenuControlState menuControlState;
     public MenuSettingsState menuSettingState;
     public MenuCreditsState menuCreditState;
     public MenuExitState menuExitState;
     
     public MenuStateInterface currentState;
    
     public StartScreenState startScreenState;
    public Nifty nifty;
    public Screen screen;
    
    public int current;
    
    public Application app;
    
    
     
    public MyScreenController(SimpleApplication application){
      this.app = application;
    }
    
    public void bind(Nifty nifty, Screen screen) {}

      
    public void onStartScreen() {}

    public void onEndScreen() {}

    
     @Override
 public void initialize(AppStateManager stateManager,Application app){
     this.app =app;
 }
     
  @Override
  public void update(float tpf) {}
}
