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
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.cinematic.Cinematic;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.BatchEffect;
import tonegod.gui.effects.Effect;
import tonegod.gui.effects.EffectQueue;

/**
 *
 * @author JSC
 */
public class CreditsState extends AbstractAppState {
    private AppStateManager stateManager;
    private SimpleApplication app;
    private Node rootNode;
    private Node guiNode;
    private Screen screen;
    private Element title;
    private Element time;
    private Element programmer;
    private Element sound;
    private Element art;
    Effect effect;
    private Camera cam;
    EffectQueue eq;
    private boolean quit=false;
    private int start = -150;
    boolean nyphoonDone = false;
    private Effect effect2;
    private boolean jmeDone;
    private Effect effectadobe;
    private boolean adobeDone;
    private Effect effect4;
    private boolean garageDone;
    private Element thanks;
    private Element forum;
    private Element beta;
    private Element nich;
    private Element lawrence;
    private Element allen;
    private Element rishi;
    private Element ashish;
    float fontsize ;
    private Element playing;
    private Element url;
    private Element visitusat;
    private Element garagePhoto;
    private Element garageband;
    private Element adobePhoto;
    private Element adobe;
    private Element jme;
    private Element nyphoon;
    private Element sprite;
    private Element engine;
    private Element software;
    private Element studios;
    private ButtonAdapter quit_button;
    private ButtonAdapter retry_button;
    private AudioNode ending;
    private float best_time;
    private String timestring;
    private Element dtime;
    private ButtonAdapter skip_button;
    private boolean skip = false;
    
    
    
    
    
    
    CreditsState(Screen screen) {
        this.screen = screen;
    }
    
