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
public class CollisionGroup extends TiledObjectGroup {
    
    CollisionGroup(String name,int width, int height,Vector group){
        setName(name);
        setWidth(width);
        setHeight(height);
        setGroup(group);
        
    }
    
}
