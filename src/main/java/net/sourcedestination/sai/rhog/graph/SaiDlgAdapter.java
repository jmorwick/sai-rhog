package net.sourcedestination.sai.rhog.graph;

import dlg.util.Label;
import net.sourcedestination.sai.graph.Feature;
import net.sourcedestination.sai.graph.Graph;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *  An adapter to the SAI Graph interface for RhoG directed labeled graph objects as SAI.
 *  Node ids are preserved whereas edge id's are the sum of the source node id multiplied
 *  by the number of nodes and the target node id.
 *
 *  All SAI methods are implemented as default methods so that DLG objects can be cast to
 *  this interface in order to be used as SAI graph. Only DLG objects are intended to be
 *  cast to this interface. Though it is not type-safe, always assume SaiDlgAdapters can
 *  be cast back to DLG objects.
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
        return IntStream.range(1, getNVertices()).mapToObj(Integer::new);
    }

    @Override
    public default Stream<Integer> getEdgeIDs() {
        return IntStream.range(1, getNVertices()*getNVertices())
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
        Label l = this.getEdge(eid/getNVertices(), eid%getNVertices());
        return l == null ? Stream.empty() :
                Stream.of(new Feature(getLabelName(), l.get()));
    }

    @Override
    public default int getEdgeSourceNodeID(int eid) { return eid/getNVertices(); }

    @Override
    public default int getEdgeTargetNodeID(int eid) { return eid%getNVertices(); }
}
