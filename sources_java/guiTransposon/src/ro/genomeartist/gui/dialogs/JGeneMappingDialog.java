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
import ro.genomeartist.gui.controller.genes.GeneItemWrapper;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.interfaces.ILocalManager;
import ro.genomeartist.gui.mainpanels.genes.JGeneMappingPane;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  Dialogul wrapper peste panoul cu afisearea unui Partial Result
 * @author iulian
 */
public class JGeneMappingDialog extends JDialog {
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 400;
    private static final int BUTTON_WIDTH = 100;
    //Constante
    private static final String BUTTON_OK = "   Ok   ";
    //Constante pentru actiuni
    private static final String ACTION_OK = "ok";
    private static final String ACTION_SET = "set";
    
    //Managerii
    private IGlobalManager globalManager;
    private ILocalManager localManager;
    private GeneItemWrapper geneItemWrapper;

    private JPanel middlePane;
    private JPanel bottomPane;
        private JButton buttonSet;
        private JButton buttonOk;



    /**
     * Dialog de afisare partial result
     */
    public JGeneMappingDialog(IGlobalManager globalManager, 
            String title, boolean modal,
            GeneItemWrapper geneItemWrapper )  {
        super(globalManager.getTheRootFrame(), title, modal);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);

        //Setez managerii
        this.globalManager = globalManager;
        this.geneItemWrapper = geneItemWrapper;

        //Initialize the dialog
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Middle pane
        middlePane = new JGeneMappingPane(globalManager,geneItemWrapper);
        this.add(middlePane,BorderLayout.CENTER);

        //Making the buttons
        bottomPane = new JPanel();
        bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.X_AXIS));
        bottomPane.add(Box.createHorizontalGlue());
            buttonOk = new JButton(BUTTON_OK);
            buttonOk.setActionCommand(ACTION_OK);
            buttonOk.addActionListener(buttonListener);
        bottomPane.add(buttonOk);
        bottomPane.add(Box.createHorizontalGlue());
        this.add(bottomPane,BorderLayout.SOUTH);

        //Set it's location
        setLocationRelativeTo(globalManager.getTheRootFrame());
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
                if (ACTION_OK.equals(cmd)) {
                    JGeneMappingDialog.this.dispose();
                } 
            }
        };
}
