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
package ro.genomeartist.components.toolbar;

import javax.swing.JToolBar;

/**
 * A toolbar that comes with a preconfigured set of settings
 * @author iulian
 */
public class JConfiguredToolbar extends JToolBar {

    /**
     * Construiesc apeland in super si configurez
     */
    public JConfiguredToolbar(String name, int orientation) {
        super(name, orientation);
        configureToolbar();
    }

    /**
     * Construiesc apeland in super si configurez
     */
    public JConfiguredToolbar(String name) {
        super(name);
        configureToolbar();
    }

    /**
     * Construiesc apeland in super si configurez
     */
    public JConfiguredToolbar(int orientation) {
        super(orientation);
        configureToolbar();
    }

    /**
     * Construiesc apeland in super si configurez
     */
    public JConfiguredToolbar() {
        configureToolbar();
    }
    
    /**
     * Fac configurarea initiala de toolbar
     */
    private void configureToolbar() {
        this.setFocusable(false); //nu am nevoie de focus aici
        this.setFloatable(false); //Daca toolbarul pluteste sau nu
        this.setRollover(true);
    }
}
