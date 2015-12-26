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
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author iulian
 */
public class StartServerCallable extends AbstractProgressCallable<Boolean> {

    /**
     * Pregatesc argumetele
     * @param searchQuery
     * @param outputFile
     */
    public StartServerCallable() {
    }

    /**
     * Fac cautarea
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
       String args[]= new String[2];
       args[0] = ReadOnlyConfiguration.getString("serverFile");
       args[1] = ReadOnlyConfiguration.getString("PARAM_FISIERE");

       ProcessBuilder pbuild;
        pbuild = new ProcessBuilder(args);
        Process process = pbuild.start();

        //Setez variabila proces
        ExternalLink.setServerProcess(process);
        
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        String stopWordServer = ReadOnlyConfiguration.getString("stopWordServer");
        //Citesc fisierul tinand cont de taguri
        this.setProgressRange(0, 100);
        TaggedFileConsumerCallable taggedFileConsumer =
                new TaggedFileConsumerCallable(br, stopWordServer);
        taggedFileConsumer.setProgressInfoManager(this);
        taggedFileConsumer.call();

        //Am terminat resetez variabila process
        ExternalLink.setServerProcess(null);

        //Wait for the end of process to be sure
        process.waitFor();
        int exitValue = process.exitValue();
        if (exitValue != 0)
            throw new Exception("Genome loading failed !");
        
        return Boolean.TRUE;
    }
}
