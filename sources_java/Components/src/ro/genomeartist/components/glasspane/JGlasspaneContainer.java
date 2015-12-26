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

package ro.genomeartist.components.glasspane;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * Clasa ce implementeaza un GlassPane ce deseneaza pe fundal o imagine blurata
 * si capteaza inputul mouseului catre fundal
 * @author iulian
 */
public class JGlasspaneContainer extends JPanel {
    BufferedImage fakeScreen;   //Fundalul fals
    Component window;           //Fereastra din fata
    IDoScreenshot parent;

    /**
     * Construiesc ecranul de login
     */
    public JGlasspaneContainer(Component window,IDoScreenshot parent) {
        JButton button;
        this.setLayout(null);
        this.setOpaque(false);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mousemotionListener);
        this.addComponentListener(componentListener);
        fakeScreen = null;

        //Setez parintele
        this.parent = parent;

        //Setez panelul
        this.window = window;
        add(window);
    }

    /**
     * Centreaza desenul
     */
    public void centerWindow(){
        int panelwidth,panelheight,objwidth,objheight;

        if (window != null) {
            panelwidth = this.getWidth();
            panelheight = this.getHeight();
            objwidth = window.getWidth();
            objheight = window.getHeight();
            window.setBounds((panelwidth-objwidth)/2, (panelheight-objheight)/2 ,objwidth,objheight);
        }
    }

    /**
     * Afisez pe fundal o imagine Fake a aplicatiei
     */
    public void setFakeScreen(BufferedImage image){
        fakeScreen = image;
    }
       
    /**
     * Aici fac desenarea fundalului fals
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (fakeScreen != null) {
            float[] my_kernel = {
            0.10f, 0.10f, 0.10f,
            0.10f, 0.20f, 0.10f,
            0.10f, 0.10f, 0.10f };
            ConvolveOp op = new ConvolveOp(new Kernel(3,3, my_kernel));
            Image img = op.filter(fakeScreen,null);
            g.drawImage(img,0,0,null);
        }

        Graphics2D g2d = (Graphics2D) g;
        float alpha = 0.1f;
        Color color = new Color(0, 0, 1, alpha);
        g2d.setPaint(color);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
        //      Sectiunea in care definesc ascultatorii         /
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    /**
     * Definesc ascultatorul pe mouse
     */
    MouseListener mouseListener = new MouseListener() {
        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    };

    /**
     * Definesc ascultatorul pe miscarea mouse-ului
     */
    MouseMotionListener mousemotionListener = new MouseMotionListener() {
        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    };

     /**
     * Definesc ascultatorul pentru redimensionare
     */
    ComponentListener componentListener = new ComponentListener() {

        public void componentResized(ComponentEvent e) {
            parent.refresh();
            fakeScreen = parent.takeScreenshot();
            centerWindow();
            repaint();
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentHidden(ComponentEvent e) {
        }
    };
}
