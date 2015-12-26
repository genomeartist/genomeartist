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

package ro.genomeartist.gui.cellrenderers;

import ro.genomeartist.coloredtable.cellrenderers.DefaultColoredCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 *
 * @author iulian
 */
public class JTransposonDefaultCellRenderer extends DefaultColoredCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //Apelez in super
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);

        //Aliniez central
        setVerticalAlignment(SwingConstants.CENTER);
        return this;
    }
}
