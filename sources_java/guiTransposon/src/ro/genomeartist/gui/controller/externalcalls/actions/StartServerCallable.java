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
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author iulian
 */
public class StartServerCallable extends AbstractProgressCallable<Boolean> {

    Process process = null;
    Integer shmMemoryId = 0;
    /**
     * Pregatesc argumetele
     * @param searchQuery
     * @param outputFile
     */
    public StartServerCallable(Integer ShmMemoryId) {
        shmMemoryId = ShmMemoryId;
    }

    /**
     * Fac cautarea
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
        if (process == null){
            String args[]= new String[3];
            args[0] = ReadOnlyConfiguration.getString("serverFile");
            args[1] = ReadOnlyConfiguration.getString("PARAM_FISIERE");
            args[2] = shmMemoryId.toString();
            
            ProcessBuilder pbuild;
                pbuild = new ProcessBuilder(args);

                String saveOutputToFile = System.getenv("FILEOUTPUT");

                try {
                    Integer toSave=Integer.parseInt(saveOutputToFile);
                    if (toSave == 1)
                    {
                        String serverLogFileName = "server_log_"+shmMemoryId.toString()+".log";
                        File log = new File(serverLogFileName);
                       pbuild.redirectErrorStream(true);
                       pbuild.redirectOutput(log);
                       System.out.println("Am creat un nou proces server cu output directat in fisier");
                    }
                }
                catch (Exception e) {
                }
                process = pbuild.start();
        }
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
