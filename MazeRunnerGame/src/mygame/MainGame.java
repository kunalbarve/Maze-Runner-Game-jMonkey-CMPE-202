package mygame;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;

public class MainGame extends AbstractSimpleApplication{
 
  private Trigger pause_trigger = new KeyTrigger(KeyInput.KEY_Q);
  private Trigger start_trigger = new KeyTrigger(KeyInput.KEY_RETURN);
  public StateManager startScreenState;
  public StateManager gameState;
  public StateManager currentState;
  public StateManager nextState;
  public AbstractSimpleApplication application;
  
  public static void main(String[] args) {
    MainGame app = new MainGame();
    app.start();
  }
 
  @Override
  public void simpleInitApp() {
    setDisplayFps(true);
    setDisplayStatView(false);
    application = this;
    startScreenState    = new StartScreenState(this);
    gameState = new GameState(this);
 
    stateManager.attach(startScreenState);
    
    setNextState(gameState);
  
    inputManager.addMapping("Game Start", start_trigger);
    inputManager.addListener(actionListener, new String[]{"Game Start"});
    
     
    inputManager.addMapping("Game Pause", pause_trigger);
    inputManager.addListener(actionListener, new String[]{"Game Pause"});
 
  }
 
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean isPressed, float tpf) {
      if (name.equals("Game Start") && !isPressed && startScreenState.getCurrent() == 0){
          doAttach();
      }else if (name.equals("Game Start") && !isPressed && startScreenState.getCurrent() == 4){
          application.stop();
      }
      
      if (name.equals("Game Pause") && !isPressed){
          doDetach();
      }
    }
  };
  
    @Override
     public void simpleUpdate(float tpf) {

     }

    public void detachMainGame(){
        stateManager.detach(gameState);
        gameState.setCoinCount(0);
    }
    
    public StateManager getCurrentState(){
        return currentState;
    }
    
    public void setCurrentState(StateManager newState){
        currentState = newState;
    }
    
    public StateManager getNextState(){
        return nextState;
    }
    
    public void setNextState(StateManager newState){
        nextState = newState;
    }
    
    public void doAttach(){
        stateManager.attach(nextState);
        setCurrentState(getNextState());
    }
    
    public void doDetach(){
        stateManager.detach(currentState);
        this.getGuiNode().detachAllChildren();
    }
}
 