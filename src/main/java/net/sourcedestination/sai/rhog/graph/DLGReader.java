package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;
import net.sourcedestination.sai.db.graph.GraphDeserializer;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jmorwick on 7/2/17.
 */
public interface DLGReader extends GraphDeserializer {

    Logger logger = Logger.getLogger(DLGReader.class.getCanonicalName());

    public default SaiDlg apply(String encoding) {
        try {
            var in = new HackedReader(new StringReader(encoding));
            var g = load(new BufferedReader(new StringReader(encoding)));
            var label = in.getLabel();
            return new SaiDlg(g, label);
        } catch(Exception e) {
            logger.log(Level.ALL, "error loading new DLG structure", e);
            return null;
        }
    }

    public DLG load(BufferedReader in) throws Exception;
}
