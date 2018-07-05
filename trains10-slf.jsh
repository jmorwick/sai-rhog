/env -class-path ~/.m2/repository/net/sourcedestination/sai-rhog/1.0-SNAPSHOT/sai-rhog-1.0-SNAPSHOT.jar:~/.m2/repository/net/sourcedestination/sai/2.0-SNAPSHOT/sai-2.0-SNAPSHOT.jar:~/.m2/repository/net/sourcedestination/funcles/2.0/funcles-2.0.jar:lib/rhog-2017-07-01.jar:~/.m2/repository/com/google/guava/guava/25.1-jre/guava-25.1-jre.jar

import net.sourcedestination.sai.experiment.retrieval.GraphSimilarityMetric;
import net.sourcedestination.sai.db.graph.GraphTransformation;
import net.sourcedestination.sai.db.BasicDBInterface;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;
import net.sourcedestination.sai.db.DBWrapper;
import net.sourcedestination.sai.experiment.retrieval.Retriever;
import net.sourcedestination.sai.rhog.ClassifierWrapper;
import net.sourcedestination.sai.rhog.graph.GMLPopulator;
import net.sourcedestination.sai.rhog.comparison.distance.RhogDistanceAdapter;
import net.sourcedestination.sai.rhog.ClassificationLabelLoader;
import net.sourcedestination.sai.experiment.learning.ClassificationModelGenerator;
import net.sourcedestination.sai.experiment.learning.CrossValidatedClassificationExperiment;
import java.net.URL;
import java.io.*;

import dlg.bridges.GMLBridge;
import dlg.core.*;
import dlg.core.subsumption.*;
import dlg.core.refinement.base.*;

import static net.sourcedestination.sai.experiment.retrieval.Retriever.simpleSequentialRetrieverFactory;
import static net.sourcedestination.sai.experiment.retrieval.Retriever.rerankingRetriever;
import static net.sourcedestination.sai.experiment.learning.ClassificationModelGenerator.knnClassifierGenerator;
import static net.sourcedestination.sai.rhog.comparison.ComparisonUtil.constructRefinementOperator;
import static net.sourcedestination.sai.rhog.comparison.distance.RhogDistanceAdapter.getAUDistance;

// experiment parameters
var k = 1;
var trainsDataSet = "";
//var trainsDataSet = "100";
//var trainsDataSet = "900";
var dataUrl = new URL("https://raw.githubusercontent.com/santiontanon/RHOG/master/data/gml/trains"+trainsDataSet+"/trains"+trainsDataSet+"-instances.gml");
var labelsUrl = new URL("https://raw.githubusercontent.com/santiontanon/RHOG/master/data/gml/trains"+trainsDataSet+"/trains"+trainsDataSet+"-labels.txt");


// initialize basic objects
var dlgGen = new DLGFactory();
var db = new BasicDBInterface();

// load data in to database
var populator = new GMLPopulator(dataUrl);
var task1 = populator.apply(db);
task1.get();

// find expected classifications
var classes = new ClassificationLabelLoader(labelsUrl);

// create refinement operator
var subsumption = new dlg.core.subsumption.FlatSubsumption(true);
var op = constructRefinementOperator(dlg.core.refinement.base.FlatRefinement::new, db);
var t = getAUDistance(subsumption, op, db, classes);
var dist = t._1;
t._2.get();                  // run task training AUDist

// create model generator for k-NN
var modelGen = knnClassifierGenerator(db -> rerankingRetriever(simpleSequentialRetrieverFactory(db), db, dist), k);

// run leave-one-out classification experiment
var experiment = new CrossValidatedClassificationExperiment(modelGen, db, "db1", classes);
System.out.println(experiment.get());



/exit
