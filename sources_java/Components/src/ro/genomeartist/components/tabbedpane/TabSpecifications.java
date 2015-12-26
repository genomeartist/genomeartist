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

package ro.genomeartist.components.tabbedpane;

import javax.swing.Icon;

/**
 * Clasa ce defineste specificatiile pentru un tab
 * @author iulian
 */
public class TabSpecifications {
    public String title;
    public Icon icon;
    public boolean shouldBeBold;

    public TabSpecifications() {
        this.title = "";
        this.icon = null;
        this.shouldBeBold = false;
    }
}
