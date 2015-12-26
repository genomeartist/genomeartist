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

import ro.genomeartist.components.jtable.rowheader.implementation.RowHeaderIcons;
import javax.swing.Icon;

/**
 *
 * @author iulian
 */
public class WarningRowAnnotation extends AbstractRowAnnotation {

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *       Constructor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Constructorul default
     */
    public WarningRowAnnotation() {
        super();
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *       Getter
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Obtine iconul in functie de contex
     * @return 
     */
    public Icon getIcon() {
        if (suggestions != null && !suggestions.isEmpty()) {
            return RowHeaderIcons.WARNING_SUGGESTION.getIcon();
        } else {
            return RowHeaderIcons.WARNING.getIcon();
        }
    }
}
