package az.edu.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CSPInstance {
    int k;
    Map<Integer, Set<Integer>> adj = new HashMap<>();
    boolean valid = true;

    static CSPInstance fromFile(Path p) throws IOException {
        CSPInstance inst = new CSPInstance();
        List<String> lines = Files.readAllLines(p);

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.startsWith("colors=")) {
                String rhs = line.substring("colors=".length()).trim();
                inst.k = Integer.parseInt(rhs);
            } else {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                int u = Integer.parseInt(parts[0].trim());
                int v = Integer.parseInt(parts[1].trim());

                // Self-loop check
                if (u == v) {
                    inst.valid = false;
                }

                inst.adj.computeIfAbsent(u, x -> new HashSet<>()).add(v);
                inst.adj.computeIfAbsent(v, x -> new HashSet<>()).add(u);
            }
        }

        if (inst.k <= 0) inst.k = 1;

        // Ensure isolated vertices have an entry
        for (Integer v : new HashSet<>(inst.adj.keySet())) {
            inst.adj.putIfAbsent(v, new HashSet<>());
        }

        return inst;
    }
}