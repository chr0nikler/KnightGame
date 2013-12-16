/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;
import mygame.Main;

/**
 *
 * @author JSC
 */
public class MainMenuState extends AbstractAppState {
    
    AppStateManager stateManager;
    SimpleApplication app;
    Screen screen;
    Node guiNode;
    Node rootNode;
           
    private boolean start_game = false;
    
    @Override
    public void initialize(final AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.stateManager = stateManager;
        this.app = (SimpleApplication)app;
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        System.out.println(this.app + " MAINMENU");
        screen = new Screen(this.app);
        guiNode.addControl(screen);
        
        ButtonAdapter start_button = new ButtonAdapter(screen,"panel", new Vector2f(15, 15)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                
                start_game = true;
                               
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };
        start_button.setText("hello");
        //panel.hide();
        screen.addElement(start_button);
        
        app.getInputManager().setCursorVisible(true);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        if(start_game){
            stateManager.detach(Main.main_menu_state);
            stateManager.attach(Main.level_state);
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        guiNode.removeControl(screen);
        guiNode.detachAllChildren();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
