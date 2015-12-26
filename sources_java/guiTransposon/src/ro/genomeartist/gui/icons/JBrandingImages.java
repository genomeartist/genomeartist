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

package ro.genomeartist.gui.icons;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import ro.genomeartist.components.singleton.IGlobalClass;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import ro.genomeartist.gui.utils.MyUtils;
import java.awt.*;
import javax.swing.*;

/**
 * Icon pentru TreeTable
 *
 * @author iulian
 */
public class JBrandingImages implements IGlobalClass {
    public static final String GLOBAL_NAME = "JBrandingImages";

    //Constante pentru tipuri
    public static final int TOOLBAR = 1;

    //Icoane
    private Image iconToolbar;

    private ImageIcon imageIconToolbar;

    //In caz ca nu am icon
    private Icon noIcon;

    public JBrandingImages() {
        //Icoanele folosite
        noIcon = null;
        loadIcons();
    }

    /**
     * Incarc imaginile pentru icoane
     */
    public void loadIcons() {
        String iconsFolder,iconsPath;
        iconsFolder = ReadOnlyConfiguration.getString("BRANDING_FOLDER");

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("ARTIST_36");
        iconToolbar = MyUtils.createImage(iconsPath);
        imageIconToolbar =  new ImageIcon(iconToolbar) {

            @Override
            public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintIcon(c, g, x, y);
            }
        };
    }
    
    /**
     * Obtine iconul pentru resursa specificata
     * @param type      Tipul resursei
     * @return Iconul pentru resursa sau noIcon
     */
    public Icon getIcon(int type) {
        switch (type) {
            case (TOOLBAR):
                if (iconToolbar != null) {
                    return imageIconToolbar;
                } else return noIcon;
            default:
                return noIcon;
        }
    }

    /**
     * Obtine numele global
     * @return Numele global
     */
    public String getGlobalName() {
        return GLOBAL_NAME;
    }
}