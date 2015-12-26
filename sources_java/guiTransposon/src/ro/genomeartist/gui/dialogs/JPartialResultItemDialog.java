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
import ro.genomeartist.gui.controller.partialresult.PartialResultItem;
import ro.genomeartist.gui.mainpanels.partialresult.JPartialResultItemPane;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  Dialogul wrapper peste panoul cu afisearea unui Partial Result
 * @author iulian
 */
public class JPartialResultItemDialog extends JDialog {
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 400;
    private static final int BUTTON_WIDTH = 100;

    private JPanel middlePane;
    private JPanel bottomPane;

    /**
     * Dialog de afisare partial result
     */
    public JPartialResultItemDialog(Frame owner, String title, boolean modal,
            PartialResultItem partialResultItem )  {
        super(owner, title, modal);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);

        //Initialize the dialog
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Middle pane
        middlePane = new JPartialResultItemPane(partialResultItem);
        this.add(middlePane,BorderLayout.CENTER);

        //Making the buttons
        bottomPane = new JPanel();
            JButton button = new JButton("   Ok   ");
            button.setActionCommand("ok");
            button.addActionListener(buttonListener);
        bottomPane.add(button);
        this.add(bottomPane,BorderLayout.SOUTH);

        //Set it's location
        setLocationRelativeTo(owner);
    }

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
                if (cmd.equals("ok")) JPartialResultItemDialog.this.dispose();
            }
        };
}
