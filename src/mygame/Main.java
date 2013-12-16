package mygame;

import XMLReader.*;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.system.lwjgl.LwjglTimer;
import com.jme3.texture.Texture2D;
import java.awt.image.BufferedImage;
import java.util.Vector;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;
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
    
    static SpriteEngine engine = new SpriteEngine();
    static LevelState level_state = new LevelState();
    static MainMenuState main_menu_state = new MainMenuState();
    @Override
    
    public void simpleInitApp() {
       
        
        
        
        stateManager.attach(main_menu_state);
        

       
    }

    @Override
    public void simpleUpdate(float tpf) {


    }
    
   
}