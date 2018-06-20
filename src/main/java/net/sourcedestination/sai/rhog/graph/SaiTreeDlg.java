package net.sourcedestination.sai.rhog.graph;

import dlg.core.TreeDLG;

/**
 * Created by jmorwick on 7/1/17.
 */
public class SaiTreeDlg extends TreeDLG implements SaiDlgAdapter {

    private final String label;


    public String getLabelName() {
        return label;
    }

    public SaiTreeDlg(int n, String label) {
        super(n);
        this.label = label;
    }

    public SaiTreeDlg(TreeDLG g, String label) {
        super(g);
        this.label = label;
    }
}
