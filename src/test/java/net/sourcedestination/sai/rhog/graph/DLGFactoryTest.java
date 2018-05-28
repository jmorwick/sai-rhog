package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;
import net.sourcedestination.sai.db.graph.Feature;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.db.graph.GraphTransformation;
import net.sourcedestination.sai.db.graph.MutableGraph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by jmorwick on 7/1/17.
 */
public class DLGFactoryTest {

    @Test
    public void testConvertToDLG() {
        GraphTransformation<SaiDlg> f = new DLGFactory();
        MutableGraph g = new MutableGraph();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addEdge(0,0,1);
        g.addEdge(1,0,2);
        g.addNodeFeature(0, new Feature("", "foo"));
        g.addNodeFeature(2, new Feature("", "bar"));
        g.addEdgeFeature(0, new Feature("", "baz"));
        g.addEdgeFeature(1, new Feature("", "bazs"));

        Graph g2 = ((DLGFactory) f).apply(g);

        assertNotNull(g2);
        assertTrue(g2 instanceof SaiDlgAdapter);
        assertTrue(g2 instanceof DLG);
        assertEquals(g.getEdgeIDsSet().size(), g2.getEdgeIDsSet().size());
        assertEquals(g.getNodeIDsSet().size(), g2.getNodeIDsSet().size());
        assertEquals(g.getEdgeSourceNodeID(0), g2.getEdgeSourceNodeID(0*3 + 1));
        assertEquals(g.getEdgeTargetNodeID(0), g2.getEdgeTargetNodeID(0*3 + 1));
        assertEquals(g.getEdgeSourceNodeID(1), g2.getEdgeSourceNodeID(0*3 + 2));
        assertEquals(g.getEdgeTargetNodeID(1), g2.getEdgeTargetNodeID(0*3 + 2));
        assertEquals(g.getNodeFeaturesSet(0), g2.getNodeFeaturesSet(0));
        assertEquals(1, g2.getNodeFeaturesSet(1).size());
        assertEquals(new Feature("",""), g2.getNodeFeaturesSet(1).iterator().next());
        assertEquals(g.getNodeFeaturesSet(2), g2.getNodeFeaturesSet(2));

        assertEquals(g.getEdgeFeaturesSet(0), g2.getEdgeFeaturesSet(0*3 + 1));
        assertEquals(g.getEdgeFeaturesSet(1), g2.getEdgeFeaturesSet(0*3 + 2));
    }

}
