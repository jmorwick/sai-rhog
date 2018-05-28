package net.sourcedestination.sai.rhog.comparison.distance;

import dlg.ml.distance.BagOfLabelsDistance;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;
import net.sourcedestination.sai.rhog.graph.SaiDlgAdapter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jmorwick on 7/2/17.
 */
public class RhogDistanceAdapterTest {

    @Test
    public void testBagOfLabelsDistance() {/*
        GraphFactory<SaiDlg> f = new DLGFactory();
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
        SaiDlg dlg1 = f.copy(g);


        MutableGraph g2 = new MutableGraph();
        g2.addNode(0);
        g2.addNode(1);
        g2.addNode(2);
        g2.addEdge(0,0,1);
        g2.addEdge(1,0,2);
        g2.addNodeFeature(0, new Feature("", "foo"));
        g2.addNodeFeature(2, new Feature("", "bar"));
        g2.addEdgeFeature(0, new Feature("", "baz"));
        g2.addEdgeFeature(1, new Feature("", "baz"));
        SaiDlg dlg2 = f.copy(g2);


        MutableGraph g3 = new MutableGraph();
        g3.addNode(0);
        g3.addNode(1);
        g3.addNode(2);
        g3.addEdge(0,0,1);
        g3.addEdge(1,0,2);
        g3.addNodeFeature(0, new Feature("", "fooz"));
        g3.addNodeFeature(2, new Feature("", "barz"));
        g3.addEdgeFeature(0, new Feature("", "baz"));
        g3.addEdgeFeature(1, new Feature("", "bazs"));
        SaiDlg dlg3 = f.copy(g3);

        GraphDistance<SaiDlgAdapter> dist =
                (RhogDistanceAdapter)(new BagOfLabelsDistance(true,true,null))::distance;
        System.out.println(dist.compare(dlg1, dlg1));
        System.out.println(dist.compare(dlg1, dlg2));
        System.out.println(dist.compare(dlg1, dlg3));
        System.out.println(dist.compare(dlg2, dlg2));
        System.out.println(dist.compare(dlg3, dlg3));
        Assert.assertTrue(dist.compare(dlg1, dlg2) < dist.compare(dlg1, dlg3));
        */
    }
}
