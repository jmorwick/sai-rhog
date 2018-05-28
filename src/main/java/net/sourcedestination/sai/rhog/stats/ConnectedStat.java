package net.sourcedestination.sai.rhog.stats;

import dlg.core.operations.Connectivity;
import net.sourcedestination.sai.analysis.metrics.types.GraphType;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**  TODO: add test
 *   TODO: comment / license
 */
public class ConnectedStat implements GraphType {

    private final DLGFactory factory;

    public ConnectedStat() {
        factory = new DLGFactory();
    }

    @Override
    public boolean test(Graph g) {
        return Connectivity.isConnected(g instanceof SaiDlg ? (SaiDlg)g : factory.apply(g));
    }
}