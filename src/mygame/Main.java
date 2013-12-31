package mygame;

import XMLReader.*;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import java.io.File;
import java.util.Vector;
import tonegod.gui.core.Screen;
//import tonegod.gui.core.Screen;

public class Main extends SimpleApplication {
    
     
    
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        app.setDisplayFps(false);
        app.setDisplayStatView(false);
        app.setSettings(settings);
        //app.setShowSettings(false);
        app.start();
       
    }
    
    BulletAppState bas;
    public static final int LEVELS = 4;
    static SpriteEngine engine = new SpriteEngine();
    public static String[] levels = {"level1.tmx","level2.tmx","level3.tmx","level4.tmx"};
    public static int level_count = 0;
    public static float current_time  = 0f;
    public static String[] titles = {"Take a step of faith", "Take a leap of faith",
            "Always rise to the challenge","But don't be afraid to fall"};
    public static Node times = new Node("times");
    public static Vector<Float> times_list = new Vector<Float>();
    public static String[] invis_tiles = {"ReiatsuMod.png","junglegrowth.png","cavefall.png","icecrack.png"};
    
    static float tlratio;
    private Screen screen;

    public Main() {
    }
    
    @Override
    
    public void simpleInitApp() {
       
        inputManager.deleteMapping( SimpleApplication.INPUT_MAPPING_EXIT );
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
        
        //MainMenuState main_menu_state = new MainMenuState(screen);
        CreditsState main_menu_state = new CreditsState(screen);
        //LevelState main_menu_state = new LevelState(screen);
        //stateManager.attach(new VideoRecorderAppState()); //starts recording(remove when not needed)
        stateManager.attach(main_menu_state);
        

       
    }

    @Override
    public void simpleUpdate(float tpf) {


    }
    
   
}