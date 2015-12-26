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
package ro.genomeartist.components.icons;

import ro.genomeartist.components.utils.ComponentsUtils;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Clasa are singurul scop sa incarce si sa pastreze informatii despre icon
 * @author iulian
 */
public class IconDelegate {
    private BufferedImage image;
    private Icon icon;

    /**
     * Construiesc imaginea unui icon
     * @param path 
     */
    public IconDelegate(String path) {
        image = ComponentsUtils.loadImage(path);
        if (image != null) {
            icon =  new ImageIcon(image);
        } else {
            icon = JNoIcon.ICON16;
        }
    }
    
    
    /**
     * Obtine imaginea ce sta la baza iconului
     * @return 
     */
    public BufferedImage getImage() {
        return image;
    }
    
    /**
     * Obtin iconul reprezentat de delegat
     * @return 
     */
    public Icon getIcon() {
        return icon;
    }
}
