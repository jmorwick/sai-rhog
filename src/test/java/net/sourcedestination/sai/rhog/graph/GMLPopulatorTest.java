package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;
import net.sourcedestination.sai.db.BasicDBInterface;
import net.sourcedestination.sai.db.DBInterface;
import net.sourcedestination.sai.db.DBPopulator;
import net.sourcedestination.sai.db.graph.*;
import net.sourcedestination.sai.util.Task;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by jmorwick on 7/1/17.
 */
public class GMLPopulatorTest {


    @Test
    public void testLoadGMLFile() throws Exception {
        File f = new File(getClass().getClassLoader().getResource("sponge-instances.gml").getFile());
        BufferedReader in = new BufferedReader(new FileReader(f));
        Set<DLG> graphs = new HashSet<>();
        for(Iterator<SaiDlgAdapter> i = GMLPopulator.gmlCollectionToDLG(in);
            i.hasNext();
            graphs.add((DLG)i.next()));

        assertEquals(503, graphs.size());
    }

    @Test
    public void testPopulatorAsync() throws Exception {
        File f = new File(getClass().getClassLoader().getResource("sponge-instances.gml").getFile());
        assertEquals(503, GMLPopulator.countGraphsInGmlFile(new FileReader(f)));
        DBInterface db = new BasicDBInterface();
        DBPopulator pop = new GMLPopulator(f);
        Task t = pop.apply(db);
        assertEquals(0, db.getDatabaseSize());
        assertEquals(0, t.getProgressUnits());
        assertEquals(503, t.getTotalProgressUnits());
        assertTrue(0.0001 > t.getPercentageDone());
        CompletableFuture future = CompletableFuture.supplyAsync(t);
        while(!future.isDone()) Thread.sleep(100);
        assertEquals(503, db.getDatabaseSize());
        assertEquals(503, t.getProgressUnits());
        assertTrue(t.getPercentageDone() > 0.99999 && t.getPercentageDone() < 1.000001);
    }

    @Test
    public void testSaveLoadGML() {
        GraphSerializer serializer = GMLPopulator::saiToGml;
        GraphDeserializer deserializer = GMLPopulator::gmlToDlg;
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

        String gml = serializer.apply(g);
        Graph g2 = deserializer.apply(gml);

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
