/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author JSC
 */
public class PauseState extends AbstractAppState {
    
    AppStateManager stateManager;
    SimpleApplication app;
    Screen screen;
    Panel window;
    
    
    
    PauseState(Screen screen){
        this.screen = screen;
    }
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.app = (SimpleApplication)app;
        this.stateManager = stateManager;
        
        float fontSize = Math.abs(Main.tlratio) * 15;

        window = new Panel(screen, "window", new Vector2f(10,10), new Vector2f(screen.getWidth()-20, screen.getHeight()-20));
        window.center();
        window.setFontSize(fontSize * 1.5f);
        window.setTextAlign(BitmapFont.Align.Center);
        screen.addElement(window);
        
        Label title = new Label(screen, "pause_title", new Vector2f(0,0), new Vector2f(300,0));
        title.setText("GAME PAUSED");
        window.addChild(title);
        title.setTextAlign(BitmapFont.Align.Center);
        title.setFontSize(fontSize * 1.5f);
        title.setPosition(window.getWidth()/2-title.getWidth()/2f, window.getHeight() * .96f);
        
        Label goal = new Label(screen, "goal", new Vector2f(0,0), new Vector2f(200,0));
        goal.setText("Save the princess!");
        window.addChild(goal);
        goal.setFontSize(fontSize*1.2f);
        goal.setPosition(window.getWidth() * .05f, window.getHeight() * .8f);
        
        Label up = new Label(screen, "up", new Vector2f(0,0), new Vector2f(400,0));
        up.setText("Hit the 'W' key or the UP arrow to JUMP!");
        up.setFontSize(fontSize);
        window.addChild(up);
        up.setPosition(window.getWidth() * .05f, window.getHeight() * .6f);
        
        Element up_pic = new Element(screen, "up_pic", new Vector2f(0,0), new Vector2f(288,32)
                , new Vector4f(5,5,5,5), "Textures/Sprite.png");
        window.addChild(up_pic);
        up_pic.setPosition(window.getWidth() * .6f,  window.getHeight() * .6f);
        
        Label left = new Label(screen, "left", new Vector2f(0,0), new Vector2f(500,0));
        left.setText("Hold the 'A' key or the LEFT arrow to go LEFT!");
        left.setFontSize(fontSize);
        window.addChild(left);
        left.setPosition(window.getWidth() * .05f, window.getHeight() * .4f);
         
        Element left_pic = new Element(screen, "left_pic", new Vector2f(0,0), new Vector2f(360,720)
                , new Vector4f(5,5,5,5), "Textures/Knight.png");
        window.addChild(left_pic);
        left_pic.setPosition(window.getWidth() * .6f,  window.getHeight() * .4f);
        
        
        Label right = new Label(screen, "right", new Vector2f(0,0), new Vector2f(500,0));
        right.setText("Hold the 'D' key or the RIGHT arrow to go RIGHT!");
        window.addChild(right);
        right.setFontSize(fontSize);
        right.setPosition(window.getWidth() * .05f, window.getHeight() * .2f);
        
        Element right_pic = new Element(screen, "right_pic", new Vector2f(0,0), new Vector2f(144,16)
                , new Vector4f(5,5,5,5), "Textures/Sprite.png");
        window.addChild(right_pic);
        right_pic.setPosition(window.getWidth() * .6f,  window.getHeight() * .2f);
                
        
        
        
        createMappings();
        
        
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        screen.removeElement(window);
        stateManager.getState(LevelState.class).setEnabled(true);
    }
    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String binding, boolean value, float tpf) {
            if (binding.equals("Pause") && value){
                app.getInputManager().removeListener(actionListener);
                stateManager.detach(stateManager.getState(PauseState.class));
            }
            
        }

    };
    
    private void createMappings() {
        app.getInputManager().addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        
        app.getInputManager().addListener(actionListener, new String[]{"Pause"});
    }
}
