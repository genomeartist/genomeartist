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
package ro.genomeartist.components.jtable.viewportcontainer;

import ro.genomeartist.components.utils.MouseEventForwarder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;

/**
 * Clasa reprezinta un container pentru JTable care adauga la sfarsit un spacer
 * @author Iulian
 */
public class JScrollableBottomSpacingContainer extends JPanel 
            implements IViewportAdditionalContainer {
    private static final int DEFAULT_SPACER = 100;
    
    //Variabile de clasa
    private JComponent component;
    private int spacerHeight;

    //Componenta de spacing
    private JPanel fillPanel;
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Constructori
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Consturctor secundar
     * @param component 
     */
    public JScrollableBottomSpacingContainer(JComponent component) {
        this(component, DEFAULT_SPACER);
    }
    
    /**
     * Constructor principal
     * Construieste un container ce adauga spatiu la un JTable
     * @param spacerHeight 
     */
    public JScrollableBottomSpacingContainer(JComponent component, int spacerHeight) {
        super();
        
        //Pastrez variabilele de clasa
        this.component = component;
        this.spacerHeight = spacerHeight;
        
        //Setez layout-ul
        this.setLayout(new BorderLayout());
        
        //Construiesc componenta de spacing
        fillPanel = new JPanel();
            //Setari de dimensiune
            fillPanel.setPreferredSize(new Dimension(10, spacerHeight));
            fillPanel.setMaximumSize(new Dimension(10, spacerHeight));
            fillPanel.setMinimumSize(new Dimension(10, spacerHeight));
            fillPanel.setBackground(component.getBackground());
            fillPanel.setOpaque(true);

            //Setari de focus
            fillPanel.setFocusable(false);
            
        //Forwardez evenimentele de mouse
        MouseEventForwarder mouseEventForwarder = new MouseEventForwarder(component);
        fillPanel.addMouseListener(mouseEventForwarder);
            
        //Fac layout-ul
        this.add(component, BorderLayout.CENTER);
        this.add(fillPanel, BorderLayout.SOUTH);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Interfata scrollable
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Obtine unitatea cu care se face scroll
     * @param visibleRect
     * @param orientation
     * @param direction
     * @return 
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (component instanceof Scrollable) {
            return ((Scrollable) component)
                    .getScrollableUnitIncrement(visibleRect, orientation, direction);
        } else {
            return 10;
        }
    }

    /**
     * Obtine marimea blocului de scroll
     * @param visibleRect
     * @param orientation
     * @param direction
     * @return 
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (component instanceof Scrollable) {
            return ((Scrollable) component)
                    .getScrollableBlockIncrement(visibleRect, orientation, direction);
        } else {
            return 10;
        }
    }    

    /**
     * Obtine boolean daca viewportul va incerca sa mareasca width-ul 
     *  containerul sau va folosi scroll
     * @return 
     */
    public boolean getScrollableTracksViewportWidth() {
        if (component instanceof Scrollable) {
            return ((Scrollable) component)
                    .getScrollableTracksViewportWidth();
        } else {
            return false;
        }
    }

    /**
     * Obtine boolean daca viewportul va incerca sa mareasca heightul
     *  containerul sau va folosi scroll
     * @return 
     */
    public boolean getScrollableTracksViewportHeight() {
        if (component instanceof Scrollable) {
            boolean parentIsViewport = getParent() instanceof JViewport;
            boolean parentHeightIsBigger = 
                    getParent().getHeight() > component.getPreferredSize().height+spacerHeight;

            //Intorc rezultatul
            return  parentIsViewport && parentHeightIsBigger;
        } else {
            return false;
        }
    }

    /**
     * Obtine dimensiunea preferata a ferestrei de scroll
     * @return 
     */
    public Dimension getPreferredScrollableViewportSize() {
        if (component instanceof Scrollable) {
            return ((Scrollable) component)
                    .getPreferredScrollableViewportSize();
        } else {
            return new Dimension(100, 100);
        }
    }
}
