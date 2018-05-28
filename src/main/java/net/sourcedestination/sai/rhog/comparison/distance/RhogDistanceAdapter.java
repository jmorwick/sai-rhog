package net.sourcedestination.sai.rhog.comparison.distance;

import dlg.core.DLG;
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
        SaiDlg sg2 = g1 instanceof SaiDlg ?
                (SaiDlg) g1 :
                f.apply(g1);
        try {return distance(sg1, sg2); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public double distance(DLG g1, DLG g2) throws Exception;
}
