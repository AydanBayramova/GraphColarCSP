package az.edu.java;

import java.util.*;

public class Solver {

    CSPInstance inst;
    Map<Integer, Set<Integer>> domains = new HashMap<>();
    Map<Integer, Integer> assignment = new HashMap<>();
    Map<Integer, Set<Integer>> neighbors;
    Deque<Prune> trail = new ArrayDeque<>();

    public Solver(CSPInstance inst) {
        this.inst = inst;
        this.neighbors = inst.adj;
        for (Integer v : neighbors.keySet()) {
            Set<Integer> dom = new LinkedHashSet<>();
            for (int i = 1; i <= inst.k; i++) dom.add(i);
            domains.put(v, dom);
        }
    }


    public Map<Integer, Integer> solve() {
        if (!ac3Initialize()) return null;
        boolean ok = backtrack();
        return ok ? new HashMap<>(assignment) : null;
    }


    private boolean backtrack() {
        if (assignment.size() == domains.size()) return true;

        Integer var = selectMRV();
        if (var == null) return false; // dead end

        List<Integer> orderedValues = orderByLCV(var);
        int trailSizeBefore = trail.size();

        for (Integer val : orderedValues) {
            if (!domains.get(var).contains(val)) continue;

            assignment.put(var, val);


            for (Integer other : new ArrayList<>(domains.get(var))) {
                if (!other.equals(val)) {
                    domains.get(var).remove(other);
                    trail.push(new Prune(var, other));
                }
            }


            Queue<Arc> q = new ArrayDeque<>();
            for (Integer nb : neighbors.getOrDefault(var, Collections.emptySet())) {
                q.add(new Arc(var, nb));
            }

            boolean consistent = ac3(q);
            if (consistent && backtrack()) return true;


            assignment.remove(var);
            undoToSize(trailSizeBefore);
        }

        return false;
    }


    Integer selectMRV() {
        Integer best = null;
        int bestSize = Integer.MAX_VALUE;
        for (Integer v : domains.keySet()) {
            if (assignment.containsKey(v)) continue;
            int sz = domains.get(v).size();
            if (sz == 0) return null;
            if (sz < bestSize) {
                bestSize = sz;
                best = v;
            }
        }
        return best;
    }


    List<Integer> orderByLCV(Integer var) {
        List<Integer> vals = new ArrayList<>(domains.get(var));
        Map<Integer, Integer> elimCount = new HashMap<>();
        for (Integer val : vals) {
            int count = 0;
            for (Integer nb : neighbors.getOrDefault(var, Collections.emptySet())) {
                if (assignment.containsKey(nb)) continue;
                if (domains.get(nb).contains(val)) count++;
            }
            elimCount.put(val, count);
        }
        vals.sort(Comparator.comparingInt(elimCount::get));
        return vals;
    }


    boolean ac3(Queue<Arc> q) {
        while (!q.isEmpty()) {
            Arc arc = q.poll();
            if (revise(arc.x, arc.y)) {
                if (domains.get(arc.x).isEmpty()) return false;
                for (Integer z : neighbors.getOrDefault(arc.x, Collections.emptySet())) {
                    if (!z.equals(arc.y)) q.add(new Arc(z, arc.x));
                }
            }
        }
        return true;
    }

    boolean revise(int X, int Y) {
        boolean removedAny = false;
        Set<Integer> domX = domains.get(X);
        Set<Integer> domY = domains.get(Y);
        List<Integer> toRemove = new ArrayList<>();

        for (Integer a : domX) {
            boolean supported = domY.stream().anyMatch(b -> !b.equals(a));
            if (!supported) toRemove.add(a);
        }

        for (Integer a : toRemove) {
            domX.remove(a);
            trail.push(new Prune(X, a));
            removedAny = true;
        }
        return removedAny;
    }

    void undoToSize(int size) {
        while (trail.size() > size) {
            Prune p = trail.pop();
            domains.get(p.var).add(p.value);
        }
    }


    private boolean ac3Initialize() {
        Queue<Arc> q = new ArrayDeque<>();
        for (Integer x : neighbors.keySet())
            for (Integer y : neighbors.get(x))
                q.add(new Arc(x, y));
        return ac3(q);
    }

    static class Prune {
        int var, value;
        Prune(int var, int value) { this.var = var; this.value = value; }
    }

    static class Arc {
        int x, y;
        Arc(int x, int y) { this.x = x; this.y = y; }
    }
}