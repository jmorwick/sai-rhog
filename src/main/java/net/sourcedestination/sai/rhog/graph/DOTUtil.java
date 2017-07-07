package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.DOTBridge;
import dlg.core.DLG;
import net.sourcedestination.sai.graph.Graph;

/**
 * Created by jmorwick on 7/2/17.
 */
public class DOTUtil {

    public static String dlgToDot(DLG g) {
        return FileFormatUtil.serialize(g, new DOTBridge());
    }

    public static String saiToDot(Graph g, String featureName, String defaultLabel) {
        DLGFactory f = new DLGFactory(featureName, defaultLabel);
        return dlgToDot(f.copy(g));
    }
    /** reference this static method as a serializer object where needed */
    public static String saiToDot(Graph g) {
        return saiToDot(g,"","");
    }
}
