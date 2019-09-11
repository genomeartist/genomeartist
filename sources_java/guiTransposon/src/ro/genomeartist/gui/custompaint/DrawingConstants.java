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

package ro.genomeartist.gui.custompaint;

import java.awt.Color;

/**
 *
 * @author iulian
 */
public class DrawingConstants {
     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Versiunea programului
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static final String APP_VERSION = "Genome ARTIST 1.19";
    public static final String APP_NAME = "Genome ARTIST";

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Constante
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    //Culorile standard
    public static final Color COLOR_CHENAR_BORDER = new Color(100,100,100);
    public static final Color COLOR_CHENAR_FILL = new Color(230,230,230);
    //Culoarea intervalului
    public static final Color COLOR_GENOM_BORDER = new Color(0, 0, 200);
    public static final Color COLOR_GENOM_FILL = new Color(200, 200, 255);
    //Culoarea intervalului
    public static final Color COLOR_TRANSPOSON_BORDER = new Color(200, 0, 0);
    public static final Color COLOR_TRANSPOSON_FILL = new Color(255, 200, 200);
    //Culoarea pozitiei de insertie
    public static final Color COLOR_INSERTION_POSITION = new Color(50, 190, 50);
    //Culoarea genelor
    public static final Color COLOR_GENE_BORDER = new Color(0, 200, 0);
    public static final Color COLOR_GENE_FILL = new Color(200, 255, 200);
    //Culoare pentru tabele
    public static final Color COLOR_TABLE_BG_YELLOW = new Color(255, 255, 205);
    public static final Color COLOR_TABLE_BG_BLUE = new Color(230, 230, 255);
    public static final Color COLOR_TABLE_BG_RED = new Color(255, 230, 230);
    public static final Color COLOR_TABLE_BG_GREEN = new Color(230, 255, 230);
    //Constante pentru desenare
    public static final int LINEWIDTH = 2;
    public static final int ARC_WIDTH = 5;
    public static final int ARC_HEIGHT = 5;
    public static final int ARROW_WIDTH = 25;
    public static final int MARGIN_LEFT = 10;
    public static final int MARGIN_RIGHT = 10;
    public static final int MARGIN_TOP = 10;
    public static final int MARGIN_BOTTOM = 10;
    public static final int TEXT_MARKER_HEIGHT = 6;
    public static final int TEXT_MARKER_WIDTH = 4;
    //Constante pentru dimensiuni
    public static final int RECOMMENDED_WIDTH = 300;
    //Constante pentru dimensiuni
    public static final int IMAGE_WIDTH = 800;
    //Constante pentru textul de pe panoul de cautare
    public static final String TEXT_QUERY_ROW1 = "Running query";
    public static final String TEXT_QUERY_ROW2 = "(please wait)";
    public static final String TEXT_SETTINGS_ROW1 = "Applying settings";
    public static final String TEXT_SETTINGS_ROW2 = "(please wait)";
    public static final String TEXT_OPEN_ROW1 = "Opening file";
    public static final String TEXT_OPEN_ROW2 = "(please wait)";
    public static final String TEXT_SAVING_ROW1 = "Saving file";
    public static final String TEXT_SAVING_ROW2 = "(please wait)";
    public static final String EXPORTING_ROW1 = "Processing result";
    public static final String EXPORTING_ROW2 = "(please wait)";
    //Constante pentru directia catenelor
    public static final String TEXT_STRAND_FORWARD = "(+)";
    public static final String TEXT_STRAND_COMPLEMENTARY = "(-)";
    //Constante pentru dimensiuniile raportului
    public static final int REPORT_WIDTH = 555;         //Dimensiunea raporului
    public static final int REPORT_POSITIONING_CELL_HEIGHT = 60;    //Inaltimea unei celule
    public static final int REPORT_SW_CELL_HEIGHT = 90;    //Inaltimea unei celule la sw
    public static final int REPORT_MAPPING_CELL_HEIGHT = 150;    //Inaltimea unei celule
    public static final int REPORT_MONOSPACE_FONT_SIZE = 10;
}
