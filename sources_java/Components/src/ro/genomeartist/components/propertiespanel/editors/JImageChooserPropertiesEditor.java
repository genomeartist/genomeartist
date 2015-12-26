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

package ro.genomeartist.components.propertiespanel.editors;

import ro.genomeartist.components.filechooser.filters.ImageSpecificFileFilter;
import ro.genomeartist.components.propertiespanel.IEditorChangeListener;
import ro.genomeartist.components.propertiespanel.IPropertiesEditor;
import ro.genomeartist.components.utils.ImageUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author iulian
 */
public class JImageChooserPropertiesEditor extends JPanel
        implements IPropertiesEditor, ActionListener {
    private final Frame rootFrame;
    private JLabel imageLabel;
    private JButton selectButton;
    private JFileChooser fileChooser;
    
    //Variabila stocata
    private BufferedImage storedImage;
    
    //Dimensiunile labelului de afisare
    private int width;
    private int height;
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se extind constructorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Constructor secundar
     * @param rootFrame
     * @param columns
     */
    public JImageChooserPropertiesEditor(Frame rootFrame, int columns) {
        this(rootFrame, columns, 100, 100);
    }

    /**
     * Constructorul principal
     * @param rootFrame
     * @param columns
     * @param fileFilter
     */
    public JImageChooserPropertiesEditor(Frame rootFrame, int columns, int width, int height) {
        super();
        //this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setLayout(new BorderLayout());
        
        //Variabila de setare
        storedImage = null;
        
        //Variabile de dimensiune
        this.width = width;
        this.height = height;
        
        //Configurez componentele
        this.rootFrame = rootFrame;
        this.imageLabel = new JLabel("No image");
        this.imageLabel.setBorder(BorderFactory.createEtchedBorder());
        this.imageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        this.imageLabel.setBackground(Color.WHITE);
        this.imageLabel.setOpaque(true);
        this.selectButton = new JButton("..");

        //Fac layout-ul
        //this.add(Box.createHorizontalGlue());
        this.add(imageLabel, BorderLayout.CENTER);
        this.add(selectButton, BorderLayout.EAST);

        //Fac chooserul
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new ImageSpecificFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

        //Adaug actiunea de deschidere panou de selectie
        this.selectButton.addActionListener(this);
    }


    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Actiunea pe buton
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Actiunea pe butonul de preluare
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        //Obtin fisierul indicat
        int returnVal = fileChooser.showOpenDialog(JImageChooserPropertiesEditor.this.rootFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage loadedImage = ImageIO.read(file);
                setImage(loadedImage);
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * Fac actiunea de afisare imagine
     * @param image 
     */
    private void setImage(BufferedImage image) {
        this.storedImage = image;

        //Pun un avatar al imagini in icon
        BufferedImage resizedIcon = ImageUtils.resizeImage(image, width, height);
        imageLabel.setText("");
        imageLabel.setIcon(new ImageIcon(resizedIcon));
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se implementeaza interfata IPropertiesEditor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin containerul global al editorului
     * @return
     */
    public JComponent getContainerComponent() {
        return this;
    }

    /**
     * Obtine componenta care realizeaza editarea propriu-zisa
     * @return
     */
    public JComponent getEditorComponent() {
        return imageLabel;
    }

    /**
     * Seteaza numele componentei
     * @param name
     */
    @Override
    public void setEditorName(String name) {
        this.setName(name);
        imageLabel.setName(name);
    }

    /**
    * Se seteaza valoarea ce se editeaza
    * @param objectClass
    * @param renderedObject
    */
    public void setValue(Class objectClass, Object renderedObject) {
        //Setez obiectul in textfield
        if (BufferedImage.class == objectClass) {
            setImage((BufferedImage) renderedObject);
        } else
            throw new UnsupportedOperationException(objectClass + " not supported in JImageChooserPropertiesEditor");
    }

    /**
     * Se obtione valoarea din textfield cu cast la clasa specificata
     * @param objectClass
     * @return
     */
    public Object getValue(Class objectClass) {
        //Obtin obiectul din textfield
        if (BufferedImage.class == objectClass) {
            return storedImage;
        } else
            throw new UnsupportedOperationException(objectClass + " not supported in JImageChooserPropertiesEditor");
    }

    /**
     * Activare/Dezactivare editor
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        //Propag si in copii
        imageLabel.setEnabled(enabled);
        selectButton.setEnabled(enabled);
    }


    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Managementul actiunilor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Adaug un ascultator pentru schimbare valori
     * @param listener
     */
    public void addEditorChangeListener(IEditorChangeListener listener) {
        //Listeners not supported
    }

    /**
     * Deinregistrez un ascultator pentru schimbare valori
     * @param listener
     */
    public void removeEditorChangeListener(IEditorChangeListener listener) {
        //Listeners not supported
    }
}
