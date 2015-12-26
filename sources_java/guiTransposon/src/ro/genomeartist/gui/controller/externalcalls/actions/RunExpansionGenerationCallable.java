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

package ro.genomeartist.gui.controller.externalcalls.actions;

import ro.genomeartist.components.swingworkers.progressworker.AbstractProgressCallable;
import ro.genomeartist.gui.controller.externalcalls.ExternalLink;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *
 * @author iulian
 */
public class RunExpansionGenerationCallable extends AbstractProgressCallable<Boolean> {

    /**
     * Fac actiunea propriu zis-a
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
       String args[]= new String[3];
       args[0] = ReadOnlyConfiguration.getString("expansionFile");
       args[1] = ReadOnlyConfiguration.getString("EXPANSION_TABLE_FILE");
       args[2] = ExternalLink.PARAM_EXPANDED_SIZE+"";

       ProcessBuilder pbuild;

        try {
            pbuild = new ProcessBuilder(args);
            Process process = pbuild.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            System.out.printf("Output of running %s is:",
                    Arrays.toString(args));

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                //Consume content
            }
        } catch (IOException iOException) {
            System.err.println("Eroare la expansion generation");
            iOException.printStackTrace();
        }

       //Valoare default
       return Boolean.TRUE;
    }

}
