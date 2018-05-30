package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.GMLBridge;
import dlg.core.DLG;
import jdk.nashorn.api.scripting.URLReader;
import net.sourcedestination.sai.db.DBPopulator;
import net.sourcedestination.sai.db.graph.Graph;
import org.apache.log4j.Logger;

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

    private static Logger logger = Logger.getLogger(GMLPopulator.class);

    private Reader gmlFile;
    private int numGraphs;

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

    public GMLPopulator(File gmlFile) throws IOException {
        this.numGraphs = countGraphsInGmlFile(new FileReader(gmlFile));
        this.gmlFile = new FileReader(gmlFile);
    }

    public GMLPopulator(URL gmlFile) throws IOException {
        this.numGraphs = countGraphsInGmlFile(new URLReader(gmlFile));
        this.gmlFile = new URLReader(gmlFile);
    }

    @Override
    public int getNumGraphs() { return numGraphs; }

    @Override
    public Stream<Graph> getGraphStream() {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                    gmlCollectionToDLG(new BufferedReader((gmlFile)) {
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
