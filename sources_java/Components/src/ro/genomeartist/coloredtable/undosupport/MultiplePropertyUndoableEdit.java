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
package ro.genomeartist.coloredtable.undosupport;

import ro.genomeartist.components.coloredtable.JColoredTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 *
 * @author iulian
 */
public class MultiplePropertyUndoableEdit extends AbstractUndoableEdit {
    //Eveniment de proprietati
    private ActionEvent someAction =
            new ActionEvent(this, 0, "MultiplePropertyUndoableEdit");
    
    //Editarea unei valori in controller
    private JColoredTable table;
    private AbstractTableModel tableModel;
    private int[] modelRows;
    private UndoableEdit controllerAction;
    
    //Ascultatorul de actiune
    private ActionListener actionListener;

    /**
     * Construieste o actiune de undo
     * @param targetNode
     * @param controllerAction
     */
    public MultiplePropertyUndoableEdit(JColoredTable table, int[] modelRows,
            UndoableEdit controllerAction, ActionListener actionListener) {
        this.table = table;
        this.modelRows = modelRows;
        this.controllerAction = controllerAction;
        this.actionListener = actionListener;
        
        //Obtin modelul tabelului
        TableModel modelInterface = table.getModel();
        if (modelInterface instanceof AbstractTableModel) {
            tableModel = (AbstractTableModel) modelInterface;
        }
    }

    /**
     * Executa actualizarea 
     */
    public void notifyTable() {
        notifyTableModel();
    }
    
    /**
     * Executa actualizarea 
     */
    public void notifyListener() {
        notifyExternalListener();
    }
    
    /**
     * Operatia de undo
     * @throws CannotUndoException
     */
    @Override
    public void undo() throws CannotUndoException {
        super.undo();

        //1. Anulez actiunea din controller
        controllerAction.undo();

        //2. Notific modelul meu
        notifyActionPerformed();
    }

    /**
     * Operatia de redo
     * @throws CannotRedoException
     */
    @Override
    public void redo() throws CannotRedoException {
        super.redo();

        //1. Refac acctiunea din controller
        controllerAction.redo();

        //2. Notific modelul meu
        notifyActionPerformed();
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Metode ajutatoare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Notific modelul tabelului
     */
    private void notifyTableModel() {
        if (tableModel != null) {
            for (int i = 0; i < modelRows.length; i++) {
                int j = modelRows[i];
                tableModel.fireTableRowsUpdated(j, j);
            }
        }
    }
    
    /**
     * Notific modelul tabelului
     */
    private void notifyExternalListener() {
        if (actionListener != null)
            actionListener.actionPerformed(someAction);
    }
    
    /**
     * Metoda apelata pentru notificare
     */
    private void notifyActionPerformed() {
        //1. Notific modelul meu
        notifyTableModel();

        //2. Fac selectia nodului care s-a modificat
        if (table != null) {
            table.clearSelection();
            for (int i = 0; i < modelRows.length; i++) {
                int modelRow = modelRows[i];
                int viewRow = table.convertRowIndexToView(modelRow);
                table.selectAndScrollToRow(viewRow);
            }
        }

        //3. Notific modificarea pretului
        notifyExternalListener();
    } 
}
