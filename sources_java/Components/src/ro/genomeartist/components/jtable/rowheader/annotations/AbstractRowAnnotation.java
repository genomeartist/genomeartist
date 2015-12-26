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
package ro.genomeartist.components.jtable.rowheader.annotations;

import ro.genomeartist.components.jtable.rowheader.RowAnnotation;
import ro.genomeartist.components.jtable.rowheader.RunnableSuggestion;
import java.util.Vector;

/**
 * Clasa ce serveste drept model pentru implementarea de anotatii
 * @author iulian
 */
public abstract class AbstractRowAnnotation implements RowAnnotation {
    protected String tooltipText;
    protected Vector<RunnableSuggestion> suggestions;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *       Constructor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Constructorul default
     */
    public AbstractRowAnnotation() {
        tooltipText = null;
        suggestions = null;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *  Getteri (Interfata RowAnnotation)
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin textul ce descrie anotatia
     * @return 
     */
    public String getTooltipText() {
        return tooltipText;
    }    
    
    /**
     * Obtin sugestiile pentru rezolvarea anotatiilor
     * @return 
     */
    public Vector<RunnableSuggestion> getSuggestions() {
        return suggestions;
    }
    
    /**~~~~~~~~~~~~~
     *  Setteri
     *~~~~~~~~~~~~~~*/

    /**
     * Sete tooltip textul
     * @param tooltipText 
     */
    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }    
    
    /**
     * Setez vectorul de sugestii
     * @param suggestions 
     */
    public void setSuggestions(Vector<RunnableSuggestion> suggestions) {
        this.suggestions = suggestions;
    }
}
