package net.sourcedestination.sai.rhog.stats;

import dlg.core.operations.GraphTypeChecker;
import net.sourcedestination.sai.graph.Graph;
import net.sourcedestination.sai.reporting.stats.GraphFilterStatistic;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**
 * Created by jmorwick on 7/7/17.
 */
public class LatticesStat implements GraphFilterStatistic {

    private final DLGFactory factory;

    public LatticesStat() {
        factory = new DLGFactory();
    }

    public boolean filterGraph(Graph g) {
        return GraphTypeChecker.isLattice(g instanceof SaiDlg ? (SaiDlg)g : factory.copy(g));
    }
}
