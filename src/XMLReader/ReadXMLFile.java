/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLReader;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class ReadXMLFile {
 
    AssetManager asset_manager;
    InputStream xml_to_read;
    private int map_width;
    private int map_height;
    private int tile_width;
    private int tile_height;
    int total_tilesets = 0;
    Vector tilesets = new Vector<TileSet>();
    Vector layers = new Vector<TileSet>();
    Vector tiles = new Vector<Integer>();
    Vector objectgroups = new Vector<TiledObjectGroup>();
    int objectgroups_counter=0;
    private AssetManager assetManager;
    private BulletAppState bas;
    
    Vector3f start;
    Vector3f end;
    
   public ReadXMLFile(InputStream to_read,AssetManager as, BulletAppState bas){
       asset_manager = as;
       xml_to_read = to_read;
       this.bas = bas;
   }
   
   public ReadXMLFile(){}
   
   public void parse(){
       try {
 
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
 
	DefaultHandler handler = new DefaultHandler() {
 
	boolean bfname = false;
	boolean blname = false;
	boolean bnname = false;
	boolean bsalary = false;
        
        
        
        int firstgid;
        String tileset_name;
        int tileset_tile_width;
        int tileset_tile_height;
        int tileset_image_width;
        int tileset_image_height;
        String tileset_image_path;
        
        String objectgroup_name;
        int objectgroup_width;
        int objectgroup_height;
        
        int object_width;
        int object_height;
        int object_x;
        int object_y;
        
        Vector objects = new Vector<ObjectDetails>();
        
 
	public void startElement(String uri, String localName,String qName, 
                Attributes attributes) throws SAXException {
 
 
                if(qName.equalsIgnoreCase("tileset")){
                   firstgid = Integer.parseInt(attributes.getValue("firstgid"));

                   tileset_name = attributes.getValue("name");
                   tileset_tile_width = Integer.parseInt(attributes.getValue("tilewidth"));
                   tileset_tile_height = Integer.parseInt(attributes.getValue("tileheight"));
                }
		if(qName.equalsIgnoreCase("image")){
                    tileset_image_path = "Textures/"+attributes.getValue("source");
                    tileset_image_height = Integer.parseInt(attributes.getValue("height"));
                    tileset_image_width = Integer.parseInt(attributes.getValue("width"));
                    tilesets.add(new TileSet(firstgid,tileset_name,tileset_tile_width,
                            tileset_tile_height,tileset_image_path,tileset_image_width,tileset_image_height));
                    total_tilesets++;
                }
                if(qName.equalsIgnoreCase("layer")){
                    layers.add(attributes.getValue("name"));
                }
                if(qName.equalsIgnoreCase("map")){
                        setMap_width(Integer.parseInt(attributes.getValue("width")));
                        setMap_height(Integer.parseInt(attributes.getValue("height")));
                        setTile_width(Integer.parseInt(attributes.getValue("tilewidth")));
                        setTile_height(Integer.parseInt(attributes.getValue("tileheight")));
                }
                if(qName.equalsIgnoreCase("tile")) {
                    tiles.add(attributes.getValue("gid"));
                }
                if(qName.equalsIgnoreCase("objectgroup")){
                    if(attributes.getValue("name").equalsIgnoreCase("collision")){
                        objectgroup_name = attributes.getValue("name");
                        objectgroup_width = Integer.parseInt(attributes.getValue("width"));
                        objectgroup_height = Integer.parseInt(attributes.getValue("height"));
                    }
                }
                
                if(qName.equalsIgnoreCase("object")){
                    if(attributes.getValue("name")!=null){
                        if(attributes.getValue("name").equalsIgnoreCase("Start")){
                            start = new Vector3f(Integer.parseInt(attributes.getValue("x")),Integer.parseInt(attributes.getValue("y")),0);
                        } else if(attributes.getValue("name").equalsIgnoreCase("End")){
                            end = new Vector3f(Integer.parseInt(attributes.getValue("x")),Integer.parseInt(attributes.getValue("y")),0);
                        }
                        
                        return;
                    }
                    
                    object_x = Integer.parseInt(attributes.getValue("x"));
                    object_y = Integer.parseInt(attributes.getValue("y"));
                    object_width = Integer.parseInt(attributes.getValue("width"));
                    object_height = Integer.parseInt(attributes.getValue("height"));
                    objects.add(new ObjectDetails(object_x, object_y, object_width, object_height));
                }
	}
       
        public void endElement(String uri, String localName,String qName) throws SAXException {
                
                if(qName.equalsIgnoreCase("objectgroup")){ 
                    if(objectgroup_name.equalsIgnoreCase("collision")){
                        objectgroups.add(new CollisionGroup(objectgroup_name,objectgroup_width,objectgroup_height,objects));
                    }
                }
            
        }
 
 
     };
      // File xml = (File)asset_manager.loadAsset("Textures/testMap.tmx");
       saxParser.parse(xml_to_read, handler);
 
     } catch (Exception e) {
       e.printStackTrace();
     }
       
       
   }
   
   public Node createLevel(Camera cam){
      
       Node level = new Node("level");
       
       int[][] tile_coords = new int[getMap_height()][getMap_width()];
       for(int i  =0; i< layers.size(); i++){
        for(int y = 0; y < getMap_height(); y++){
            for(int x = 0; x < getMap_width(); x++){
                
                if(i>0 && tile_coords[y][x]!=0){
                    continue;
                }
                
                tile_coords[y][x] = Integer.parseInt((String)tiles.get((x+(y*getMap_width()))+(i*(getMap_height())*(getMap_width()))));

            }
        }
       }
       Vector geoms = new Vector<Object[]>();
       Vector tilesetImages = loadTileSets();
       
       BufferedImage map = new BufferedImage(getMap_width()*getTile_width(), getMap_height()*getTile_height(), BufferedImage.TYPE_INT_ARGB);
       
       float width = cam.getFrustumRight()-cam.getFrustumLeft();
       float height = cam.getFrustumTop()-cam.getFrustumBottom();
       
       for(int sprite_for_y=0;sprite_for_y < getMap_height(); sprite_for_y++){
           for(int sprite_for_x=0;sprite_for_x < getMap_width(); sprite_for_x++){
               int tile_gid = tile_coords[sprite_for_y][sprite_for_x];
               
              
               if(tile_gid==0){
                   continue;
               }
               int current_counter = 0;
               TileSet current = null;
               for(int i =0; i < tilesetImages.size(); i++){
                   if(tile_gid >= ((TileSet)tilesets.get(i)).getFirstgid()){
                       current = ((TileSet)tilesets.get(i));
                       current_counter++;
                   } 
               }
               
               current_counter--;
               int dest_y = sprite_for_y * getTile_height();
               int dest_x = sprite_for_x * getTile_width();
               
               tile_gid -= current.getFirstgid() -1;
               int source_y;
               int source_x;
               if(tile_gid % current.getTile_amount_width() - 1 < 0){
                   source_x = (tile_gid % current.getTile_amount_width())+current.getTile_amount_width()-1;
                   source_y =  (int)tile_gid/current.getTile_amount_width()-1;
               } else {
                   source_x = tile_gid % current.getTile_amount_width() - 1;
                   source_y = (int)Math.ceil(tile_gid/current.getTile_amount_width());
               }
                            
               
               map.setRGB(dest_x, dest_y, current.getTile_width(), current.getTile_height(), ((BufferedImage)tilesetImages.get(current_counter)).getRGB
                       (source_x*current.getTile_width(), source_y*current.getTile_height(), current.getTile_width(), current.getTile_height(), null,0, current.getTile_width()), 0, current.getTile_width());

               
                BufferedImage tile = new BufferedImage(getTile_width(), getTile_height(), BufferedImage.TYPE_INT_ARGB);
                tile.setRGB(0,0, current.getTile_width(),current.getTile_width(), ((BufferedImage)tilesetImages.get(current_counter)).getRGB
                       (source_x*current.getTile_width(), source_y*current.getTile_height(), current.getTile_width(), current.getTile_height(), null,0, current.getTile_width()), 0, current.getTile_width());
              
                Quad b = new Quad(1,1); // create cube shape
                Geometry geom = new Geometry("Quad", b);  // create cube geometry from the shape
                AWTLoader awtl = new AWTLoader();
                //geom.setLocalScale(1, 10, 0);
                Image i = awtl.load(tile,true);
                Texture2D t = new Texture2D(i);
                Material mat = new Material(asset_manager,"Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
                mat.setTexture("ColorMap",t);  // set color of material to blue
                mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
                geom.setMaterial(mat);
                geom.setQueueBucket(Bucket.Transparent); 
                Node quad_tile = new Node("Quad");
                if(tile_gid == 1 && current_counter == 0){
                    quad_tile.setUserData("name","invisible");
                } else {
                    quad_tile.setUserData("name","normal");
                }
                quad_tile.attachChild(geom);
                
                quad_tile.setLocalTranslation(dest_x/32f,((getMap_height()*getTile_height())-dest_y)/32f,0f);
                level.attachChild(quad_tile);
                Object[] group = new Object[3];
                group[0] = geom;
                group[1] = dest_x;
                group[2] = dest_y;
                geoms.add(group);
           
           }
           
           
       }
       
       
       
      // return map;
       
        level.setLocalScale(width/getMap_width(),height/getMap_height(),0);
        for(int  i = 0; i < level.getChildren().size(); i++){
            ((Node)level.getChild(i)).move(-getMap_width()/2f,-getMap_height()/2f - 1,0);
            if(((Node)level.getChild(i)).getUserData("name").equals("invisible")){
                GhostControl floorDetect = new GhostControl(new BoxCollisionShape(new Vector3f((tile_width*level.getLocalScale().x)/32f/4f/2f,(tile_height*level.getLocalScale().y/32f/2f/3f),3f)));
                Node adjustGhost = new Node();
                
                ((Node)level.getChild(i)).attachChild(adjustGhost);
                adjustGhost.move(tile_width/32f/2f,(tile_height/32f)+(tile_height/32f/2f),0);
               // floorDetect.setPhysicsLocation(new Vector3f(0,0,-5));
                adjustGhost.addControl(floorDetect);
                bas.getPhysicsSpace().add(adjustGhost);
            }
        }
       
       return level;
   }
 
   private Vector<BufferedImage> loadTileSets(){
       Vector tilesetImages = new Vector<BufferedImage>();
       for(int i=0; i < tilesets.size();i++){
            try{
                tilesetImages.add(ImageIO.read(asset_manager.locateAsset(
                new AssetKey(((TileSet)tilesets.get(i)).getSource())).openStream()));
            }
            catch (IOException ex) {
             
            }
       }
       return tilesetImages;
   }
   

    public Vector getCollidables(){
        
        return objectgroups;

    }

    /**
     * @return the map_width
     */
    public int getMap_width() {
        return map_width;
    }

    /**
     * @param map_width the map_width to set
     */
    public void setMap_width(int map_width) {
        this.map_width = map_width;
    }

    /**
     * @return the map_height
     */
    public int getMap_height() {
        return map_height;
    }

    /**
     * @param map_height the map_height to set
     */
    public void setMap_height(int map_height) {
        this.map_height = map_height;
    }

    /**
     * @return the tile_width
     */
    public int getTile_width() {
        return tile_width;
    }
    
    public Vector3f getStart(){
        return start;
    }
    
    public Vector3f getEnd(){
        return end;
    }

    /**
     * @param tile_width the tile_width to set
     */
    public void setTile_width(int tile_width) {
        this.tile_width = tile_width;
    }

    /**
     * @return the tile_height
     */
    public int getTile_height() {
        return tile_height;
    }

    /**
     * @param tile_height the tile_height to set
     */
    public void setTile_height(int tile_height) {
        this.tile_height = tile_height;
    }
}

