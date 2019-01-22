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
public class JToolbarFereastraIcons implements IGlobalClass {
    public static final String GLOBAL_NAME = "JToolbarFereastraIcons";

    //Constante pentru tipuri
    public static final int LOAD = 2;
    public static final int SAVE = 1;
    public static final int SAVEAS = 3;
    public static final int SEARCH = 4;
    public static final int SETTINGS = 5;
    public static final int EXPORT = 6;

    //Icoane
    private Image iconLoad;
    private Image iconSave;
    private Image iconSaveas;
    private Image iconSearch;
    private Image iconSettings;
    private Image iconExport;

    private ImageIcon imageIconLoad;
    private ImageIcon imageIconSave;
    private ImageIcon imageIconSaveas;
    private ImageIcon imageIconSearch;
    private ImageIcon imageIconSettings;
    private ImageIcon imageIconExport;

    //In caz ca nu am icon
    private Icon noIcon;

    public JToolbarFereastraIcons() {
        //Icoanele folosite
        noIcon = null;
        loadIcons();
    }

    /**
     * Incarc imaginile pentru icoane
     */
    public void loadIcons() {
        String iconsFolder,iconsPath;
        iconsFolder = ReadOnlyConfiguration.getString("TOOLBAR_FEREASTRA_ICONS_FOLDER");

         //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FEREASTRA_ICONS_LOAD");
        iconLoad = MyUtils.createImage(iconsPath);
        imageIconLoad =  new ImageIcon(iconLoad);       
        
        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FEREASTRA_ICONS_SAVE");
        iconSave = MyUtils.createImage(iconsPath);
        imageIconSave =  new ImageIcon(iconSave);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FEREASTRA_ICONS_SAVEAS");
        iconSaveas = MyUtils.createImage(iconsPath);
        imageIconSaveas =  new ImageIcon(iconSaveas);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FEREASTRA_ICONS_SEARCH");
        iconSearch = MyUtils.createImage(iconsPath);
        imageIconSearch =  new ImageIcon(iconSearch);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FEREASTRA_ICONS_SETTINGS");
        iconSettings = MyUtils.createImage(iconsPath);
        imageIconSettings =  new ImageIcon(iconSettings);
        
        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FEREASTRA_ICONS_EXPORT");
        iconExport = MyUtils.createImage(iconsPath);
        imageIconExport =  new ImageIcon(iconExport);
    }
    
    /**
     * Obtine iconul pentru resursa specificata
     * @param type      Tipul resursei
     * @return Iconul pentru resursa sau noIcon
     */
    public Icon getIcon(int type) {
        switch (type) {
            case (SAVE):
                if (iconSave != null) {
                    return imageIconSave;
                } else return noIcon;
            case (LOAD):
                if(iconLoad != null) {
                    return imageIconLoad;
                } else return noIcon;
            case (SAVEAS):
                if(iconSaveas != null) {
                    return imageIconSaveas;
                } else return noIcon;
            case (SEARCH):
                if(iconSearch != null) {
                    return imageIconSearch;
                } else return noIcon;
            case (SETTINGS):
                if(iconSaveas != null) {
                    return imageIconSettings;
                } else return noIcon;
            case (EXPORT):
                if(iconExport != null) {
                    return imageIconExport;
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
