/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import XMLReader.CollisionGroup;
import XMLReader.ObjectDetails;
import XMLReader.ReadXMLFile;
import XMLReader.Sprite;
import XMLReader.SpriteLibrary;
import XMLReader.TiledObjectGroup;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;
import com.jme3.system.lwjgl.LwjglTimer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mygame.Main.engine;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;

/**
 *
 * @author JSC
 */
public class LevelState extends AbstractAppState {
    SpriteLibrary library;
    Sprite playerIdleSprite;
    Sprite playerJumpSprite;
    Label time_l;
    BulletAppState bas;
    Sprite playerSprite;
    Sprite playerFallSprite;
    int gravity = -80;
    Vector3f playerVelocity = new Vector3f(0,gravity,0);
    Node playerNode;
    boolean jumping = false;
    Element level_words;
    boolean on_ground = false;
    boolean idle = false;
    Node l;
    BufferedImage level;
    BufferedImage scaled_level;
    CharacterControl player_character;
    private boolean left = false, right = false, up = false, down = false;
    private Vector3f walkDirection = new Vector3f(0,0,0); // stop
    private float airTime = 0;
    LwjglTimer game_timer;
    float tlratio;
    private Node death_node;
    AudioNode landed;
    private boolean flipped = false;
    Vector3f start;
    Vector3f end;
    Node ending_node;
    
    AudioNode footstep;
    
    private ReadXMLFile rxf;
    
    SimpleApplication app;
    AppStateManager stateManager;
    AssetManager assetManager;
    Node rootNode;
    Node guiNode;
    Camera cam;
    Screen screen;
    
    Element time_info_l;
    Element word;
    String[] words;
    Element[] title_elements;
    private boolean complete = false;
    private boolean facingLeft = false;
    private boolean jumped;
    private boolean jumpingLeft;
    private boolean fall;
    private float this_pos;
    private float last_pos = -10000;
    private boolean fallingLeft;
    private boolean resetJump = false;
    private boolean started;
    private float frameCount = 0;
    private Element pause_l;

