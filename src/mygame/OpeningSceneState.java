/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import XMLReader.Sprite;
import XMLReader.SpriteLibrary;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import static mygame.Main.engine;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;
import tonegod.gui.effects.EffectQueue;

/**
 *
 * @author JSC
 */
public class OpeningSceneState extends AbstractAppState {
    private final Screen screen;
    private AppStateManager stateManager;
    private SimpleApplication app;
    private Node rootNode;
    private Node guiNode;
    private Sprite playerSprite;
    private Sprite playerSprite2;
    private Sprite playerSprite3;
    private AssetManager assetManager;
    private Sprite sorcerer;
    private Node playerNode;
    private Effect dialogIn;
    private Camera cam;
    private int count;
    private Element dialogText;
    private Effect dialogOut;
    private Window dialog;
    private Node sorcererNode;
    private int moves = 0;
    private boolean done = false;
    private Vector3f waypoint = new Vector3f(2,1,0);
    private Vector3f dir;
    
    OpeningSceneState(Screen screen) {
        this.screen = screen;
    }
    
    @Override
    public void initialize(final AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.stateManager = stateManager;
        this.app = (SimpleApplication)app;
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.cam = this.app.getCamera();
        
        playerSprite = new Sprite("Textures/knightop1.png", "Player", assetManager, true, true, 50,1, .03f, "NoLoop", "Start");
        playerSprite2 = new Sprite("Textures/knightop2.png", "Player2",assetManager, true, true, 50,1, .03f, "NoLoop", "Start");
        playerSprite3 = new Sprite("Textures/knightop3.png", "Player2",assetManager, true, true, 50,1, .03f, "NoLoop", "Start");
        
        sorcerer = new Sprite("Textures/Sorcererop.png", "so",assetManager, true, true, 9,1, .05f, "Loop", "Start");
        
        playerSprite.setPaused(true);
        SpriteLibrary library = new SpriteLibrary("Library 1", false);
        SpriteLibrary.setL_guiNode(rootNode);
        library.addSprite(playerSprite);
        
        playerSprite2.setPaused(true);
        library.addSprite(playerSprite2);
        
        playerSprite2.setPaused(true);
        library.addSprite(playerSprite3);
        
        sorcerer.setPaused(false);
        library.addSprite(sorcerer);
        engine.addLibrary(library);
        
        playerSprite.setPaused(false);
        
        playerNode = new Node();
        sorcererNode = new Node();
        playerNode.attachChild(playerSprite.getNode());
        playerNode.attachChild(playerSprite2.getNode());
        playerNode.detachChild(playerSprite2.getNode());
        playerNode.attachChild(playerSprite3.getNode());
        playerNode.detachChild(playerSprite3.getNode());
        sorcererNode.attachChild(sorcerer.getNode());
        //sorcererNode.detachChild(sorcerer.getNode());

        dialog = new Window(screen, "dialog", new Vector2f(0,0), new Vector2f(screen.getWidth(), screen.getHeight()*.3f));
        dialog.setWindowTitle("Knight");
        dialog.setFont("Interface/Fonts/KnightsQuest.fnt");

        
        dialogText = new Element (screen, "text",new Vector2f(0,0),new Vector2f(dialog.getWidth()-40,0),
                new Vector4f(5,5,5,5),null);
        
        
        dialog.setPosition(0, screen.getHeight()-dialog.getHeight());
       
        dialogText.setText("...huh? Where am I? What is ...! Me " +
                        "lady! My lady! Your highness, where " +
                        "art thou!? Gah, how did this " +
                        "happen?");
        dialogText.setTextPosition(20, 40);
        dialogText.setFont("Interface/Fonts/KnightsQuest.fnt");
        dialogText.setTextAlign(BitmapFont.Align.Left);
        
        dialog.addChild(dialogText);

        
        dialogIn = new Effect(Effect.EffectType.SlideIn,Effect.EffectEvent.Show, 1f);
        dialogOut = new Effect(Effect.EffectType.SlideOut,Effect.EffectEvent.Hide, 1f);

        dialogIn.setEffectDirection(Effect.EffectDirection.Bottom);
        dialogOut.setEffectDirection(Effect.EffectDirection.Bottom);
        dialogIn.setElement(dialog);
        dialogOut.setElement(dialog);

        
        screen.addElement(dialog);
        dialog.hide();

        
        Element cont = new Element(screen, "cont",new Vector2f(0,0),new Vector2f(screen.getWidth(),0),
                new Vector4f(5,5,5,5),null);
        cont.setText("Hit the spacebar to continue");
        cont.setFont("Interface/Fonts/KnightsQuest.fnt");
        cont.setFontSize(18f);
        dialog.addChild(cont);
        cont.setPosition(dialog.getWidth()-200, 30);

        playerNode.scale(2);
        sorcererNode.scale(2);
        rootNode.attachChild(playerNode);
        rootNode.attachChild(sorcererNode);
        sorcererNode.setLocalTranslation(10, 0, 0);
        playerNode.setLocalTranslation(cam.getFrustumLeft()+3, 0, 0);
        
        createMappings();
    }
    
