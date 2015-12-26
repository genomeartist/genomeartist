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

package ro.genomeartist.components.tabbedpane;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * Acest panou lucreaza doar cu componente closable
 * @author iulian
 */
public class JDragableTabbedPane extends JTabbedPane {
    private static final int LINEWIDTH = 3;
    private static final String NAME = "test";
    private final GhostGlassPane glassPane = new GhostGlassPane();
    private final Rectangle lineRect  = new Rectangle();
    private final Color     lineColor = new Color(0, 100, 255);
    private int dragTabIndex = -1;

    //Butoane pentru navigare in taburi
    private static Rectangle rBackward = new Rectangle();
    private static Rectangle rForward  = new Rectangle();
    private static int rwh = 20;
    private static int buttonsize = 30; //xxx magic number of scroll button size
    private boolean hasGhost = true;


    /**
     * Constructorul principal al clasei
     */
    public JDragableTabbedPane() {
        super();

        //Setarile taburilor
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setFocusable(false);

        //Configurare drag and drop
        final DragSourceListener dsl = new DragSourceListener() {
            public void dragEnter(DragSourceDragEvent e) {
                e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            }
            public void dragExit(DragSourceEvent e) {
                e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
                lineRect.setRect(0,0,0,0);
                glassPane.setPoint(new Point(-1000,-1000));
                glassPane.repaint();
            }
            public void dragOver(DragSourceDragEvent e) {
                Point glassPt = e.getLocation();
                SwingUtilities.convertPointFromScreen(glassPt, glassPane);
                int targetIdx = getDNDTargetTabIndex(glassPt);
                //if(getTabAreaBounds().contains(tabPt) && targetIdx>=0 &&
                if(getTabAreaBounds().contains(glassPt) && targetIdx>=0 &&
                   targetIdx!=dragTabIndex && targetIdx!=dragTabIndex+1) {
                    e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
                }else{
                    e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
                }
            }
            public void dragDropEnd(DragSourceDropEvent e) {
                lineRect.setRect(0,0,0,0);
                dragTabIndex = -1;
                glassPane.setVisible(false);
                if(hasGhost()) {
                    glassPane.setVisible(false);
                    glassPane.setImage(null);
                }
            }
            public void dropActionChanged(DragSourceDragEvent e) {}
        };
        final Transferable t = new Transferable() {
            private final DataFlavor FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, NAME);
            public Object getTransferData(DataFlavor flavor) {
                return "phony transferable";
            }
            public DataFlavor[] getTransferDataFlavors() {
                DataFlavor[] f = new DataFlavor[1];
                f[0] = this.FLAVOR;
                return f;
            }
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.getHumanPresentableName().equals(NAME);
            }
        };
        final DragGestureListener dgl = new DragGestureListener() {
            public void dragGestureRecognized(DragGestureEvent e) {
                if(getTabCount()<=1) return;
                Point tabPt = e.getDragOrigin();
                dragTabIndex = indexAtLocation(tabPt.x, tabPt.y);
                //"disabled tab problem".
                if(dragTabIndex<0 || !isEnabledAt(dragTabIndex)) return;
                initGlassPane(e.getComponent(), e.getDragOrigin());
                try{
                    e.startDrag(DragSource.DefaultMoveDrop, t, dsl);
                }catch(InvalidDnDOperationException idoe) {
                    idoe.printStackTrace();
                }
            }
        };
        new DropTarget(glassPane, DnDConstants.ACTION_COPY_OR_MOVE,
                new CDropTargetListener(), true);
        new DragSource().createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_COPY_OR_MOVE, dgl);
        
        //Inchid tabul la click middle mouse
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2) {
                    int tab = getTabAtPoint(e.getPoint());
                    if (tab != -1) {
                        Component component = JDragableTabbedPane.this.getComponentAt(tab);
                        if (tab != -1) {
                            JDragableTabbedPane.this.remove(tab);
                            if (component instanceof INeedCleanup)
                                ((INeedCleanup) component).cleanup();
                            e.consume();
                        }
                    }
                }
            }
        });
    }

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Metode publice de adaugare ale clasei
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * SECONDARY
     * Adaug o componenta folosind numele
     * @param component
     * @return
     */
    @Override
    public Component add(Component component) {
        return this.add(component.getName(),component);
    }    

    /**
     * SECONDARY
     * Ingnor constrangerile
     * @param component
     * @param constraints
     */
    @Override
    public void add(Component component, Object constraints) {
        this.add(component);
    }

    /**
     * SECONDARY
     * Adauga o componenta la un anumit indez
     * @param component
     * @param index
     * @return
     */
    @Override
    public Component add(Component component, int index) {
        return this.add(component.getName(), component, index);
    }

    /**
     * SECONDARY
     * Ignor constrangerile
     * @param component
     * @param constraints
     * @param index
     */
    @Override
    public void add(Component component, Object constraints, int index) {
        this.add(component, index);
    }

    /**
     * SECONDARY
     * Adaug specificatii pentru tab
     * @param title
     * @param component
     * @param index
     * @return
     */
    public Component add(String title, Component component, int index) {
        TabSpecifications specifications = new TabSpecifications();
        specifications.title = title;

        return this.add(specifications, component, index);
    }

    /**
     * SECONDARY
     * Adaug specificatii pentru tab
     * @param title
     * @param component
     * @return
     */
    @Override
    public Component add(String title, Component component) {
        TabSpecifications specifications = new TabSpecifications();
        specifications.title = title;

        return this.add(specifications, component);
    }
    
    /**
     * PRIMARY
     * Adaug o componente pe o anumita pozitie
     * @param text
     * @param component
     * @param index
     * @return
     */
    public Component add(TabSpecifications specifications, Component component, int index) {
        int i;

        //Adaug componenta in parinte
        super.add(component,index);
        i = this.indexOfComponent(component);

        //Setez Tabul pentru componenta nou adaugata
        if (i!= -1) {
            JComponentForTab componentForTab = new JComponentForTab(
                    specifications.icon,specifications.title,this,specifications.shouldBeBold);
            this.setTabComponentAt(i, componentForTab );
        }
        return component;
    }

    /**
     * PRIMARY
     * Adauga o componenta la Tabbed Pane
     * @param text  Textul atasat labelului
     * @param componenta Compunenta ce se adauga la Pane
     * @return
     */
    public Component add(TabSpecifications specifications, Component component) {
        int i;

        //Adaug componenta in parinte
        super.add(specifications.title,component);
        i = this.indexOfComponent(component);

        //Setez Tabul pentru componenta nou adaugata
        if (i!= -1) {
            JComponentForTab componentForTab = new JComponentForTab(
                    specifications.icon,specifications.title,this,specifications.shouldBeBold);
            this.setTabComponentAt(i, componentForTab );
        }
        return component;
    }

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Mecanism de modificate taburi
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Schimba titlul unui tab
     * @param index
     * @param title
     */
    @Override
    public void setTitleAt(int index, String title) {
        //Notific parintele
        super.setTitleAt(index, title);

        JComponentForTab tabComponent = (JComponentForTab) this.getTabComponentAt(index);
        tabComponent.setTitle(title);
    }    

    /**
     * Setez iconul pentru o componenta de tab
     * @param index
     * @param icon
     */
    @Override
    public void setIconAt(int index, Icon icon) {
        super.setIconAt(index, icon);

        JComponentForTab tabComponent = (JComponentForTab) this.getTabComponentAt(index);
        tabComponent.setIcon(icon);
    }

    /**
     * Setez o componenta bold
     * @param index
     * @param icon
     */
    public void setTextBoldAt(int index, boolean shouldBeBold) {
        JComponentForTab tabComponent = (JComponentForTab) this.getTabComponentAt(index);
        tabComponent.setTextBold(shouldBeBold);
    }

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Mecanism pentru navigare prin butoane
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Auto scroll pentru tab
     * @param glassPt
     */
    private void autoScrollTest(Point glassPt) {
        Rectangle r = getTabAreaBounds();
        int tabPPlacement = getTabPlacement();
        if(tabPPlacement==TOP || tabPPlacement==BOTTOM) {
            rBackward.setBounds(r.x, r.y, rwh, r.height);
            rForward.setBounds(r.x+r.width-rwh-buttonsize, r.y, rwh+buttonsize, r.height);
        }else if(tabPPlacement==LEFT || tabPPlacement==RIGHT) {
            rBackward.setBounds(r.x, r.y, r.width, rwh);
            rForward.setBounds(r.x, r.y+r.height-rwh-buttonsize, r.width, rwh+buttonsize);
        }
        if(rBackward.contains(glassPt)) {
            //System.out.println(new java.util.Date() + "Backward");
            clickArrowButton("scrollTabsBackwardAction");
        }else if(rForward.contains(glassPt)) {
            //System.out.println(new java.util.Date() + "Forward");
            clickArrowButton("scrollTabsForwardAction");
        }
    }

    /**
     * Actiuniile pentru butoanele de navigare in taburi
     * @param actionKey
     */
    private void clickArrowButton(String actionKey) {
        ActionMap map = getActionMap();
        if(map != null) {
            javax.swing.Action action = map.get(actionKey);
            if (action != null && action.isEnabled()) {
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null, 0, 0));
            }
        }
    }

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Mecanismul de inchidere la middle mouse
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin tabul de la un anumit punct
     * @param p
     * @return 
     */
    private int getTabAtPoint(Point p) {
        for(int i=0;i<getTabCount();i++) {
            Rectangle r = getBoundsAt(i);
            if(r.contains(p)) return i;
        }
        
        //NU s-a gasit tabul
        return  -1;
    }
    
     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Mecanismul pentru Drag and Drop Tab
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Seteaza flagul pentru desenare ghost panel
     * @param flag
     */
    public void setPaintGhost(boolean flag) {
        hasGhost = flag;
    }
    
    /**
     * Obtine flagul de desenare ghost
     * @return
     */
    public boolean hasGhost() {
        return hasGhost;
    }

    /**
     * Pentru DND. Obtine tabul despre care e vorba la Drag and Drop
     * @param glassPt
     * @return
     */
    private int getDNDTargetTabIndex(Point glassPt) {
        Point tabPt = SwingUtilities.convertPoint(glassPane, glassPt, JDragableTabbedPane.this);
        boolean isTB = getTabPlacement()==JTabbedPane.TOP || getTabPlacement()==JTabbedPane.BOTTOM;
        for(int i=0;i<getTabCount();i++) {
            Rectangle r = getBoundsAt(i);
            if(isTB) r.setRect(r.x-r.width/2, r.y,  r.width, r.height);
            else     r.setRect(r.x, r.y-r.height/2, r.width, r.height);
            if(r.contains(tabPt)) return i;
        }
        Rectangle r = getBoundsAt(getTabCount()-1);
        if(isTB) r.setRect(r.x+r.width/2, r.y,  r.width, r.height);
        else     r.setRect(r.x, r.y+r.height/2, r.width, r.height);
        return   r.contains(tabPt)?getTabCount():-1;
    }

    /**
     * Conversie intr taburi, necesar pentru navigare prin butoane
     * @param prev
     * @param next
     */
    private void convertTab(int prev, int next) {
        if(next<0 || prev==next) {
            return;
        }
        Component cmp = getComponentAt(prev);
        Component tab = getTabComponentAt(prev);
        String str    = getTitleAt(prev);
        Icon icon     = getIconAt(prev);
        String tip    = getToolTipTextAt(prev);
        boolean flg   = isEnabledAt(prev);
        int tgtindex  = prev>next ? next : next-1;
        remove(prev);
        insertTab(str, icon, cmp, tip, tgtindex);
        setEnabledAt(tgtindex, flg);
        //When you drag'n'drop a disabled tab, it finishes enabled and selected.
        //pointed out by dlorde
        if(flg) setSelectedIndex(tgtindex);

        //I have a component in all tabs (jlabel with an X to close the tab) and when i move a tab the component disappear.
        //pointed out by Daniel Dario Morales Salas
        setTabComponentAt(tgtindex, tab);
    }

    /**
     * DND. Desenarea liniilor ce reprezinta locatia de drop
     * @param next
     */
    private void initTargetLeftRightLine(int next) {
        if(next<0 || dragTabIndex==next || next-dragTabIndex==1) {
            lineRect.setRect(0,0,0,0);
        }else if(next==0) {
            Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(0), glassPane);
            lineRect.setRect(r.x-LINEWIDTH/2,r.y,LINEWIDTH,r.height);
        }else{
            Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(next-1), glassPane);
            lineRect.setRect(r.x+r.width-LINEWIDTH/2,r.y,LINEWIDTH,r.height);
        }
    }
    /**
     * DND. Desenarea liniilor ce reprezinta locatia de drop
     * @param next
     */
    private void initTargetTopBottomLine(int next) {
        if(next<0 || dragTabIndex==next || next-dragTabIndex==1) {
            lineRect.setRect(0,0,0,0);
        }else if(next==0) {
            Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(0), glassPane);
            lineRect.setRect(r.x,r.y-LINEWIDTH/2,r.width,LINEWIDTH);
        }else{
            Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(next-1), glassPane);
            lineRect.setRect(r.x,r.y+r.height-LINEWIDTH/2,r.width,LINEWIDTH);
        }
    }

    /**
     * Initializeaza glasspane-ul pe care se va desena componenta
     * ce se trage
     * @param c
     * @param tabPt
     */
    private void initGlassPane(Component c, Point tabPt) {
        getRootPane().setGlassPane(glassPane);
        if(hasGhost()) {
            Rectangle rect = getBoundsAt(dragTabIndex);
            BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            c.paint(g);
            rect.x = rect.x<0?0:rect.x;
            rect.y = rect.y<0?0:rect.y;
            image = image.getSubimage(rect.x,rect.y,rect.width,rect.height);
            glassPane.setImage(image);
        }
        Point glassPt = SwingUtilities.convertPoint(c, tabPt, glassPane);
        glassPane.setPoint(glassPt);
        glassPane.setVisible(true);
    }

    /**
     * Obtin marginile zonei de desenare
     * @return
     */
    private Rectangle getTabAreaBounds() {
        Rectangle tabbedRect = getBounds();
        Rectangle scr = getSelectedComponent().getBounds();
        int tabPPlacement = getTabPlacement();
        if(false) {
        }else if(tabPPlacement==TOP) {
            tabbedRect.height = tabbedRect.height - scr.height;
        }else if(tabPPlacement==BOTTOM) {
            tabbedRect.y = tabbedRect.y + scr.y + scr.height;
            tabbedRect.height = tabbedRect.height - scr.height;
        }else if(tabPPlacement==LEFT) {
            tabbedRect.width = tabbedRect.width - scr.width;
        }else if(tabPPlacement==RIGHT) {
            tabbedRect.x = tabbedRect.x + scr.x + scr.width;
            tabbedRect.width = tabbedRect.width - scr.width;
        }
        tabbedRect.grow(2, 2);
        return tabbedRect;
    }

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Clase interne
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Ascultatorul de drag and drop
     */
    class CDropTargetListener implements DropTargetListener{
        public void dragEnter(DropTargetDragEvent e) {
            if(isDragAcceptable(e)) e.acceptDrag(e.getDropAction());
            else e.rejectDrag();
        }
        public void dragExit(DropTargetEvent e) {}
        public void dropActionChanged(DropTargetDragEvent e) {}

        private Point pt_ = new Point();
        public void dragOver(final DropTargetDragEvent e) {
            Point pt = e.getLocation();
            if(getTabPlacement()==JTabbedPane.TOP || getTabPlacement()==JTabbedPane.BOTTOM) {
                initTargetLeftRightLine(getDNDTargetTabIndex(pt));
            }else{
                initTargetTopBottomLine(getDNDTargetTabIndex(pt));
            }
            if(hasGhost()) {
                glassPane.setPoint(pt);
            }
            if(!pt_.equals(pt)) glassPane.repaint();
            pt_ = pt;
            autoScrollTest(pt);
        }

        public void drop(DropTargetDropEvent e) {
            if(isDropAcceptable(e)) {
                convertTab(dragTabIndex, getDNDTargetTabIndex(e.getLocation()));
                e.dropComplete(true);
            }else{
                e.dropComplete(false);
            }
            repaint();
        }
        public boolean isDragAcceptable(DropTargetDragEvent e) {
            Transferable t = e.getTransferable();
            if(t==null) return false;
            DataFlavor[] f = e.getCurrentDataFlavors();
            if(t.isDataFlavorSupported(f[0]) && dragTabIndex>=0) {
                return true;
            }
            return false;
        }
        public boolean isDropAcceptable(DropTargetDropEvent e) {
            Transferable t = e.getTransferable();
            if(t==null) return false;
            DataFlavor[] f = t.getTransferDataFlavors();
            if(t.isDataFlavorSupported(f[0]) && dragTabIndex>=0) {
                return true;
            }
            return false;
        }
    }

    /**
     * Glass pane-ul pe care se deseneaza drag-ul
     */
    class GhostGlassPane extends JPanel {
        private final AlphaComposite composite;
        private Point location = new Point(0, 0);
        private BufferedImage draggingGhost = null;
        public GhostGlassPane() {
            setOpaque(false);
            composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            //http://bugs.sun.com/view_bug.do?bug_id=6700748
            setCursor(null); //xxx
        }
        public void setImage(BufferedImage draggingGhost) {
            this.draggingGhost = draggingGhost;
        }
        public void setPoint(Point location) {
            this.location = location;
        }
        boolean debug = false;
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setComposite(composite);
            if(debug && getTabLayoutPolicy()==SCROLL_TAB_LAYOUT) {
                g2.setPaint(Color.RED);
                g2.fill(rBackward);
                g2.fill(rForward);
            }
            if(draggingGhost != null) {
                double xx = location.getX() - (draggingGhost.getWidth(this) /2d);
                double yy = location.getY() - (draggingGhost.getHeight(this)/2d);
                g2.drawImage(draggingGhost, (int)xx, (int)yy , null);
            }
            if(dragTabIndex>=0) {
                g2.setPaint(lineColor);
                g2.fill(lineRect);
            }
        }
    }
}
