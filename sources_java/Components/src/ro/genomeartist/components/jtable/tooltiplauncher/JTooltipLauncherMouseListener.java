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

package ro.genomeartist.components.jtable.tooltiplauncher;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;

/**
 * Clasa ce este responsabila cu afisarea unui tooltip la click
 * @author iulian
 */
public class JTooltipLauncherMouseListener extends MouseAdapter {
    private int targetedColumn;
    private int iconLocation;

    //Constanta pentru dimensiune icoanei
    private static final int ICON_SIZE = 24;


    /**
     * Creez un launcher pentru tooltip. Launcherul este conceput pentru o singura coloana
     * @param column Coloana pe care se inregistreaza
     */
    public JTooltipLauncherMouseListener(int column) {
        this(column, SwingConstants.RIGHT);
    }

    /**
     * Creez un launcher pentru tooltip. Launcherul este conceput pentru o singura coloana
     * @param column        Coloana pe care se inregistreaza
     * @param iconLocation  Locatia icoane <code>SwingConstants.LEFT</code> sau <code>SwingConstants.RIGHT</code>
     */
    public JTooltipLauncherMouseListener(int column, int iconLocation) {
        this.targetedColumn = column;
        this.iconLocation = iconLocation;
    }

    /**
     * Captez doar evenimentul pe apasare mouse
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        if ( e.getSource() instanceof JTable &&
                e.getButton() == MouseEvent.BUTTON1 ) {
            JTable table = (JTable) e.getSource();

            //Test area
            int row = table.rowAtPoint(e.getPoint());
            int col = table.columnAtPoint(e.getPoint());

            //System.out.println("cell = ["+row+","+col+"]");
            if (row != -1 && col == targetedColumn ) {
                //System.out.println("Click = ["+e.getX()+","+e.getY()+"]");
                Rectangle rect = table.getCellRect(row, col, false);
                //System.out.println("Rect start = ["+rect.getX()+","+rect.getY()+"]");
                //System.out.println("Rect end = ["+(rect.getX()+rect.getWidth())+","
                //        +(rect.getY()+rect.getHeight())+"]");

                //Calculez daca clickul a fost in zona tinta
                int x_difference;   //Diferenta intre marginea celulei si click
                int y_difference;
                switch (iconLocation) {
                    case SwingConstants.RIGHT:
                        x_difference = (int) (rect.getX()+rect.getWidth() - e.getX());
                        break;
                    case SwingConstants.LEFT:
                        x_difference = (int) (e.getX() - rect.getX());
                        break;
                    default:
                        x_difference = 0;
                        break;
                }

                //Lansez tooltipul
                if (x_difference < ICON_SIZE) {
                    final MouseEvent myMouseEvent = new MouseEvent(table, 0, 0, 0,
                                    e.getX(), e.getY(), // X-Y of the mouse for the tool tip
                                    0, false);
                    
                    //System.out.println(table.getToolTipText(myMouseEvent));

                    int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();
                    ToolTipManager.sharedInstance().setInitialDelay(0);
                    ToolTipManager.sharedInstance().mouseMoved(myMouseEvent );
                    ToolTipManager.sharedInstance().setInitialDelay(initialDelay);
                }
            }
        }
    }
}
