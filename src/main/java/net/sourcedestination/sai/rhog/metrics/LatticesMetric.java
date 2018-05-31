package net.sourcedestination.sai.rhog.metrics;

import dlg.core.operations.GraphTypeChecker;
import net.sourcedestination.sai.analysis.metrics.types.GraphType;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**
 * Created by jmorwick on 7/7/17.
 */
public class LatticesMetric implements GraphType {

    private final DLGFactory factory;

    public LatticesMetric() {
        factory = new DLGFactory();
    }

    public boolean test(Graph g) {
        return GraphTypeChecker.isLattice(g instanceof SaiDlg ? (SaiDlg)g : factory.apply(g));
    }
}