    LevelState(Screen screen){
        this.screen = screen;
        this.time_l = (Label)screen.getElementById("time");
        this.time_info_l = screen.getElementById("time_info");
        this.pause_l = screen.getElementById("pause_button");
        
       
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app = (SimpleApplication)app;
        this.stateManager = stateManager;
        this.assetManager = this.app.getAssetManager();
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        this.bas = this.stateManager.getState(BulletAppState.class);
        
        
        
        library = new SpriteLibrary("Library 1", false);
        SpriteLibrary.setL_guiNode(rootNode);
        engine.addLibrary(library);

        Quad bg = new Quad(1,1);
        Geometry geo = new Geometry("BG",bg);
        
        Texture t = null;
        if(Main.level_count < 4){
            t = assetManager.loadTexture("Textures/background-transport.png");
        } else if (Main.level_count < 8){
            t = assetManager.loadTexture("Textures/background-cave.png");
        } else if (Main.level_count < 12){
            t = assetManager.loadTexture("Textures/background-glacier.png");
        } else if (Main.level_count < 16){
            t = assetManager.loadTexture("Textures/background-jungle.png");
        } else if (Main.level_count < 20){
            t = assetManager.loadTexture("Textures/background-lair.png");
        }
        Material s_material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        s_material.setTexture("ColorMap", t);
        //geo.setQueueBucket(Bucket.Transparent); 
        //geo.setQueueBucket(Bucket.Sky);
        geo.setCullHint(CullHint.Never);
        geo.setMaterial(s_material);
        s_material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        
        rootNode.attachChild(geo);
        
        geo.move(cam.getFrustumLeft(),cam.getFrustumBottom(),-.5f);
        
        geo.setLocalScale(cam.getFrustumRight()- cam.getFrustumLeft(),cam.getFrustumTop()- cam.getFrustumBottom(),1);

        generalSetup();
        
        String level_word = "Stage " + (Main.level_count+1) + " of " + Main.LEVELS;
        
        if(Main.level_count < 4){
            level_words = new Element(screen, "level_words",new Vector2f(0,0),new Vector2f(screen.getWidth(),screen.getHeight()),
                new Vector4f(5,5,5,5),"Textures/TransparentYellow.png");
        } else if (Main.level_count < 8){
            level_words = new Element(screen, "level_words",new Vector2f(0,0),new Vector2f(screen.getWidth(),screen.getHeight()),
                new Vector4f(5,5,5,5),"Textures/TransparentBrown.png");
        } else if (Main.level_count < 12){
            level_words = new Element(screen, "level_words",new Vector2f(0,0),new Vector2f(screen.getWidth(),screen.getHeight()),
                new Vector4f(5,5,5,5),"Textures/TransparentBlue.png");
        } else if (Main.level_count < 16){
            level_words = new Element(screen, "level_words",new Vector2f(0,0),new Vector2f(screen.getWidth(),screen.getHeight()),
                new Vector4f(5,5,5,5),"Textures/TransparentGreen.png");
        } else if (Main.level_count < 20){
            level_words = new Element(screen, "level_words",new Vector2f(0,0),new Vector2f(screen.getWidth(),screen.getHeight()),
                new Vector4f(5,5,5,5),"Textures/TransparentGrey.png");
        } 
        
        level_words.setText(level_word);
        
        level_words.setTextAlign(BitmapFont.Align.Center);
        level_words.setTextVAlign(BitmapFont.VAlign.Center);
        level_words.setFontSize(Math.abs(tlratio)*30);
        level_words.setFont("Interface/Fonts/KnightsQuest.fnt");
        level_words.setFontColor(ColorRGBA.Black);
        screen.addElement(level_words);
        
        createTitle();
        
        //createMappings();
        InputStream xml = this.getClass().getClassLoader().getResourceAsStream("Textures/" + Main.levels[Main.level_count]);
        
        rxf = new ReadXMLFile(xml,assetManager,bas);
        rxf.parse();
        l = rxf.createLevel(cam);
        
        start = rxf.getStart();
        end = rxf.getEnd();
                           
        rootNode.attachChild(l);
        
        setupCollidables();
        
        setupPlayer();
        
        createMappings();

        game_timer = new LwjglTimer();

        footstep = new AudioNode(assetManager, "Sounds/TSBFootstep.wav");
        landed = new AudioNode(assetManager, "Sounds/JumpLandSFX.wav");
        landed.setVolume(1.0f);
        
       
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
        if(Main.bg.getStatus() == AudioSource.Status.Stopped){
            Main.bg.play();
        }
        Vector3f camDir = cam.getDirection().clone().multLocal(0.25f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.25f);
        camDir.y = 0;
        camLeft.y = 0;
        
        
        if(game_timer.getTimeInSeconds() < 1.5f && !started){
            return;
        } else {
            if(!started){
                screen.removeElement(level_words);
                game_timer.reset();
                started = true;
                screen.addElement(title_elements[0]);
                title_elements[0].showWithEffect();
            }
        }
        
        if(stateManager.getState(LevelState.class).isEnabled() ){
            try{
               time_l.setText((String)(Float.toString(game_timer.getTimeInSeconds() + Main.current_time)).subSequence(0, 5));
            } catch(Exception e) {
               time_l.setText((String)(Float.toString(game_timer.getTimeInSeconds() +  Main.current_time)).subSequence(0, 3));
            }
            
            Main.engine.update(tpf);

            walkDirection.set(0, 0, 0);
            
            if(player_character.onGround() && jumped && fall){
                landed.setPitch(2.0f);
                landed.playInstance();
            }

            if(!player_character.onGround() && ((!jumped && !fall) || (left && !jumpingLeft) || (right && jumpingLeft))  ){
                playerNode.detachAllChildren();
                playerNode.attachChild(playerJumpSprite.getNode());
                if((flipped && !jumpingLeft && !jumped) || (left && !jumpingLeft)){
                    playerJumpSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                    playerJumpSprite.setPaused(true);
                    playerJumpSprite.scaleTexture(new Vector2f(1f,-1f));                    
                    jumpingLeft = true;
                    playerJumpSprite.rotate(0,0,-180);
                    playerJumpSprite.move(.2f,.65f);
                    playerJumpSprite.getNode().move(0,0,10);
                } else if((!flipped && jumpingLeft && !jumped) || (right && jumpingLeft)){
                    playerJumpSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                    playerJumpSprite.setPaused(true);
                    playerJumpSprite.scaleTexture(new Vector2f(1f,-1f));
                    jumpingLeft = false;
                    playerJumpSprite.rotate(0,0,-180);
                    playerJumpSprite.move(-.2f,-.35f);
                    playerJumpSprite.getNode().move(0,0,10);
                }
                if(!resetJump){
                    playerJumpSprite.setPaused(false);
                }
                jumped = true;
            } else {
                this_pos = playerNode.getLocalTranslation().getY();
                if(!fall || (!fallingLeft && (left || jumpingLeft)) || (fallingLeft && (right || !jumpingLeft))){    
                    if (last_pos > this_pos){
                        playerNode.detachAllChildren();
                        playerNode.attachChild(playerFallSprite.getNode());
                        if(!fallingLeft && (flipped || left || jumpingLeft) ){
                            playerFallSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                            playerFallSprite.setPaused(true);
                            playerFallSprite.scaleTexture(new Vector2f(1f,-1f));
                            fallingLeft = true;
                            playerFallSprite.rotate(0,0,-180);
                            playerFallSprite.move(.2f,.65f);
                            playerFallSprite.getNode().move(0,0,10);
                        } else if (fallingLeft && (!flipped || right || !jumpingLeft)){
                            playerFallSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                            playerFallSprite.setPaused(true);
                            playerFallSprite.scaleTexture(new Vector2f(1f,-1f));
                            fallingLeft = false;
                            playerFallSprite.rotate(0,0,-180);
                            playerFallSprite.move(-.2f,-.35f);
                            playerFallSprite.getNode().move(0,0,10);
                        }
                        if(!resetJump){
                            playerFallSprite.setPaused(false);
                        }
                        fall = true;
                    }
                    last_pos = this_pos;
                }
                
                if (left)  {
                    
                    idle = false;
                    walkDirection.addLocal(camLeft);
                    if(player_character.onGround()){
                        footstep.play();
                        fall = false;
                        jumped = false;
                        playerNode.detachAllChildren();
                        playerNode.attachChild(playerSprite.getNode());
                        if(!flipped){           
                            playerSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                            playerSprite.setPaused(true);
                            playerSprite.scaleTexture(new Vector2f(1f,-1f));
                            playerSprite.setPaused(false);
                            flipped = true;
                            playerSprite.rotate(0, 0, 180);
                            playerSprite.move(.4f,.65f);
                            playerSprite.getNode().move(0,0,10);
                        }
                    }
                } else if (right){
                    
                    idle =false;
                    walkDirection.addLocal(camLeft.negate());
                    if(player_character.onGround()){
                        footstep.play();
                        jumped = false;
                        fall = false;
                        playerNode.detachAllChildren();
                        playerNode.attachChild(playerSprite.getNode());
                        if(flipped){
                            playerSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                            playerSprite.setPaused(true);
                            playerSprite.scaleTexture(new Vector2f(1f,-1f));
                            playerSprite.setPaused(false);
                            flipped = false;
                            playerSprite.rotate(0,0,-180);
                            playerSprite.move(-.4f,-.35f);
                            playerSprite.getNode().move(0,0,10);
                        }
                    }
                } else {
                   /*if(footstep.getStatus() == AudioSource.Status.Stopped)
                   footstep.stop();*/
                   if(player_character.onGround()){
                       fall = false;
                       jumped = false;
                       playerNode.detachAllChildren();
                       playerNode.attachChild(playerIdleSprite.getNode());
                       if(!facingLeft && !idle && (flipped  || fallingLeft)){
                            playerIdleSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                            playerIdleSprite.setPaused(true);
                            playerIdleSprite.scaleTexture(new Vector2f(1f,-1f));
                            playerIdleSprite.setPaused(false);
                            idle = true;
                            facingLeft = true;
                            playerIdleSprite.rotate(0,0,-180);
                            playerIdleSprite.move(.1f,.65f);
                            playerIdleSprite.getNode().move(0,0,10);
                       } else if(facingLeft && !idle && (!flipped  || !fallingLeft)){
                            playerIdleSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                            playerIdleSprite.setPaused(true);
                            playerIdleSprite.scaleTexture(new Vector2f(1f,-1f));
                            playerIdleSprite.setPaused(false);
                            idle = true;
                            facingLeft = false;
                            playerIdleSprite.rotate(0,0,-180);
                            playerIdleSprite.move(-.1f,-.35f);
                            playerIdleSprite.getNode().move(0,0,10);
                       }
                   }

                }
            }
           
            player_character.setWalkDirection(walkDirection.mult(.3f));

            for(int i = 0; i < l.getChildren().size(); i++){
                if(((Node)l.getChild(i)).getChildren().size()==2){
                    if(!((Node)((Node)l.getChild(i)).getChild(1)).getControl(GhostControl.class).getOverlappingObjects().isEmpty()){
                        Sprite s = null;
                        if(Main.level_count < 4){
                            s = new Sprite("Textures/Tiles/" + Main.invis_tiles[0], "invis_tile", assetManager, true, true, 25, 1, 0.05f, "NoLoop", "Continue");
                        } else if (Main.level_count < 8){
                            s = new Sprite("Textures/Tiles/" + Main.invis_tiles[1], "invis_tile", assetManager, true, true, 24, 1, 0.05f, "NoLoop", "Continue");                            
                        }else if (Main.level_count < 12){
                            s = new Sprite("Textures/Tiles/" + Main.invis_tiles[2], "invis_tile", assetManager, true, true, 8, 1, 0.05f, "NoLoop", "Continue");                            
                        }else if (Main.level_count < 16){
                            s = new Sprite("Textures/Tiles/" + Main.invis_tiles[3], "invis_tile", assetManager, true, true, 5, 1, 0.05f, "NoLoop", "Continue");                            
                        }else if (Main.level_count < 20){
                            s = new Sprite("Textures/Tiles/" + Main.invis_tiles[4], "invis_tile", assetManager, true, true, 21, 1, 0.05f, "Loop", "Continue");                            
                        }
                        library.addSprite(s);
                        ((Node)l.getChild(i)).attachChild(s.getNode());
                        /*Texture2D t = (Texture2D) assetManager.loadTexture("Textures/Tiles/ground.png");
                        ((Geometry)((Node)l.getChild(i)).getChild(0)).getMaterial().setTexture("ColorMap", t);*/
                    }
                }
            }

            if(!death_node.getControl(GhostControl.class).getOverlappingObjects().isEmpty()){
                playerNode.getControl(CharacterControl.class).setPhysicsLocation(start);
                for(int i = 0; i < l.getChildren().size(); i++){
                    if(((Node)l.getChild(i)).getChildren().size()==3){
                        
                        ((Node)l.getChild(i)).detachChildNamed("invis_tile");
                        /*Texture2D t = (Texture2D) assetManager.loadTexture("Textures/Tiles/transparent.png");
                        ((Geometry)((Node)l.getChild(i)).getChild(0)).getMaterial().setTexture("ColorMap", t);*/

                    }
                }

            }

            if(!ending_node.getControl(GhostControl.class).getOverlappingObjects().isEmpty()){
                app.getInputManager().removeListener(actionListener);
                complete = true;
                String userHome = System.getProperty("user.home");
                File file = new File(userHome+"/KnightGame/levels.j3o");
                File times = new File(userHome+"/KnightGame/time.j3o");
                BinaryExporter exporter = BinaryExporter.getInstance();
                Node level_count = new Node("level_count");
                level_count.setUserData("Count", Main.level_count+1);
                Node time = new Node("time");
                time.setUserData("time", Float.parseFloat(time_l.getText()));
                try {
                    exporter.save(level_count, file);
                    exporter.save(time, times);
                    System.out.println("SAVING");
                    System.out.println("COUNT: " + level_count.getUserData("Count"));
                } catch (IOException ex) {
                    
                    Logger.getLogger(LevelState.class.getName()).log(Level.SEVERE, null, ex);
                }
                stateManager.detach(stateManager.getState(LevelState.class));
            }
        }
        frameCount++;
    }
    
