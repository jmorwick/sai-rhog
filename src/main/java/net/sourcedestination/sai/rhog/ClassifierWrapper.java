package net.sourcedestination.sai.rhog;

import dlg.core.DLG;
import dlg.ml.Classifier;
import net.sourcedestination.sai.db.graph.Graph;
import net.sourcedestination.sai.experiment.learning.ClassificationModel;
import net.sourcedestination.sai.rhog.graph.DLGFactory;

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
        } catch(Exception e) { throw new RuntimeException(e); }
    }

}