    @Override
    public void update(float tpf){ 
        engine.update(tpf);
        if(playerSprite.getCurrentFrame() == 47 && playerNode.hasChild(playerSprite.getNode())){
            screen.getEffectManager().applyEffect(dialogIn);
            app.getInputManager().addListener(actionListener, new String[]{"Continue"});
        }
        if(moves == 1 && !done){
            app.getInputManager().removeListener(actionListener);
            sorcererNode.move(dir.x/2 * tpf, dir.y/2 * tpf,0);
            System.out.println(dir.x + " " + dir.y);
            if(Math.abs(sorcererNode.getLocalTranslation().subtract(waypoint).x) < .1f &&
                   Math.abs(sorcererNode.getLocalTranslation().subtract(waypoint).y) < .1f ){
                done =true;
                dialogText.setText("Welcome to my realm, armored one. " +
                    "It seems you seek the princess.");
                dialog.setWindowTitle("Sorcerer");
                dialogText.setTextAlign(BitmapFont.Align.Right);
                app.getInputManager().addListener(actionListener, new String[]{"Continue"});
                
            }
        } else if (moves == 2 && !done){
            dialogText.setText("I do. Where is she? And what sort " +
                        "of black magic have you performed " +
                        "here?");
            dialog.setWindowTitle("Knight");
            dialogText.setTextAlign(BitmapFont.Align.Left);
        } else if(moves == 3 && !done){
            dialogText.setText("She is with me. Worry not, all you" + 
                    " have to do to get her back is fight " +"me.");
            dialog.setWindowTitle("Sorcerer");
            dialogText.setTextAlign(BitmapFont.Align.Right);
        } else if(moves == 4 && !done){
            dialogText.setText("Fight you, when you have stripped " +
                        "me of sword and stallion? How " +
                        "underhanded you are, villain.");
            dialog.setWindowTitle("Knight");
            dialogText.setTextAlign(BitmapFont.Align.Left);
        } else if(moves == 5 && !done){
            dialogText.setText("Hahahaha. Yes, it would seem that " +
                        "way, would it not? Now prepare " +
                        "thy sharpest blade, for the fight " +
                        "is not in the flesh . . .");
            dialog.setWindowTitle("Sorcerer");
            dialogText.setTextAlign(BitmapFont.Align.Right);
        } else if(moves == 6 && !done){
            screen.getEffectManager().applyEffect(dialogOut);
            app.getInputManager().removeListener(actionListener);
            sorcererNode.scale(.99f);
            if(sorcererNode.getLocalScale().x < .001f){
                stateManager.detach(stateManager.getState(OpeningSceneState.class));
            }
        }
         
        
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        rootNode.detachAllChildren();
        screen.removeElement(dialog);
        
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
        
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String binding, boolean value, float tpf) {
            if (binding.equals("Continue") && value){
                if(count == 0){
                    playerNode.detachChild(playerSprite.getNode());
                    playerNode.attachChild(playerSprite2.getNode());
                    playerSprite2.setPaused(false);
                    dir = waypoint.subtract(sorcererNode.getLocalTranslation());
                } else if (count == 1){
                    playerNode.detachChild(playerSprite2.getNode());
                    playerNode.attachChild(playerSprite3.getNode());
                    playerSprite3.setPaused(false);
                    done = false;
                } else if(count >= 2){
                    done = false;
                }
                count++;
                moves++;
            }
            
        }

    };
    
    private void createMappings() {
        app.getInputManager().addMapping("Continue", new KeyTrigger(KeyInput.KEY_SPACE));
        
        
    }
}
