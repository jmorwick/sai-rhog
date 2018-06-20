package net.sourcedestination.sai.rhog.comparison;

import dlg.core.refinement.RefinementOperator;
import dlg.util.Label;
import net.sourcedestination.funcles.function.Function2;
import net.sourcedestination.funcles.tuple.Tuple2;
import net.sourcedestination.sai.db.DBInterface;
import net.sourcedestination.sai.db.graph.Feature;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

import java.util.List;
import java.util.stream.Collectors;

import static net.sourcedestination.funcles.tuple.Tuple.makeTuple;

public class ComparisonUtil {
    public static Tuple2<List<Label>,List<Label>> getNodeAndEdgeLabels(DBInterface db) {
        var nodeLabels = db.getGraphIDStream()
                .map(db::retrieveGraph)
                .flatMap(g -> g.getNodeIDs().flatMap(g::getNodeFeatures))
                .map(Feature::getValue)
                .map(SaiDlg::labelFromString)
                .collect(Collectors.toList());

        var edgeLabels = db.getGraphIDStream()
                .map(db::retrieveGraph)
                .flatMap(g -> g.getEdgeIDs().flatMap(g::getEdgeFeatures))
                .map(Feature::getValue)
                .map(SaiDlg::labelFromString)
                .collect(Collectors.toList());

        return makeTuple(nodeLabels, edgeLabels);
    }

    public static RefinementOperator constructRefinementOperator(
            Function2<List<Label>,List<Label>,RefinementOperator> f,
            DBInterface db) {
        return f.apply(getNodeAndEdgeLabels(db));
    }
}
