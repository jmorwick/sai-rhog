package net.sourcedestination.sai.rhog.comparison.distance;

import dlg.bridges.GMLBridge;
import dlg.core.DLG;
import dlg.ml.distance.BagOfLabelsDistance;
import dlg.ml.distance.NormalizedCompressionDistance;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.experiment.retrieval.GraphSimilarityMetric;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**
 * Created by jmorwick on 7/2/17.
 */
@FunctionalInterface
public interface RhogDistanceAdapter extends GraphSimilarityMetric {

    @Override
    default Double apply(Graph g1, Graph g2) {
        DLGFactory f = new DLGFactory();
        SaiDlg sg1 = g1 instanceof SaiDlg ?
                (SaiDlg)g1 :
                f.apply(g1);
        SaiDlg sg2 = g2 instanceof SaiDlg ?
                (SaiDlg) g2 :
                f.apply(g2);
        try {return distance(sg1, sg2); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    double distance(DLG g1, DLG g2) throws Exception;

    static RhogDistanceAdapter getBagOfLabelsDistance(boolean useVertexLabels, boolean useEdgeLabels) {
        BagOfLabelsDistance d = new BagOfLabelsDistance(useVertexLabels, useEdgeLabels, null);
        return d::distance;
    }

    static RhogDistanceAdapter getCompressionDistance() {
        NormalizedCompressionDistance d = new NormalizedCompressionDistance(new GMLBridge());
        return d::distance;
    }
}
