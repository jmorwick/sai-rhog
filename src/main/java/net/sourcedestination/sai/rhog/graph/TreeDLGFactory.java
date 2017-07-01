package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;
import dlg.core.TreeDLG;
import net.sourcedestination.sai.graph.Graph;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlgAdapter;

/**
 * Created by jmorwick on 6/30/17.
 */
public class TreeDLGFactory extends DLGFactory {
    /**
     *
     * @param featureName the name of the feature to extract for labels
     * @param defaultLabel the label value to use if no feature with given name is available for a node/edge
     */
    public TreeDLGFactory(String featureName, String defaultLabel) {
        super(featureName, defaultLabel);
    }

    public SaiDlgAdapter copy(Graph g) {
        return (SaiDlgAdapter)new TreeDLG((DLG)super.copy(g));
    }
}
