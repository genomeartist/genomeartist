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
package ro.genomeartist.components.jtable.configuredtable;

import ro.genomeartist.components.jtable.viewportcontainer.IViewportAdditionalContainer;
import java.awt.Component;
import java.awt.Container;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Clasa reprezinta tabelul de baza care se extinde.
 * Acesta este configurat cu optiuniile cu anumite optiuni default
 * @author Iulian
 */
public abstract class JExenneAbstractTable extends JTable {

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Constructori
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Contructor
     * @param rowData
     * @param columnNames 
     */
    public JExenneAbstractTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    /**
     * Contructor
     * @param rowData
     * @param columnNames 
     */
    public JExenneAbstractTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
    }

    /**
     * Contructor
     * @param rowData
     * @param columnNames 
     */
    public JExenneAbstractTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    /**
     * Contructor
     * @param rowData
     * @param columnNames 
     */
    public JExenneAbstractTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    /**
     * Contructor
     * @param rowData
     * @param columnNames 
     */
    public JExenneAbstractTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    /**
     * Contructor
     * @param rowData
     * @param columnNames 
     */
    public JExenneAbstractTable(TableModel dm) {
        super(dm);
    }

    /**
     * Contructor
     * @param rowData
     * @param columnNames 
     */
    public JExenneAbstractTable() {
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Suprascrierea JTable pentru 
     *     a suporta JTableBottomSpacingContainer
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Notification from the <code>UIManager</code> that the L&F has changed.
     * Replaces the current UI object with the latest version from the
     * <code>UIManager</code>.
     *
     * @see JComponent#updateUI
     */
    @Override
    public void updateUI() {
        // Update UI applied to parent ScrollPane
        configureEnclosingScrollPaneUI();
        
        //Call to super
        super.updateUI();
    }    
    
    /**
     * If this <code>JTable</code> is the <code>viewportView</code> of an enclosing <code>JScrollPane</code>
     * (the usual situation), configure this <code>ScrollPane</code> by, amongst other things,
     * installing the table's <code>tableHeader</code> as the <code>columnHeaderView</code> of the scroll pane.
     * When a <code>JTable</code> is added to a <code>JScrollPane</code> in the usual way,
     * using <code>new JScrollPane(myTable)</code>, <code>addNotify</code> is
     * called in the <code>JTable</code> (when the table is added to the viewport).
     * <code>JTable</code>'s <code>addNotify</code> method in turn calls this method,
     * which is protected so that this default installation procedure can
     * be overridden by a subclass.
     *
     * @see #addNotify
     */
    @Override
    public void configureEnclosingScrollPane() {
        JScrollPane scrollPane = getValidEnclosingScrollPane();
        
        //Identific scrollpane-ul
        if (scrollPane != null) {
            scrollPane.setColumnHeaderView(getTableHeader());
            // configure the scrollpane for any LAF dependent settings
            configureEnclosingScrollPaneUI();
        }
    }
    
    /**
     * This is a sub-part of configureEnclosingScrollPane() that configures
     * anything on the scrollpane that may change when the look and feel
     * changes. It needed to be split out from configureEnclosingScrollPane() so
     * that it can be called from updateUI() when the LAF changes without
     * causing the regression found in bug 6687962. This was because updateUI()
     * is called from the constructor which then caused
     * configureEnclosingScrollPane() to be called by the constructor which
     * changes its contract for any subclass that overrides it. So by splitting
     * it out in this way configureEnclosingScrollPaneUI() can be called both
     * from configureEnclosingScrollPane() and updateUI() in a safe manor.
     */
    private void configureEnclosingScrollPaneUI() {
        JScrollPane scrollPane = getValidEnclosingScrollPane();
        
        //Identific scrollpane-ul
        if (scrollPane != null) {
            //  scrollPane.getViewport().setBackingStoreEnabled(true);
            Border border = scrollPane.getBorder();
            if (border == null || border instanceof UIResource) {
                Border scrollPaneBorder =
                    UIManager.getBorder("Table.scrollPaneBorder");
                if (scrollPaneBorder != null) {
                    scrollPane.setBorder(scrollPaneBorder);
                }
            }
            // add JScrollBar corner component if available from LAF and not already set by the user
            Component corner =
                    scrollPane.getCorner(JScrollPane.UPPER_TRAILING_CORNER);
            if (corner == null || corner instanceof UIResource){
                corner = null;
                Object componentClass = UIManager.get(
                        "Table.scrollPaneCornerComponent");
                if (componentClass instanceof Class){
                    try {
                        corner = (Component)
                                ((Class)componentClass).newInstance();
                    } catch (Exception e) {
                        // just ignore and don't set corner
                    }
                }
                scrollPane.setCorner(JScrollPane.UPPER_TRAILING_CORNER,
                        corner);
            }
        }
    }
    
    /**
     * Reverses the effect of <code>configureEnclosingScrollPane</code>
     * by replacing the <code>columnHeaderView</code> of the enclosing
     * scroll pane with <code>null</code>. <code>JTable</code>'s
     * <code>removeNotify</code> method calls
     * this method, which is protected so that this default uninstallation
     * procedure can be overridden by a subclass.
     *
     * @see #removeNotify
     * @see #configureEnclosingScrollPane
     * @since 1.3
     */
    @Override
    public void unconfigureEnclosingScrollPane() {
        JScrollPane scrollPane = getValidEnclosingScrollPane();
        
        //Identific scrollpane-ul
        if (scrollPane != null) {
            scrollPane.setColumnHeaderView(null);
            // remove ScrollPane corner if one was added by the LAF
            Component corner =
                    scrollPane.getCorner(JScrollPane.UPPER_TRAILING_CORNER);
            if (corner instanceof UIResource){
                scrollPane.setCorner(JScrollPane.UPPER_TRAILING_CORNER, 
                        null);
            }
        }
    }
    
    /**
     * Incearca obtinerea scrollpane-ului parinte
     * @return null daca nu s-a gasit un scroll pane valid
     */
    private JScrollPane getValidEnclosingScrollPane() {
        Container p = getParent();
        Container viewportAsContainer = null;
        
        //Identific viewportul
        if (p instanceof JViewport) {
            viewportAsContainer = p;
        } else
        if (p instanceof IViewportAdditionalContainer) {
            Container pp = p.getParent();
            if (pp instanceof JViewport) {
                viewportAsContainer = pp;
            }
        }
        
        //Identific scrollpane-ul
        if (viewportAsContainer != null) {
            Container ppp = viewportAsContainer.getParent();
            if (ppp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane)ppp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null) {
                    return null;
                } else 
                if (viewport != null) {
                    if (!(viewport.getView() instanceof IViewportAdditionalContainer))
                        if (viewport.getView() != this)
                            return null;
                }
                
                //Daca am gasit un scrollpane valid
                return scrollPane;
            }
        }
        
        //Valoare default
        return null;
    }
}
