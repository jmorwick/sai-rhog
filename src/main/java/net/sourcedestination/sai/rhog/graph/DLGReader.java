package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;
import net.sourcedestination.sai.graph.GraphDeserializer;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Created by jmorwick on 7/2/17.
 */
public interface DLGReader extends GraphDeserializer<SaiDlg> {

    public default SaiDlg apply(String encoding) {
        try {
            return new SaiDlg(load(new BufferedReader(new StringReader(encoding))));
        } catch(Exception e) {
            return null;
        }
    }

    public DLG load(BufferedReader in) throws Exception;
}
