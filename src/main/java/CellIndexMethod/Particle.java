package CellIndexMethod;

import java.util.ArrayList;
import java.util.List;

public class Particle {
    private static int globalId = 1;
    private final int id;
    private final double x, y;
    private final double rad;
    private final double prop;
    private final List<Particle> neighbors;

    public Particle(double x, double y, double rad, double prop) {
        this.id = globalId++;
        this.x = x;
        this.y = y;
        this.rad = rad;
        this.prop = prop;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(Particle neighbor) {
        this.neighbors.add(neighbor);
    }

    private double getDistance(Particle p, boolean boundPeriodicity, double L) {
        if (!boundPeriodicity)
            return Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));

        return Math.sqrt(
                    Math.pow(
                            Math.min(Math.abs(p.x - x), L - Math.abs(p.x - x)), 2
                    ) +
                    Math.pow(
                            Math.min(Math.abs(p.y - y), L - Math.abs(p.y - y)), 2
                    )
        );
    }

    public double getEdgeDistance(Particle p, boolean boundPeriodicity, double L) {
        return getDistance(p, boundPeriodicity, L) - rad - p.rad;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getProp() {
        return prop;
    }

    @Override
    public String toString() {
        return "%d: %.2f:%.2f".formatted(id, x, y);
    }

    public String stringNeighborhoods() {
        StringBuilder sb = new StringBuilder()
                .append(id)
                .append("\t\t");
        for (Particle p : neighbors) {
            sb.append(p.id).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), "\n");
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public double getRad() {
        return rad;
    }

    public List<Particle> getNeighbors() {
        return neighbors;
    }
}
