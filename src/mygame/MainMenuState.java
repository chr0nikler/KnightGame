/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import java.io.File;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.controls.windows.Window;
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
    ButtonAdapter continue_button;
    ButtonAdapter quit_button;
    ButtonAdapter onwards_button;
    Element title;
    Label credits_titles;
    Label credits_names;
    Label credits_dots;
    ButtonAdapter back_button;
    Panel autosave_info;
    float fontsize;
               
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
        
        fontsize = Math.abs(Main.tlratio) * 15;
        title = new Element(screen, "title",new Vector2f(0,0),new Vector2f(600,150),
                new Vector4f(5,5,5,5),"Textures/SampleTitle.png");
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        title.setPosition(screen.getWidth()/2f - title.getWidth()/2f, 10f);
        
        Effect effect = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
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
                autosaveInfo();
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
        
        String userHome = System.getProperty("user.home");
        app.getAssetManager().registerLocator(userHome, FileLocator.class);
        

        try{
             final Node level_count = (Node)app.getAssetManager().loadModel("/KnightGame/levels.j3o");
             final Node time = (Node)app.getAssetManager().loadModel("/KnightGame/time.j3o");
            
             continue_button = new ButtonAdapter(screen,"continue", new Vector2f(0, 0), new Vector2f(screen.getWidth()/8f,screen.getHeight()/10f)) {
                @Override
                public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                    if(level_count != null){
                        Main.current_time = time.getUserData("time");
                        Main.level_count = level_count.getUserData("Count");
                    }
                    
                    stateManager.detach(stateManager.getState(MainMenuState.class));

                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            };
            continue_button.setPosition(screen.getWidth()/2f-start_button.getWidth()/2f,screen.getHeight()/2f - start_button.getHeight() - 5);
            //continue_button.setButtonIcon(continue_button.getWidth()*2,continue_button.getHeight()*2,"Textures/ContinueButton.png");
            continue_button.setText("Continue");
            continue_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
            continue_button.setFontSize(screen.getWidth()/30f);
            continue_button.setTextPosition(0f,0);
            //start_button.setTextAlign(BitmapFont.Align.Center);
            continue_button.setTextVAlign(BitmapFont.VAlign.Center);

            //continue_button.addEffect(effect);
            
            
            screen.addElement(continue_button);
            //continue_button.hide();
            //continue_button.showWithEffect();
        } catch (Exception e){
            System.out.println("Error");
        }
        
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
        quit_button = new ButtonAdapter(screen,"quit", new Vector2f(0, 0), new Vector2f(screen.getWidth()/8, screen.getHeight()/10)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                app.stop();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }



        };
        quit_button.setText("Quit");
        quit_button.setPosition(screen.getWidth()/2f-credits_button.getWidth()/2f,screen.getHeight()/2f + start_button.getHeight()+ 5+ credits_button.getHeight()+ 5);
        quit_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        quit_button.setFontSize(screen.getWidth()/30f);
        quit_button.setTextPosition(0f,0);
        //start_button.setTextAlign(BitmapFont.Align.Center);
        quit_button.setTextVAlign(BitmapFont.VAlign.Center);
        screen.addElement(quit_button);

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

        removeHomeElements();
        
        
        
        if(continue_button!=null){
            screen.removeElement(continue_button);
        }
        
        Element time_info_l = new Element(screen, "time_info",new Vector2f(0,0),new Vector2f(190,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        time_info_l.setPosition(screen.getWidth()/2f - time_info_l.getWidth()/2f, 10f);
        time_info_l.setText("Seconds since start:");
        time_info_l.setFontColor(ColorRGBA.White);
        time_info_l.setFontSize(Math.abs(Main.tlratio) * 15);
        time_info_l.setFont("Interface/Fonts/KnightsQuest.fnt");
        
        time_info_l.setFontSize(Math.abs(Main.tlratio) * 15);

        Label time_l = new Label(screen, "time", new Vector2f(0,0),new Vector2f(70,0));
        time_l.setPosition(time_info_l.getPosition().x + time_info_l.getWidth(),10f);
        time_l.setFontColor(ColorRGBA.White);
        time_l.setFontSize(Math.abs(Main.tlratio) * 15);
        time_l.setFont("Interface/Fonts/KnightsQuest.fnt");
        


        screen.addElement(time_l);
        screen.addElement(time_info_l);
        
        Element pause_l = new Label(screen, "pause_button", new Vector2f(0,0),new Vector2f(50,50),
                new Vector4f(5,5,5,5),"Textures/pause.png");
        pause_l.setFontSize(Math.abs(Main.tlratio * 15));
        pause_l.setTextAlign(BitmapFont.Align.Center);
        pause_l.setPosition(screen.getWidth()-pause_l.getWidth(),10);
        pause_l.setText("P");
        pause_l.setFont("Interface/Fonts/KnightsQuest.fnt");

        
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
        removeHomeElements();
        
        if(continue_button!=null){
            screen.removeElement(continue_button);
        }
        
        credits_names = new Label(screen,"Names Info", new Vector2f(0,0),new Vector2f(screen.getWidth()/3f,screen.getHeight()));
        credits_names.setText(" Joraaver Chahal \n \n Amarinder Chahal \n \n Suchaaver Chahal ");
        credits_names.setPosition((screen.getWidth()/3f) * 2, 0);
        credits_names.setFont("Interface/Fonts/KnightsQuest.fnt");
        credits_names.setTextAlign(BitmapFont.Align.Left);
        credits_names.setFontSize(Math.abs(Main.tlratio * 22));
        credits_names.setTextVAlign(BitmapFont.VAlign.Center);
        
        screen.addElement(credits_names);
        
        credits_titles = new Label(screen,"Credits Info", new Vector2f(0,0),new Vector2f(screen.getWidth()/3f,screen.getHeight()));
        credits_titles.setText("Programming \n \n Sound/FX \n \n Graphics/Art ");
        credits_titles.setTextAlign(BitmapFont.Align.Right);
        credits_titles.setTextVAlign(BitmapFont.VAlign.Center);
        credits_titles.setFont("Interface/Fonts/KnightsQuest.fnt");
        credits_titles.setFontSize(Math.abs(Main.tlratio * 22));
        
        screen.addElement(credits_titles);
        
        credits_dots = new Label(screen,"Credits Dots", new Vector2f(0,0),new Vector2f(screen.getWidth()/3f,screen.getHeight()));
        credits_dots.setText("..............................................\n\n"
                + "..............................................\n\n"
                + "..............................................");
        credits_dots.setPosition(screen.getWidth()/3f, 0);
        credits_dots.setTextAlign(BitmapFont.Align.Center);
        //credits_dots.setTextVAlign(BitmapFont.VAlign.Center);
        credits_dots.setFont("Interface/Fonts/KnightsQuest.fnt");
        credits_dots.setFontSize(Math.abs(Main.tlratio * 22));
        
        screen.addElement(credits_dots);
        
        back_button = new ButtonAdapter(screen,"back", new Vector2f(0, 0), new Vector2f(screen.getWidth()/12, screen.getHeight()/15)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                homeGUI("credits");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        back_button.setText("back");
        back_button.setPosition(0,screen.getHeight()-back_button.getHeight());
        back_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        back_button.setFontSize(screen.getWidth()/30f);
       // back_button.setTextPosition(0,0);
        //start_button.setTextAlign(BitmapFont.Align.Center);
        back_button.setTextVAlign(BitmapFont.VAlign.Center);
        screen.addElement(back_button);
        
        
    }
    
    private void homeGUI(String s) {
        
        if(s.equals("credits")){
            screen.removeElement(credits_titles);
            screen.removeElement(credits_names);
            screen.removeElement(credits_dots);
            screen.removeElement(back_button);
            screen.addElement(start_button);
            screen.addElement(credits_button);
            screen.addElement(title);
            screen.addElement(quit_button);
            if(continue_button!=null){
                screen.addElement(continue_button);
            }
        } else {
            screen.removeElement(autosave_info);
        }
        
    }
    
    private void autosaveInfo(){
        
        autosave_info = new Panel(screen, "window", new Vector2f(screen.getWidth()*.3f/2,screen.getHeight()*.3f/2), new Vector2f(screen.getWidth()*.7f, screen.getHeight()*.7f));
        autosave_info.center();
        autosave_info.setText("Your progress will be saved after completing each stage. If you have any saved progress, it will be overwritten");
        autosave_info.setFontSize(fontsize * 1.5f);
        autosave_info.setTextAlign(BitmapFont.Align.Center);
        autosave_info.setTextVAlign(BitmapFont.VAlign.Center);
        autosave_info.setFont("Interface/Fonts/KnightsQuest.fnt");
        
        onwards_button = new ButtonAdapter(screen,"onwards", new Vector2f(0, 0), new Vector2f(autosave_info.getWidth()/4, autosave_info.getHeight()/5)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(autosave_info);
                screen.removeElement(onwards_button);
                Main.current_time = 0;
                Main.level_count = 0;
                stateManager.detach(stateManager.getState(MainMenuState.class));
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        onwards_button.setText("Onward");
        onwards_button.setPosition(autosave_info.getWidth() - onwards_button.getWidth()- 5,autosave_info.getHeight()-onwards_button.getHeight()-5);
        onwards_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        onwards_button.setFontSize(screen.getWidth()/30f);
        onwards_button.setTextPosition(0f,0);
        //start_button.setTextAlign(BitmapFont.Align.Center);
        //onwards_button.setTextVAlign(BitmapFont.VAlign.Center);
        autosave_info.addChild(onwards_button);
        
        back_button = new ButtonAdapter(screen,"back", new Vector2f(0, 0), new Vector2f(autosave_info.getWidth()/4, autosave_info.getHeight()/5)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                homeGUI("onwards");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        back_button.setText("Back");
        back_button.setPosition(5,autosave_info.getHeight()-back_button.getHeight()-5);
        back_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        back_button.setFontSize(screen.getWidth()/30f);
        back_button.setTextPosition(0f,0);
        start_button.setTextAlign(BitmapFont.Align.Center);
        //back_button.setTextVAlign(BitmapFont.VAlign.Center);
        autosave_info.addChild(back_button);
        
       
        screen.addElement(autosave_info);      
        
            
    }
    
    private void removeHomeElements(){
        screen.removeElement(title);
        screen.removeElement(start_button);
        screen.removeElement(credits_button);
        screen.removeElement(quit_button);
        
    }
 
            

}
