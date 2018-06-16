package net.sourcedestination.sai.rhog;

import net.sourcedestination.sai.db.graph.Feature;
import net.sourcedestination.sai.db.graph.Graph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ClassificationLabelLoader implements Function<Graph,String> {

    private Map<String,String> labels = new HashMap<>();

    public ClassificationLabelLoader(InputStream in) {
        BufferedReader rin = new BufferedReader(new InputStreamReader(in));
        rin.lines().forEach(line -> {
            String[] tokens = line.trim().split("\\s+");
            if(tokens.length != 2)
                throw new IllegalArgumentException("Label file isn't properly formatted");
            labels.put(tokens[0], tokens[1]);
        });
    }

    @Override
    public String apply(Graph g) {
        return labels.get(g.getFeatures().filter(f -> f.getName().equals("label")).findFirst().get());
    }
}
