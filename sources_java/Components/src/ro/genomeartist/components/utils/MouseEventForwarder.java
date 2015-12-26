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
package ro.genomeartist.components.utils;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.SwingUtilities;

/**
 * Clasa reprezinta un ascultator de MouseEvent al carei scop este 
 * sa forwardeze evenimentele catre o anumita componenta destinatie
 * @author Iulian
 */
public class MouseEventForwarder implements MouseListener, MouseWheelListener, MouseMotionListener {
    private Component destinationComponent;
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Constructor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Construiesc un forwarder
     * @param destinationComponent 
     */
    public MouseEventForwarder(Component destinationComponent) {
        this.destinationComponent = destinationComponent;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Interfetele standard
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Event
     */
    public void mouseClicked(MouseEvent e) {
        forwardEvent(e);
    }

    /**
     * Event
     */
    public void mousePressed(MouseEvent e) {
        forwardEvent(e);
    }

    /**
     * Event
     */
    public void mouseReleased(MouseEvent e) {
        forwardEvent(e);
    }

    /**
     * Event
     */
    public void mouseEntered(MouseEvent e) {
        forwardEvent(e);
    }

    /**
     * Event
     */
    public void mouseExited(MouseEvent e) {
        forwardEvent(e);
    }

    /**
     * Event
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        forwardEvent(e);
    }

    /**
     * Event
     */
    public void mouseDragged(MouseEvent e) {
        forwardEvent(e);
    }

    /**
     * Event
     */
    public void mouseMoved(MouseEvent e) {
        forwardEvent(e);
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Metoda de prelucrare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Forwardeaza evenimentul
     * @param e 
     */
    private void forwardEvent(MouseEvent e) {
        Point translatedPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), destinationComponent);
        MouseEvent translatedMouseEvent = new MouseEvent(
                destinationComponent, e.getID(), e.getWhen(), e.getModifiers(), 
                translatedPoint.x, translatedPoint.y, e.getXOnScreen(),e.getYOnScreen(), 
                e.getClickCount(), e.isPopupTrigger(), e.getButton());
        destinationComponent.dispatchEvent(translatedMouseEvent);
    }
    
    /**
     * Forwardeaza evenimentul
     * @param e 
     */
    private void forwardEvent(MouseWheelEvent e) {
        Point translatedPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), destinationComponent);
        MouseWheelEvent translatedMouseEvent = new MouseWheelEvent(
                destinationComponent, e.getID(), e.getWhen(), e.getModifiers(), 
                translatedPoint.x, translatedPoint.y, e.getXOnScreen(),e.getYOnScreen(), 
                e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), 
                e.getScrollAmount(), e.getWheelRotation());
        destinationComponent.dispatchEvent(translatedMouseEvent);
    }
    
}
