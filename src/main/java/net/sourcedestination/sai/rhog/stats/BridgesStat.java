package net.sourcedestination.sai.rhog.stats;

import dlg.core.operations.Connectivity;
import net.sourcedestination.sai.graph.Graph;
import net.sourcedestination.sai.graph.GraphFactory;
import net.sourcedestination.sai.reporting.stats.IndependentDBStatistic;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**  TODO: add test
 *   TODO: comment / license
 */
public class BridgesStat implements IndependentDBStatistic {

    private int totalBridges = 0;
    private GraphFactory<SaiDlg> factory = new DLGFactory();

    public void processGraph(Graph g) {
        SaiDlg dlg = g instanceof SaiDlg ? (SaiDlg)g : factory.copy(g);
        totalBridges += Connectivity.getBridges(dlg).size();
    }

    public double getResult() {
        return totalBridges;
    }
}