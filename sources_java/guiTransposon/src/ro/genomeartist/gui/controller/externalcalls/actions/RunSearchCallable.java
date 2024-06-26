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
import ro.genomeartist.gui.controller.query.SearchQuery;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author iulian
 */
public class RunSearchCallable extends AbstractProgressCallable<Boolean> {
    SearchQuery searchQuery;
    File outputFile;

    /**
     * Pregatesc argumetele
     * @param searchQuery
     * @param outputFile
     */
    public RunSearchCallable(SearchQuery searchQuery, File outputFile) {
        this.searchQuery = searchQuery;
        this.outputFile = outputFile;
    }

    /**
     * Fac cautarea
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
       String outputFileName = outputFile.getPath();
       String args[]= new String[5];
       args[0] = ReadOnlyConfiguration.getString("clientFile");
       args[1] = searchQuery.getQueryContent();
       args[2] = ReadOnlyConfiguration.getString("EXPANSION_TABLE_FILE");
       args[3] = searchQuery.getQueryName();
       args[4] = outputFileName;

       ProcessBuilder pbuild;

        pbuild = new ProcessBuilder(args);
        Process process = pbuild.start();

        //Setez variabila proces
        ExternalLink.setSearchProcess(process);

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        String stopWordClient = ReadOnlyConfiguration.getString("stopWordClient");

        //Citesc fisierul tinand cont de taguri
        this.setProgressRange(0, 100);
        TaggedFileConsumerCallable taggedFileConsumer =
                new TaggedFileConsumerCallable(br, stopWordClient);
        taggedFileConsumer.setProgressInfoManager(this);
        taggedFileConsumer.call();

        //Am terminat resetez variabila process
        ExternalLink.setSearchProcess(null);

        //Wait for the end of process to be sure
        process.waitFor();
        int exitValue = process.exitValue();
        if (exitValue != 0)
            throw new Exception("Search failed !");
        
        return Boolean.TRUE;
    }
}
