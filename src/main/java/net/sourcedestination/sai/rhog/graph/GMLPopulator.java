package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.GMLBridge;
import dlg.core.DLG;
import jdk.nashorn.api.scripting.URLReader;
import net.sourcedestination.sai.db.DBPopulator;
import net.sourcedestination.sai.db.graph.Graph;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by jmorwick on 6/30/17.
 */
public class GMLPopulator extends DBPopulator {

    private static Logger logger = Logger.getLogger(GMLPopulator.class.getCanonicalName());

    private URL url = null;
    private File file = null;

    private int numGraphs = -1;

    public static Iterator<SaiDlgAdapter> gmlCollectionToDLG(final BufferedReader in) {
        return FileFormatUtil.fileToDLGs(in, new GMLBridge()::load);
    }

    public static String dlgToGml(DLG g) {
        return FileFormatUtil.serialize(g, new GMLBridge());
    }

    public static String saiToGml(Graph g, String featureName, String defaultLabel) {
        DLGFactory f = new DLGFactory(featureName, defaultLabel);
        return dlgToGml(f.apply(g));
    }

    /** reference this static method as a deserializer object where needed */
    public static SaiDlg gmlToDlg(String gml) {
        DLGReader reader = new GMLBridge()::load;
        return reader.apply(gml);
    }

    /** reference this static method as a serializer object where needed */
    public static String saiToGml(Graph g) {
        return saiToGml(g,"","");
    }

    public static int countGraphsInGmlFile(Reader in) {
        Scanner s = new Scanner(in);
        s.useDelimiter("\\s*graph\\s*\\[");
        int instances = 0;
        for(;s.hasNext();s.next()) instances++;
        return instances;
    }

    public GMLPopulator(File gmlFile) {
        this.file = gmlFile;
    }

    public GMLPopulator(URL gmlFile) {
        this.url = gmlFile;
    }

    @Override
    public int getNumGraphs() {
        if (numGraphs == -1) {
            if (url != null) {
                numGraphs = countGraphsInGmlFile(new URLReader(url));
            } else if (file != null) {
                try {
                    numGraphs = countGraphsInGmlFile(new FileReader(file));
                } catch (FileNotFoundException e) {
                    logger.log(Level.ALL, "can't create graph stream, no such file: " + file, e);
                }

            }
        }
        return numGraphs;
    }

    @Override
    public Stream<Graph> getGraphStream() {
        Reader r = null;
        if(url != null) {
            r = new URLReader(url);
        } else if(file != null) {
            try {
                r = new FileReader(file);
            } catch(FileNotFoundException e) {
                logger.log(Level.ALL, "can't create graph stream, no such file: " +file, e);
                return Stream.empty();
            }

        }
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                    gmlCollectionToDLG(new BufferedReader((r)) {
                        // rhog lib uses .ready() to see if data is left in the stream.
                        // this isn't the default behavior of this function when dealing with streams other than files,
                        // so this fixes it to work that way.
                        public boolean ready() {
                            int b = -1;
                            try {
                                mark(2);
                                b = read();
                                reset();
                            } catch(IOException e) {}
                            return b != -1;
                        }
                    }),
                    Spliterator.ORDERED),
                    false);
    }
}