    @Override
    public void initialize(final AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.stateManager = stateManager;
        this.app = (SimpleApplication)app;
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        this.cam = this.app.getCamera();
        
        this.app.getInputManager().setCursorVisible(true);
        ending = new AudioNode(app.getAssetManager(), "Sounds/Revelation.wav");
        ending.play();
        
        String userHome = System.getProperty("user.home");
        app.getAssetManager().registerLocator(userHome, FileLocator.class);
        
        try{
             final Node bestnode = (Node)app.getAssetManager().loadModel("/KnightGame/best.j3o");
             if(bestnode.getUserData("time") != null){
                if((Float)bestnode.getUserData("time") > Main.current_time){
                    timestring = "New Best Time! But can you do it again?";
                    BinaryExporter exporter = BinaryExporter.getInstance();
                    File file = new File(userHome+"/KnightGame/best.j3o");
                    bestnode.setUserData("time", Main.current_time);
                    exporter.save(bestnode, file);
                    Main.best_time = Main.current_time;
                } else {
                    timestring = "Sorry, you didn't beat your best time";
                }
            } else {
                 timestring = "";
                 BinaryExporter exporter = BinaryExporter.getInstance();
                 File file = new File(userHome+"/KnightGame/best.j3o");
                 bestnode.setUserData("time", Main.current_time);
                 exporter.save(bestnode, file);
             }
        } catch (Exception e){
            timestring = "Can you beat it?";
            BinaryExporter exporter = BinaryExporter.getInstance();
            Node bestnode = new Node("best time");
            File file = new File(userHome+"/KnightGame/best.j3o");
            bestnode.setUserData("time", Main.current_time);
            try {
                exporter.save(bestnode, file);
            } catch (IOException ex) {
                Logger.getLogger(CreditsState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        skip_button = new ButtonAdapter(screen,"skip", new Vector2f(0, 0), new Vector2f(screen.getWidth()/10, screen.getHeight()/15)) {
        @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                skip = true;
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        

        
        fontsize = Math.abs(Main.tlratio) * 15;
        
        skip_button.setPosition(20,screen.getHeight() - skip_button.getHeight() - 20);
        skip_button.setText("skip");
        skip_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        skip_button.setFontSize(fontsize);
        skip_button.setTextPosition(0f,0);
        skip_button.setTextAlign(BitmapFont.Align.Center);
        skip_button.setTextVAlign(BitmapFont.VAlign.Center);
        screen.addElement(skip_button);
        
        title = new Element(screen, "title",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
       //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        title.setPosition(0,start);
        title.setText("The Sharpest Blade");
        title.setFont("Interface/Fonts/HumboldtFraktur.fnt");
        title.setFontSize(fontsize * 3);
        title.setTextAlign(BitmapFont.Align.Center);
        
        /*effect = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            2f // The duration of time over which the effect executes (2.2 seconds)
        ); 
        
        effect.setEffectDirection(Effect.EffectDirection.Top);
        effect.setElement(title);*/
        screen.addElement(title);
        //title.hide();
        //creen.getEffectManager().applyEffect(effect);  
        //eq = new EffectQueue();
        
        
       
        studios = new Element(screen, "studios",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        studios.setPosition(0,start - 100);
        studios.setText("Produced by Elrel Studios ");
        studios.setFont("Interface/Fonts/KnightsQuest.fnt");
        studios.setFontSize(fontsize * 2f);
        studios.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(studios);
        
        programmer = new Element(screen, "programmer",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        programmer.setPosition(0,start - 200);
        programmer.setText("Programmer: Joraaver Chahal");
        programmer.setFont("Interface/Fonts/KnightsQuest.fnt");
        programmer.setFontSize(fontsize * 1.3f);
        programmer.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(programmer);
        
        sound = new Element(screen, "sound",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        sound.setPosition(0,start - 300);
        sound.setText("Music/SFX Designer: Amarinder Chahal");
        sound.setFont("Interface/Fonts/KnightsQuest.fnt");
        sound.setFontSize(fontsize * 1.3f);
        sound.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(sound);
        
        art = new Element(screen, "art",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        art.setPosition(0,start - 400);
        art.setText("Artist: Suchaaver Chahal");
        art.setFont("Interface/Fonts/KnightsQuest.fnt");
        art.setFontSize(fontsize * 1.3f);
        art.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(art);
        
        software = new Element(screen, "software",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        software.setPosition(0,start - 500);
        software.setText("Software Used");
        software.setFont("Interface/Fonts/KnightsQuest.fnt");
        software.setFontSize(fontsize * 2f);
        software.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(software);
        
        engine = new Element(screen, "engine",new Vector2f(0,0),new Vector2f(screen.getWidth()/4f,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        engine.setPosition(screen.getWidth() * (4f/5f) - engine.getWidth() ,start - 600);
        System.out.println(engine.getPosition());
        engine.setText("JMonkeyEngine");
        engine.setFont("Interface/Fonts/KnightsQuest.fnt");
        engine.setFontSize(fontsize * 1.3f);
        engine.setTextAlign(BitmapFont.Align.Right);
        
        screen.addElement(engine);
        
        sprite = new Element(screen, "sprite",new Vector2f(0,0),new Vector2f(screen.getWidth()/4f,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        sprite.setPosition(screen.getWidth()/5f,start - 800);
        sprite.setText("The Sprite Project, created by Nyphoon Games");
        sprite.setFont("Interface/Fonts/KnightsQuest.fnt");
        sprite.setFontSize(fontsize * 1.3f);
        sprite.setTextAlign(BitmapFont.Align.Left);
        
        screen.addElement(sprite);
        
        nyphoon = new Element(screen, "nyphoon",new Vector2f(0,0),new Vector2f(100,100),
                new Vector4f(5,5,5,5),"Textures/Nyphoon.png");
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        nyphoon.setPosition(screen.getWidth()*(4/5f)-nyphoon.getWidth(), start-800);
        nyphoon.setTextAlign(BitmapFont.Align.Right);
        
        effect = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            3f // The duration of time over which the effect executes (2.2 seconds)
        );
        effect.setElement(nyphoon);
        screen.addElement(nyphoon);
        nyphoon.hide();
        
        jme = new Element(screen, "jme",new Vector2f(0,0),new Vector2f(100,100),
                new Vector4f(5,5,5,5),"Textures/jME.png");
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        jme.setPosition(screen.getWidth()/5f, start-600-jme.getHeight()/2f );
        effect2 = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            3f // The duration of time over which the effect executes (2.2 seconds)
        );
        effect2.setElement(jme);
        screen.addElement(jme);
        jme.hide();
        
        adobe = new Element(screen, "adobe",new Vector2f(0,0),new Vector2f(screen.getWidth()/4f,100),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        adobe.setPosition(screen.getWidth()*(4/5f)-adobe.getWidth(), start-1000);
        adobe.setText("Adobe Illustrator, Photoshop, and Flash");
        adobe.setTextAlign(BitmapFont.Align.Right);
        adobe.setFont("Interface/Fonts/KnightsQuest.fnt");
        adobe.setFontSize(fontsize * 1.3f);
        screen.addElement(adobe);
       
        adobePhoto = new Element(screen, "adobePhoto",new Vector2f(0,0),new Vector2f(200,100),
                new Vector4f(5,5,5,5),"Textures/adobe.png");
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        adobePhoto.setPosition(screen.getWidth()/5f, start-1000);
        effectadobe = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            3f // The duration of time over which the effect executes (2.2 seconds)
        );
        effectadobe.setElement(adobePhoto);
        screen.addElement(adobePhoto);
        adobePhoto.hide();
        
        garageband = new Element(screen, "garageband",new Vector2f(0,0),new Vector2f(screen.getWidth()/4f,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        garageband.setPosition(screen.getWidth()/5f,start - 1200);
        garageband.setText("GarageBand");
        garageband.setFont("Interface/Fonts/KnightsQuest.fnt");
        garageband.setFontSize(fontsize * 1.3f);
        garageband.setTextAlign(BitmapFont.Align.Left);
        
        screen.addElement(garageband);
       
        garagePhoto = new Element(screen, "garagePhoto",new Vector2f(0,0),new Vector2f(100,100),
                new Vector4f(5,5,5,5),"Textures/garageband.png");
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        garagePhoto.setPosition(screen.getWidth()*(4/5f)-nyphoon.getWidth(), start-1200);
        garagePhoto.setTextAlign(BitmapFont.Align.Right);
        
        effect4 = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            3f // The duration of time over which the effect executes (2.2 seconds)
        );
        effect4.setElement(garagePhoto);
        screen.addElement(garagePhoto);
        garagePhoto.hide();
        
        thanks = new Element(screen, "thanks",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        thanks.setPosition(0,start - 1400);
        thanks.setText("A Special Thanks To");
        thanks.setFont("Interface/Fonts/KnightsQuest.fnt");
        thanks.setFontSize(fontsize * 2f);
        thanks.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(thanks);
        
        
        forum = new Element(screen, "forum",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        forum.setPosition(0,start - 1600);
        forum.setText("Normen Hansen, pspeed, t0neg0d, and other users on the jME forums");
        forum.setFont("Interface/Fonts/KnightsQuest.fnt");
        forum.setFontSize(fontsize * 1.3f);
        forum.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(forum);
        
        nich = new Element(screen, "nicholas",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        nich.setPosition(0,start - 1800);
        nich.setText("Nicholas Mamo, with Nyphoon Games, for his help with The Sprite Project");
        nich.setFont("Interface/Fonts/KnightsQuest.fnt");
        nich.setFontSize(fontsize * 1.3f);
        nich.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(nich);
        
        beta = new Element(screen, "beta",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        beta.setPosition(0,start - 1900);
        beta.setText("Beta Testers");
        beta.setFont("Interface/Fonts/KnightsQuest.fnt");
        beta.setFontSize(fontsize * 1.5f);
        beta.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(beta);
        
        lawrence = new Element(screen, "lawrence",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        lawrence.setPosition(0,start - 2000);
        lawrence.setText("Lawrence Tran");
        lawrence.setFont("Interface/Fonts/KnightsQuest.fnt");
        lawrence.setFontSize(fontsize * 1f);
        lawrence.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(lawrence);
        
        allen = new Element(screen, "allen",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        allen.setPosition(0,start - 2050);
        allen.setText("Allen Yao");
        allen.setFont("Interface/Fonts/KnightsQuest.fnt");
        allen.setFontSize(fontsize * 1f);
        allen.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(allen);
        
        rishi = new Element(screen, "rishi",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        rishi.setPosition(0,start - 2100);
        rishi.setText("Rishabh Jha");
        rishi.setFont("Interface/Fonts/KnightsQuest.fnt");
        rishi.setFontSize(fontsize * 1f);
        rishi.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(rishi);
        
        ashish = new Element(screen, "ashish",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        ashish.setPosition(0,start - 2150);
        ashish.setText("Ashish Agrawal");
        ashish.setFont("Interface/Fonts/KnightsQuest.fnt");
        ashish.setFontSize(fontsize * 1f);
        ashish.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(ashish);
        
        
        time = new Element(screen, "time",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        time.setPosition(0,start - 2700);
        time.setText("Your time: " + Main.current_time);
        time.setFont("Interface/Fonts/KnightsQuest.fnt");
        time.setFontSize(fontsize * 2f);
        time.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(time);
        
        dtime = new Element(screen, "dtime",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        dtime.setPosition(0,start - 2650);
        dtime.setText(timestring);
        dtime.setFont("Interface/Fonts/KnightsQuest.fnt");
        dtime.setFontSize(fontsize * 1.5f);
        dtime.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(dtime);
        
        playing = new Element(screen, "playing",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        playing.setPosition(0,start - 2800);
        playing.setText("Thanks for playing!");
        playing.setFont("Interface/Fonts/KnightsQuest.fnt");
        playing.setFontSize(fontsize * 1.5f);
        playing.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(playing);
    }
    
    @Override
    public void update(float tpf) {
        

        if(guiNode.getLocalTranslation().getY() > -3100){
            if(skip){
                skip_button.move(0f,800f*tpf,0);
                guiNode.move(0,-800f * tpf,0);
            } else {
                skip_button.move(0f,90f*tpf,0);
                guiNode.move(0,-90f * tpf,0);
            }
            
            if(guiNode.getLocalTranslation().getY() < -1000 && !nyphoonDone && !skip){
                nyphoonDone = true;
                screen.getEffectManager().applyEffect(effect);
            }
            if(guiNode.getLocalTranslation().getY() < -800 && !jmeDone && !skip){
                jmeDone = true;
                screen.getEffectManager().applyEffect(effect2);
            }
            if(guiNode.getLocalTranslation().getY() < -1200 && !adobeDone && !skip){
                adobeDone = true;
                screen.getEffectManager().applyEffect(effectadobe);
            }
            if(guiNode.getLocalTranslation().getY() < -1400 && !garageDone && !skip){
                garageDone = true;
                screen.getEffectManager().applyEffect(effect4);
            }
        } else if(screen.getElementById("visitusat")==null){
            screen.removeElement(skip_button);
            visitusat= new Element(screen, "visitusat",new Vector2f(0,0),new Vector2f(250,0),
                new Vector4f(5,5,5,5),null);
            //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
            visitusat.setPosition(screen.getWidth() - visitusat.getWidth() - 20, guiNode.getLocalTranslation().getY()
                    +screen.getHeight()- 100);
            visitusat.setText("Follow us!"); 
            visitusat.setFont("Interface/Fonts/KnightsQuest.fnt");
            visitusat.setFontSize(fontsize * 2f);
            visitusat.setTextAlign(BitmapFont.Align.Right);

            screen.addElement(visitusat);
            
            url= new Element(screen, "url",new Vector2f(0,0),new Vector2f(300,0),
                new Vector4f(5,5,5,5),null);
            //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
            url.setPosition(screen.getWidth() - url.getWidth() - 20, guiNode.getLocalTranslation().getY()
                    +screen.getHeight()- 50);
            url.setText("www.indiebn.com"); 
            url.setFont("Interface/Fonts/KnightsQuest.fnt");
            url.setFontSize(fontsize * 1.5f);
            url.setTextAlign(BitmapFont.Align.Right);

            screen.addElement(url);
            
            quit_button = new ButtonAdapter(screen,"quit", new Vector2f(0, 0), new Vector2f(screen.getWidth()/10, screen.getHeight()/15)) {
            @Override
                public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                    quit = true;
                    stateManager.detach(stateManager.getState(CreditsState.class));
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };

            quit_button.setPosition(20,guiNode.getLocalTranslation().getY() + 20);
            quit_button.setText("Quit");
            quit_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
            quit_button.setFontSize(fontsize);
            quit_button.setTextPosition(0f,0);
            quit_button.setTextAlign(BitmapFont.Align.Center);
            quit_button.setTextVAlign(BitmapFont.VAlign.Center);
            screen.addElement(quit_button);
            
            retry_button = new ButtonAdapter(screen,"retry", new Vector2f(0, 0), new Vector2f(screen.getWidth()/10, screen.getHeight()/15)) {
            @Override
                public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                    quit = false;
                    stateManager.detach(stateManager.getState(CreditsState.class));
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };

            retry_button.setPosition(screen.getWidth() - retry_button.getWidth() - 20
                    ,guiNode.getLocalTranslation().getY() +20);
            retry_button.setText("Retry?");
            retry_button.setFont("Interface/Fonts/HumboldtFraktur.fnt");
            retry_button.setFontSize(fontsize);
            retry_button.setTextPosition(0f,0);
            retry_button.setTextAlign(BitmapFont.Align.Center);
            retry_button.setTextVAlign(BitmapFont.VAlign.Center);
            screen.addElement(retry_button);
        }
        
        
        
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        screen.removeElement(studios);
        screen.removeElement(software);
        screen.removeElement(engine);
        screen.removeElement(sprite);
        screen.removeElement(nyphoon);
        screen.removeElement(jme);
        screen.removeElement(adobe);
        screen.removeElement(adobePhoto);
        screen.removeElement(garagePhoto);
        screen.removeElement(garageband);
        screen.removeElement(visitusat);
        screen.removeElement(url);
        screen.removeElement(playing);
        screen.removeElement(ashish);
        screen.removeElement(allen);
        screen.removeElement(rishi);
        screen.removeElement(lawrence);
        screen.removeElement(nich);
        screen.removeElement(beta);
        screen.removeElement(forum);
        screen.removeElement(art);
        screen.removeElement(sound);
        screen.removeElement(programmer);
        screen.removeElement(time);
        screen.removeElement(title);
        screen.removeElement(quit_button);
        screen.removeElement(retry_button);
        screen.removeElement(thanks);
        screen.removeElement(dtime);
        
      
        
        ending.stop();
        if(quit){
            app.stop();
        } else {
            guiNode.setLocalTranslation(0, 0, 0);
            BulletAppState bas = new BulletAppState();   
            stateManager.attach(bas);
            bas.setDebugEnabled(false);
            MainMenuState main_state = new MainMenuState(screen);
            stateManager.attach(main_state);
           
        }
        
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
