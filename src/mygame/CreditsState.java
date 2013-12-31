/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.cinematic.Cinematic;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
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
    private int start = -150;
    boolean nyphoonDone = false;
    private Effect effect2;
    private boolean jmeDone;
    
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
        

        AudioNode ending = new AudioNode(app.getAssetManager(), "Sounds/Revelation.wav");
        ending.play();
        
        float fontsize = Math.abs(Main.tlratio) * 15;
        title = new Element(screen, "title",new Vector2f(0,0),new Vector2f(600,0),
                new Vector4f(5,5,5,5),null);
       //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        title.setPosition(screen.getWidth()/2f - title.getWidth()/2f,start);
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
        
        
       
        Element studios = new Element(screen, "studios",new Vector2f(0,0),new Vector2f(400,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        studios.setPosition(screen.getWidth()/2f - studios.getWidth()/2f,start - 100);
        studios.setText("Produced by Elrel Studios ");
        studios.setFont("Interface/Fonts/KnightsQuest.fnt");
        studios.setFontSize(fontsize * 1.5f);
        studios.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(studios);
        
        programmer = new Element(screen, "programmer",new Vector2f(0,0),new Vector2f(700,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        programmer.setPosition(screen.getWidth()/2f - programmer.getWidth()/2f,start - 200);
        programmer.setText("Programmer: Joraaver Chahal");
        programmer.setFont("Interface/Fonts/KnightsQuest.fnt");
        programmer.setFontSize(fontsize * 1.3f);
        programmer.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(programmer);
        
        sound = new Element(screen, "sound",new Vector2f(0,0),new Vector2f(700,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        sound.setPosition(screen.getWidth()/2f - sound.getWidth()/2f,start - 300);
        sound.setText("Music/SFX Designer: Amarinder Chahal");
        sound.setFont("Interface/Fonts/KnightsQuest.fnt");
        sound.setFontSize(fontsize * 1.3f);
        sound.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(sound);
        
        art = new Element(screen, "art",new Vector2f(0,0),new Vector2f(600,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        art.setPosition(screen.getWidth()/2f - art.getWidth()/2f,start - 400);
        art.setText("Artist: Suchaaver Chahal");
        art.setFont("Interface/Fonts/KnightsQuest.fnt");
        art.setFontSize(fontsize * 1.3f);
        art.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(art);
        
        Element software = new Element(screen, "software",new Vector2f(0,0),new Vector2f(300,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        software.setPosition(screen.getWidth()/2f - software.getWidth()/2f,start - 500);
        software.setText("Software Used");
        software.setFont("Interface/Fonts/KnightsQuest.fnt");
        software.setFontSize(fontsize * 1.5f);
        software.setTextAlign(BitmapFont.Align.Center);
        
        screen.addElement(software);
        
        Element engine = new Element(screen, "engine",new Vector2f(0,0),new Vector2f(200,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        engine.setPosition(screen.getWidth() * (4f/5f) - engine.getWidth() ,start - 600);
        System.out.println(engine.getPosition());
        engine.setText("JMonkeyEngine");
        engine.setFont("Interface/Fonts/KnightsQuest.fnt");
        engine.setFontSize(fontsize * 1.3f);
        engine.setTextAlign(BitmapFont.Align.Right);
        
        screen.addElement(engine);
        
        Element sprite = new Element(screen, "sprite",new Vector2f(0,0),new Vector2f(200,0),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        sprite.setPosition(screen.getWidth()/5f,start - 750);
        sprite.setText("The Sprite Project, created by Nyphoon Games");
        sprite.setFont("Interface/Fonts/KnightsQuest.fnt");
        sprite.setFontSize(fontsize * 1.3f);
        sprite.setTextAlign(BitmapFont.Align.Left);
        
        screen.addElement(sprite);
        
        Element nyphoon = new Element(screen, "nyphoon",new Vector2f(0,0),new Vector2f(100,100),
                new Vector4f(5,5,5,5),"Textures/Nyphoon.png");
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        nyphoon.setPosition(screen.getWidth()*(4/5f) - nyphoon.getWidth(), start-750);
        
        effect = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            5f // The duration of time over which the effect executes (2.2 seconds)
        );
        effect.setElement(nyphoon);
        screen.addElement(nyphoon);
        nyphoon.hide();
        
        Element jme = new Element(screen, "jme",new Vector2f(0,0),new Vector2f(100,100),
                new Vector4f(5,5,5,5),"Textures/jME.png");
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        jme.setPosition(screen.getWidth()/5f, start-600-jme.getHeight()/2f );
        effect2 = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            5f // The duration of time over which the effect executes (2.2 seconds)
        );
        effect2.setElement(jme);
        screen.addElement(jme);
        jme.hide();
        
        
       
        /*screen.addElement(studios);
        studios.hide();
        
        /*time = new Element(screen, "time",new Vector2f(0,0),new Vector2f(300,150),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        time.setPosition(screen.getWidth()/2f - time.getWidth()/2f, screen.getHeight() * .3f);
        time.setText("Total Time: " + Main.current_time);
        time.setFont("Interface/Fonts/KnightsQuest.fnt");
        time.setFontSize(fontsize * 1.5f);
        time.setTextAlign(BitmapFont.Align.Center);
        
        Effect effect2 = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            2f // The duration of time over which the effect executes (2.2 seconds)
        ); 
        effect2.setEffectDirection(Effect.EffectDirection.Bottom);
        effect2.setElement(time);
        screen.addElement(time);
        time.hide();
        
        eq.addEffect(effect, 1f);
        eq.addEffect(effect3, 1f);
        eq.addEffect(effect2, 1f);
        // eq.setEffectManager(screen.getEffectManager());

       
          
        Effect effectpg = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            2f // The duration of time over which the eff6ect executes (2.2 seconds)
        ); 
        effectpg.setEffectDirection(Effect.EffectDirection.Left);
        effectpg.setElement(programmer);
        screen.addElement(programmer);
        programmer.hide();
        

        Element engine = new Element(screen, "engine",new Vector2f(0,0),new Vector2f(200,150),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        engine.setPosition(screen.getWidth() - engine.getWidth(), screen.getHeight() * .5f);
        engine.setText("Developed using the JMonkeyEngine and Nyhoon Games's Sprite Project");
        engine.setFont("Interface/Fonts/KnightsQuest.fnt");
        engine.setFontSize(fontsize * 1.3f);
        engine.setTextAlign(BitmapFont.Align.Right);
        
        Effect effectpgthanks = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            2f // The duration of time over which the eff6ect executes (2.2 seconds)
        ); 
        effectpgthanks.setEffectDirection(Effect.EffectDirection.Right);
        effectpgthanks.setElement(engine);
        screen.addElement(engine);
        engine.hide();
        
        BatchEffect be = new BatchEffect();
        be.addEffect(effectpg);
        
        be.addEffect(effectpgthanks);
        
        //eq.addBatchEffect(be, 1f);
        
        Effect hidepgthanks = new Effect(
            Effect.EffectType.SlideOut, // The type of effect to use
            Effect.EffectEvent.Hide, // The event that the effect is associated with
            2f // The duration of time over which the eff6ect executes (2.2 seconds)
        ); 
        hidepgthanks.setEffectDirection(Effect.EffectDirection.Right);
        hidepgthanks.setElement(engine);
        
        eq.addEffect(hidepgthanks, 3f);
        
        sound = new Element(screen, "sound",new Vector2f(0,0),new Vector2f(700,150),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        sound.setPosition(screen.getWidth() * .03f, screen.getHeight() * .7f);
        sound.setText("Music/SFX Designer: Amarinder Chahal");
        sound.setFont("Interface/Fonts/KnightsQuest.fnt");
        sound.setFontSize(fontsize * 1.5f);
        sound.setTextAlign(BitmapFont.Align.Left);
        
        Effect effectsd = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            2f // The duration of time over which the eff6ect executes (2.2 seconds)
        ); 
        effectsd.setEffectDirection(Effect.EffectDirection.Left);
        effectsd.setElement(sound);
        screen.addElement(sound);
        sound.hide();
        
        eq.addEffect(effectsd, 1f);
        
        art = new Element(screen, "art",new Vector2f(0,0),new Vector2f(600,150),
                new Vector4f(5,5,5,5),null);
        //Label time_info_l = new Label(screen,"label", new Vector2f(0,0),new Vector2f(190,0));
        art.setPosition(screen.getWidth() * .03f, screen.getHeight() * .9f);
        art.setText("Artist: Suchaaver Chahal");
        art.setFont("Interface/Fonts/KnightsQuest.fnt");
        art.setFontSize(fontsize * 1.5f);
        art.setTextAlign(BitmapFont.Align.Left);
        
        Effect effectart = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            2f // The duration of time over which the eff6ect executes (2.2 seconds)
        ); 
        effectart.setEffectDirection(Effect.EffectDirection.Left);
        effectart.setElement(art);
        screen.addElement(art);
        art.hide();
        
        eq.addEffect(effectart, 1);
        
        screen.getEffectManager().applyEffectQueue(eq);
        screen.getEffectManager().applyBatchEffect(be);*/
    }
    
    @Override
    public void update(float tpf) {
        
        System.out.println(guiNode.getLocalTranslation());
        guiNode.move(0,-30f * tpf,0);
        if(guiNode.getLocalTranslation().getY() < -850 && !nyphoonDone){
            nyphoonDone = true;
            screen.getEffectManager().applyEffect(effect);
        }
        if(guiNode.getLocalTranslation().getY() < -800 && !jmeDone){
            jmeDone = true;
            screen.getEffectManager().applyEffect(effect2);
        }
        
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
