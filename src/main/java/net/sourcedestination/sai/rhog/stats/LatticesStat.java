package net.sourcedestination.sai.rhog.stats;

import dlg.core.operations.GraphTypeChecker;
import net.sourcedestination.sai.analysis.metrics.types.GraphType;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**
 * Created by jmorwick on 7/7/17.
 */
public class LatticesStat implements GraphType {

    private final DLGFactory factory;

    public LatticesStat() {
        factory = new DLGFactory();
    }

    public boolean test(Graph g) {
        return GraphTypeChecker.isLattice(g instanceof SaiDlg ? (SaiDlg)g : factory.apply(g));
    }
}
