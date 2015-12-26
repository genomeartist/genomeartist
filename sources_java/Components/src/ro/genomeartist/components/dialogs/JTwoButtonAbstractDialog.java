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

package ro.genomeartist.components.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 *
 * @author iulian
 */
public abstract class JTwoButtonAbstractDialog extends JDialog {
    //Variabile pentru textul de pe butoane
    private String text_ok;
    private String text_cancel;

    //Constante pentru textul de pe butoane
    public static final String TEXT_OK = "     Ok     ";
    public static final String TEXT_CANCEL = " Anuleaza ";

    //Constante pentru actiuniile de pe butoane
    private static final String ACTION_OK = "ok";
    private static final String ACTION_CANCEL = "cancel";

    //Structura ferestrei
    //CENTER
        // - user space
    //SOUTH
    protected JPanel buttonsPane;
        protected JButton buttonOk;
        protected JButton buttonCancel;

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Construirea Dialogului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 

    /**
     * Construiesc un panou cu valorile default pentru
     * titlurile butoanelor
     * Constructor secundar.
     * @param owner Frame-ul in care se va afisa
     * @param title Titlul ferestrei
     * @param modal Daca fereastra este modala sau nu
     */
    public JTwoButtonAbstractDialog(Frame owner, String title, boolean modal) {
        this(owner, title, modal,TEXT_OK,TEXT_CANCEL);
    }

    /**
     * Construiesc un panou cu valori custom pentru textul de
     * pe butoane
     * @param owner Frame-ul in care se va afisa
     * @param title Titlul ferestrei
     * @param modal Daca fereastra este modala sau nu
     * @param text_ok   Textul pentru butonul OK
     * @param text_cancel TExtul pentru butonul Cancel
     */
    public JTwoButtonAbstractDialog(Frame owner, String title, boolean modal,
            String text_ok, String text_cancel) {
        super(owner, title, modal);

        //Obtin valorile
        this.text_ok = text_ok;
        this.text_cancel = text_cancel;

        // Setez layout-ul pentru panou
        // Folosesc BorderLayout pentru a forta utilizatorul a adauge continut doar
        // in centru sau sus. Partea de jos este rezervata pentru Toolbar
        this.setLayout(new BorderLayout());

        //Operatia de inchidere trece prin metoda de Cancel
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);

        //Construiesc toolbarul si il adaug in partea de jos
        buttonsPane = createButtonsPane();
        this.add(buttonsPane,BorderLayout.SOUTH);

        //Fac bindingurile default pentru fereastra
        initKeyBindings();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     *
     * @return
     */
    private JPanel createButtonsPane() {
        JPanel localPane = new JPanel();
        localPane.setLayout(new BoxLayout(localPane, BoxLayout.X_AXIS));

        //Making the buttons
        //buton OK
        buttonOk = new JButton(text_ok);
        buttonOk.setActionCommand(ACTION_OK);
        buttonOk.addActionListener(buttonListener);

        //buton CANCEL
        buttonCancel = new JButton(text_cancel);
        buttonCancel.setActionCommand(ACTION_CANCEL);
        buttonCancel.addActionListener(buttonListener);

        //Layout the buttons

        //DEFAULT windws layout
        localPane.add(Box.createHorizontalGlue());
        localPane.add(buttonOk);
        localPane.add(buttonCancel);
        localPane.add(Box.createHorizontalGlue());

        return localPane;
    }

