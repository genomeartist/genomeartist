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

/**
 * Implementare taburilor de pe TabbedPane. Ele trebuie sa poata fi inchise.
 * @author iulian
 */


import ro.genomeartist.components.utils.ComponentsUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class JComponentForTab extends JPanel implements ActionListener {
    //este final pentru a putea fi accesata din clasele imbricate
    private JTabbedPane tabbedpane;
    private String title;

    //Structura panoului
    private JLabel label;
    private JPanel buttonPanel;
        private JButton closeButton;

    /**
     * Constructor fara icon si fara bold
     * @param title
     * @param pane
     */
    public JComponentForTab(String title,JTabbedPane pane) {
        this(null, title, pane, false);
    }

    /**
     * Constructor fara bold
     * @param title
     * @param pane
     */
    public JComponentForTab(Icon icon, String title,JTabbedPane pane) {
        this(icon, title, pane, false);
    }

    /**
     * Construiesc o componeta ce se va pozitiona in tab.
     * @param icon  Iconul afisat
     * @param title Titlul afisat
     * @param pane  Panoul parinte
     */
    public JComponentForTab(Icon icon, String title, JTabbedPane pane, boolean shouldBeBold) {
        super();
        
        //Nu se poate fara tabbedpane-ul principal
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }        

        //Pastrez variabilele de clasa
        this.tabbedpane = pane;
        this.title = title;
        
        //Setez layout-ul
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

            label = new JLabel(title);
            label.setIcon(icon);
            //add more space between the label and the button
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            //Fac bold
            if (shouldBeBold)
                ComponentsUtils.setFontBold(label);
        this.add(label,BorderLayout.CENTER);


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
            //tab button
            closeButton = new JTabCloseButton();
            closeButton.addActionListener(this);
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createVerticalGlue());
        this.add(buttonPanel,BorderLayout.EAST);
    }

    /**
     * Setez titlul panoului
     */
    public void setTitle(String title) {
        this.label.setText(title);
    }

    /**
     * Setez iconul pentru componenta
     * @param icon
     */
    public void setIcon(Icon icon) {
        this.label.setIcon(icon);
    }

    /**
     * Setez iconul pentru componenta
     * @param icon
     */
    public void setTextBold(boolean shouldBeBold) {
        if (shouldBeBold)
            ComponentsUtils.setFontBold(label);
        else
            ComponentsUtils.setFontPlain(label);
    }

    /**
     * Actiunea pe butonul de inchidere tab
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        int i = tabbedpane.indexOfTabComponent(this);
        Component component = tabbedpane.getComponentAt(i);
        if (i != -1) {
            tabbedpane.remove(i);
            if (component instanceof INeedCleanup)
                ((INeedCleanup) component).cleanup();
        }
    }
}



