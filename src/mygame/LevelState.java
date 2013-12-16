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
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.lwjgl.LwjglTimer;
import com.jme3.texture.Texture2D;
import java.awt.image.BufferedImage;
import java.util.Vector;
import static mygame.Main.engine;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;

/**
 *
 * @author JSC
 */
public class LevelState extends AbstractAppState {
    
    Label time_l;
    BulletAppState bas;
    Sprite playerSprite;
    int gravity = -80;
    Vector3f playerVelocity = new Vector3f(0,gravity,0);
    Node playerNode;
    boolean jumping = false;
    boolean on_ground = false;
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
    
    private ReadXMLFile rxf;
    
    SimpleApplication app;
    AppStateManager stateManager;
    AssetManager assetManager;
    Node rootNode;
    Node guiNode;
    Camera cam;

    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        System.out.println(app);
        this.app = (SimpleApplication)app;
        this.stateManager = stateManager;
        this.assetManager = this.app.getAssetManager();
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        
        generalSetup();
        
        setupPlayer();
        
        //createMappings();
        
        rxf = new ReadXMLFile("Textures/testMap.tmx",assetManager,bas);
        rxf.parse();
        l = rxf.createLevel(cam);
                           
        rootNode.attachChild(l);
        
        setupCollidables();
        
        createMappings();

        game_timer = new LwjglTimer();
        
        System.out.println(this.app);
        /*Screen screen = new Screen(this.app);
        guiNode.addControl(screen);
        
        Element time_info_l = new Element(screen, "label",new Vector2f(0,0),new Vector2f(190,0),
                new Vector4f(5,5,5,5),"Textures/Sprite.png");
        time_info_l.setPosition(cam.getWidth()/2f - time_info_l.getWidth()/2f, 10f);
        time_info_l.setText("Seconds since start:");
        time_info_l.setFontColor(ColorRGBA.White);
        time_info_l.setFontSize(Math.abs(tlratio) * 15);

        time_l = new Label(screen, "time", new Vector2f(0,0),new Vector2f(70,0));
        time_l.setPosition(time_info_l.getPosition().x + time_info_l.getWidth(),10);
        time_l.setFontColor(ColorRGBA.White);
        time_l.setFontSize(Math.abs(tlratio) * 15);
        
        Effect effect = new Effect(
            Effect.EffectType.FadeIn, // The type of effect to use
            Effect.EffectEvent.Show, // The event that the effect is associated with
            10f // The duration of time over which the effect executes (2.2 seconds)
        ); 
        
        time_info_l.addEffect(effect);
        time_info_l.hide();
  
        //screen.getEffectManager().applyEffect(effect);
        

        screen.addElement(time_l);
        screen.addElement(time_info_l);
        
        
        
        time_info_l.showWithEffect();*/
        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
         /*try{
            time_l.setText((String)(Float.toString(game_timer.getTimeInSeconds()).subSequence(0, 5)));
        } catch(Exception e) {
            time_l.setText((String)(Float.toString(game_timer.getTimeInSeconds()).subSequence(0, 3)));
        }*/
        //game_timer.update();
        
        walkDirection.set(0, 0, 0);
        
        if (left)  walkDirection = new Vector3f(-.1f,0f,0f);
        if (right) walkDirection = new Vector3f(.1f,0f,0f);

        player_character.setWalkDirection(walkDirection);
        
        for(int i = 0; i < l.getChildren().size(); i++){
            if(((Node)l.getChild(i)).getChildren().size()==2){
                if(!((Node)((Node)l.getChild(i)).getChild(1)).getControl(GhostControl.class).getOverlappingObjects().isEmpty()){
                    
                    Texture2D t = (Texture2D) assetManager.loadTexture("Textures/Tiles/ground.png");
                    ((Geometry)((Node)l.getChild(i)).getChild(0)).getMaterial().setTexture("ColorMap", t);
                }
            }
        }

