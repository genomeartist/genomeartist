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

package ro.genomeartist.components.modalpanel.progresspanel;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Construiesc un panou de progress pentru un task
 * @author iulian
 */
public class JProgressPanel extends JPanel {
    //Constante
    private static final int SPACER_WIDTH = 10;
    private static final int SPACER_HEIGHT = 10;

    //Moduri
    public static final int INDETERMINATE = 0;
    public static final int DETERMINATE = 1;
    
    //Structura ferestrei
    private JPanel textContainer;
        private JLabel labelText;
    private JPanel progressContainer;
        private JProgressBar progressBar;

    /**
     * Construiesc o bara de progress
     */
    public JProgressPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        //glue
        this.add(Box.createVerticalGlue());
        
        //Setez textul
        textContainer = new JPanel();
        textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.X_AXIS));
            textContainer.add(Box.createHorizontalStrut(SPACER_WIDTH));
                labelText = new JLabel("Se incarca ceva");
            textContainer.add(labelText);
            textContainer.add(Box.createHorizontalGlue());
        this.add(textContainer);

        //Setez progressbarul
        progressContainer = new JPanel();
        progressContainer.setLayout(new BoxLayout(progressContainer, BoxLayout.X_AXIS));
            progressContainer.add(Box.createHorizontalStrut(SPACER_WIDTH));
                progressBar = new JProgressBar(0, 100);
                progressBar.setValue(0);
                progressBar.setIndeterminate(false);
                progressBar.setStringPainted(true);
            progressContainer.add(progressBar);
            progressContainer.add(Box.createHorizontalStrut(SPACER_WIDTH));
        this.add(progressContainer);

        //glue
        this.add(Box.createVerticalGlue());
    }

    /**
     * Seteaza modul in care se va afisa progresul
     */
    public void setDisplayMode(int mode) {
        setDisplayMode("", mode);
    }


    /**
     * Seteaza modul in care se va afisa progresul
     */
    public void setDisplayMode(String title,int mode) {
        //Setez titlul
        labelText.setText(title);

        //Setez modul
        switch (mode) {
            case DETERMINATE:
                progressBar.setValue(0);
                progressBar.setStringPainted(true);
                progressBar.setIndeterminate(false);
                break;
            default:
            case INDETERMINATE:
                progressBar.setValue(0);
                progressBar.setStringPainted(false);
                progressBar.setIndeterminate(true);
                break;
        }
    }

    /**
     * Seteaza progresul inregistrat
     * @param value
     */
    public void setText(String text) {
        labelText.setText(text);
    }

    /**
     * Seteaza progresul inregistrat
     * @param value
     */
    public void setProgress(int value) {
        progressBar.setValue(value);
    }
}
