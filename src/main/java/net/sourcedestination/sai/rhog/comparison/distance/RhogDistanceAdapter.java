package net.sourcedestination.sai.rhog.comparison.distance;

import dlg.core.DLG;
import net.sourcedestination.sai.comparison.distance.GraphDistance;
import net.sourcedestination.sai.rhog.graph.SaiDlgAdapter;

/**
 * Created by jmorwick on 7/2/17.
 */
@FunctionalInterface
public interface RhogDistanceAdapter extends GraphDistance<SaiDlgAdapter> {
    public default Double apply(SaiDlgAdapter g1, SaiDlgAdapter g2) {
        try {return distance((DLG)g1, (DLG)g2); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public double distance(DLG g1, DLG g2) throws Exception;
}
