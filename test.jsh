/env -class-path ~/.m2/repository/net/sourcedestination/sai-rhog/1.0-SNAPSHOT/sai-rhog-1.0-SNAPSHOT.jar:~/.m2/repository/net/sourcedestination/sai/2.0-SNAPSHOT/sai-2.0-SNAPSHOT.jar:~/.m2/repository/net/sourcedestination/funcles/2.0/funcles-2.0.jar:lib/rhog-2017-07-01.jar:~/.m2/repository/com/google/guava/guava/22.0/guava-22.0.jar

import net.sourcedestination.sai.experiment.retrieval.GraphSimilarityMetric;
import net.sourcedestination.sai.db.graph.GraphTransformation;
import net.sourcedestination.sai.db.BasicDBInterface;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;
import net.sourcedestination.sai.db.DBWrapper;
import net.sourcedestination.sai.experiment.retrieval.Retriever;
import net.sourcedestination.sai.rhog.graph.GMLPopulator;
import net.sourcedestination.sai.rhog.ClassificationLabelLoader;
import java.net.URL;
import java.io.*;

import dlg.bridges.GMLBridge;
import dlg.core.subsumption.FlatSubsumption;

import static net.sourcedestination.sai.rhog.comparison.distance.RhogDistanceAdapter.getBagOfLabelsDistance;
import static net.sourcedestination.sai.rhog.comparison.distance.RhogDistanceAdapter.getCompressionDistance;
import static net.sourcedestination.sai.rhog.comparison.distance.RhogDistanceAdapter.getAUDistance;
import static net.sourcedestination.sai.rhog.comparison.ComparisonUtil.constructRefinementOperator;
import static net.sourcedestination.sai.experiment.retrieval.Retriever.simpleSequentialRetrieverFactory;
import static net.sourcedestination.sai.db.GraphTransformingDBWrapper.wrap;
import static net.sourcedestination.sai.experiment.retrieval.Retriever.rerankingRetriever;

// initialize objects
var bagDist = getBagOfLabelsDistance(true,true);
var compDist = getCompressionDistance();
var db = new BasicDBInterface();

var spongePopulator = new GMLPopulator(new URL("https://raw.githubusercontent.com/santiontanon/RHOG/master/data/gml/trains100/trains100-instances.gml"));
var task1 = spongePopulator.apply(db);
task1.get();

var classes = new ClassificationLabelLoader(new URL("https://raw.githubusercontent.com/santiontanon/RHOG/master/data/gml/trains100/trains100-labels.txt"));

var subsumption = new dlg.core.subsumption.FlatSubsumption(true);
var op = constructRefinementOperator(dlg.core.refinement.base.FlatRefinement::new, db);
var t = getAUDistance(subsumption, op, db, classes);
var auDist = t._1;
t._2.get();                  // run task training AUDist


var dlgGen = new DLGFactory();
var rdb = wrap(db, dlgGen);
var sequentialRetriever = simpleSequentialRetrieverFactory(rdb);
var rankByBagDist = rerankingRetriever(sequentialRetriever, rdb, bagDist);

auDist.apply(db.retrieveGraph(1), db.retrieveGraph(2));
