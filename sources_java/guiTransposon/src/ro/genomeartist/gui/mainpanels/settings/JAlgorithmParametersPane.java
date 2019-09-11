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
package ro.genomeartist.gui.mainpanels.settings;

import ro.genomeartist.components.propertiespanel.AbstractPropertiesPanelModel;
import ro.genomeartist.components.propertiespanel.IPropertiesEditor;
import ro.genomeartist.components.propertiespanel.IPropertiesHeader;
import ro.genomeartist.components.propertiespanel.JPropertiesPanel;
import ro.genomeartist.components.propertiespanel.editors.JCheckBoxPropertiesEditor;
import ro.genomeartist.components.propertiespanel.editors.JSliderPropertiesEditor;
import ro.genomeartist.components.propertiespanel.editors.JTextFieldPropertiesEditor;
import ro.genomeartist.components.propertiespanel.headers.JLabelPropertiesHeader;
import ro.genomeartist.gui.controller.externalcalls.ExternalLink;
import ro.genomeartist.gui.controller.settings.AlgorithmParams;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JPanel;

/**
 *
 * @author iulian
 */
public class JAlgorithmParametersPane extends JPanel {
    //Variabile de clasa
    private AlgorithmParams algorithmParams;
    
    //Structura panoului
    JPropertiesPanel propertiesPanel;           //CENTER
        MyPropertiesModel propertiesPanelModel;
    Component leftSpacerPanel;                     //WEST    
    Component rightSpacerPanel;                    //EAST
    
    /**
     * Construiesc un panou pentru afisarea algoritmilor
     * @param algorithmParams 
     */
    public JAlgorithmParametersPane(AlgorithmParams algorithmParams) {
        this.algorithmParams = algorithmParams;
        
        //Setez layout-ul
        this.setLayout(new BorderLayout());
        
        //Pun un panou de proprietati
        propertiesPanelModel = new MyPropertiesModel();
        propertiesPanel = new JPropertiesPanel(propertiesPanelModel);

        //Creez o componenta de spacing
        leftSpacerPanel = Box.createHorizontalStrut(50);
        rightSpacerPanel = Box.createHorizontalStrut(50);
        
        //Setez panoul central
        this.add(leftSpacerPanel, BorderLayout.WEST);
        this.add(propertiesPanel, BorderLayout.CENTER);
        this.add(rightSpacerPanel, BorderLayout.EAST);
    }
    
