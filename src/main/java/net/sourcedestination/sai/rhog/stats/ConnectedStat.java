package net.sourcedestination.sai.rhog.stats;

import dlg.core.operations.Connectivity;
import net.sourcedestination.sai.graph.Graph;
import net.sourcedestination.sai.reporting.stats.GraphFilterStatistic;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**  TODO: add test
 *   TODO: comment / license
 */
public class ConnectedStat implements GraphFilterStatistic {

    private final DLGFactory factory;

    public ConnectedStat() {
        factory = new DLGFactory();
    }

    public boolean filterGraph(Graph g) {
        return Connectivity.isConnected(g instanceof SaiDlg ? (SaiDlg)g : factory.copy(g));
    }
}