    /**
     * Fac key bindingurile pentru tabel
     */
    private void initKeyBindings() {
        JRootPane contentPane = this.getRootPane();

        //   Maparea actiunilor pe nume
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ActionMap map = contentPane.getActionMap();
        //Wrapper peste actiunea de ok
        Action actionOk = new AbstractAction(ACTION_OK) {
            public void actionPerformed(ActionEvent e) {
                shouldFireActionOk();
            }
        };
        map.put(actionOk.getValue(Action.NAME),
                actionOk);
        //Wrapper peste actiunea de cancel
        Action actionCancel = new AbstractAction(ACTION_CANCEL) {
            public void actionPerformed(ActionEvent e) {
                shouldFireActionCancel();
            }
        };
        map.put(actionCancel.getValue(Action.NAME),
                actionCancel);


        //   Maparea numelor pe taste
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        InputMap imap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imap.put(KeyStroke.getKeyStroke("ENTER"),
            actionOk.getValue(Action.NAME));
        imap.put(KeyStroke.getKeyStroke("ESCAPE"),
            actionCancel.getValue(Action.NAME));
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      metode pentru modificare dimensiune
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Seteaza dimensiunea panoului ca si procent din parintele sau
     * @param procentualDimension 
     */
    public void setProcentualSize(int procentWidth, int procentHeight) {
        setProcentualSize(new Dimension(procentWidth, procentHeight));
    }
    
    /**
     * Seteaza dimensiunea panoului ca si procent din parintele sau
     * @param procentualDimension 
     */
    public void setProcentualSize(Dimension procentualDimension) {
        Dimension parentSize = this.getOwner().getSize();
        
        //Calculez noua dimensiune
        int newWidth = (parentSize.width * procentualDimension.width) / 100;
        int newHeight = (parentSize.height * procentualDimension.height) / 100;
        this.setSize(newWidth,newHeight);
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      metode pentru adaugare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Add a component to center
     * @param comp
     */
    public void setCenterComponent(Component comp) {
        this.add(comp,BorderLayout.CENTER);
    }

    /**
     * Add a component to center
     * @param comp
     */
    public void setTopComponent(Component comp) {
        this.add(comp,BorderLayout.NORTH);
    }

    /**
     * Add a component to center
     * @param comp
     */
    public void setLeftComponent(Component comp) {
        this.add(comp,BorderLayout.WEST);
    }

    /**
     * Add a component to center
     * @param comp
     */
    public void setRightComponent(Component comp) {
        this.add(comp,BorderLayout.EAST);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Modificare textului pe componente
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Seteaza textul pentru butonul ok
     * @param text_ok
     */
    public void setTextOk(String text_ok) {
        this.text_ok  = text_ok;
        buttonOk.setText(text_ok);
    }

    /**
     * Seteaza textul pentru butonul cancel
     * @param text_cancel
     */
    public void setTextCancel(String text_cancel) {
        this.text_cancel = text_cancel;
        buttonCancel.setText(text_cancel);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Modificare starea butoanelor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Seteaza starea butonului ok
     * @param isEnabled
     */
    public void setEnableOk(boolean isEnabled) {
        buttonOk.setEnabled(isEnabled);
    }

    /**
     * Seteaza starea butonului ok
     * @param isEnabled
     */
    public void setEnableCancel(boolean isEnabled) {
        buttonCancel.setEnabled(isEnabled);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Actiunile asupra Dialogului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Lansez actiunea OK
     */
    private void shouldFireActionOk() {
        if (buttonOk.isEnabled())
            fireActionOk();
    }

    /**
     * Lansez actiunea OK
     */
    public abstract void fireActionOk();

    /**
     * Lansez actiunea CANCEL
     */
    private void shouldFireActionCancel() {
        if (buttonCancel.isEnabled())
            fireActionCancel();
    }

    /**
     * Lansez actiunea de CANCEL
     */
    public abstract void fireActionCancel();

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Ascultatori pentru dialog
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     *  <p style="margin-top: 0">
     *  Clasa ce reprezinta un ascultator pe butoanele de ok si Cancel
     *  </p>
     * @author iulian
     */
    ActionListener buttonListener = new ActionListener() {
        /**
         *  <p style="margin-top: 0">
         *  Invoked when an action occurs.
         *  </p>
         * @author iulian
         */
            public void actionPerformed(ActionEvent e)
            {
                String cmd = e.getActionCommand();
                if (ACTION_OK.equals(cmd)) {
                    shouldFireActionOk();
                } else
                if (ACTION_CANCEL.equals(cmd)) {
                    shouldFireActionCancel();
                }
            }
        };

    /**
     *  <p style="margin-top: 0">
     *  Clasa ce reprezinta un ascultator pe Fereastra principala
     *  </p>
     * @author iulian
     */
    WindowListener windowListener = new WindowAdapter() {
        @Override
          public void windowClosing(WindowEvent e) {
            shouldFireActionCancel();
          }
    };

}
