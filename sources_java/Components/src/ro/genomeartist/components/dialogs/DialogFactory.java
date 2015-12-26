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

import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

/**
 * Clasa cu metode statice de afisare warninguri, erori si informatii
 * @author iulian
 */
public class DialogFactory {

    /**
     * Afisez un dialog cu diverse informatii
     * @param rootFrame
     * @param title
     * @param message
     */
    public static void showInformationDialog(Frame rootFrame, String title, String message) {
         JOptionPane.showMessageDialog(rootFrame,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    /**
     * Afisez un dialog cu diverse informatii
     * @param rootFrame
     * @param title
     * @param message
     */
    public static void showErrorDialog(Frame rootFrame, String title, String message) {
         JOptionPane.showMessageDialog(rootFrame,
            message,
            title,
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    /**
     * Afisez un dialog cu diverse informatii
     * @param rootFrame
     * @param title
     * @param message
     */
    public static void showWarningDialog(Frame rootFrame, String title, String message) {
         JOptionPane.showMessageDialog(rootFrame,
            message,
            title,
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    /**
     * Afisez o intrebare cu da sau nu
     * @param rootFram
     * @param title
     * @param message
     * @return True daca s-a raspouns da, False daca s-a raspuns nu
     */
    public static boolean showQuestionDialog(Frame rootFrame, String title, String message) {
        return showQuestionDialog(rootFrame,title,message,"   Da   ","   Nu   ");
    }

    /**
     * Afisez o intrebare cu da sau nu
     * @param rootFram
     * @param title
     * @param message
     * @return True daca s-a raspouns da, False daca s-a raspuns nu
     */
    public static boolean showQuestionDialog(Frame rootFrame, String title, String message, String option1, String option2) {
        Object[] options = {option1,
                            option2};

        //Afisez dialogul
        int n = JOptionPane.showOptionDialog(rootFrame,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            options,  //the titles of buttons
            options[0]); //default button title

        //Verific raspunsul
        if ( n == 0 ) return true;
        else return false;
    }

    /**
     * Afisez o intrebare cu mai multe optiuni
     * @param rootFram
     * @param title
     * @param message
     * @return True daca s-a raspouns da, False daca s-a raspuns nu
     */
    public static int showQuestionDialog(Frame rootFrame, String title, String message, Object[] options) {
        //Afisez dialogul
        int n = JOptionPane.showOptionDialog(rootFrame,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            options,  //the titles of buttons
            options[0]); //default button title

        return n;
    }

    /**
     * Afisez o intrebare cu mai multe optiuni
     * @param rootFram
     * @param title
     * @param message
     * @return True daca s-a raspouns da, False daca s-a raspuns nu
     */
    public static Object showOptionDialog(Frame rootFrame, String title, String message, 
            String[] selectionValues, String[] options) {
        int okOption = 0;

        //Construiesc panoul
        JOptionPane    pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE,
                                              JOptionPane.OK_CANCEL_OPTION, null,
                                              options, options[0]);
        //Setez parametrii de pornire
        pane.setWantsInput(true);
        pane.setSelectionValues(selectionValues);
        pane.setInitialSelectionValue(selectionValues[0]);
        pane.setComponentOrientation(((rootFrame == null) ?
	    JOptionPane.getRootFrame() : rootFrame).getComponentOrientation());

        //Afisez dialogul
        int style = JRootPane.PLAIN_DIALOG;
        pane.setMessageType(JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = pane.createDialog(rootFrame, title);

        pane.selectInitialValue();
        dialog.show();
        dialog.dispose();

        //Obtin butonul apasat
        int selectedOption = JOptionPane.CLOSED_OPTION;
        Object selectedValue = pane.getValue();
        if(selectedValue == null) {
            selectedOption = JOptionPane.CLOSED_OPTION;
        } else
        if(options == null) {
            if(selectedValue instanceof Integer)
                selectedOption = ((Integer)selectedValue).intValue();
            else selectedOption = JOptionPane.CLOSED_OPTION;
        } else {
            for(int counter = 0, maxCounter = options.length;
                counter < maxCounter; counter++) {
                if(options[counter].equals(selectedValue)) {
                    selectedOption = counter;
                    break;
                }
            }
        }


        //Obtin valoarea selectata
        if (selectedOption == okOption) {
            Object value = pane.getInputValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return null;
            }
            return value;
        } else
            return null;
    }

}
