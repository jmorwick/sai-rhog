package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;
import dlg.util.Label;

/**
 * Created by jmorwick on 7/1/17.
 */
public class SaiDlg extends DLG implements SaiDlgAdapter {

    private final String label;

    public SaiDlg(int n, String label) {
        super(n);
        this.label = label;
    }

    public SaiDlg(DLG g, String label) {
        super(g);
        this.label = label;
    }

    public String getLabelName() {
        return label;
    }

    /** needed to conveniently get around checked exception in constructor for stream processing */
    public static Label labelFromString(String value) {
        try {
            return new Label(value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
