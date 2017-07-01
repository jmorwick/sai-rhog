package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;
import dlg.util.Label;
import net.sourcedestination.sai.graph.Feature;
import net.sourcedestination.sai.graph.Graph;
import net.sourcedestination.sai.graph.GraphFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmorwick on 6/30/17.
 */
public class DLGFactory implements GraphFactory<SaiDlgAdapter> {

    private final String featureName;
    private final String defaultLabel;
    /**
     *
     * @param featureName the name of the feature to extract for labels
     * @param defaultLabel the label value to use if no feature with given name is available for a node/edge
     */
    public DLGFactory(String featureName, String defaultLabel) {
        this.featureName = featureName;
        this.defaultLabel = defaultLabel;
    }

    @Override
    public SaiDlg copy(Graph g) {
        List<Integer> nodeIds = new ArrayList<>(g.getNodeIDsSet());
        Map<Integer,Integer> nodeIdMap = new HashMap<>();
        SaiDlg gc = new SaiDlg(nodeIds.size());
        for(int i=0; i<nodeIds.size(); i++) {
            Feature f = g.getNodeFeature(featureName, nodeIds.get(i));
            String labelValue = f == null ? defaultLabel : f.getValue();
            Label l = null;
            try { l = new Label(labelValue); } catch(Exception e) {}
            gc.setVertex(i, l);
            nodeIdMap.put(nodeIds.get(i), i);
        }
        g.getEdgeIDs().forEach(eid -> {
            Feature f = g.getEdgeFeature(featureName, nodeIds.get(eid));
            String labelValue = f == null ? defaultLabel : f.getValue();
            Label l = null;
            try { l = new Label(labelValue); } catch(Exception e) {}
            gc.setEdge(
                    nodeIdMap.get(g.getEdgeSourceNodeID(eid)),
                    nodeIdMap.get(g.getEdgeTargetNodeID(eid)),
                    l);
        });

        return gc;
    }
}
