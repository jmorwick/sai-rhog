package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.DLGWriter;
import dlg.bridges.GMLBridge;
import dlg.core.DLG;
import dlg.core.TreeDLG;

import java.io.BufferedReader;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * Created by jmorwick on 7/2/17.
 */
public class FileFormatUtil {
    public static String serialize(DLG g, DLGWriter bridge) {
        StringWriter sw = new StringWriter();
        try { bridge.save(g, sw); }
        catch (Exception e) { return null; }
        return sw.getBuffer().toString();
    }


    public static Iterator<SaiDlgAdapter> fileToDLGs(final BufferedReader in, DLGReader reader) {
        return new Iterator<SaiDlgAdapter>() {
            SaiDlgAdapter g = null;

            private void peekNextGraph() {
                try {
                    DLG dlg = reader.load(in);
                    if(dlg instanceof TreeDLG)
                        g = new SaiTreeDlg((TreeDLG)dlg);
                    else g = new SaiDlg(dlg);
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
                SaiDlgAdapter ret = g;
                g = null;
                return ret;
            }
        };
    }
}
