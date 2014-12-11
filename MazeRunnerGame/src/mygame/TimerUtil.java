package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TimerUtil implements Subject{
    private BitmapText hudText;
    private Node guiNode;
    private BitmapFont guiFont;
    private AssetManager assetManager;
    private AppSettings settings;
    
    //private Timer timer;
    private int seconds;
    private AbstractSimpleApplication sApp;
    private ArrayList<Observer> observers = new ArrayList<Observer>() ;
    
    public TimerUtil(AbstractSimpleApplication app, int sec) {
        guiNode = app.getGuiNode();
        assetManager = app.getAssetManager();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        this.seconds = sec;
        this.sApp = app;
        settings = app.getContext().getSettings();
    }
   
  public void setTimerOnScreen() {
    hudText = new BitmapText(guiFont, false);          
//    hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
    hudText.setSize(40);      // font size
    hudText.setColor(ColorRGBA.Cyan);                             // font color
    this.updateTime();
    hudText.setLocalTranslation(settings.getWidth() - 170, settings.getHeight() - 40, 0); // position
    guiNode.attachChild(hudText);
  } 
  
  public void updateTime() {
    if(seconds != 0){
        if(seconds > 0)
            this.seconds = this.seconds - 1;
        notifyObservers();
    }
    displayTime();
  }
  
  public void displayTime() {
    long millis = this.seconds * 1000;
    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    if(seconds > 0) {
        hudText.setText(hms); 
    } else {
        guiNode.detachAllChildren();
        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Textures/game_over.png", true);
        pic.setWidth(settings.getWidth());
        pic.setHeight(settings.getHeight());
        pic.setPosition(0, 0);
        guiNode.attachChild(pic);
    }
  }

    public void add(Observer obj) {
        observers.add(obj);
    }

    public void remove(Observer obj) {
        observers.remove(obj);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.updateBasedOnTimer(sApp);
        }
    }
    
    public int getTimerSeconds(){
        return seconds;
    }
}
