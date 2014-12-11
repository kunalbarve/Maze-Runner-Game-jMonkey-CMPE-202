package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import java.util.ArrayList;

public class Counter implements Subject{
    private BitmapText hudText;
    private Node guiNode;
    private BitmapFont guiFont;
    private AssetManager assetManager;
    private AppSettings settings;
    private int coinCount;
    private AbstractSimpleApplication app;
    private ArrayList<Observer> observers = new ArrayList<Observer>() ;

    Counter(AbstractSimpleApplication app) {
        guiNode = app.getGuiNode();
        assetManager = app.getAssetManager();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        settings = app.getContext().getSettings();
        this.app = app;
    }
    
   public void setScoreScreen() {
    hudText = new BitmapText(guiFont, false);          
//    hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
    hudText.setSize(40);      // font size
    hudText.setColor(ColorRGBA.Cyan);                             // font color
    this.displayScore();
    hudText.setLocalTranslation(settings.getMinWidth() + 30, settings.getHeight() - 40, 0); // position
    guiNode.attachChild(hudText);
  }
   
   public void displayScore() {
    String hms = "Score : "+getCoinCount();
    if(coinCount < 7) {
        hudText.setText(hms); 
    }else if(coinCount == 7) {
        guiNode.detachAllChildren();
        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Textures/game-win.png", true);
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
            observer.updatebasedOnCounter(app);
        }
    }
    
    public int getCoinCount(){
        return coinCount;
    } 
    
    public void setCoinCount(int count){
        coinCount = count;
        notifyObservers();
    }
}