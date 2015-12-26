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

package ro.genomeartist.components.textfield.validator;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author iulian
 */
public class JValidatorTextfield extends JTextField implements DocumentListener {
    private boolean isValid;
    private IValidateString validator;
    private Border originalBorder;
    private Border problemBorder;

    //Constanta pentru culoare
    private static final Color ERROR_COLOR = new Color(255, 0, 0);
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Constructorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Construiesc componenta fara validator
     */
    public JValidatorTextfield() {
        this(null,null);
    }

    /**
     * Construiesc componenta fara validator
     */
    public JValidatorTextfield(Border originalBorder) {
        this(null,originalBorder);
    }

    /**
     * Contruiesc componenta cu validator
     */
    public JValidatorTextfield(IValidateString validator, Border originalBorder) {
        super();
        this.getDocument().addDocumentListener(this);
        //Metoda de a selecta tot textul la click
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                JValidatorTextfield.this.selectAll();
            }
        });
        this.isValid = true;
        this.validator = validator;

        //Pastrez borderul orginal
        if (originalBorder != null)
            setBorder(originalBorder);
        this.originalBorder = getBorder();
        this.problemBorder = new ColorizingBorder(originalBorder,ERROR_COLOR);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Getter/Setter Validator
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin validatorul curent
     * @return
     */
    public IValidateString getValidator() {
        return validator;
    }

    /**
     * Setez validatorul curent
     * @param validator
     */
    public void setValidator(IValidateString validator) {
        this.validator = validator;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Punctele de intrare si iesire din clasa
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Testeaza daca valoarea curenta este una valida pentru acest panou
     * @return
     */
    public boolean isValidValue() {
        return isValid;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Etapa de validare a valorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Notificarea unui eveniment de insert
     * @param e
     */
    public void insertUpdate(DocumentEvent e) {
        validateValue();
    }

    /**
     * Notificarea unui eveniment de stergere
     * @param e
     */
    public void removeUpdate(DocumentEvent e) {
        validateValue();
    }

    /**
     * Notificarea unui eveniment de modificare
     * @param e
     */
    public void changedUpdate(DocumentEvent e) {
        validateValue();
    }

    /**
     * Validez valoarea introdusa
     */
    private void validateValue() {
        if (validator != null) {
            String text = this.getText();
            boolean localIsValid = validator.validate(text);

            //Fac modificari doar daca se schimba starea
            if (isValid != localIsValid) {
                this.isValid = localIsValid;

                if (this.isValid ) {
                    //Pun borderul original
                    setBorder(originalBorder);
                } else {
                    //Pun borderul colorat
                    setBorder(problemBorder);
                }
            }
        }
    }
}