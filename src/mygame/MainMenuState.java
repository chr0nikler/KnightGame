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
    Label credits_titles;
    Label credits_names;
    Label credits_dots;
    ButtonAdapter back_button;
               
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
        
        
        start_button = new ButtonAdapter(screen,"start", new Vector2f(0, 0), new Vector2f(screen.getWidth()/8f,screen.getHeight()/10f)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                stateManager.detach(stateManager.getState(MainMenuState.class));
             
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };
        start_button.setPosition(screen.getWidth()/2f-start_button.getWidth()/2f,screen.getHeight()/2f);
        start_button.setText("Start");
        start_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        start_button.setFontSize(screen.getWidth()/30f);
        start_button.setTextPosition(0f,0);
        //start_button.setTextAlign(BitmapFont.Align.Center);
        start_button.setTextVAlign(BitmapFont.VAlign.Center);
        screen.addElement(start_button);
        
        credits_button = new ButtonAdapter(screen,"credits", new Vector2f(0, 0), new Vector2f(screen.getWidth()/8, screen.getHeight()/10)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                creditsGUI();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            

        };
        credits_button.setText("Credits");
        credits_button.setPosition(screen.getWidth()/2f-credits_button.getWidth()/2f,screen.getHeight()/2f + start_button.getHeight()+ 5);
        credits_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        credits_button.setFontSize(screen.getWidth()/30f);
        credits_button.setTextPosition(0f,0);
        //start_button.setTextAlign(BitmapFont.Align.Center);
        credits_button.setTextVAlign(BitmapFont.VAlign.Center);
        screen.addElement(credits_button);
        
        screen.removeElement(credits_button);
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
        
        Element pause_l = new Label(screen, "pause_button", new Vector2f(0,0),new Vector2f(50,50),
                new Vector4f(5,5,5,5),"Textures/pause.png");
        pause_l.setFontSize(Math.abs(Main.tlratio * 15));
        pause_l.setTextAlign(BitmapFont.Align.Center);
        pause_l.setPosition(screen.getWidth()-pause_l.getWidth(),10);
        pause_l.setText("P");

        
        screen.addElement(pause_l);
        
       
 
        LevelState level_state  = new LevelState(screen);
        stateManager.attach(level_state);

        //screen.r
        //guiNode.detachAllChildren();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    private void creditsGUI() {
        screen.removeElement(title);
        screen.removeElement(start_button);
        screen.removeElement(credits_button);
        
        credits_names = new Label(screen,"Names Info", new Vector2f(0,0),new Vector2f(screen.getWidth()/3f,screen.getHeight()));
        credits_names.setText(" Joraaver Chahal \n \n Amarinder Chahal \n \n Suchaaver Chahal ");
        credits_names.setPosition((screen.getWidth()/3f) * 2, 0);
        credits_names.setTextAlign(BitmapFont.Align.Left);
        credits_names.setTextVAlign(BitmapFont.VAlign.Center);
        
        screen.addElement(credits_names);
        
        credits_titles = new Label(screen,"Credits Info", new Vector2f(0,0),new Vector2f(screen.getWidth()/3f,screen.getHeight()));
        credits_titles.setText("Programming \n \n Sound/FX \n \n Graphics/Art ");
        credits_titles.setTextAlign(BitmapFont.Align.Right);
        credits_titles.setTextVAlign(BitmapFont.VAlign.Center);
       
        
        screen.addElement(credits_titles);
        
        credits_dots = new Label(screen,"Credits Dots", new Vector2f(0,0),new Vector2f(screen.getWidth()/3f,screen.getHeight()));
        credits_dots.setText(".............................................. \n \n "
                + ".............................................. \n \n "
                + ".............................................. ");
        credits_dots.setPosition(screen.getWidth()/3f, 0);
        credits_dots.setTextAlign(BitmapFont.Align.Center);
        credits_dots.setTextVAlign(BitmapFont.VAlign.Center);
        
        screen.addElement(credits_dots);
        
        back_button = new ButtonAdapter(screen,"back", new Vector2f(0, 0), new Vector2f(screen.getWidth()/12, screen.getHeight()/15)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                homeGUI();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        back_button.setText("back");
        back_button.setPosition(0,screen.getHeight()-back_button.getHeight());
        back_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        back_button.setFontSize(screen.getWidth()/30f);
        back_button.setTextPosition(0f,0);
        //start_button.setTextAlign(BitmapFont.Align.Center);
        back_button.setTextVAlign(BitmapFont.VAlign.Center);
        screen.addElement(back_button);
        
        
    }
    
    private void homeGUI() {
        
        screen.removeElement(credits_titles);
        screen.removeElement(credits_names);
        screen.removeElement(credits_dots);
        screen.removeElement(back_button);
        screen.addElement(start_button);
        screen.addElement(credits_button);
        screen.addElement(title);
    }

            

}
