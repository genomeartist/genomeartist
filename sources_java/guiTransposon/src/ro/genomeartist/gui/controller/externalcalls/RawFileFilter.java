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

package ro.genomeartist.gui.controller.externalcalls;

import ro.genomeartist.gui.utils.MyUtils;
import java.io.File;
import java.io.FileFilter;


/**
 *
 * @author iulian
 */
public class RawFileFilter implements FileFilter {

    //Accept specified files
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = MyUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(MyUtils.RAW_EXT)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Genome ARTIST (.ga)";
    }
}

