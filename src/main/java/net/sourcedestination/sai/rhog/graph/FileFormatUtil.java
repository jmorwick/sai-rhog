package net.sourcedestination.sai.rhog.graph;

import dlg.bridges.DLGWriter;
import dlg.core.DLG;
import dlg.core.TreeDLG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jmorwick on 7/2/17.
 */
public class FileFormatUtil {

    private static Logger logger = Logger.getLogger(FileFormatUtil.class.getCanonicalName());

    public static String serialize(DLG g, DLGWriter bridge) {
        StringWriter sw = new StringWriter();
        try { bridge.save(g, sw); }
        catch (Exception e) {
            logger.log(Level.ALL, "can'tserialize", e);
            return null;
        }
        return sw.getBuffer().toString();
    }

    public static Iterator<SaiDlgAdapter> fileToDLGs(final HackedReader in, DLGReader reader) {
        return new Iterator<SaiDlgAdapter>() {
            SaiDlgAdapter g = null;

            private void peekNextGraph() {
                try {
                    DLG dlg = reader.load(in);
                    if(dlg != null && dlg instanceof TreeDLG) {
                        String label = in.getLabel();
                        in.resetLabel();
                        g = new SaiTreeDlg((TreeDLG) dlg,label);
                    } else if(dlg != null) {

                        String label = in.getLabel();
                        in.resetLabel();
                        g = new SaiDlg(dlg,label);
                    }
                } catch (Exception e) {
                    logger.log(Level.ALL, "can't peek at next", e);
                    g = null;
                }
            }

            @Override
            public boolean hasNext() {
                try {
                    if(g == null) {
                        peekNextGraph();
                        if(g == null) return false;
                    }
                    return true;
                } catch(Exception e) {
                    logger.log(Level.ALL, "can't see if next exists", e);
                    return false;
                }
            }

            @Override
            public SaiDlgAdapter next() {
                if(g == null) peekNextGraph();
                SaiDlgAdapter ret = g;
                g = null;
                return ret;
            }
        };
    }
}


class HackedReader extends BufferedReader {
    final private Reader r;
    final private HackedReader self = this;
    public HackedReader(Reader r) {
        super(r);
        this.r = r;
    }

    // rhog lib uses .ready() to see if data is left in the stream.
    // this isn't the default behavior of this function when dealing with streams other than files,
    // so this fixes it to work that way.
    @Override public boolean ready() {
        int b = -1;
        try {
            super.mark(2);
            b = super.read();
            super.reset();
        } catch(IOException e) {}
        return b != -1;
    }

    private int depth = 0;
    private String labelNameSoFar = "";
    private String label = "";
    private final String LABEL_NAME = "label \"";
    public int read() throws IOException {
        int c = super.read();
        if((char)c == '[') depth++;
        else if((char)c == ']') depth--;
        if(depth == 1) { // look for label name / label value
            if(labelNameSoFar.length() < LABEL_NAME.length() &&
                    LABEL_NAME.startsWith(labelNameSoFar + (char)c))
                labelNameSoFar += (char) c;
            else if(labelNameSoFar.length() > 0 &&
                    labelNameSoFar.length() < LABEL_NAME.length())
                labelNameSoFar = "";
            else if(labelNameSoFar.equals(LABEL_NAME)) {
                if((char)c == '\"')
                    labelNameSoFar = "";
                else label += (char)c;
            }
        }

        return c;
    }

    public String getLabel() { return label; }
    public void resetLabel() { label = ""; }

}