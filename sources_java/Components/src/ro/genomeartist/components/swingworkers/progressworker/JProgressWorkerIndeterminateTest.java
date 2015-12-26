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

package ro.genomeartist.components.swingworkers.progressworker;

import ro.genomeartist.components.modalpanel.progresspanel.JProgressPanel;
import ro.genomeartist.components.utils.NumberUtils;
import ro.genomeartist.components.utils.RandomDataGenerator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author iulian
 */
public class JProgressWorkerIndeterminateTest {
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Test area
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private static final AbstractProgressCallable<Integer> mainCallable =
            new AbstractProgressCallable<Integer>() {
        public Integer call() throws Exception {
            Integer integer = RandomDataGenerator.getRandomInt(10);

            //Thread.sleep(400);
            
            //Intorc rezultatul
            return integer;
        }
    };


    /**
     * Metoda main de test
     * @param args
     */
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocale(NumberUtils.LOCALE_RO);

        JButton button = new JButton("Do bg action");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            //Umplu raportul in background pentru a afisa un panou modal de incarcare
            JProgressSwingWorker fillWorker =
                    new JProgressSwingWorker(frame, "Computing",
                    mainCallable,
                    JProgressPanel.INDETERMINATE);
            fillWorker.setStandardErrorMessage("Eroare la rulare");

            //Obtin elementul de tiparit
            Integer integer = (Integer)
                    fillWorker.executeTask();
            System.out.println("Got result "+integer);
            }
        });
        frame.add(button);
        frame.pack();

        //Afisez
        frame.setVisible(true);
    }
}
