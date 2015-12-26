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

import ro.genomeartist.components.dialogs.DialogFactory;
import ro.genomeartist.components.modalpanel.progresspanel.JModalProgressPanel;
import ro.genomeartist.components.modalpanel.progresspanel.JProgressPanel;
import ro.genomeartist.components.swingworkers.IWorker;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 * A worker that executes a callable class and displays a progress
 * @author iulian
 */
public class JProgressSwingWorker<T> extends SwingWorker<T, String>
        implements IWorker, ProgressInfoManager, PropertyChangeListener {
    private final Frame rootFrame;
    protected JModalProgressPanel progressPanel;
    private boolean hasFinished;
    private boolean isPanelVisible;
    private int delay;
    
    //Bundle de mesaje
    private ResourceBundle resourceBundle = null;

    //Valoare default pentru delay
    private static final int DEFAULT_DELAY_STEP = 100;
    private static int DEFAULT_DELAY = 500;

    //Outside method to call
    protected AbstractProgressCallable <T> callableMethod;
    protected T result;

    //Option to display a standard error no mather what
    private Vector<String> errorVector;
    private String standardError = null;
    
    

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Constructor area
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * DELEGATE CONTRUCTION
     * @param rootFrame
     * @param callableMethod
     */
    public JProgressSwingWorker(Frame rootFrame, AbstractProgressCallable <T> callableMethod) {
        this(rootFrame, "", callableMethod);
    }

    /**
     * DELEGATE CONTRUCTION
     * @param rootFrame
     * @param callableMethod
     * @param mode      JProgressPanel.INDETERMINATE or JProgressPanel.DETERMINATE
     */
    public JProgressSwingWorker(Frame rootFrame, AbstractProgressCallable <T> callableMethod,
            int mode) {
        this(rootFrame, "", callableMethod, mode);
    }


    /**
     * DELEGATE CONTRUCTION
     * @param rootFrame
     * @param description
     * @param callableMethod
     */
    public JProgressSwingWorker(Frame rootFrame, String description,
            AbstractProgressCallable <T> callableMethod) {
        this(rootFrame, description, callableMethod, DEFAULT_DELAY, JProgressPanel.INDETERMINATE);
    }


    /**
     *
     * @param rootFrame
     * @param description
     * @param callableMethod
     * @param mode      JProgressPanel.INDETERMINATE or JProgressPanel.DETERMINATE
     */
    public JProgressSwingWorker(Frame rootFrame, String description,
            AbstractProgressCallable <T> callableMethod, int mode) {
        this(rootFrame, description, callableMethod, DEFAULT_DELAY, mode);
    }

    /**
     * MAIN CONSTRUCTOR
     * BUild a progress displayable worker
     * @param rootFrame     Root frame of the application
     * @param description   Description shown
     * @param callableMethod The method which will be called
     * @param delay         The delay waited before deciding to show the progress
     * @param mode      JProgressPanel.INDETERMINATE or JProgressPanel.DETERMINATE
     */
    public JProgressSwingWorker(Frame rootFrame, String description,
            AbstractProgressCallable <T> callableMethod, int delay, int mode) {
        this.rootFrame = rootFrame;

        //Read the message bundle
        this.resourceBundle = 
                ResourceBundle.getBundle("ro/genomeartist/components/swingworkers/progressworker/messages",
                    rootFrame.getLocale());
        
        //Show a progress panel
        progressPanel = new JModalProgressPanel(rootFrame);

        //Store the callable method
        this.callableMethod = callableMethod;
        callableMethod.setProgressInfoManager(this);
        this.addPropertyChangeListener(this);
        
        //Set the panel delay
        this.delay = delay;

        //Use a description
        progressPanel.setDisplayMode(description, mode);
        
        //Configurez vectorul de erori
        errorVector = new Vector<String>();
    }

    /**
     * Set the panel displayed text
     * @param description
     */
    public void setDescription(String description) {
        progressPanel.setDisplayMode(description, JProgressPanel.INDETERMINATE);
    }

    /**
     * Set a standard error message. This will surpress errors from the processing
     *  and will display the message no mather what
     */
    public void setStandardErrorMessage(String errorMessage) {
        standardError = errorMessage;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Processing begining area
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Make this worker process
     */
    public T executeTask() {
        //Start the excution of background task
        this.execute();
        
        //Impart delay-ul in cuante de 
        int steps = delay / DEFAULT_DELAY_STEP;
        hasFinished = false;
        for (int i = 0; i < steps; i++) {
            if (this.getState() == StateValue.DONE) {
                hasFinished = true;
                break;
            }
            
            //Force wait
            try {
                Thread.sleep(DEFAULT_DELAY_STEP);
            } catch (InterruptedException interruptedException) {
                //At least we tried
            }
        }
        
        //Doar daca nu s-a terminat
        isPanelVisible = false;
        hasFinished = this.getState() == StateValue.DONE;
        if (!hasFinished) {
            isPanelVisible = true;
            progressPanel.setVisible(true);
        }
        
        //Get the result
        try {
            //Get the result
            result = get();
        } catch (Exception ex) {
            //Return a null result
            result = null;

            //Log the error
            Logger.getLogger(JProgressSwingWorker.class.getName()).log(Level.WARNING, 
                    "Exceptie la rulare Worker", ex);
            
            //Build a error message
            StringBuilder error = new StringBuilder();
            if (standardError != null)
                error.append(standardError);
            else error.append(ex.getMessage());
            
            if (!errorVector.isEmpty())
                error.append("\n")
                     .append(resourceBundle.getString(MessagesKeys.ERROR_CAUSE))
                     .append("\n");
            
            for (int i = 0; i < errorVector.size(); i++) {
                String stringItem = errorVector.elementAt(i);
                error.append(i+1).append(". ").append(stringItem).append("\n");
            }
            DialogFactory.showErrorDialog(rootFrame, resourceBundle.getString(MessagesKeys.ERROR_TITLE),
                    error.toString());
        }
        
        //Return the result
        return result;
    }

    /**
     * The background executing task
     * @return
     * @throws Exception
     */
    @Override
    protected T doInBackground() throws Exception {
        return callableMethod.call();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Process monitoring area
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Seteaza valoarea panoului. Metoda este apelata din Callable
     * @param value
     */
    public void setProgressValue(int value) {
        this.setProgress(value);
    }

     /**
      * Ascult modificarea valorii pentru progress din taskul de background
      * @param evt
      */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            progressPanel.setProgress(progress);
        }
    }

    /**
     * Seteaza textul descriptiv al panoului
     * @param info
     */
    public void setProgressInfo(String info) {
        this.publish(info);
    }

    /**
     * Adaug feedback despre eroare
     * @param message 
     */
    public void addErrorMessage(String message) {
        errorVector.add(message);
    }
    
    @Override
    protected void process(List<String> chunks) {
        String info = chunks.get(chunks.size() - 1);
        progressPanel.setText(info);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Finalizing area
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Method invoked when background task finishes
     */
    @Override
    protected void done() {
        hasFinished = true;
        closeProgressPanel();
    }

    /**
     * Close the progress panel
     */
    private void closeProgressPanel() {
        //This action will be tun in the main thread
        //sequencialy with the openProgressPanel method
        if (isPanelVisible) {
            progressPanel.setVisible(false);
            progressPanel.dispose();
            isPanelVisible = false;
        }
    }
}
