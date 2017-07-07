package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.GMLBridge;
import dlg.core.DLG;
import dlg.core.TreeDLG;
import net.sourcedestination.sai.graph.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * Created by jmorwick on 6/30/17.
 */
public class GMLUtil {

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
}
