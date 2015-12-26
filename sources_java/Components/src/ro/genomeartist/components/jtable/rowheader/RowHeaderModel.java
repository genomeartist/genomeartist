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
package ro.genomeartist.components.jtable.rowheader;

import java.util.HashMap;
import javax.swing.AbstractListModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Un model de lista ce se conecteaza cu un table model.
 * Acest model poate fi vazut ca o extensie a table modelului
 * @author iulian
 */
public abstract class RowHeaderModel extends AbstractListModel implements TableModelListener {
    private TableModel tabelModel;

    //Un cache temporar pentru a mentine setarile pentru ciclare noduri
    private HashMap<Integer, RowHeaderCellData> nodeCache;
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *       Constructor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Constructorul principal
     * @param tabelModel 
     */
    public RowHeaderModel(TableModel tabelModel) {
        this.tabelModel = tabelModel;
        tabelModel.addTableModelListener(this);
        
        //Initializez cache-ul de noduri
        nodeCache = new HashMap<Integer, RowHeaderCellData>();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Implementare AbstractListModel
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Obtin marimea de la tabel
     * @return 
     */
    @Override
    public int getSize() {
        return tabelModel.getRowCount();
    }

    /**
     * Obtin elementul din tabel
     * @param i
     * @return 
     */
    @Override
    public Object getElementAt(int i) {
        RowHeaderCellData value;
        
        //Incerc obtinerea din cache
        value = nodeCache.get(i);
        if (value != null) {
            return value;
        }
        
        //Preiau din model
        return getListCellData(tabelModel.getValueAt(i, 0));
    }

    /**
     * Metoda ce se suprascrie pentru a oferi componenta proprie spre desenare
     * @param tableObject
     * @return 
     */
    public abstract RowHeaderCellData getListCellData(Object tableObject);

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Implementare TableModelListener
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Propag modificarile din tabel in lista
     * @param tme 
     */
    public void tableChanged(TableModelEvent tme) {
        int type = tme.getType();
        switch (type) {
            case TableModelEvent.INSERT:
                nodeCache.clear();
                this.fireIntervalAdded(tme.getSource(), 
                        tme.getFirstRow(), tme.getLastRow());
                break;
            case TableModelEvent.DELETE:
                nodeCache.clear();
                this.fireIntervalRemoved(tme.getSource(), 
                        tme.getFirstRow(), tme.getLastRow());
                break;
            case TableModelEvent.UPDATE:
                nodeCache.clear();
                this.fireContentsChanged(tme.getSource(), 
                        tme.getFirstRow(), tme.getLastRow());
                break;
            default:
                break;                
        }
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *          Actiuni publice
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Cicleaza anotatiile de la un anumit rand
     * @param row 
     */
    public void cycleAnnotations(int row) {
        RowHeaderCellData cellData = (RowHeaderCellData) getElementAt(row);
        if (cellData.cycleAnnotations()) {
            //Memorez nodul
            nodeCache.put(row, cellData);
            
            //Lansez evenimentul
            this.fireContentsChanged(this, row, row);
        }
    }

}
