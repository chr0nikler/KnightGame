package XMLReader;

import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * @author Nicholas Mamo - Nyphoon Games
 */

public class SpriteEngine {
    
    private ArrayList<SpriteLibrary> e_libraries = new ArrayList<SpriteLibrary>();
    
    public SpriteEngine(){
    }
    
    public void addLibrary(SpriteLibrary library){
        SpriteLibrary.getL_guiNode().attachChild(library.getNode());
        this.e_libraries.add(library);
    }
    
    public void removeLibrary(String name){
        SpriteLibrary.getL_guiNode().detachChildNamed(getLibrary(name).getName());
    }
    
    public SpriteLibrary getLibrary(String name){
        for (int i = 0; i < e_libraries.size(); i++){
            if (this.e_libraries.get(i).getName().equals(name)){
                return this.e_libraries.get(i);
            }
        }
        return null;
    }
    
    public void update(float tpf){
        for (int l = 0; l < e_libraries.size(); l++){
            for (int s = 0; s < e_libraries.get(l).getLibrary().size(); s++){
                if (!e_libraries.get(l).getStatic()){
                    Sprite t_sprite = e_libraries.get(l).getSprite(s);
                    t_sprite.setTime(t_sprite.getTime() + tpf);
                    if (!t_sprite.getPaused() && t_sprite.getAnimated()){
                        if (t_sprite.getTime() >= t_sprite.getTimeSeparation()){
                            if (t_sprite.getCurrentFrame() < (t_sprite.getFrames() - 1)){
                                if (t_sprite.getOnEnd().equals("Scroll")){
                                    for (int a = 0; a < t_sprite.getVertexArray().length; a++) {
                                        if (a%2 == 0){
                                            if (t_sprite.getVertexArray()[a] + (int)Math.pow(-1, t_sprite.getDirection())*(1f/t_sprite.getFrames()) <= 1){
                                                t_sprite.updateVertexArray(a, t_sprite.getVertexArray()[a] + (int)Math.pow(-1, t_sprite.getDirection())*(1f/t_sprite.getFrames()));
                                            } else if (a >= 4){
                                                t_sprite.setCurrentFrame(t_sprite.getFrames());
                                            }
                                        }
                                    }
                                    t_sprite.getVertexBuffer().updateData(BufferUtils.createFloatBuffer(t_sprite.getVertexArray()));
                                    t_sprite.setTime(0);
                                } else {
                                    for (int a = 0; a < t_sprite.getVertexArray().length; a++) {
                                        if (a%2 == 0){
                                            t_sprite.updateVertexArray(a, t_sprite.getVertexArray()[a] + (int)Math.pow(-1, t_sprite.getDirection())*(1f/t_sprite.getFrames()));
                                        }
                                    }         
                                    t_sprite.getVertexBuffer().updateData(BufferUtils.createFloatBuffer(t_sprite.getVertexArray()));
                                    t_sprite.setTime(0);
                                    t_sprite.setCurrentFrame(t_sprite.getCurrentFrame() + 1);
                                }
                            } else if (t_sprite.getOnEnd().equals("Loop")){
                                for (int a = 0; a < t_sprite.getVertexArray().length; a++) {
                                    if (a%2 == 0){
                                        t_sprite.updateVertexArray(a, t_sprite.getVertexArray()[a] - ((float)(t_sprite.getFrames()-1)/t_sprite.getFrames()));
                                    }
                                }                        
                                t_sprite.getVertexBuffer().updateData(BufferUtils.createFloatBuffer(t_sprite.getVertexArray()));
                                t_sprite.setTime(0);
                                t_sprite.setCurrentFrame(0);
                            } else if (t_sprite.getOnEnd().equals("NoLoop")){
                                for (int a = 0; a < t_sprite.getVertexArray().length; a++) {
                                    if (a%2 == 0){
                                        t_sprite.updateVertexArray(a, t_sprite.getVertexArray()[a] - ((float)(t_sprite.getFrames()-1)/t_sprite.getFrames()));
                                    }
                                }                        
                                t_sprite.getVertexBuffer().updateData(BufferUtils.createFloatBuffer(t_sprite.getVertexArray()));
                                t_sprite.setPaused(true);
                                t_sprite.setTime(0);
                                t_sprite.setCurrentFrame(0);
                            } else if (t_sprite.getOnEnd().equals("Reverse")){
                                t_sprite.changeDirection();
                                for (int a = 0; a < t_sprite.getVertexArray().length; a++) {
                                    if (a%2 == 0){
                                        t_sprite.updateVertexArray(a, t_sprite.getVertexArray()[a] + (int)Math.pow(-1, t_sprite.getDirection())*(1f/t_sprite.getFrames()));
                                    }
                                }         
                                t_sprite.getVertexBuffer().updateData(BufferUtils.createFloatBuffer(t_sprite.getVertexArray()));
                                t_sprite.setTime(0);
                                t_sprite.setCurrentFrame(1);
                            } else if (t_sprite.getOnEnd().equals("Scroll")){
                                for (int a = 0; a < t_sprite.getVertexArray().length; a++) {
                                    if (a == 0 || a == 6){
                                        t_sprite.updateVertexArray(a, 0);
                                    } else if (a%2 == 0){
                                        t_sprite.updateVertexArray(a, (1f/t_sprite.getFrameWidth()));
                                    }
                                }
                                t_sprite.getVertexBuffer().updateData(BufferUtils.createFloatBuffer(t_sprite.getVertexArray()));
                                t_sprite.setTime(0);
                                t_sprite.setCurrentFrame(0);
                            }
                        }
                    }
                }
            }
        }
    }
    
}