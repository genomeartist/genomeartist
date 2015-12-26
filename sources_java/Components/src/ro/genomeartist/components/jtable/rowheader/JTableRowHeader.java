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

import ro.genomeartist.components.jtable.rowheader.implementation.JSuggestionsPopup;
import ro.genomeartist.components.jtable.rowheader.implementation.PhonyListSelectionModel;
import ro.genomeartist.components.jtable.rowheader.implementation.JRowHeaderCellRenderer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 * O lista ce va servi drept Row Header pentru un tabel
 * @author iulian
 */
public class JTableRowHeader extends JList {
    private static final Color DEFAULT_BACKGROUND = new Color(230,230,255); 
    private static final int NR_OF_CHARS = 4;
    
    //Varibile de clase
    private JTable jtable;
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *       Constructori
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Constructor auxiliar
     * @param jtable
     * @param listModel 
     */
    public JTableRowHeader(JTable jtable, 
            RowHeaderModel listModel) {
        this(jtable,listModel,NR_OF_CHARS);
    }
    
    /**
     * Construiesc lista
     * @param jtable
     * @param listModel 
     */
    public JTableRowHeader(JTable jtable, 
            RowHeaderModel listModel, int cols) {
        super();
        
        //Pastrez variabilele
        this.jtable = jtable;
        
        //Anulez selectia
        this.setSelectionModel(new PhonyListSelectionModel());
        
        //Setez inaltimea celulei
        this.setFixedCellHeight(jtable.getRowHeight());
        jtable.addComponentListener(new ComponentListener() {
            /**
             * Hack pentru a prinde evenimentul care schimba inaltimea celulei
             */
            @Override
            public void componentResized(ComponentEvent e) {
                checkForHeightChange();
            }

            public void componentMoved(ComponentEvent e) {
                checkForHeightChange();
            }

            public void componentShown(ComponentEvent e) {
                checkForHeightChange();
            }

            public void componentHidden(ComponentEvent e) {
                //Do nothing
            }
        });

        //Setez lungimea celulei
        int charSize = this.getFontMetrics(getFont()).charWidth('0');
        this.setFixedCellWidth(charSize*cols);
        
        //Setez modelul primit
        this.setModel(listModel);
        
        //Setez cell renderul default
        this.setCellRenderer(new JRowHeaderCellRenderer());
        
        //Setez culoarea de background default
        this.setBackground(DEFAULT_BACKGROUND);
        
        //Inregistrez mouse listenerul
        this.addMouseListener(mouseListener);
    }
    
    /**
     * Metoda ce verifica schimbarea dimensiunii
     */
    private void checkForHeightChange() {
        int rowHeight = jtable.getRowHeight();
        if (rowHeight != JTableRowHeader.this.getFixedCellHeight())
            JTableRowHeader.this.setFixedCellHeight(rowHeight);
    }

    /**
     * Suprascriu paint pentru a verifica tot timpul daca 
     * s-a schimbat dimensiunea randului la tablelul corespondend
     * @param g 
     */
    @Override
    public void paint(Graphics g) {
        checkForHeightChange();
        super.paint(g);
    }    
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *       Actiuni private
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Afisez sugestile pentru un anumit rand
     * @param row 
     */
    private void showSuggestionsForRow(int row) {
        Rectangle cellBounds = this.getCellBounds(row, row);
        int popupX = (int) (cellBounds.getX()+cellBounds.getWidth()+5);
        int popupY = (int) (cellBounds.getY()+cellBounds.getHeight());

        //Obtin Datele de la randul respectiv
        RowHeaderCellData cellData = (RowHeaderCellData) this.getModel().getElementAt(row);
        RowAnnotation currentAnnotation = cellData.getCurrentAnnotation();
        if (currentAnnotation != null) {
            Vector <RunnableSuggestion> suggestions = currentAnnotation.getSuggestions();
            if (suggestions != null && !suggestions.isEmpty())  {
                JSuggestionsPopup suggestionsPopup = new JSuggestionsPopup(suggestions);
                suggestionsPopup.show(this, popupX, popupY);
            }
        }
    }
    
    /**
     * Afisez tooltipul pentru un anumit rand
     * @param row 
     */
    private void showTooltipForRow(int row) {
         final JList sourceList = this;
        
        Rectangle cellBounds = sourceList.getCellBounds(row, row);
        final int middleX = (int) (cellBounds.getX()+cellBounds.getWidth()/2);
        final int middleY = (int) (cellBounds.getY()+cellBounds.getHeight()/2);

        //Simulez un eveniment de mouse
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MouseEvent moveEvent = new MouseEvent(sourceList, 0, 0, 0,
                                middleX, middleY, // X-Y of the mouse for the tool tip
                                0, false);
                int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();
                ToolTipManager.sharedInstance().setInitialDelay(0);
                ToolTipManager.sharedInstance().mouseMoved(moveEvent);
                ToolTipManager.sharedInstance().setInitialDelay(initialDelay);
            }
        });
    }
    
    /**
     * Cicleaza anotatiile unui anumit rand
     * @param row 
     */
    private void doActionCycleAnnotations(int row) {
        //Obtin Datele de la randul respectiv
        RowHeaderModel rowModel = (RowHeaderModel) this.getModel();
        rowModel.cycleAnnotations(row);
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *       Clase interne
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Mouse listenerul ce proceseaza evenimentele pe celule
     */
    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            
            //Verific provenienta
            JList sourceList = (JList) e.getSource();
            if (sourceList != JTableRowHeader.this) {
                return;
            }
        
            //Obtin randul
            int row = sourceList.locationToIndex(e.getPoint());
            if (row != -1) {
                RowHeaderCellData rowHeaderCellData = (RowHeaderCellData) 
                        sourceList.getModel().getElementAt(row);
                
                //Obtin marginile celulei
                JComponent renderedComponent = (JComponent) sourceList.getCellRenderer()
                        .getListCellRendererComponent(sourceList, rowHeaderCellData, 
                        row, false, false);
                Insets borderInsets = 
                        renderedComponent.getBorder().getBorderInsets(renderedComponent);
                
                //Calculez daca click-ul pica in border sau nu
                Rectangle cellBounds = sourceList.getCellBounds(row, row);
                double offsetRight = cellBounds.getX() + cellBounds.getWidth() - e.getX();
                
                //Caz special cand am mai multe anotatii
                if (rowHeaderCellData.getRowAnnotationCount() > 1) {
                    if (offsetRight < borderInsets.right) {
                        doActionCycleAnnotations(row);
                        return;
                    }
                }
                //Cazul default
                showSuggestionsForRow(row);
            } else {
                return;
            }
        }
        
        /**
         * Operatiunea efectuata la intrarea mouse-ului in componenta
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            
            //Verific provenienta
            JList sourceList = (JList) e.getSource();
            if (sourceList != JTableRowHeader.this) {
                return;
            }
        
            //Obtin randul
            int row = sourceList.locationToIndex(e.getPoint());
            if (row != -1) {
                showTooltipForRow(row);
            } else {
                return;
            }
        }
    };
}
