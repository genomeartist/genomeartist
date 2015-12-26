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

package ro.genomeartist.components.coloredtable;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author iulian
 */
public abstract class AbstractColoredTableModel extends AbstractTableModel {

    /**
     * Obtine scara la care se rotunjeste un BigDecimal
     * @return
     */
    public int getDecimalScale() {
        return 2;
    }

    /**
     * Obtine tooltip-textul pentru o anumita celula
     * @param row
     * @return
     */
    public String getTooltipText(int row, int col) {
        //Comporatamentul implicit este sa se intorca null
        return null;
    }

    /**
     * Obtin iconul celulei
     * @param row
     * @param vol
     * @return 
     */
    public Icon getCellIcon(int row, int col) {
        return null;
    }
    
    /**
     * Obtine culoarea standard de background
     * @param row Randul pentru care se obtine culoarea
     * @return Culoarea de fundal
     */
    public abstract Color getBackground(int row, int col);

    /**
     * Obtine culoarea standard pentru scris
     * @param row Randul pentru care se obtine culoarea
     * @return Culoarea pentru scris
     */
    public abstract Color getForeground(int row, int col);
}
