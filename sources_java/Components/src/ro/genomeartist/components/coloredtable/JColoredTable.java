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

import ro.genomeartist.coloredtable.cellrenderers.AbstractColoredCellRenderer;
import ro.genomeartist.coloredtable.cellrenderers.DefaultColoredCellRenderer;
import ro.genomeartist.components.jtable.configuredtable.JExenneAbstractTable;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author iulian
 */
public class JColoredTable extends JExenneAbstractTable {

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Constructori
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Construiesc un tabel care rendeaza randurile prietenos
     * @param dm 
     */
    public JColoredTable(AbstractColoredTableModel dm) {
        super(dm);
        AbstractColoredCellRenderer renderer = new DefaultColoredCellRenderer();
        setDefaultCellRenderer(renderer);
        
        //Inregistrez ascultator de mouse
        this.addMouseListener(mouseListener);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Getteri publici
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Obtine modelul din spate
     * @return
     */
    @Override
    public AbstractColoredTableModel getModel() {
        return (AbstractColoredTableModel) super.getModel();
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Setteri publici
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Seteaza un cellrenderer default
     */
    public void setDefaultCellRenderer(AbstractColoredCellRenderer renderer) {
        setDefaultRenderer(Object.class,
                renderer);
        setDefaultRenderer(BigDecimal.class,
                renderer);
        setDefaultRenderer(Integer.class,
                renderer);
        setDefaultRenderer(String.class,
                renderer);
    }

    /**
     * Seteaza un cellrenderer default
     * @param columnClass
     * @param renderer
     */
    @Override
    public void setDefaultRenderer(Class<?> columnClass, TableCellRenderer renderer) {
        if (renderer instanceof AbstractColoredCellRenderer)
            super.setDefaultRenderer(columnClass, renderer);
        else return;
    }

    /**
     * Seteaza modelul din spate
     * @param dataModel
     */
    @Override
    public void setModel(TableModel dataModel) {
        if (dataModel instanceof AbstractColoredTableModel)
            super.setModel(dataModel);
        else return;
    }

    /**
     * Seteaza modelul din spate
     * @param dataModel
     */
    public void setModel(AbstractColoredTableModel dataModel) {
        super.setModel(dataModel);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Actiuni publice
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Selecteaza un rand
     */
    public void selectRow(int row) {
        //Select the row
        int numberOfRows = this.getRowCount();
        if (row >= 0 && row < numberOfRows) {
            this.addRowSelectionInterval(row, row);
        }
        if (row >= 0 && row < numberOfRows) {
            this.addRowSelectionInterval(row, row);
        }
    }

    /**
     * Selecteaza si fa scroll la randul selectat
     * @param row
     */
    public void selectAndScrollToRow(int row) {
        int numberOfRows = this.getRowCount();
        if (row >= 0 && row < numberOfRows) {
            this.addRowSelectionInterval(row, row);
            Rectangle rectangle = this.getCellRect(row, 0, true);
            this.scrollRectToVisible(rectangle);
        }
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Clase interne
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Ascultator pentru extindere la click mijloc
     */
    private MouseListener mouseListener = new MouseAdapter() {
        
        /**
         * Metoda apelata la clickul de mouse
         */
        @Override
        public void mousePressed(MouseEvent e) {
            maybeDisableSelection(e);
        }

        /**
         * Daca nu e selectat nimic deselectez elementele selectate
         */
        private void maybeDisableSelection(MouseEvent e) {
            //Identifica randul
            int row = JColoredTable.this.rowAtPoint(e.getPoint());
            if(row == -1)
                JColoredTable.this.clearSelection();
        }
    };
    
}
