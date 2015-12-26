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

package ro.genomeartist.coloredtable.filter;

import ro.genomeartist.components.textfield.fixedwidth.JFixedWidthTextField;
import ro.genomeartist.components.toolbar.JConfiguredToolbar;
import ro.genomeartist.components.toolbar.JToolbarButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author iulian
 */
public class JRegexFilterPanel extends JConfiguredToolbar implements ActionListener {
    //Soretrul pe care se aplica
    private TableRowSorter<TableModel> tableSorter;
    private int[] indices;

    //Actiunea
    private static final String ACTION_SEARCH= "search";
    private static final String ACTION_RESET_FILTRU = "reset_filtru";

    //Structura panoului
    private JLabel labelFiltru;
    private JTextField textfieldFiltru;
    private JButton buttonResetFiltru;


    /**
     * Construiesc un panoul filtru
     * @param tableSorter
     */
    private JRegexFilterPanel(TableRowSorter<TableModel> tableSorter, int[] indices) {
        super();
        this.tableSorter = tableSorter;
        this.indices = indices;

        labelFiltru = new JLabel("Filtru ");
        this.add(Box.createHorizontalStrut(15));
        this.add(labelFiltru);
        this.add(Box.createHorizontalStrut(5));

        textfieldFiltru = new JFixedWidthTextField(200);
            textfieldFiltru.getDocument().addDocumentListener(filtruDocumentListener);
        this.add(textfieldFiltru);

        buttonResetFiltru = new JToolbarButton();
        buttonResetFiltru.setActionCommand(ACTION_RESET_FILTRU);
        buttonResetFiltru.setToolTipText("Reseteaza filtru");
        buttonResetFiltru.addActionListener(this);
        buttonResetFiltru.setIcon(JFilterIcons.RESET.getIcon());

        this.add(Box.createHorizontalStrut(5));
        this.add(buttonResetFiltru);
        
        //Mapez actiunea de ENTER pe textfield
        initKeyBindings();
    }


    /**
     * Singura metoda de obtinere a Filtereului
     * @param tableSorter
     * @param indices
     * @return
     */
    public static JRegexFilterPanel getRegexFilterPanel(TableRowSorter<TableModel> tableSorter,
                                                               int... indices) {
        return new JRegexFilterPanel(tableSorter,indices);
    }

    /**
     * Fac key bindingurile pentru tabel
     */
    private void initKeyBindings() {

        //   Maparea actiunilor pe nume
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ActionMap map = textfieldFiltru.getActionMap();
        //Wrapper peste actiunea de stergere
        Action actionSearch = new AbstractAction(ACTION_SEARCH) {
            public void actionPerformed(ActionEvent e) {
                //Do nothing
            }
        };
        map.put(actionSearch.getValue(Action.NAME),
                actionSearch);

        //   Maparea numelor pe taste
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        InputMap imap = textfieldFiltru.getInputMap();
        imap.put(KeyStroke.getKeyStroke("ENTER"),
            actionSearch.getValue(Action.NAME));
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Metode pentru actiuni
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Controllerul pentru JPAnel-ul de sus
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (ACTION_RESET_FILTRU.equals(cmd)) {
            fireActionResetFiltru();
        }
    }

    /**
     * Lansez actiunea de resetare filtru
     */
    public void fireActionResetFiltru() {
        textfieldFiltru.setText("");
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Definitia de clase interne
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Actiunea filtrului pe extrasul de materiale
     */
    private DocumentListener filtruDocumentListener = new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            filterRows();
        }

        public void removeUpdate(DocumentEvent e) {
            filterRows();
        }

        public void changedUpdate(DocumentEvent e) {
            filterRows();
        }

        private void filterRows() {
            String text = textfieldFiltru.getText();
            if (text.length() == 0) {
             tableSorter.setRowFilter(null);
            } else {
             try {
               tableSorter.setRowFilter(
                   RegexRowFilter.getRegexFilter(text,indices));
                } catch (PatternSyntaxException pse) {
                    System.err.println("Bad regex pattern");
                }
            }
        }
    };
}
