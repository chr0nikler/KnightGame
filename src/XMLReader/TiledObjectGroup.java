/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLReader;

import java.util.Vector;

/**
 *
 * @author JSC
 */
public abstract class TiledObjectGroup{
    
    private String name;
    private int width;
    private int height;
    private Vector group = new Vector<ObjectDetails>();
    
    
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
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the group
     */
    public Vector getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(Vector group) {
        this.group = group;
    }


    
    
}
