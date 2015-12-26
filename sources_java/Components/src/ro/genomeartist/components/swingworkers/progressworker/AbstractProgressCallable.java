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

import java.util.concurrent.Callable;

/**
 *
 * @author iulian
 */
public abstract class AbstractProgressCallable<V> 
        implements Callable<V>, ProgressInfoManager {
    //Constante pentru valori granite
    private static final int INF_LIMIT = 0;
    private static final int SUP_LIMIT = 100;

    //Variabile pentru progres
    private int inferiorLimit = INF_LIMIT;
    private int superiorLimit = SUP_LIMIT;

    //Pastrez un ascultator
    private ProgressInfoManager progressInfoManager;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Local setters
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Setez progres info managerul
     * @param progressInfoManager
     */
    public void setProgressInfoManager(ProgressInfoManager progressInfoManager) {
        this.progressInfoManager = progressInfoManager;
    }

    /**
     * Setez limita inferioara
     * @param inferiorLimit
     */
    public void setInferiorLimit(int inferiorLimit) {
        this.inferiorLimit = inferiorLimit;
    }

    /**
     * Setez limita superioara
     * @param superiorLimit
     */
    public void setSuperiorLimit(int superiorLimit) {
        this.superiorLimit = superiorLimit;
    }


    /**
     * Setez limitele in care intra progresul ce se va inregistra ulterior
     * @param inferiorLimit
     * @param superiorLimit
     */
    public void setProgressRange(int inferiorLimit, int superiorLimit) {
        setInferiorLimit(inferiorLimit);
        setSuperiorLimit(superiorLimit);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    ProgressInfoManager implementation
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Setez textul de informare
     * @param info
     */
    public void setProgressInfo(String info) {
        if (progressInfoManager != null)
            progressInfoManager.setProgressInfo(info);
    }

    /**
     * Setez valoarea progresului
     * @param value
     */
    public void setProgressValue(int value) {
        if (progressInfoManager != null) {
            int var1 = inferiorLimit*(SUP_LIMIT-value);
            int var2 = superiorLimit*value;
            int newValue = (var1+var2)/SUP_LIMIT;
            progressInfoManager.setProgressValue(newValue);
        }
    }

    /**
     * Adaug un mesaj de eroare la stringul de mesaje
     * @param error 
     */
    public void addErrorMessage(String error) {
        if (progressInfoManager != null) 
            progressInfoManager.addErrorMessage(error);
    }
    

}
