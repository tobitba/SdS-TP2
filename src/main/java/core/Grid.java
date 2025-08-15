package core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Grid {
    private final double L;
    private final int M;
    private final double cellLength;
    private final List<List<Particle>> grid;
    private final List<Particle> particles;

    public Grid(double L, int M) {
        this.L = L;
        this.M = M;
        this.cellLength = L / M;
        this.particles = new ArrayList<>();
        this.grid = new ArrayList<>();
        for (int i = 0; i < M*M; i++) {
            grid.add(new ArrayList<>());
        }
    }

    /**
     * Particles on horizontal cell borders go to the upper cell,
     * and particles on vertical cell borders go to the right cell.
    */
    public Grid addParticle(Particle particle) {
        double parX = particle.getX();
        double parY = particle.getY();

        if (parX >= L || parX < 0 || parY >= L || parY < 0)
            throw new IndexOutOfBoundsException("The particle doesn't fit on the grid");
        int i = (int) (parX / cellLength) + M * (int) (parY / cellLength);
        grid.get(i).add(particle);
        particles.add(particle);
        return this;
    }

    /** To find the amount of particles per cell */
    public String getParticlesPerCell() {
        return getCustomGridRepresentation(cellParticles -> String.valueOf(cellParticles.size()));
    }

    @Override
    public String toString() {
        return getCustomGridRepresentation(particles1 -> particles1.toString() + "\t");
    }

    private String getCustomGridRepresentation(Function<List<Particle>, String> cellToString) {
        StringBuilder sb = new StringBuilder();
        for (int row = M - 1; row >= 0; row--) {
            for (int col = 0; col < M; col++) {
                int index = row * M + col;
                sb.append(cellToString.apply(grid.get(index)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void performCellIndexMethod(double neighborRadius, boolean boundPeriodicity) {
        double maxRadius = getParticles().stream()
                .max(Comparator.comparingDouble(Particle::getRad)).orElseThrow().getRad();

        if (L/M - 2 * maxRadius <= neighborRadius || neighborRadius <= 0)
            throw new IllegalArgumentException("NeighborRadius needs to be a positive number smaller than L/M - 2*max_radius");

        for (int i = 0; i < M*M; i++) {
            for (Particle particle : grid.get(i)) {
                List<Particle> neighbors = getAboveAndRightAdjacentParticles(i, boundPeriodicity, particle);
                for (Particle neighbor : neighbors) {
                    if (neighbor.getEdgeDistance(particle, boundPeriodicity, L) <= neighborRadius) {
                        particle.addNeighbor(neighbor);
                        neighbor.addNeighbor(particle);
                    }
                }
                for (Particle neighbor : getCurrentCellParticles(i, particle)) {
                    if (neighbor.getEdgeDistance(particle, boundPeriodicity, L) <= neighborRadius)
                        particle.addNeighbor(neighbor);
                }

            }
        }
    }

    private List<Particle> getAboveAndRightAdjacentParticles(int cellIndex, boolean boundPeriodicity, Particle particle) {
        List<Particle> adjacentParticles = new ArrayList<>();

        int row = cellIndex / M;
        int col = cellIndex % M;

        int[][] directions = {
                {1, 0}, {1, 1}, // above, upper right
                        {0, 1}, // right
                        {-1, 1} // lower right
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (boundPeriodicity) {
                newRow = (newRow + M) % M;
                newCol = (newCol + M) % M;
            }

            if (newRow >= 0 && newRow < M && newCol >= 0 && newCol < M) {
                int neighborCellIndex = newRow * M + newCol;
                adjacentParticles.addAll(grid.get(neighborCellIndex));
                if (boundPeriodicity)
                    adjacentParticles.remove(particle);
            }
        }

        return adjacentParticles;
    }

    private List<Particle> getCurrentCellParticles(int i, Particle p) {
        List<Particle> toReturn = new ArrayList<>(grid.get(i));
        toReturn.remove(p);
        return toReturn;
    }

    public int getM() {
        return M;
    }

    public double getL() {
        return L;
    }
}