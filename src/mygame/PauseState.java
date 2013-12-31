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
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.buttons.ButtonAdapter;
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
    ButtonAdapter quit_button;
    ButtonAdapter main_button;
    Panel window;
    boolean quit = false;
    boolean home = false;
    
    
    
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
        title.setText("Game Paused");
        window.addChild(title);
        title.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        //title.setFontSize(screen.getWidth()/30f);
        //title.setTextPosition(0f,0);
        title.setTextAlign(BitmapFont.Align.Center);
        title.setFont("Interface/Fonts/KnightsQuest.fnt");
        title.setFontSize(fontSize * 1.5f);
        title.setPosition(window.getWidth()/2-title.getWidth()/2f, window.getHeight() * .95f);
        
        Label goal = new Label(screen, "goal", new Vector2f(0,0), new Vector2f(400,0));
        goal.setText("Save the princess!");
        window.addChild(goal);
        goal.setFont("Interface/Fonts/KnightsQuest.fnt");
        goal.setFontSize(fontSize*1.2f);
        goal.setPosition(window.getWidth() * .05f, window.getHeight() * .8f);
        
        Label up = new Label(screen, "up", new Vector2f(0,0), new Vector2f(500,0));
        up.setText("Hit the 'W' key or the UP arrow to JUMP!");
        up.setFontSize(fontSize);
        window.addChild(up);
        up.setFont("Interface/Fonts/KnightsQuest.fnt");
        up.setPosition(window.getWidth() * .05f, window.getHeight() * .6f);
        
        /*Element up_pic = new Element(screen, "up_pic", new Vector2f(0,0), new Vector2f(288,32)
                , new Vector4f(5,5,5,5), "Textures/Sprite.png");
        window.addChild(up_pic);
        up_pic.setPosition(window.getWidth() * .6f,  window.getHeight() * .6f);*/
        
        Label left = new Label(screen, "left", new Vector2f(0,0), new Vector2f(500,0));
        left.setText("Hold the 'A' key or the LEFT arrow to go LEFT!");
        left.setFontSize(fontSize);
        window.addChild(left);
        left.setFont("Interface/Fonts/KnightsQuest.fnt");
        left.setPosition(window.getWidth() * .05f, window.getHeight() * .4f);
         
        /*Element left_pic = new Element(screen, "left_pic", new Vector2f(0,0), new Vector2f(360,720)
                , new Vector4f(5,5,5,5), "Textures/Knight.png");
        window.addChild(left_pic);
        left_pic.setPosition(window.getWidth() * .6f,  window.getHeight() * .4f);*/
        
        
        Label right = new Label(screen, "right", new Vector2f(0,0), new Vector2f(500,0));
        right.setText("Hold the 'D' key or the RIGHT arrow to go RIGHT!");
        window.addChild(right);
        right.setFontSize(fontSize);
        right.setFont("Interface/Fonts/KnightsQuest.fnt");
        right.setPosition(window.getWidth() * .05f, window.getHeight() * .2f);
        
        /*Element right_pic = new Element(screen, "right_pic", new Vector2f(0,0), new Vector2f(144,16)
                , new Vector4f(5,5,5,5), "Textures/Sprite.png");
        window.addChild(right_pic);
        right_pic.setPosition(window.getWidth() * .6f,  window.getHeight() * .2f);*/
         
        
        
       
        
        Label unpause = new Label(screen,"unpause",new Vector2f(0,0),new Vector2f(200,50));
        unpause.setPosition(20,window.getHeight()-unpause.getHeight()-5);
        unpause.setText("Hit 'P' to play");
        unpause.setFontSize(fontSize);
        unpause.setFont("Interface/Fonts/KnightsQuest.fnt");
        window.addChild(unpause);
        
         quit_button = new ButtonAdapter(screen,"quit", new Vector2f(0, 0), new Vector2f(screen.getWidth()/10, screen.getHeight()/15)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                app.getInputManager().removeListener(actionListener);
                quit = true;
                app.stop();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            

        };
        
       // quit_button.setPosition(20,20);
        window.addChild(quit_button);
        quit_button.setText("Quit");
        quit_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        quit_button.setFontSize(fontSize);
        quit_button.setTextPosition(0f,0);
        quit_button.setTextAlign(BitmapFont.Align.Center);
        
        //quit_button.setTextVAlign(BitmapFont.VAlign.Top);
        
       
        
        main_button = new ButtonAdapter(screen,"main", new Vector2f(0, 0), new Vector2f(screen.getWidth()/10, screen.getHeight()/15)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                home = true;
                app.getInputManager().removeListener(actionListener);
                app.getStateManager().detach(app.getStateManager().getState(PauseState.class));
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            } 
        };
        main_button.setText("Home");
        main_button.setPosition(window.getWidth()-main_button.getWidth()-20,20);
        main_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        main_button.setFontSize(fontSize);
        main_button.setTextPosition(0f,0);
        main_button.setTextAlign(BitmapFont.Align.Center);
        //quit_button.setTextVAlign(BitmapFont.VAlign.Top);
        window.addChild(main_button);
        
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
        if(quit){
            stateManager.detach(stateManager.getState(LevelState.class));
        } else if(home){
            System.out.println("HERE");
            stateManager.detach(stateManager.getState(LevelState.class));
        } else{
            stateManager.getState(LevelState.class).setEnabled(true);
        }
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
