package net.sourcedestination.sai.rhog.stats;

import dlg.core.operations.GraphTypeChecker;
import net.sourcedestination.sai.graph.Graph;
import net.sourcedestination.sai.reporting.stats.GraphFilterStatistic;
import net.sourcedestination.sai.rhog.graph.DLGFactory;

/**
 * Created by jmorwick on 7/7/17.
 */
public class TreesStat implements GraphFilterStatistic {

    private final DLGFactory factory;

    public TreesStat() {
        factory = new DLGFactory();
    }

    public boolean filterGraph(Graph g) {
        return GraphTypeChecker.isTree(factory.copy(g));
    }
}
