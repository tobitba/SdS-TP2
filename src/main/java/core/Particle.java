package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particle {
    private static int globalId = 1;
    private static double halfNoise;
    private final int id;
    private double x, y;
    private final double rad;
    private static double speed;
    private double direction;
    private final List<Double> neighborDirections;
    private final Random noiseRandom;

    public Particle(double x, double y, double rad, double direction) {
        this.id = globalId++;
        this.x = x;
        this.y = y;
        this.rad = rad;
        this.neighborDirections = new ArrayList<>();
        this.direction = direction;
        this.noiseRandom = new Random();
    }

    public void addNeighborDirection(Double neighborDirection) {
        this.neighborDirections.add(neighborDirection);
    }

    public void resetNeighbors(){
        this.neighborDirections.clear();
    }

    private double getDistance(Particle p, double L) {
        return Math.sqrt(
                    Math.pow(
                            Math.min(Math.abs(p.x - x), L - Math.abs(p.x - x)), 2
                    ) +
                    Math.pow(
                            Math.min(Math.abs(p.y - y), L - Math.abs(p.y - y)), 2
                    )
        );
    }

    public void move(double L) {
        x = x + speed * Math.cos(direction);
        y = y + speed * Math.sin(direction);

        // Readjust particle if out of bounds
        x = x % L;
        if (x < 0) {
            x += L;
        }
        y = y % L;
        if (y < 0) {
            y += L;
        }

        double senTotal = 0;
        double cosTotal = 0;
        for (Double dir : neighborDirections) {
            senTotal += Math.sin(dir);
            cosTotal += Math.cos(dir);
        }
        senTotal += Math.sin(direction);
        cosTotal += Math.cos(direction);
        senTotal /= neighborDirections.size() + 1;
        cosTotal /= neighborDirections.size() + 1;
        direction = Math.atan2(senTotal, cosTotal);
        if(halfNoise > 0) { //TODO: evaluar complejidad, por ahi se puede poner el check en un solo lugar y no en todas las particulas
            direction += noiseRandom.nextDouble(-halfNoise , halfNoise);
        }

    }

    public double getEdgeDistance(Particle p, double L) {
        return getDistance(p, L) - rad - p.rad;
    }

    public double getDirection() {
        return direction;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "%d: x=%.2f y=%.2f dir=%.2f".formatted(id, x, y, direction);
    }

    public String csvString() {
        return "%d;%.2f;%.2f;%.2f".formatted(id, x, y, direction);
    }

    public int getId() {
        return id;
    }

    public double getRad() {
        return rad;
    }

    public List<Double> getNeighborDirections() {
        return neighborDirections;
    }

    public static void setSpeed(double speed) {
        Particle.speed = speed;
    }

    public static void setNoise(double noise) {
        Particle.halfNoise = noise/2;
    }

}
