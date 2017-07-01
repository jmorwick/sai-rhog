package net.sourcedestination.sai.rhog.graph;

import dlg.core.DLG;

/**
 * Created by jmorwick on 7/1/17.
 */
public class SaiDlg extends DLG implements SaiDlgAdapter {

    public SaiDlg(int n) {
        super(n);
    }

    public SaiDlg(DLG g) {
        super(g);
    }
}
