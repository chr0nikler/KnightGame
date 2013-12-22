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
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;

/**
 *
 * @author JSC
 */
public class MainMenuState extends AbstractAppState {
    
    AppStateManager stateManager;
    SimpleApplication app;
    Screen screen;
    Node guiNode;
    Node rootNode;
    ButtonAdapter start_button;
    ButtonAdapter credits_button;
    Element title;
               
    MainMenuState(Screen screen) {
        this.screen = screen;
    }
    
    @Override
    public void initialize(final AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.stateManager = stateManager;
        this.app = (SimpleApplication)app;
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        
        title = new Element(screen, "time_info",new Vector2f(0,0),new Vector2f(600,200),
                new Vector4f(5,5,5,5),"Textures/SampleTitle.png");
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        title.setPosition(screen.getWidth()/2f - title.getWidth()/2f, 10f);
        
        Effect effect = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            3f // The duration of time over which the effect executes (2.2 seconds)
        ); 
        
        title.addEffect(effect);
        
        
        screen.addElement(title);
        title.hide();
        title.showWithEffect();
        
        
        start_button = new ButtonAdapter(screen,"start", new Vector2f(0, 0)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                stateManager.detach(stateManager.getState(MainMenuState.class));
             
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };
        start_button.setPosition(screen.getWidth()/2f-start_button.getWidth()/2f,screen.getHeight()/2f);
        start_button.setText("Start");
        screen.addElement(start_button);
        
        credits_button = new ButtonAdapter(screen,"credits", new Vector2f(0, 0)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                stateManager.detach(stateManager.getState(MainMenuState.class));
             
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };
        credits_button.setText("Credits");
        credits_button.setPosition(screen.getWidth()/2f-credits_button.getWidth()/2f,screen.getHeight()/2f + start_button.getHeight()+ 5);

        screen.addElement(credits_button);
        
        app.getInputManager().setCursorVisible(true);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime

    }
    
    @Override
    public void cleanup() {
        super.cleanup();

        screen.removeElement(title);
        screen.removeElement(credits_button);
        screen.removeElement(start_button);
        
        Element time_info_l = new Element(screen, "title",new Vector2f(0,0),new Vector2f(190,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        time_info_l.setPosition(screen.getWidth()/2f - time_info_l.getWidth()/2f, 10f);
        time_info_l.setText("Seconds since start:");
        time_info_l.setFontColor(ColorRGBA.White);
        time_info_l.setFontSize(Math.abs(Main.tlratio) * 15);

        
        time_info_l.setFontSize(Math.abs(Main.tlratio) * 15);

        Label time_l = new Label(screen, "time", new Vector2f(0,0),new Vector2f(70,0));
        time_l.setPosition(time_info_l.getPosition().x + time_info_l.getWidth(),10f);
        time_l.setFontColor(ColorRGBA.White);
        time_l.setFontSize(Math.abs(Main.tlratio) * 15);
        
        Effect effect = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            10f // The duration of time over which the effect executes (2.2 seconds)
        ); 
        
        time_info_l.addEffect(effect);
        time_info_l.hide();

        screen.addElement(time_l);
        screen.addElement(time_info_l);

        time_info_l.showWithEffect();
        
        Label pause_l = new Label(screen, "pause_button", new Vector2f(0,0),new Vector2f(200,0));
        pause_l.setFontSize(Math.abs(Main.tlratio * 15));
        pause_l.setTextAlign(BitmapFont.Align.Right);
        pause_l.setPosition(screen.getWidth()-pause_l.getWidth(),10);
        pause_l.setText("Press 'P' to pause");
        
        screen.addElement(pause_l);
        
 
        LevelState level_state  = new LevelState(screen);
        stateManager.attach(level_state);

        //screen.r
        //guiNode.detachAllChildren();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
