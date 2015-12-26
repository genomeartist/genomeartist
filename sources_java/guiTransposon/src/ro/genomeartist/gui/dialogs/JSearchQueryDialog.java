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

package ro.genomeartist.gui.dialogs;
import java.util.LinkedList;
import java.util.List;
import ro.genomeartist.components.dialogs.JTwoButtonAbstractDialog;
import ro.genomeartist.components.propertiespanel.AbstractPropertiesPanelModel;
import ro.genomeartist.components.propertiespanel.IPropertiesEditor;
import ro.genomeartist.components.propertiespanel.IPropertiesHeader;
import ro.genomeartist.components.propertiespanel.JPropertiesPanel;
import ro.genomeartist.components.propertiespanel.editors.JCheckBoxPropertiesEditor;
import ro.genomeartist.components.propertiespanel.editors.JTextAreaPropertiesEditor;
import ro.genomeartist.components.propertiespanel.editors.JTextFieldPropertiesEditor;
import ro.genomeartist.components.propertiespanel.headers.JLabelPropertiesHeader;
import ro.genomeartist.gui.controller.query.SearchQuery;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.utils.DNAUtils;
import ro.genomeartist.gui.utils.JMyBoolean;
import ro.genomeartist.gui.utils.StringUtils;

/**
 *  Dialogul wrapper peste panoul cu afisearea unui Partial Result
 * @author iulian
 */
public class JSearchQueryDialog extends JTwoButtonAbstractDialog {
    //Constante
    private static final String BUTTON_SEARCH = "  Search  ";
    private static final String BUTTON_CANCEL = "  Cancel  ";


    //Variabile de clasa
    private JMyBoolean isOk;

    //Structura panoului
    JPropertiesPanel propertiesPanel;           //CENTER
        MyPropertiesModel propertiesPanelModel;

