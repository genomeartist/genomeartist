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
package ro.genomeartist.components.jtable.rowheader.implementation;

import ro.genomeartist.components.icons.IconDelegate;

/**
 *
 * @author iulian
 */
public class RowHeaderIcons {
    //Incarca iconitele
    private static final String ICONS_FOLDER = "/ro/genomeartist/components/jtable/rowheader/images/";
    public static final IconDelegate WARNING = 
            new IconDelegate(ICONS_FOLDER+"warning16.gif");
    public static final IconDelegate WARNING_SUGGESTION = 
            new IconDelegate(ICONS_FOLDER+"warning_suggestion16.gif");
    public static final IconDelegate FATAL = 
            new IconDelegate(ICONS_FOLDER+"fatal16.gif");
    public static final IconDelegate FATAL_SUGGESTION = 
            new IconDelegate(ICONS_FOLDER+"fatal_suggestion16.gif");
    //~~~
    public static final IconDelegate MULTIPLE_ANNOTATIONS = 
            new IconDelegate(ICONS_FOLDER+"multiple_annotations12.gif");
    
    //Nu se instantiaza
    private RowHeaderIcons() {}
}
