import net.sourcedestination.sai.experiment.retrieval.GraphSimilarityMetric;
import net.sourcedestination.sai.db.graph.GraphTransformation;
import net.sourcedestination.sai.db.BasicDBInterface;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;
import net.sourcedestination.sai.db.DBWrapper;
import net.sourcedestination.sai.experiment.retrieval.Retriever;
import net.sourcedestination.sai.rhog.graph.GMLPopulator;
import java.net.URL;
import java.io.*;

import dlg.bridges.GMLBridge;

import static net.sourcedestination.sai.rhog.comparison.distance.RhogDistanceAdapter.getBagOfLabelsDistance;
import static net.sourcedestination.sai.experiment.retrieval.Retriever.simpleSequentialRetrieverFactory;
import static net.sourcedestination.sai.db.GraphTransformingDBWrapper.wrap;
import static net.sourcedestination.sai.experiment.retrieval.Retriever.rerankingRetriever;

// initialize objects
var bagDist = getBagOfLabelsDistance(true,true);
var dlgGen = new DLGFactory();
var db = new BasicDBInterface();
var rdb = wrap(db, dlgGen);
var sequentialRetriever = simpleSequentialRetrieverFactory(rdb);
var rankByBagDist = rerankingRetriever(sequentialRetriever, rdb, bagDist);
var spongePopulator = new GMLPopulator(new URL("https://raw.githubusercontent.com/santiontanon/RHOG/master/data/gml/trains100/trains100-instances.gml"));

// prepare tasks
var task1 = spongePopulator.apply(db);


// run tasks
task1.get();

// test stuff
var b = new GMLBridge();
var f = new BufferedReader(new FileReader(new File("sponge-instances.gml")));
var g1 = b.load(f);
var sg1 = new SaiDlg(g1);
var g2 = b.load(f);
var sg2 = new SaiDlg(g2);

bagDist.distance(sg1, sg2);
bagDist.apply(sg1, sg2);