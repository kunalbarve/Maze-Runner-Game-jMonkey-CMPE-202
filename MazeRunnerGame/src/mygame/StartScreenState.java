package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.JmeContext;
import de.lessvoid.nifty.Nifty;

public class StartScreenState extends StateManager{
 
  private ViewPort viewPort;
  private AssetManager assetManager;
    protected MouseInput mouseInput;
    protected KeyInput keyInput;
    protected JoyInput joyInput;
    protected TouchInput touchInput;
    protected InputManager inputManager;
    protected AppStateManager stateManager;
     protected JmeContext context;
     public FlyByCamera flycam;
     public Nifty nifty;
     private MyScreenController myScreenController;
     public MenuStateInterface currentState;
     int current =0;
    private  KeyTrigger up_trigger;
    private  KeyTrigger down_trigger;
    private  KeyTrigger key_Return;
    
     public MenuStartState menuStartState;
     public MenuControlState menuControlState;
     public MenuSettingsState menuSettingState;
     public MenuCreditsState menuCreditState;
     public MenuExitState menuExitState;
     public StartScreenState startScreenState;
    public Application app2;
 
public StartScreenState(SimpleApplication app){
   
    this.viewPort     = app.getViewPort();
    this.assetManager = app.getAssetManager(); 
    this.flycam = app.getFlyByCamera();
    this.inputManager = app.getInputManager();
    app2 = app;
    myScreenController = new MyScreenController(app);
    currentState = menuStartState;
    up_trigger = new KeyTrigger(KeyInput.KEY_UP);
    down_trigger = new KeyTrigger(KeyInput.KEY_DOWN);
    key_Return = new KeyTrigger(KeyInput.KEY_RETURN);
  
    inputManager = app.getInputManager();
    inputManager.addMapping("Move up", up_trigger);
    inputManager.addListener(actionListener, new String[]{"Move up"});
    
    inputManager.addMapping("Move down", down_trigger);
    inputManager.addListener(actionListener, new String[]{"Move down"});
    
    inputManager.addMapping("Press Return", key_Return);
    inputManager.addListener(actionListener, new String[]{"Press Return"});
    
    menuStartState = new MenuStartState(this);
  
    currentState= menuStartState;
    
    current =0;
    
    menuControlState = new MenuControlState(this);
    
    menuCreditState = new MenuCreditsState(this);
   
     menuExitState = new MenuExitState(this);
    
       menuSettingState = new MenuSettingsState(this);
   }

  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay( assetManager, inputManager,null, viewPort);
    nifty = niftyDisplay.getNifty();
    viewPort.addProcessor(niftyDisplay);
   flycam.setDragToRotate(true);

    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");

   
    nifty.addScreen("start", new ScreenBuilder("start") {{
       controller(myScreenController);
        layer(new LayerBuilder("background") {{
            childLayoutCenter();
            
            // add image
            image(new ImageBuilder() {{
                filename("Interface/tutorial/menu1.png");
                
            }});

        }});

    }}.build(nifty));
    
    nifty.addScreen("controls", new ScreenBuilder("controls") {{
       controller(myScreenController);
        layer(new LayerBuilder("background") {{
            childLayoutCenter();
            
            // add image
            image(new ImageBuilder() {{
                filename("Interface/tutorial/menu2.png");
                
            }});

        }});

    }}.build(nifty));

    
    nifty.addScreen("settings", new ScreenBuilder("settings") {{
       controller(myScreenController);
        layer(new LayerBuilder("background") {{
            childLayoutCenter();
            
            // add image
            image(new ImageBuilder() {{
                filename("Interface/tutorial/menu3.png");
                
            }});
            
            }});

    }}.build(nifty));
    
    nifty.addScreen("credits", new ScreenBuilder("credits") {{
       controller(myScreenController);
        layer(new LayerBuilder("background") {{
            childLayoutCenter();
            
            // add image
            image(new ImageBuilder() {{
                filename("Interface/tutorial/menu4.png");
                }}); 
        }});

    }}.build(nifty));
    
      nifty.addScreen("exit", new ScreenBuilder("exit") {{
       controller(myScreenController);
        layer(new LayerBuilder("background") {{
            childLayoutCenter();
            
            // add image
            image(new ImageBuilder() {{
                filename("Interface/tutorial/menu5.png");
                }}); 
        }});
        
         }}.build(nifty));
        
       nifty.addScreen("controlScreen", new ScreenBuilder("ControlScreen") {{
       controller(myScreenController);
        layer(new LayerBuilder("background") {{
            childLayoutCenter();
            
            // add image
            image(new ImageBuilder() {{
                filename("Interface/tutorial/controls-screen.png");
                }}); 
        }});
        
              }}.build(nifty));
       
        nifty.addScreen("creditScreen", new ScreenBuilder("CreditScreen") {{
       controller(myScreenController);
        layer(new LayerBuilder("background") {{
            childLayoutCenter();
            
            // add image
            image(new ImageBuilder() {{
                filename("Interface/tutorial/credits-screen.png");
                }}); 
        }});
        
              }}.build(nifty));
 
   nifty.gotoScreen("start"); 
    
  }
 
  @Override
  public void update(float tpf) {
  
 
  }
 
  @Override
  public void stateAttached(AppStateManager stateManager) {

  }
 
  @Override
  public void stateDetached(AppStateManager stateManager) {
   // stateManager.
    
    this.app2.getViewPort().clearScenes();
      this.app2.getViewPort().clearProcessors();
  }
 
   public int getCurrent()
  {
      
      return current;
      
  }
  
   public void setCurrent(int value)
  {
      
      current = value;
      
  }
  
    private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean isPressed, float tpf) {
      if (name.equals("Move up") && !isPressed)
     {
         current =currentState.getUpScreen();
         setCurrentScreen(current);
                 
     }
      if (name.equals("Move down") && !isPressed){
          current=currentState.getDownScreen();
          setCurrentScreen(current);
     }
     if (name.equals("Press Return") && !isPressed ){
        current = currentState.getEnterScreen();
         setCurrentScreen(current);
     }
   
     }
    };
    
     public void setCurrentScreen(int current)
    {
        if(current==0)
        {
           nifty.gotoScreen("start");
           currentState = menuStartState;
           
        }
        else if (current==1)
        {
              nifty.gotoScreen("controls");
              currentState = menuControlState;
        }
        else if (current==2)
        {
              nifty.gotoScreen("settings");
              currentState = menuSettingState;
        }
         else if (current==3)
        {
             nifty.gotoScreen("credits");
               currentState = menuCreditState;
        }
         else if (current==4)
        {
              nifty.gotoScreen("exit");
               currentState = menuExitState;
        }
        else if (current==5)
        {
              nifty.gotoScreen("controlScreen");
        }
        else
        {
            nifty.gotoScreen("creditScreen");
        }
    }

    @Override
    void setCoinCount(int count) {}
}