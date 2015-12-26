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

/**
 *
 * @author iulian
 */
public class JBooleanWrapper {
    private boolean var = false;

    public JBooleanWrapper() {
    }

    public boolean isTrue() {
        return var;
    }

    public void setTrue() {
        this.var = true;
    }

    public void setFalse() {
        this.var = false;
    }

    public void setInverse() {
        if (this.var == true) this.var=false;
        else this.var = true;
    }
}
