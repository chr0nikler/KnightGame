/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLReader;

/**
 *
 * @author JSC
 */
public class TileSet {
    
    private int firstgid;
    private int lastgid;
    private String name;
    private int tile_width;
    private int tile_height;
    private String source;
    private int image_width;
    private int image_height;
    private int tile_amount_width;
    
    public TileSet(int firstgid, String name, int tile_width, int tile_height, String source, 
            int image_width, int image_height){
        this.firstgid=firstgid;
        this.name=name;
        this.tile_width=tile_width;
        this.tile_height=tile_height;
        this.source=source;
        this.image_width=image_width;
        this.image_height=image_height;
        tile_amount_width = (int)Math.floor(image_width/tile_width);
        lastgid=tile_amount_width * (int)Math.floor(image_height/tile_height) + firstgid -1;
    }

    /**
     * @return the firstgid
     */
    public int getFirstgid() {
        return firstgid;
    }

    /**
     * @param firstgid the firstgid to set
     */
    public void setFirstgid(int firstgid) {
        this.firstgid = firstgid;
    }

    /**
     * @return the lastgid
     */
    public int getLastgid() {
        return lastgid;
    }

    /**
     * @param lastgid the lastgid to set
     */
    public void setLastgid(int lastgid) {
        this.lastgid = lastgid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the tile_width
     */
    public int getTile_width() {
        return tile_width;
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

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the image_width
     */
    public int getImage_width() {
        return image_width;
    }

    /**
     * @param image_width the image_width to set
     */
    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    /**
     * @return the image_height
     */
    public int getImage_height() {
        return image_height;
    }

    /**
     * @param image_height the image_height to set
     */
    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    /**
     * @return the tile_amount_width
     */
    public int getTile_amount_width() {
        return tile_amount_width;
    }

    /**
     * @param tile_amount_width the tile_amount_width to set
     */
    public void setTile_amount_width(int tile_amount_width) {
        this.tile_amount_width = tile_amount_width;
    }
    
    
    
}
