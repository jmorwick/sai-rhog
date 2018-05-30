package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;
import net.sourcedestination.sai.db.graph.GraphDeserializer;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Created by jmorwick on 7/2/17.
 */
public interface DLGReader extends GraphDeserializer {

    static Logger logger = Logger.getLogger(DLGReader.class);

    public default SaiDlg apply(String encoding) {
        try {
            return new SaiDlg(load(new BufferedReader(new StringReader(encoding))));
        } catch(Exception e) {
            logger.error("error loading new DLG structure", e);
            return null;
        }
    }

    public DLG load(BufferedReader in) throws Exception;
}
