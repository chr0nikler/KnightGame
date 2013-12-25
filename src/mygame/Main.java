package mygame;

import XMLReader.*;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.system.AppSettings;
import java.util.Vector;
import tonegod.gui.core.Screen;
//import tonegod.gui.core.Screen;

public class Main extends SimpleApplication {
    
     
    
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        app.setDisplayFps(true);
        app.setDisplayStatView(false);
        settings.setResolution(640,640);
        app.setSettings(settings);
        //app.setShowSettings(false);
        app.start();
       
    }
    
    BulletAppState bas;
    static SpriteEngine engine = new SpriteEngine();
    public static String[] levels = {"testMap.tmx","testMap2.tmx"};
    public static int level_count = 0;
    public static float current_time  = 0f;
    public static String[] titles = {"Take a step of faith", "Take a leap of faith"};
    
    
    static float tlratio;
    private Screen screen;
    
    @Override
    
    public void simpleInitApp() {
       
        
        cam.setParallelProjection(true);
        tlratio = cam.getFrustumLeft()/cam.getFrustumTop();
        cam.setFrustumTop(5);
        cam.setFrustumBottom(-5f);
        cam.setFrustumLeft(cam.getFrustumTop() * tlratio);
        cam.setFrustumRight(cam.getFrustumTop() * -(tlratio));
        cam.update();
        getFlyByCamera().setEnabled(false);
        getInputManager().setCursorVisible(false);
        bas = new BulletAppState();
        stateManager.attach(bas);
        bas.setDebugEnabled(false);    
        
        screen = new Screen(this);
        guiNode.addControl(screen);
        
        MainMenuState main_menu_state = new MainMenuState(screen);
        //LevelState main_menu_state = new LevelState(screen);

        stateManager.attach(main_menu_state);
        

       
    }

    @Override
    public void simpleUpdate(float tpf) {


    }
    
   
}