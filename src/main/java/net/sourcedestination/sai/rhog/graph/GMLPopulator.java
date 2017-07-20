package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.GMLBridge;
import dlg.core.DLG;
import dlg.core.TreeDLG;
import net.sourcedestination.sai.db.DBInterface;
import net.sourcedestination.sai.graph.Graph;
import net.sourcedestination.sai.task.DBPopulator;
import net.sourcedestination.sai.task.Task;

import java.io.*;
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

    private File gmlFile;
    private int numGraphs;

    public static Iterator<SaiDlgAdapter> gmlCollectionToDLG(final BufferedReader in) {
        return FileFormatUtil.fileToDLGs(in, new GMLBridge()::load);
    }

    public static String dlgToGml(DLG g) {
        return FileFormatUtil.serialize(g, new GMLBridge());
    }

    public static String saiToGml(Graph g, String featureName, String defaultLabel) {
        DLGFactory f = new DLGFactory(featureName, defaultLabel);
        return dlgToGml(f.copy(g));
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
        this.gmlFile = gmlFile;
        this.numGraphs = countGraphsInGmlFile(new FileReader(gmlFile));
    }

    @Override
    public int getNumGraphs() { return numGraphs; }

    @Override
    public Stream<Graph> getGraphStream() {
        try {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                    gmlCollectionToDLG(new BufferedReader(new FileReader(gmlFile))),
                    Spliterator.ORDERED),
                    false);
        } catch(IOException e) { throw new IllegalStateException("Could not open file: " + gmlFile); }
    }
}
