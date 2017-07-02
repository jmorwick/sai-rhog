package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.DLGWriter;
import dlg.core.DLG;

import java.io.StringWriter;

/**
 * Created by jmorwick on 7/2/17.
 */
public class FileFormatUtil {
    public static String serialize(DLG g, DLGWriter bridge) {
        StringWriter sw = new StringWriter();
        try { bridge.save(g, sw); }
        catch (Exception e) { return null; }
        return sw.getBuffer().toString();
    }
}