        if(!death_node.getControl(GhostControl.class).getOverlappingObjects().isEmpty()){
            playerNode.getControl(CharacterControl.class).setPhysicsLocation(new Vector3f(cam.getFrustumLeft()+1,-2,0));
            for(int i = 0; i < l.getChildren().size(); i++){
                if(((Node)l.getChild(i)).getChildren().size()==2){

                    Texture2D t = (Texture2D) assetManager.loadTexture("Textures/Tiles/transparent.png");
                    ((Geometry)((Node)l.getChild(i)).getChild(0)).getMaterial().setTexture("ColorMap", t);
                    
                }
            }
        
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String binding, boolean value, float tpf) {
            
            if (binding.equals("Left")) {
                if (value) left = true;
                else left = false;
            } else if (binding.equals("Right")) {
                if (value) right = true;
                else right = false;
            } else if (binding.equals("Jump")){
                player_character.jump();
            }
        }

    };
    
    private void generalSetup(){
        cam.setParallelProjection(true);
        tlratio = cam.getFrustumLeft()/cam.getFrustumTop();
        cam.setFrustumTop(5);
        cam.setFrustumBottom(-5f);
        cam.setFrustumLeft(cam.getFrustumTop() * tlratio);
        cam.setFrustumRight(cam.getFrustumTop() * -(tlratio));
        cam.update();
        this.app.getFlyByCamera().setEnabled(true);
        this.app.getInputManager().setCursorVisible(false);
        bas = new BulletAppState();
        stateManager.attach(bas);
        bas.setDebugEnabled(true);
    }

    private void createMappings() {
        app.getInputManager().addMapping("Left",  new KeyTrigger(KeyInput.KEY_A), 
                                 new KeyTrigger(KeyInput.KEY_LEFT)); // A and left arrow
        app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D), 
                                 new KeyTrigger(KeyInput.KEY_RIGHT)); // D and right arrow
        app.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_W), 
                                 new KeyTrigger(KeyInput.KEY_UP)); // D and right arrow
        
        app.getInputManager().addListener(actionListener, new String[]{"Left", "Right","Jump"});
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
                    
                    x = (x/32f)  * ((cam.getFrustumRight()-cam.getFrustumLeft())/rxf.getMap_width());///(rxf.getMap_width()*32f))*(cam.getFrustumRight());
                    y = (rxf.getMap_height() -(y/32f)) * (cam.getFrustumTop()-cam.getFrustumBottom())/rxf.getMap_height();//- cam.getFrustumTop();//((1- (y/(32f*rxf.getMap_height()))))-(h/(32f*rxf.getMap_height()))-rxf.getMap_height()/2;//+cam.getFrustumTop())-h;
                    w = w/32f * ((cam.getFrustumRight()-cam.getFrustumLeft())/rxf.getMap_width());///(cam.getFrustumRight()*2);
                    h = h/32f * ((cam.getFrustumTop()-cam.getFrustumBottom())/rxf.getMap_height());
                   
                    
                    RigidBodyControl floorMesh = new RigidBodyControl(new BoxCollisionShape(new Vector3f(w/2f,h/2f,2)),0.0f);
                    //GhostControl floorDetect = new GhostControl(new BoxCollisionShape(new Vector3f(w/32f,h/2f,2)));

                    Quad b = new Quad(.1f,.1f); // create cube shape
                    
                    Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
                    Material mat = new Material(assetManager,
                      "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
                    mat.setColor("Color",new ColorRGBA(0,0,0,0f));   // set color of material to blue
                    geom.setMaterial(mat);
                    geom.addControl(floorMesh);
                    //geom.addControl(floorDetect);
                   
                    //geom.scale(w,h,0);
                    Node floorMeshNode = new Node();
                    floorMeshNode.attachChild(geom);
                    //geom.move(-w/32f/2f,-h/32f/2f,0);
                    
                    floorMeshNode.addControl(floorMesh);
                    collisions.attachChild(floorMeshNode);
                    bas.getPhysicsSpace().add(floorMesh);
                    //bas.getPhysicsSpace().add(floorDetect);

                    floorMeshNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(x-cam.getFrustumRight()+w/2f,y-cam.getFrustumTop()-h/2f,0));
                }
             }
        }
        rootNode.attachChild(collisions);
        
        GhostControl bottom_death = new GhostControl(new BoxCollisionShape(new Vector3f(cam.getWidth()/32f/2f,2f,2f)));
        death_node = new Node("node for death");
        death_node.addControl(bottom_death);
            
        rootNode.attachChild(death_node);
        bas.getPhysicsSpace().add(bottom_death);
        death_node.setLocalTranslation(0,0-cam.getHeight()/32f, 0);
        
        

    }

    private void setupPlayer() {
        playerSprite = new Sprite("Textures/Sprite.png", "Player", assetManager, true, true, 9, 1, 0.08f, "NoLoop", "Start");
        playerSprite.setPaused(true);
        SpriteLibrary library = new SpriteLibrary("Library 1", false);
        SpriteLibrary.setL_guiNode(rootNode);
        library.addSprite(playerSprite);
        engine.addLibrary(library);
        
        playerNode = new Node();
        playerNode.attachChild(playerSprite.getNode());
        playerSprite.move(-.5f,-.5f);
        
        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(.3f,.15f);
        
        player_character = new CharacterControl(capsuleShape, .2f);
        player_character.setFallSpeed(20f);
        //player_character.setGravity(30f);
        
        playerNode.addControl(player_character);
        rootNode.attachChild(playerNode);
        bas.getPhysicsSpace().add(playerNode);
        
        playerNode.getControl(CharacterControl.class).setPhysicsLocation(new Vector3f(cam.getFrustumLeft()+1,-3,0));
               
    }
}
