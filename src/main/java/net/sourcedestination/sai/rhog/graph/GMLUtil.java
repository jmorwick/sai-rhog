package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.GMLBridge;
import dlg.core.DLG;
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

    public Iterator<SaiDlgAdapter> GmlCollectionToDLG(final BufferedReader in) {
        final GMLBridge bridge = new GMLBridge();
        return new Iterator<SaiDlgAdapter>() {
            SaiDlgAdapter g = null;

            private void peekNextGraph() {
                try {
                    g = (SaiDlgAdapter)bridge.load(in);
                } catch (Exception e) { g = null; }
            }

            @Override
            public boolean hasNext() {
                try {
                    if(!in.ready()) return false;
                    if(g == null) {
                        peekNextGraph();
                        if(g == null) return false;
                    }
                    return true;
                } catch(Exception e) {
                    return false;
                }
            }

            @Override
            public SaiDlgAdapter next() {
                if(g == null) peekNextGraph();
                return g;
            }
        };
    }

    public static String DlgToGml(DLG g) {
        GMLBridge bridge = new GMLBridge();
        StringWriter sw = new StringWriter();
        try { bridge.save(g, sw); }
        catch (Exception e) { return null; }
        return sw.getBuffer().toString();
    }

    public static String SaiToGml(Graph g, String featureName, String defaultLabel) {
        DLGFactory f = new DLGFactory(featureName, defaultLabel);
        return DlgToGml(f.copyToDLG(g));
    }

    /** reference this static method as a deserializer object where needed */
    public static SaiDlgAdapter GmlToDlg(String gml) {
        GMLBridge bridge = new GMLBridge();
        try {
            return (SaiDlgAdapter)bridge.load(new BufferedReader(new StringReader(gml)));
        } catch(Exception e) {
            return null;
        }
    }

    /** reference this static method as a serializer object where needed */
    public static String SaiToGml(Graph g) {
        return SaiToGml(g,"","");
    }
}
