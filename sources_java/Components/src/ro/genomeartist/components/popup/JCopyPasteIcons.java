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

package ro.genomeartist.components.popup;

import ro.genomeartist.components.icons.IconDelegate;

/**
 * Icon pentru TreeTable
 *
 * @author iulian
 */
public class JCopyPasteIcons  {
    //Incarca iconitele
    private static final String ICONS_FOLDER = "/ro/genomeartist/components/popup/images/";
    public static final IconDelegate CUT = 
            new IconDelegate(ICONS_FOLDER+"cut16.gif");
    public static final IconDelegate COPY = 
            new IconDelegate(ICONS_FOLDER+"copy16.gif");
    public static final IconDelegate PASTE = 
            new IconDelegate(ICONS_FOLDER+"paste16.gif");
    
    //Nu se instantiaza
    private JCopyPasteIcons() {}
}