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
import java.util.Vector;
import static mygame.Main.engine;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author JSC
 */
public class LevelState extends AbstractAppState {
    SpriteLibrary library;
    Label time_l;
    BulletAppState bas;
    Sprite playerSprite;
    Sprite playerSprite2;
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
    private boolean flipped = false;
    Vector3f start;
    Vector3f end;
    Node ending_node;
    
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

    LevelState(Screen screen){
        this.screen = screen;
        this.time_l = (Label)screen.getElementById("time");
        this.time_info_l = screen.getElementById("time_info");
        
       
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
        
        float w = cam.getFrustumLeft()- cam.getFrustumRight(); 
        float h = cam.getFrustumTop()- cam.getFrustumBottom(); 

        Quad bg = new Quad(1,1);
        Geometry geo = new Geometry("BG",bg);
        
        Texture t = assetManager.loadTexture("Textures/background-cave.png");
        Material s_material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        s_material.setTexture("ColorMap", t);
        //geo.setQueueBucket(Bucket.Transparent); 
        //geo.setQueueBucket(Bucket.Sky);
        geo.setCullHint(CullHint.Never);
        geo.setMaterial(s_material);
        s_material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        
        rootNode.attachChild(geo);
        
        System.out.println(cam.getFrustumTop()-cam.getFrustumBottom());
        geo.move(cam.getFrustumLeft(),cam.getFrustumBottom(),-.5f);
        
        geo.setLocalScale(cam.getFrustumRight()- cam.getFrustumLeft(),cam.getFrustumTop()- cam.getFrustumBottom(),1);

        generalSetup();
        
        createTitle();
        
        //createMappings();
        
        rxf = new ReadXMLFile("assets/Textures/" + Main.levels[Main.level_count],assetManager,bas);
        rxf.parse();
        l = rxf.createLevel(cam);
        
        start = rxf.getStart();
        end = rxf.getEnd();
                           
        rootNode.attachChild(l);
        
        setupCollidables();
        
        setupPlayer();
        
        createMappings();

        game_timer = new LwjglTimer();

        
        
       
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.25f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.25f);
        camDir.y = 0;
        camLeft.y = 0;
        
        if(stateManager.getState(LevelState.class).isEnabled()){
            try{
               time_l.setText((String)(Float.toString(game_timer.getTimeInSeconds() + Main.current_time)).subSequence(0, 5));
            } catch(Exception e) {
               time_l.setText((String)(Float.toString(game_timer.getTimeInSeconds() +  Main.current_time)).subSequence(0, 3));
            }
            
            Main.engine.update(tpf);

            walkDirection.set(0, 0, 0);
            if (left)  {
                walkDirection.addLocal(camLeft);
                if(!flipped){
                    playerSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                    playerSprite.setPaused(true);
                    playerSprite.scaleTexture(new Vector2f(1f,-1f));
                    playerSprite.setPaused(false);
                    flipped = true;
                    playerSprite.rotate(0, 0, 180);
                    playerSprite.move(.5f,.65f);
                    playerSprite.getNode().move(0,0,10);
                }
            } else if (right){
                walkDirection.addLocal(camLeft.negate());
                if(flipped){
                    playerSprite.getNode().setLocalTranslation(new Vector3f(0,0,0));
                    playerSprite.setPaused(true);
                    playerSprite.scaleTexture(new Vector2f(1f,-1f));
                    playerSprite.setPaused(false);
                    flipped = false;
                    playerSprite.rotate(0,0,-180);
                    playerSprite.move(-.5f,-.35f);
                    playerSprite.getNode().move(0,0,10);
                }
            }
           
            player_character.setWalkDirection(walkDirection.mult(.3f));

            for(int i = 0; i < l.getChildren().size(); i++){
                if(((Node)l.getChild(i)).getChildren().size()==2){
                    if(!((Node)((Node)l.getChild(i)).getChild(1)).getControl(GhostControl.class).getOverlappingObjects().isEmpty()){
                        Sprite s = new Sprite("Textures/Tiles/ReiatsuMod.png", "invis_tile", assetManager, true, true, 25, 1, 0.05f, "NoLoop", "Continue");
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
                        
                        System.out.println("hi");
                        ((Node)l.getChild(i)).detachChildNamed("invis_tile");
                        /*Texture2D t = (Texture2D) assetManager.loadTexture("Textures/Tiles/transparent.png");
                        ((Geometry)((Node)l.getChild(i)).getChild(0)).getMaterial().setTexture("ColorMap", t);*/

                    }
                }

            }

            if(!ending_node.getControl(GhostControl.class).getOverlappingObjects().isEmpty()){
                app.getInputManager().removeListener(actionListener);
                stateManager.detach(stateManager.getState(LevelState.class));
            }
        }
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
            
        } else {
            createMappings();
            game_timer.reset();
            player_character.setEnabled(true);
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        if(Main.level_count != 1){
            Main.level_count +=1;
        
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
            System.out.println("DONE");
        }
   


        
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
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
                    System.out.println("Pausing");
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
                    
                    x = (x/32f)  * ((cam.getFrustumRight()-cam.getFrustumLeft())/rxf.getMap_width());///(rxf.getMap_width()*32f))*(cam.getFrustumRight());
                    y = (rxf.getMap_height() -(y/32f)) * (cam.getFrustumTop()-cam.getFrustumBottom())/rxf.getMap_height();//- cam.getFrustumTop();//((1- (y/(32f*rxf.getMap_height()))))-(h/(32f*rxf.getMap_height()))-rxf.getMap_height()/2;//+cam.getFrustumTop())-h;
                    w = w/32f * ((cam.getFrustumRight()-cam.getFrustumLeft())/rxf.getMap_width());///(cam.getFrustumRight()*2);
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

        playerSprite.setPaused(false);
        library.addSprite(playerSprite);
        engine.addLibrary(library);
        
        playerNode = new Node();
        playerNode.attachChild(playerSprite.getNode());
        playerSprite.move(-.5f,-.35f);
        playerSprite.getNode().move(0,0,10);

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(.3f,.15f);
        
        player_character = new CharacterControl(capsuleShape, .2f);
        player_character.setFallSpeed(20f);
        
        playerNode.addControl(player_character);
        rootNode.attachChild(playerNode);
        bas.getPhysicsSpace().add(playerNode);
        
        playerNode.getControl(CharacterControl.class).setPhysicsLocation(start);
        
               
    }

    private void createTitle() {
        
        String title = Main.titles[Main.level_count];
        
        title_elements = new Element[1];
        title_elements[0] = new Element(screen, "title_words",new Vector2f(0,0),new Vector2f(Math.abs(tlratio) * title.length()*10 + 10,0),
                new Vector4f(5,5,5,5),null);
        title_elements[0].setText(title);
        title_elements[0].setPosition(20,50);
        title_elements[0].setFontSize(Math.abs(tlratio)*20);
             //title_elements[i];
        screen.addElement(title_elements[0]);
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