    /**
     * Scrie schimbarile in fisier si restarteaza si reconfigureaza serverul
     */
    public boolean commitChanges() {
        boolean hadChanged = propertiesPanel.commitChanges();
        
        //Daca s-a schimbat scriu fisierul pe disc
        if (hadChanged) {
            //Le scriu pe disc
            algorithmParams.saveToFile();
            try {
                //Rescrie expansion_table si reseteaza server
                ExternalLink.getExpansionGenerationCallable().call();
            } catch (Exception ex) {
                Logger.getLogger(JAlgorithmParametersPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Intorc rezultatul
        return hadChanged;
    }
    
    /**
     * Modelul pentru proprietatile investitiei
     */
    private class MyPropertiesModel extends AbstractPropertiesPanelModel {
        //Constante
        private static final int FIELD_WIDTH = 200;
        private static final int LABEL_WIDTH = 250;

        //Fac maparea numelor pe randuri
        public static final int ROW_EXPANSION_TYPE = 0;
        //~~
        public static final int ROW_OFFSET_ZERO = 1;
        public static final int ROW_SCORE_MATCH = 2;
        public static final int ROW_SCORE_MISMATCH = 3;
        public static final int ROW_LENGTH_MODIFIER = 4;
        //~~
        public static final int ROW_PICKING_DEPTH = 5;
        public static final int ROW_NUCLEU = 6;
        public static final int ROW_NUMAR_SOLUTII = 7;
        public static final int ROW_BONUS_COMPUNERE = 8;
        public static final int ROW_LENGTH_SEQEXTRACT = 9;
        public static final int ROW_LENGTH_TOLERANCE = 10;

        /**
         * Constructorul modelului
         * @param desfasurareLucrare
         */
        public MyPropertiesModel() {
        }

        @Override
        public int getRowCount() {
            return 11;
        }

        @Override
        public int getLabelWidth() {
            return LABEL_WIDTH;
        }

        @Override
        public Class getClassAt(int row) {
            switch (row) {
                case ROW_EXPANSION_TYPE:        return AlgorithmParams.ExpansionType.class;
                case ROW_OFFSET_ZERO:           return Integer.class;
                case ROW_SCORE_MATCH:           return Integer.class;
                case ROW_SCORE_MISMATCH:        return Integer.class;
                case ROW_LENGTH_MODIFIER:       return Integer.class;
                case ROW_PICKING_DEPTH:         return Integer.class;
                case ROW_NUCLEU:                return Integer.class;
                case ROW_NUMAR_SOLUTII:         return Integer.class;               
                case ROW_BONUS_COMPUNERE:       return Boolean.class;
                case ROW_LENGTH_SEQEXTRACT:     return Integer.class;
                case ROW_LENGTH_TOLERANCE:      return Integer.class;
                default:    assert false; return null;
            }
        }

        @Override
        public String getDescriptionAt(int row) {
            switch (row) {
                case ROW_EXPANSION_TYPE:        return null;
                case ROW_OFFSET_ZERO:           return "Zero offset";
                case ROW_SCORE_MATCH:           return "Match score";
                case ROW_SCORE_MISMATCH:        return "Mismatch score";
                case ROW_LENGTH_MODIFIER:       return "Length modifier";
                case ROW_PICKING_DEPTH:         return "Picking depth";
                case ROW_NUCLEU:                return "Nucleus size";
                case ROW_NUMAR_SOLUTII:         return "Number of results";               
                case ROW_BONUS_COMPUNERE:       return null;
                case ROW_LENGTH_SEQEXTRACT:     return "Length of TSD";
                case ROW_LENGTH_TOLERANCE:      return "Length of tolerance";
                default:    assert false; return null;
            }
        }

        @Override
        public IPropertiesEditor createEditorComponentAt(int row) {
            switch (row) {
                case ROW_EXPANSION_TYPE:
                    Collection enumerationValues = Arrays.asList(AlgorithmParams.ExpansionType.values());
                    return new JSliderPropertiesEditor(enumerationValues);
                case ROW_OFFSET_ZERO:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                case ROW_SCORE_MATCH:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                case ROW_SCORE_MISMATCH:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                case ROW_LENGTH_MODIFIER:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                case ROW_PICKING_DEPTH:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                case ROW_NUCLEU:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                case ROW_NUMAR_SOLUTII:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);                
                case ROW_BONUS_COMPUNERE:
                    return new JCheckBoxPropertiesEditor("Give bonus to insertion candidates");
                case ROW_LENGTH_SEQEXTRACT:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                case ROW_LENGTH_TOLERANCE:
                    return new JTextFieldPropertiesEditor(FIELD_WIDTH);
                default:    assert false; return null;
            }
        }

        /**
         * Obtin tipul componentei ce va fi
         * @return
         */
        public IPropertiesHeader createHeaderComponentAt(int row) {
            switch (row) {
                case ROW_EXPANSION_TYPE:
                    return new JLabelPropertiesHeader("Type of interval extension");
                case ROW_OFFSET_ZERO:
                    return new JLabelPropertiesHeader("Advanced extension parameters");
                case ROW_PICKING_DEPTH:
                    return new JLabelPropertiesHeader("Candidate results");
                case ROW_LENGTH_SEQEXTRACT:
                    return new JLabelPropertiesHeader("Export Settings");
                default:
                    return null;
            }
        }

        @Override
        public void fireActionRowChanged(int row) {
            switch (row) {
                case ROW_EXPANSION_TYPE:
                    //Obtin valorile de calcul
                    AlgorithmParams.ExpansionType expansionType = (AlgorithmParams.ExpansionType) 
                            this.getEditorComponentAt(ROW_EXPANSION_TYPE)
                            .getValue(AlgorithmParams.ExpansionType.class);
                    AlgorithmParams.ExpansionParameters expansionParameters =
                            algorithmParams.getExpansionParameters(expansionType);
                    this.getEditorComponentAt(ROW_OFFSET_ZERO).setValue(Integer.class, 
                            expansionParameters.getOffsetZero());
                    this.getEditorComponentAt(ROW_SCORE_MATCH).setValue(Integer.class, 
                            expansionParameters.getScoreMatch());
                    this.getEditorComponentAt(ROW_SCORE_MISMATCH).setValue(Integer.class, 
                            expansionParameters.getScoreMismatch());
                    this.getEditorComponentAt(ROW_LENGTH_MODIFIER).setValue(Integer.class, 
                            expansionParameters.getLengthModifier());
                    break;
                case ROW_OFFSET_ZERO:
                case ROW_SCORE_MATCH:
                case ROW_SCORE_MISMATCH:
                case ROW_LENGTH_MODIFIER:
                    int localZeroOffset = (Integer) this.getEditorComponentAt(ROW_OFFSET_ZERO)
                            .getValue(Integer.class);
                    int localScoreMatch = (Integer) this.getEditorComponentAt(ROW_SCORE_MATCH)
                            .getValue(Integer.class);
                    int localScoreMismatch = (Integer) this.getEditorComponentAt(ROW_SCORE_MISMATCH)
                            .getValue(Integer.class);
                    int localLengthModifier = (Integer) this.getEditorComponentAt(ROW_LENGTH_MODIFIER)
                            .getValue(Integer.class);
                    AlgorithmParams.ExpansionParameters localExpansionParameters = 
                            new AlgorithmParams.ExpansionParameters(localZeroOffset, 
                            localScoreMatch, localScoreMismatch, localLengthModifier);
                    AlgorithmParams.ExpansionType localExpansionType = AlgorithmParams
                            .getExpansionType(localExpansionParameters);
                    this.getEditorComponentAt(ROW_EXPANSION_TYPE).setValue(AlgorithmParams.ExpansionType.class, 
                            localExpansionType);
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
                default: return AbstractPropertiesPanelModel.AUTO_HEIGHT;
            }
        }

        @Override
        public Object getValueAt(int row) {
            switch (row) {
                case ROW_EXPANSION_TYPE:
                    return algorithmParams.getExpansionType();
                case ROW_OFFSET_ZERO:
                    return algorithmParams.getOffsetZero();
                case ROW_SCORE_MATCH:
                    return algorithmParams.getScoreMatch();
                case ROW_SCORE_MISMATCH:
                    return algorithmParams.getScoreMismatch();
                case ROW_LENGTH_MODIFIER:
                    return algorithmParams.getLengthModifier();
                case ROW_PICKING_DEPTH:
                    return algorithmParams.getPickingDepth();
                case ROW_NUCLEU:
                    return algorithmParams.getNucleu();
                case ROW_NUMAR_SOLUTII:
                    return algorithmParams.getSolutii();
                case ROW_BONUS_COMPUNERE:
                    return algorithmParams.isBonusCompunere();
                case ROW_LENGTH_SEQEXTRACT:
                    return algorithmParams.getLengthSeqExtract();
                case ROW_LENGTH_TOLERANCE:
                    return algorithmParams.getLengthTolerance();
                default:    assert false; return null;
            }
        }

        @Override
        public boolean setValueAt(int row, Object newValue) {
            Integer auxInteger;
            Boolean auxBoolean;
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
                case ROW_EXPANSION_TYPE:
                    //Do nothing
                    hasChanged = false;
                    break;
                case ROW_OFFSET_ZERO:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setOffsetZero(auxInteger);
                    break;
                case ROW_SCORE_MATCH:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setScoreMatch(auxInteger);
                    break;
                case ROW_SCORE_MISMATCH:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setScoreMismatch(auxInteger);
                    break;
                case ROW_LENGTH_MODIFIER:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setLengthModifier(auxInteger);
                    break;
                case ROW_PICKING_DEPTH:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setPickingDepth(auxInteger);
                    break;
                case ROW_NUCLEU:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setNucleu(auxInteger);
                    break;
                case ROW_NUMAR_SOLUTII:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setSolutii(auxInteger);
                    break;
                case ROW_BONUS_COMPUNERE:
                    auxBoolean = (Boolean) newValue;
                    hasChanged = algorithmParams.setBonusCompunere(auxBoolean);
                    break;
                case ROW_LENGTH_SEQEXTRACT:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setLengthSeqExtract(auxInteger);
                    break;
                case ROW_LENGTH_TOLERANCE:
                    auxInteger = (Integer) newValue;
                    hasChanged = algorithmParams.setLengthTolerance(auxInteger);
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
