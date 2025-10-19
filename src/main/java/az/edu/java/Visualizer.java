package az.edu.java;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Visualizer extends JFrame {
    CSPInstance inst;
    Map<Integer, Integer> sol;
    Map<Integer, Point> positions = new HashMap<>();
    List<Integer> nodesOrdered = new ArrayList<>();

    static Color[] palette = new Color[]{
            Color.LIGHT_GRAY, Color.RED, Color.BLUE, Color.GREEN,
            Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK,
            Color.YELLOW, Color.DARK_GRAY, new Color(150, 75, 0),
            new Color(128, 0, 128)
    };

    public Visualizer(CSPInstance inst, Map<Integer, Integer> sol) {
        super("CSP Graph Coloring");
        this.inst = inst;
        this.sol = sol;
        nodesOrdered.addAll(inst.adj.keySet());
        Collections.sort(nodesOrdered);

        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        computePositions();
        add(new GraphPanel());
    }

    void computePositions() {
        int n = nodesOrdered.size();
        int r = Math.min(getWidth(), getHeight()) / 2 - 100;
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = cx + (int) (r * Math.cos(angle));
            int y = cy + (int) (r * Math.sin(angle));
            positions.put(nodesOrdered.get(i), new Point(x, y));
        }
    }

    class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            computePositions();

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));


            for (Integer u : nodesOrdered) {
                Point pu = positions.get(u);
                for (Integer v : inst.adj.getOrDefault(u, Collections.emptySet())) {
                    if (u < v) {
                        Point pv = positions.get(v);
                        g2.setColor(Color.GRAY);
                        g2.drawLine(pu.x, pu.y, pv.x, pv.y);
                    }
                }
            }


            int nodeSize = 36;
            for (Integer u : nodesOrdered) {
                Point p = positions.get(u);
                int val = sol.getOrDefault(u, 0);
                Color c = palette[(val - 1 + palette.length) % palette.length];

                g2.setColor(c);
                g2.fillOval(p.x - nodeSize / 2, p.y - nodeSize / 2, nodeSize, nodeSize);
                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - nodeSize / 2, p.y - nodeSize / 2, nodeSize, nodeSize);

                String label = String.valueOf(u);
                FontMetrics fm = g2.getFontMetrics();
                int lw = fm.stringWidth(label);
                int lh = fm.getAscent();
                g2.drawString(label, p.x - lw / 2, p.y + lh / 2 - 3);
            }
        }
    }
}