    @Override
    public void setEnabled(boolean active){
        super.setEnabled(active);
        
        if(!active){
            app.getInputManager().removeListener(actionListener);
            Main.current_time = Float.parseFloat(time_l.getText());
            PauseState pause_state = new PauseState(screen);
            stateManager.attach(pause_state);
            player_character.setEnabled(false);
            left = false;
            right = false;
            app.getInputManager().setCursorVisible(true);
        } else {
            createMappings();
            game_timer.reset();
            player_character.setEnabled(true);
            app.getInputManager().setCursorVisible(false);
            
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        if(complete){
            
            Main.level_count +=1;
            if(Main.level_count != Main.LEVELS){
                

                
                stateManager.detach(bas);
                bas = new BulletAppState();
                stateManager.attach(bas);
                bas.setDebugEnabled(false);
                rootNode.detachAllChildren();

                for(int i = 0; i < title_elements.length; i++){
                    screen.removeElement(title_elements[i]);
                }

                
                Main.current_time = Float.parseFloat(time_l.getText());
                LevelState level_state = new LevelState(screen);
                stateManager.attach(level_state);
            } else {
                for(int i = 0; i < title_elements.length; i++){
                    screen.removeElement(title_elements[i]);
                }
                Main.current_time = Float.parseFloat(time_l.getText());
                screen.removeElement(time_info_l);
                screen.removeElement(time_l);
                screen.removeElement(pause_l);
                stateManager.detach(bas);
                rootNode.detachAllChildren();
                
                CreditsState credits = new CreditsState(screen);
                stateManager.attach(credits);
                Main.bg.stop();
            }
        } else {
            
            for(int i = 0; i < title_elements.length; i++){
                screen.removeElement(title_elements[i]);
            }
            screen.removeElement(time_info_l);
            screen.removeElement(time_l);
            screen.removeElement(pause_l);
            stateManager.detach(bas);
            bas = new BulletAppState();
            stateManager.attach(bas);
            bas.setDebugEnabled(false);
            rootNode.detachAllChildren();
            //complete = false;
            Main.bg.stop();
            System.out.println(stateManager.getState(MainMenuState.class));
            MainMenuState main_state = new MainMenuState(screen);
            stateManager.attach(main_state);
        }
        
    }
    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String binding, boolean value, float tpf) {
            if(stateManager.getState(LevelState.class).isEnabled()){
                if (binding.equals("Left")) {
                    if (value) left = true;
                    else left = false;
                } else if (binding.equals("Right")) {
                    if (value) right = true;
                    else right = false;
                } else if (binding.equals("Jump") && value){
                    player_character.jump();
                } else if (binding.equals("Pause") && value){
                    stateManager.getState(LevelState.class).setEnabled(false);
                }
            }
        }

    };
    
    private void generalSetup(){
        tlratio = cam.getFrustumLeft()/cam.getFrustumTop();
        this.app.getInputManager().setCursorVisible(false);
        
    }

    private void createMappings() {
            app.getInputManager().addMapping("Left",  new KeyTrigger(KeyInput.KEY_A), 
                                     new KeyTrigger(KeyInput.KEY_LEFT)); // A and left arrow
            app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D), 
                                     new KeyTrigger(KeyInput.KEY_RIGHT)); // D and right arrow
            app.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_W), 
                                     new KeyTrigger(KeyInput.KEY_UP)); // D and right arrow
            app.getInputManager().addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));


            app.getInputManager().addListener(actionListener, new String[]{"Left", "Right","Jump","Pause"});
        
    }

    private void setupCollidables() {
        
        Vector collide = rxf.getCollidables();

        Node collisions = new Node("physical objects");

        for(int i = 0;i < collide.size(); i++){
             if(((TiledObjectGroup)collide.get(i)).getName().equalsIgnoreCase("collision")){
                CollisionGroup cg = (CollisionGroup)collide.get(i);
                for(int j = 0; j<cg.getGroup().size();j++){
                    float x = ((ObjectDetails)cg.getGroup().get(j)).getX();
                    float y = ((ObjectDetails)cg.getGroup().get(j)).getY();
                    float w = ((ObjectDetails)cg.getGroup().get(j)).getWidth();
                    float h = ((ObjectDetails)cg.getGroup().get(j)).getHeight();
                    
                    x = (x/32f)  * ((cam.getFrustumRight()-cam.getFrustumLeft())/rxf.getMap_width());
                    y = (rxf.getMap_height() -(y/32f)) * (cam.getFrustumTop()-cam.getFrustumBottom())/rxf.getMap_height();
                    w = w/32f * ((cam.getFrustumRight()-cam.getFrustumLeft())/rxf.getMap_width());
                    h = h/32f * ((cam.getFrustumTop()-cam.getFrustumBottom())/rxf.getMap_height());
                    
                    RigidBodyControl floorMesh = new RigidBodyControl(new BoxCollisionShape(new Vector3f(w/2f,h/2f,1)),0.0f);

                    Quad b = new Quad(.1f,.1f); // create cube shape
                    
                    Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
                    Material mat = new Material(assetManager,
                      "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
                    mat.setColor("Color",new ColorRGBA(0,0,0,0f));   // set color of material to blue
                    geom.setMaterial(mat);
                    geom.addControl(floorMesh);
                   
                    Node floorMeshNode = new Node("mesh for object");
                    floorMeshNode.attachChild(geom);
                    
                    floorMeshNode.addControl(floorMesh);
                    collisions.attachChild(floorMeshNode);
                    bas.getPhysicsSpace().add(floorMesh);

                    floorMeshNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(x-cam.getFrustumRight()+w/2f,y-cam.getFrustumTop()-h/2f,-2));
                }
             }
        }
        rootNode.attachChild(collisions);
        
        GhostControl bottom_death = new GhostControl(new BoxCollisionShape(new Vector3f(cam.getWidth()/32f/2f,2f,3f)));
        death_node = new Node("node for death");
        
        
        death_node.addControl(bottom_death);
            
        rootNode.attachChild(death_node);
        bas.getPhysicsSpace().add(bottom_death);
        death_node.setLocalTranslation(0,0-cam.getHeight()/32f, 0);
        
        start.x = ((start.x/32f)  * ((cam.getFrustumRight()-cam.getFrustumLeft())/rxf.getMap_width())) -cam.getFrustumRight();
        start.y = ((rxf.getMap_height() -(start.y/32f)) * (cam.getFrustumTop()-cam.getFrustumBottom())/rxf.getMap_height()) -cam.getFrustumTop();
        start.z = -2;
        
        end.x = ((end.x/32f)  * ((cam.getFrustumRight()-cam.getFrustumLeft())/rxf.getMap_width())) -cam.getFrustumRight();
        end.y = ((rxf.getMap_height() -(end.y/32f)) * (cam.getFrustumTop()-cam.getFrustumBottom())/rxf.getMap_height()) -cam.getFrustumTop();
        end.z = -2;
        
        GhostControl ending = new GhostControl(new BoxCollisionShape(new Vector3f(.1f,.1f,2)));
        ending_node = new Node("ending");
        ending_node.setLocalTranslation(end);
        ending_node.addControl(ending);
        
        Sprite warp = new Sprite("Textures/WarpHoleRow.png","WarpHole", assetManager, true, true, 25, 2, 0.05f, "Loop", "Start");
        warp.setPaused(false);
        library.addSprite(warp);
        ending_node.attachChild(warp.getNode());
        warp.move(-.5f, -.5f);
        warp.getNode().move(0,0,10);
        bas.getPhysicsSpace().add(ending_node);
        
        rootNode.attachChild(ending_node);

    }

    private void setupPlayer() {
        playerSprite = new Sprite("Textures/KnightForward.png", "Player", assetManager, true, true, 24, 1, 0.05f, "Loop", "Start");
        playerIdleSprite = new Sprite("Textures/KnightIdle.png", "PlayerIdle", assetManager, true, true, 20, 1, 0.05f, "Loop", "Start");
        playerJumpSprite = new Sprite("Textures/KnightJump.png", "PlayerJump", assetManager, true, true, 5, 1, .05f, "NoLoop", "Start");
        playerFallSprite = new Sprite("Textures/KnightFall.png", "FallJump", assetManager, true, true, 5, 1, .08f, "NoLoop", "Start");
        
        playerSprite.setPaused(false);
        playerIdleSprite.setPaused(false);
        playerJumpSprite.setPaused(false);
        playerFallSprite.setPaused(false);
        library.addSprite(playerSprite);
        library.addSprite(playerIdleSprite);
        library.addSprite(playerJumpSprite);
        library.addSprite(playerFallSprite);
        engine.addLibrary(library);
        
        playerNode = new Node();
        playerNode.attachChild(playerSprite.getNode());
        playerNode.attachChild(playerIdleSprite.getNode());
        playerNode.attachChild(playerJumpSprite.getNode());
        playerNode.attachChild(playerFallSprite.getNode());
        playerIdleSprite.move(-.1f,-.35f);
        playerIdleSprite.getNode().move(0,0,10);
        playerNode.detachChild(playerIdleSprite.getNode());
        playerJumpSprite.move(-.2f,-.35f);
        playerJumpSprite.getNode().move(0,0,10);
        playerNode.detachChild(playerJumpSprite.getNode());
        playerFallSprite.move(-.2f,-.35f);
        playerFallSprite.getNode().move(0,0,10);
        playerNode.detachChild(playerFallSprite.getNode());
        playerSprite.move(-.4f,-.35f);
        playerSprite.getNode().move(0,0,10);

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(.2f,.3f);
        
        player_character = new CharacterControl(capsuleShape, .2f);
        player_character.setFallSpeed(20f);
        
        playerNode.addControl(player_character);
        rootNode.attachChild(playerNode);
        bas.getPhysicsSpace().add(playerNode);
        
        playerNode.getControl(CharacterControl.class).setPhysicsLocation(start);
        
               
    }

    private void createTitle() {
        
       /* String title = "Stage " + Main.level_count + " of " + Main.LEVELS;
        
        
        title_elements = new Element[1];
        title_elements[0] = new Element(screen, "title_words",new Vector2f(0,0),new Vector2f(Math.abs(tlratio) * title.length()*10 + 10,0),
                new Vector4f(5,5,5,5),null);
        title_elements[0].setText(title);
        title_elements[0].setPosition(20,50);
        title_elements[0].setFontSize(Math.abs(tlratio)*20);
        title_elements[0].setFont("Interface/Fonts/KnightsQuest.fnt");
        Effect effect = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            1f // The duration of time over which the effect executes (2.2 seconds)
        ); 
        effect.setEffectDirection(Effect.EffectDirection.Left);
        title_elements[0].addEffect(effect);
        
        screen.addElement(title_elements[0]);
        
        title_elements[0].hide();
        title_elements[0].showWithEffect();
        
        Effect effect2 = new Effect(
            Effect.EffectType.SlideOut, // The type of effect to use
            Effect.EffectEvent.Hide, // The event that the effect is associated with
            1f // The duration of time over which the effect executes (2.2 seconds)
        ); */
        
        /*effect2.setEffectDirection(Effect.EffectDirection.Left);
        title_elements[0].addEffect(effect2);

 
        title_elements[0].hideWithEffect();*/
        String title = Main.titles[Main.level_count];
        
        title_elements = new Element[1];
        title_elements[0] = new Element(screen, "title_words",new Vector2f(0,0),new Vector2f(Math.abs(tlratio) * title.length()*10 + 10,0),
                new Vector4f(5,5,5,5),null);
        title_elements[0].setText(title);
        title_elements[0].setPosition(20,50);
        title_elements[0].setFontSize(Math.abs(tlratio)*20);
        title_elements[0].setFont("Interface/Fonts/KnightsQuest.fnt");
         
        Effect effect = new Effect(
            Effect.EffectType.SlideIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            1f // The duration of time over which the effect executes (2.2 seconds)
        ); 
        
        effect.setEffectDirection(Effect.EffectDirection.Left);
        title_elements[0].addEffect(effect);
        
        //screen.addElement(title_elements[0]);
       // title_elements[0].hide();
       
        /*words = title.split("\\s+");
        
        title_elements = new Element[words.length];
        
        
        for(int i = 0; i < words.length; i++){
             String k = "title_word_" + i;
             title_elements[i] = new Element(screen, k,new Vector2f(0,0),new Vector2f(Math.abs(tlratio) * words[i].length() * 7 + 10,0),
                new Vector4f(5,5,5,5),null);
             title_elements[i].setText(words[i]);
             int length = 0;
             for(int j = 0; j < i; j++){
                 length += 10 + Math.abs(tlratio) * (words[j].length() * 7);
             }
             title_elements[i].setPosition(20+length,50);
             title_elements[i].setFontSize(Math.abs(tlratio)*20);
             //title_elements[i];
             screen.addElement(title_elements[i]);
        }*/
        
    }
}
