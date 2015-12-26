/*
 *
 * This file is part of Genome Artist.
 *
 * Genome Artist is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Genome Artist is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Genome Artist.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package ro.genomeartist.components.utils;

import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author iulian
 */
public class ComponentsUtils {

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Component modifications
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
            
    /**
     * Creez un obiect imagine pornind de la cale
     * @param imagePath
     * @return null daca calea nu este corecta
     */
    public static BufferedImage loadImage(String imagePath) {
        //Look for the image.
        BufferedImage result = null;
        URL imageURL = null;
        String imgLocation = null;
        if (imagePath != null) {
            imgLocation = imagePath;
            imageURL = ComponentsUtils.class.getResource(imgLocation);
            if (imageURL != null) {
                try {
                    result = ImageIO.read(imageURL);
                } catch (IOException ex) {
                    Logger.getLogger(ComponentsUtils.class.getName())
                            .log(Level.WARNING, "IO error", ex);
                }
            } else {
                Logger.getLogger(ComponentsUtils.class.getName()).log(Level.WARNING, 
                        "File: {0} not found !", imagePath);
            }
        }

        return result;
    }

    /**
     * Modifica fontul de la un label si il face bold
     * @param component
     */
    public static void setFontBold(Component component) {
        Font oldFont = component.getFont();
        if (oldFont != null)
            component.setFont(oldFont.deriveFont(Font.BOLD));
    }

    /**
     * Modifica fontul de la un label si il face bold
     * @param component
     */
    public static void setFontPlain(Component component) {
        Font oldFont = component.getFont();
        if (oldFont != null)
            component.setFont(oldFont.deriveFont(Font.PLAIN));
    }

    /**
     * Modifica fontul de la un label si il face mai mare
     * @param component
     */
    public static void increaseFontSize(JComponent component,int offset) {
        Font oldFont = component.getFont();
        if (oldFont != null)
            component.setFont(oldFont.deriveFont(oldFont.getStyle(), oldFont.getSize()+offset));
    }

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Elaborate getters
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Obtine actiunea inregistrata pentru apasarea de tasta
     * @return actiune inregistrata pentru tasta sau null
     */
    public static Action getActionForKeystroke(JComponent component, KeyStroke keyStroke) {
        Action whenFocused = getActionForKeystroke(component, 
                JComponent.WHEN_FOCUSED, keyStroke);
        Action whenAncestorOfFocused = getActionForKeystroke(component, 
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, keyStroke);
        Action whenInWindow = getActionForKeystroke(component, 
                JComponent.WHEN_IN_FOCUSED_WINDOW, keyStroke);
        
        //Ordinea preferata pentru a le returna
        if (whenFocused != null)
            return whenFocused;
        if (whenAncestorOfFocused != null)
            return whenAncestorOfFocused;
        if (whenInWindow != null)
            return whenInWindow;
        
        //Valoare default
        return null;
    }
 
    /**
     * Obtine actiunea inregistrata pentru apasarea de tasta
     * @return 
     */
    public static Action getActionForKeystroke(JComponent component, int inputMapId, KeyStroke keyStroke) {
        //Identify the action key
        InputMap inputMap = component.getInputMap(inputMapId);
        String key = (String) inputMap.get(keyStroke);

        //Get the action
        if (key != null) {
            ActionMap localMap = component.getActionMap();
            return localMap.get(key);
        } else {
            return null;
        }
    }
}
