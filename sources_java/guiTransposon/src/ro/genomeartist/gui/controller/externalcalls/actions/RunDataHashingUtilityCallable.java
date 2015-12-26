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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Contruiesc o actiune callable ce masoara progresul
 * @author iulian
 */
public class RunDataHashingUtilityCallable extends AbstractProgressCallable<Boolean>{
    private File rawFile;

    /**
     * Constructorul principal
     * @param rawFile
     */
    public RunDataHashingUtilityCallable(File rawFile) {
        this.rawFile = rawFile;
    }


    /**
     * Actiunea propriu-zisa
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
       String args[]= new String[4];
       args[0] = ReadOnlyConfiguration.getString("hashingFile");
       args[1] = rawFile.getPath();
       args[2] = ExternalLink.PARAM_WINDOW_SIZE+"";
       args[3] = "single";

       ProcessBuilder pbuild;

        try {
            pbuild = new ProcessBuilder(args);
            Process process = pbuild.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            //Citesc fisierul tinand cont de taguri
            this.setProgressRange(0, 100);
            TaggedFileConsumerCallable taggedFileConsumer =
                    new TaggedFileConsumerCallable(br, "end");
            taggedFileConsumer.setProgressInfoManager(this);
            taggedFileConsumer.call();
        } catch (IOException iOException) {
            System.err.println("Eroare la rulare data hashing utility");
            iOException.printStackTrace();
        }

       //Default value
       return Boolean.TRUE;
    }

}
