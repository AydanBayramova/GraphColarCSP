package az.edu.java;

import javax.swing.*;
import java.nio.file.Path;
import java.util.*;

public class GraphColorGSP {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java GraphColorCSP <input-file> [--visual]");
            return;
        }

        String filename = args[0];
        boolean visual = args.length > 1 && args[1].equalsIgnoreCase("--visual");

        CSPInstance inst = CSPInstance.fromFile(Path.of(filename));
        if (!inst.valid) {
            System.out.println("failure");
            return;
        }

        Solver solver = new Solver(inst);
        Map<Integer, Integer> solution = solver.solve();

        if (solution == null) {
            System.out.println("failure");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("SOLUTION: {");
            List<Integer> vars = new ArrayList<>(solution.keySet());
            Collections.sort(vars);
            for (int i = 0; i < vars.size(); i++) {
                int v = vars.get(i);
                sb.append(v).append(": ").append(solution.get(v));
                if (i < vars.size() - 1) sb.append(", ");
            }
            sb.append("}");
            System.out.println(sb);

            if (visual) {
                SwingUtilities.invokeLater(() -> {
                    Visualizer viz = new Visualizer(inst, solution);
                    viz.setVisible(true);
                });
            }
        }
    }
}
