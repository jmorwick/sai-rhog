package net.sourcedestination.sai.rhog;

import dlg.core.DLG;
import dlg.ml.Classifier;
import net.sourcedestination.sai.db.DBInterface;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.experiment.learning.ClassificationModel;
import net.sourcedestination.sai.rhog.graph.DLGFactory;
import net.sourcedestination.sai.rhog.graph.SaiDlg;
import net.sourcedestination.sai.util.Task;

import java.util.function.Function;
import java.util.stream.Collectors;

public class ClassifierWrapper  implements ClassificationModel {

    private Classifier internalClassifier;
    private DLGFactory factory;

    public ClassifierWrapper(Classifier internalClassifier, DLGFactory factory) {
        this.internalClassifier = internalClassifier;
        this.factory = factory;
    }

    public Classifier getWrappedClassifier() { return internalClassifier; }

    public String apply(Graph g) {
        try {
            if (g instanceof DLG)
                return internalClassifier.predict((DLG) g).get();
            return internalClassifier.predict(factory.apply(g)).get();
        } catch(Exception e) {
            throw new RuntimeException(e); }
    }

    public Task train(DBInterface db, Function<Graph,String> classes) {
        return () -> {
            try {
                internalClassifier.train(
                        db.getGraphIDStream()
                                .map(db::retrieveGraph)
                                .map(g -> g instanceof DLG ? (DLG) g : (DLG) factory.apply(g))
                                .collect(Collectors.toList()),

                        db.getGraphIDStream()
                                .map(db::retrieveGraph)
                                .map(classes::apply)
                                .map(SaiDlg::labelFromString)
                                .collect(Collectors.toList()));
            } catch(Exception e) { throw new RuntimeException(e); }
            return null;
        };
    }

}
