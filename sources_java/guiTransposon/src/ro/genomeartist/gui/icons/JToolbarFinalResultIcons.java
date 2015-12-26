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
public class JToolbarFinalResultIcons implements IGlobalClass {
    public static final String GLOBAL_NAME = "JToolbarFinalResultIcons";

    //Constante pentru tipuri
    public static final int EXPORT = 1;
    public static final int EXPORT_IMAGE = 2;
    public static final int EXPORT_PDF = 3;
    public static final int PRINT = 4;
    public static final int BEST_RESULT = 5;

    //Icoane
    private Image iconExport;
    private Image iconExportImage;
    private Image iconExportPdf;
    private Image iconPrint;
    private Image iconBestResult;

    private ImageIcon imageIconExport;
    private ImageIcon imageIconExportImage;
    private ImageIcon imageIconExportPdf;
    private ImageIcon imageIconPrint;
    private ImageIcon imageIconBestResult;

    //In caz ca nu am icon
    private Icon noIcon;

    public JToolbarFinalResultIcons() {
        //Icoanele folosite
        noIcon = null;
        loadIcons();
    }

    /**
     * Incarc imaginile pentru icoane
     */
    public void loadIcons() {
        String iconsFolder,iconsPath;
        iconsFolder = ReadOnlyConfiguration.getString("TOOLBAR_FINAL_RESULT_FOLDER");

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FINAL_RESULT_EXPORT");
        iconExport = MyUtils.createImage(iconsPath);
        imageIconExport =  new ImageIcon(iconExport);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FINAL_RESULT_EXPORT_IMAGE");
        iconExportImage = MyUtils.createImage(iconsPath);
        imageIconExportImage =  new ImageIcon(iconExportImage);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FINAL_RESULT_EXPORT_PDF");
        iconExportPdf = MyUtils.createImage(iconsPath);
        imageIconExportPdf =  new ImageIcon(iconExportPdf);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FINAL_RESULT_PRINT");
        iconPrint = MyUtils.createImage(iconsPath);
        imageIconPrint =  new ImageIcon(iconPrint);

        //Iconul
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("TOOLBAR_FINAL_RESULT_BEST_RESULT");
        iconBestResult = MyUtils.createImage(iconsPath);
        imageIconBestResult =  new ImageIcon(iconBestResult);
    }
    
    /**
     * Obtine iconul pentru resursa specificata
     * @param type      Tipul resursei
     * @return Iconul pentru resursa sau noIcon
     */
    public Icon getIcon(int type) {
        switch (type) {
            case (EXPORT):
                if (iconExport != null) {
                    return imageIconExport;
                } else return noIcon;
            case (EXPORT_IMAGE):
                if(iconExportImage != null) {
                    return imageIconExportImage;
                } else return noIcon;
            case (EXPORT_PDF):
                if(iconExportPdf != null) {
                    return imageIconExportPdf;
                } else return noIcon;
            case (PRINT):
                if(iconPrint != null) {
                    return imageIconPrint;
                } else return noIcon;
            case (BEST_RESULT):
                if(iconBestResult!= null) {
                    return imageIconBestResult;
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