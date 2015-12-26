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
public class JToolbarFisiereIcons implements IGlobalClass {
    public static final String GLOBAL_NAME = "JToolbarFisiereIcons";

    //Constante pentru tipuri
    public static final int ADD_FOLDER = 1;
    public static final int ADD_FILE = 2;
    public static final int DELETE = 3;
    public static final int NEW = 4;

    //Icoane
    private Image iconAddFolder;
    private Image iconAddFile;
    private Image iconDelete;
    private Image iconNew;

    private ImageIcon imageIconAddFolder;
    private ImageIcon imageIconAddFile;
    private ImageIcon imageIconDelete;
    private ImageIcon imageIconNew;

    //In caz ca nu am icon
    private Icon noIcon;

    public JToolbarFisiereIcons() {
        //Icoanele folosite
        noIcon = null;
        loadIcons();
    }

    /**
     * Incarc imaginile pentru icoane
     */
    public void loadIcons() {
        String iconsFolder,iconsPath;
        iconsFolder = ReadOnlyConfiguration.getString("TOOLBAR_FISIERE_ICONS_FOLDER");

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FISIERE_FOLDER");
        iconAddFolder = MyUtils.createImage(iconsPath);
        imageIconAddFolder =  new ImageIcon(iconAddFolder);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FISIERE_CREEAZA");
        iconAddFile = MyUtils.createImage(iconsPath);
        imageIconAddFile =  new ImageIcon(iconAddFile);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FISIERE_STERGE");
        iconDelete = MyUtils.createImage(iconsPath);
        imageIconDelete =  new ImageIcon(iconDelete);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FISIERE_NEW");
        iconNew = MyUtils.createImage(iconsPath);
        imageIconNew =  new ImageIcon(iconNew);
    }
    
    /**
     * Obtine iconul pentru resursa specificata
     * @param type      Tipul resursei
     * @return Iconul pentru resursa sau noIcon
     */
    public Icon getIcon(int type) {
        switch (type) {
            case (ADD_FOLDER):
                if (iconAddFolder != null) {
                    return imageIconAddFolder;
                } else return noIcon;
            case (ADD_FILE):
                if (iconAddFile != null) {
                    return imageIconAddFile;
                } else return noIcon;
            case (DELETE):
                if(iconDelete != null) {
                    return imageIconDelete;
                } else return noIcon;
            case (NEW):
                if(iconNew != null) {
                    return imageIconNew;
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