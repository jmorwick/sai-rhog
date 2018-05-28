package net.sourcedestination.sai.rhog.graph;

import dlg.util.Label;
import net.sourcedestination.sai.db.graph.Feature;
import net.sourcedestination.sai.db.graph.Graph;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *  An adapter to the SAI Graph interface for RhoG directed labeled graph objects as SAI.
 *  Node ids are preserved whereas edge id's are the sum of the source node id multiplied
 *  by the number of nodes and the target node id.
 *
 *  Any class implementing this interface must also extend DLG. This will be relied on in a way that
 *  can't be checked at compile time and will result in runtime casting errors if not followed.
 */
public interface SaiDlgAdapter extends Graph {

    public int getNVertices();
    public List<Label> getAllEdgeLabels();
    public List<Label> getAllVertexLabels();
    public Label getEdge(int from, int to);
    public Label getVertex(int id);

    public default String getLabelName() {
        return "";
    }

    @Override
    public default Stream<Integer> getNodeIDs() {
        return IntStream.range(0, getNVertices()).mapToObj(Integer::new);
    }

    @Override
    public default Stream<Integer> getEdgeIDs() {
        return IntStream.range(0, getNVertices()*getNVertices())
                .filter(id -> getEdge(id/getNVertices(), id%getNVertices()) != null)
                .mapToObj(Integer::new);
    }

    @Override
    public default Stream<Feature> getFeatures() {
        return Stream.concat(
                getAllEdgeLabels().stream().map(l -> new Feature(getLabelName(), l.get())),
                getAllVertexLabels().stream().map(l -> new Feature(getLabelName(), l.get()))
        );
    }

    @Override
    public default Stream<Feature> getNodeFeatures(int nid) {
        Label l = getVertex(nid);
        return l == null ? Stream.empty() :
                Stream.of(new Feature(getLabelName(), l.get()));
    }

    @Override
    public default Stream<Feature> getEdgeFeatures(int eid) {
        Label l = getEdge(eid/getNVertices(), eid%getNVertices());
        return l == null ? Stream.empty() :
                Stream.of(new Feature(getLabelName(), l.get()));
    }

    @Override
    public default int getEdgeSourceNodeID(int eid) { return eid/getNVertices(); }

    @Override
    public default int getEdgeTargetNodeID(int eid) { return eid%getNVertices(); }
}
