package net.sourcedestination.sai.rhog.metrics;

import dlg.core.operations.Connectivity;
import net.sourcedestination.sai.analysis.GraphMetric;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.db.graph.GraphTransformation;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;

/**  TODO: add test
 *   TODO: comment / license
 */
public class BridgesMetric implements GraphMetric {

    private int totalBridges = 0;
    private GraphTransformation<SaiDlg> factory = new DLGFactory();

    @Override
    public Double apply(Graph g) {
        SaiDlg dlg = g instanceof SaiDlg ? (SaiDlg)g : factory.apply(g);
        return (double)Connectivity.getBridges(dlg).size();
    }

    public double getResult() {
        return totalBridges;
    }
}