    /**
     * Dialog de afisare partial result
     */
    public JSearchQueryDialog(IGlobalManager globalManager, 
            String title, boolean modal , JMyBoolean isOk)  {
        super(globalManager.getTheRootFrame(), title, modal);
        
        //Setez managerii
        this.isOk = isOk;
        
        //Initialize the dialog
        this.setTextOk(BUTTON_SEARCH);
        this.setTextCancel(BUTTON_CANCEL);
        setResizable(true);

        //Middle pane
        propertiesPanelModel = new MyPropertiesModel();
        propertiesPanel = new JPropertiesPanel(propertiesPanelModel);
        this.setCenterComponent(propertiesPanel);
        
        //Pack
        this.pack();

        //Set it's location
        setLocationRelativeTo(globalManager.getTheRootFrame());
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Actiunile asupra Dialogului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Lansez actiunea OK
     */
    public void fireActionOk() {
        boolean hasChanged = true;

        //Fac commit la date
        hasChanged = propertiesPanel.commitChanges() || hasChanged;
        if (hasChanged) {
            JSearchQueryDialog.this.dispose();
            isOk.setTrue();
        } else {
            JSearchQueryDialog.this.dispose();
            isOk.setFalse();
        }
    }

    /**
     * Lansez actiunea de anulare
     */
    public void fireActionCancel() {
        //Ontin informatii despre nod
        //inchid dialogul
        JSearchQueryDialog.this.dispose();
        isOk.setFalse();
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Getter publici
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Obtin searchQuery-ul
     * @return
     */
    public List<SearchQuery> getSearchQueries() {
        List<SearchQuery> searchQueries = new LinkedList<SearchQuery>();
        
        //Create query
        String queryBaseName = StringUtils.sanitizeName(propertiesPanelModel.name);
        List<String> sequences = DNAUtils.extractCleanSequences(propertiesPanelModel.content);
        boolean shouldCountQueries = sequences.size() > 1;
        for (int i = 0; i < sequences.size(); i++) {
            SearchQuery localQuery = new SearchQuery();
            if (shouldCountQueries) {
                localQuery.setQueryName(queryBaseName+"_"+(i+1));
            } else {
                localQuery.setQueryName(queryBaseName);
            }
            localQuery.setQueryContent(sequences.get(i));
            searchQueries.add(localQuery);
        }
        
        return searchQueries;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Modelul de date pentru proprietati
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Modelul pentru proprietatile investitiei
     */
    private class MyPropertiesModel extends AbstractPropertiesPanelModel {
        //Constante
        private static final int FIELD_ROWS = 10;
        private static final int FIELD_WIDTH = 350;
        private static final int LABEL_WIDTH = 150;
        private static final int TEXTAREA_HEIGHT = 150;

        //Fac maparea numelor pe randuri
        public static final int ROW_NAME = 0;
        public static final int ROW_CONTENT = 1;
        public static final int ROW_IS_REVERSE_COMPLEMENT = 2;

        //Private localValues;
        public String name = "query";
        public String content = "";
        
        
        /**
         * Constructorul modelului
         * @param manopera
         */
        public MyPropertiesModel() {
        }

        @Override
        public int getRowCount() {
            return 3;
        }

        @Override
        public int getLabelWidth() {
            return LABEL_WIDTH;
        }

        @Override
        public Class getClassAt(int row) {
            switch (row) {
                case ROW_NAME:                      return String.class;
                case ROW_CONTENT:                   return String.class;
                case ROW_IS_REVERSE_COMPLEMENT:     return Boolean.class;
                default:    assert false; return null;
            }
        }

        @Override
        public String getDescriptionAt(int row) {
            switch (row) {
                case ROW_NAME:                      return "Query name";
                case ROW_CONTENT:                   return "Query content";
                case ROW_IS_REVERSE_COMPLEMENT:     return null;
                default:    assert false; return null;
            }
        }

        @Override
        public IPropertiesEditor createEditorComponentAt(int row) {
            switch (row) {
                case ROW_NAME:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                case ROW_CONTENT:
                    return new JTextAreaPropertiesEditor(FIELD_ROWS, FIELD_WIDTH);
                case ROW_IS_REVERSE_COMPLEMENT:
                    return new JCheckBoxPropertiesEditor("Reverse complement query");
                default:    assert false; return null;
            }
        }

        /**
         * Obtin tipul componentei ce va fi
         * @return
         */
        public IPropertiesHeader createHeaderComponentAt(int row) {
            switch (row) {
                case ROW_NAME:
                    return new JLabelPropertiesHeader("Query");
                default:
                    return null;
            }
        }

        @Override
        public void fireActionRowChanged(int row) {
            switch (row) {
                case ROW_IS_REVERSE_COMPLEMENT:
                    String currentQueryContent = (String) this.getEditorComponentAt(ROW_CONTENT)
                            .getValue(String.class);
                    List<String> cleanSequences = DNAUtils.extractCleanSequences(currentQueryContent);
                    List<String> reverseComplementedSequences = DNAUtils.reverseComplementSequences(cleanSequences);
                    
                    //Compose content
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < reverseComplementedSequences.size(); i++) {
                        builder.append(reverseComplementedSequences.get(i)).append("\n");
                        if (i != reverseComplementedSequences.size() - 1) {
                            builder.append("\n");
                        }
                    }
                    this.getEditorComponentAt(ROW_CONTENT).setValue(String.class, 
                            builder.toString());
                    break;
                default:
                    //Do nothing
                    break;
            }
        }

        @Override
        public boolean isRowOnNewline(int row) {
            switch (row) {
                default: return true;
            }
        }

        @Override
        public int getRowHeightAt(int row) {
            switch (row) {
                case ROW_CONTENT:  return TEXTAREA_HEIGHT;
                default: return AbstractPropertiesPanelModel.AUTO_HEIGHT;
            }
        }

        @Override
        public Object getValueAt(int row) {
            switch (row) {
                case ROW_NAME:
                    return name;
                case ROW_CONTENT:
                    return content;
                case ROW_IS_REVERSE_COMPLEMENT:
                    return false;
                default:    assert false; return null;
            }
        }

        @Override
        public boolean setValueAt(int row, Object newValue) {
            Boolean hasChanged;

            //Verific daca s-a schiimbat valoarea
            Object oldValue = getValueAt(row);
            if (oldValue != null) {
                if (getValueAt(row).equals(newValue))
                    return false;
            } else {
                if (newValue == null) return false;
            }

            //Daca s-a schimbat o setez in model
            switch (row) {
                case ROW_NAME:
                    name = (String) newValue;
                    hasChanged = true;
                    break;
                case ROW_CONTENT:
                    content = (String) newValue;
                    hasChanged = true;
                    break;
                case ROW_IS_REVERSE_COMPLEMENT:
                    //Do nothing
                    hasChanged = false;
                    break;
                default:
                   hasChanged = false;
                   break;
            }

            //Intorc rezultatul
            return hasChanged;
        }
    };
}
