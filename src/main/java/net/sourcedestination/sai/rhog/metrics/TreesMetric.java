package net.sourcedestination.sai.rhog.metrics;

import dlg.core.operations.GraphTypeChecker;
import net.sourcedestination.sai.analysis.metrics.types.GraphType;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**
 * Created by jmorwick on 7/7/17.
 */
public class TreesMetric implements GraphType {

    private final DLGFactory factory;

    public TreesMetric() {
        factory = new DLGFactory();
    }

    public boolean test(Graph g) {
        return GraphTypeChecker.isTree(g instanceof SaiDlg ? (SaiDlg)g : factory.apply(g));
    }
}
