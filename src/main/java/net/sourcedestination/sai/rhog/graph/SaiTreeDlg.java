package net.sourcedestination.sai.rhog.graph;

import dlg.core.TreeDLG;

/**
 * Created by jmorwick on 7/1/17.
 */
public class SaiTreeDlg extends TreeDLG implements SaiDlgAdapter {

    public SaiTreeDlg(int n) {
        super(n);
    }

    public SaiTreeDlg(TreeDLG g) {
        super(g);
    }
}
