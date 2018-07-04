package net.sourcedestination.sai.rhog.comparison.distance;

import dlg.bridges.GMLBridge;
import dlg.core.DLG;
import dlg.core.PartialOrder;
import dlg.core.refinement.RefinementOperator;
import dlg.core.subsumption.Subsumption;
import dlg.ml.distance.AUDistance;
import dlg.ml.distance.BagOfLabelsDistance;
import dlg.ml.distance.KashimaKernelSparse;
import dlg.ml.distance.NormalizedCompressionDistance;
import dlg.util.Label;
import net.sourcedestination.funcles.tuple.Tuple;
import net.sourcedestination.funcles.tuple.Tuple2;
import net.sourcedestination.sai.db.DBInterface;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.experiment.retrieval.GraphSimilarityMetric;
import net.sourcedestination.sai.rhog.ClassificationLabelLoader;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;
import net.sourcedestination.sai.util.Task;

import java.util.List;
import java.util.stream.Collectors;

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

    static RhogDistanceAdapter getKashimaDistance(double p, PartialOrder order) {
        KashimaKernelSparse k = new KashimaKernelSparse(p, order);
        return k::distance;
    }

    static RhogDistanceAdapter getBagOfLabelsDistance(boolean useVertexLabels, boolean useEdgeLabels) {
        BagOfLabelsDistance d = new BagOfLabelsDistance(useVertexLabels, useEdgeLabels, null);
        return d::distance;
    }

    static RhogDistanceAdapter getCompressionDistance() {
        NormalizedCompressionDistance d = new NormalizedCompressionDistance(new GMLBridge());
        return d::distance;
    }

    static Tuple2<RhogDistanceAdapter,Task> getAUDistance(Subsumption s,
                                                          RefinementOperator r,
                                                          DBInterface db,
                                                          ClassificationLabelLoader c) {
        AUDistance d = new AUDistance(s,r);
        return Tuple.makeTuple(d::distance, new Task() {
            @Override
            public Object get() {
                List<Graph> graphs = db.getGraphIDStream()
                        .map(db::retrieveGraph)
                        .collect(Collectors.toList());
                List<Label> labels = graphs.stream()
                        .map(c::apply)
                        .map(SaiDlg::labelFromString)
                        .collect(Collectors.toList());
                return null;
            }
        });
    }
}
