package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class Grid implements Iterable<List<Particle>>{
    private final double L;
    private final int M;
    private final int maxEpoch;
    private final double neighborRadius;
    private final double cellLength;

    private int epoch;

    private List<List<Particle>> grid;
    private final List<Particle> particles;

    public Grid(double L, int M, int maxEpoch, double neighborRadius) {
        if (M <= 0 || L <= 0 || maxEpoch <= 0 || neighborRadius <= 0)
            throw new IllegalArgumentException("M, L, maxEpoch and neighborRadius must be positive");
        if (M >= L/neighborRadius)
            throw new IllegalArgumentException("M must be smaller than L/neighborRadius");
        this.L = L;
        this.M = M;
        this.maxEpoch = maxEpoch;
        this.neighborRadius = neighborRadius;
        this.cellLength = L / M;

        epoch = 0;

        this.particles = new ArrayList<>();
        this.grid = new ArrayList<>();
        for (int i = 0; i < M*M; i++) {
            grid.add(new ArrayList<>());
        }
    }

    public Grid(double L, int maxEpoch, double neighborRadius) {
        this(L, (int) (L/neighborRadius), maxEpoch, neighborRadius);
    }

    /**
     * Particles on horizontal cell borders go to the upper cell,
     * and particles on vertical cell borders go to the right cell.
    */
    public void addParticle(Particle particle, boolean addToList) { // TODO es nefasto el boolean de si agregarlo a la lista o no
        double parX = particle.getX();
        double parY = particle.getY();
        double parRad = particle.getRad();

        if (parX >= L || parX < 0 || parY >= L || parY < 0)
            throw new IndexOutOfBoundsException("The particle doesn't fit on the grid");
        if (Double.compare(parRad, 0) != 0)
            throw new IllegalArgumentException("The particle is not a point particle");
        int i = (int) (parX / cellLength) + M * (int) (parY / cellLength);
        grid.get(i).add(particle);
        if (addToList)
            particles.add(particle);
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

    @Override
    public Iterator<List<Particle>> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return epoch < maxEpoch;
            }

            @Override
            public List<Particle> next() {
                performCellIndexMethod();
                for (int i = 0; i < M*M; i++) {
                    for (Particle particle : grid.get(i)) {
                        particle.move(L);
                    }
                }

                grid = new ArrayList<>();
                for (int i = 0; i < M*M; i++) {
                    grid.add(new ArrayList<>());
                }

                for (Particle particle : getParticles()) {
                    addParticle(particle, false);
                }

                epoch++;
                return particles;
            }
        };
    }


    public void performCellIndexMethod() {
        for (int i = 0; i < M*M; i++) {
            for (Particle particle : grid.get(i)) {
                List<Particle> neighbors = getAboveAndRightAdjacentParticles(i, particle);
                for (Particle neighbor : neighbors) {
                    if (neighbor.getEdgeDistance(particle, L) <= neighborRadius) {
                        particle.addNeighborDirection(neighbor.getDirection());
                        neighbor.addNeighborDirection(particle.getDirection());
                    }
                }
                for (Particle neighbor : getCurrentCellParticles(i, particle)) {
                    if (neighbor.getEdgeDistance(particle, L) <= neighborRadius)
                        particle.addNeighborDirection(neighbor.getDirection());
                }

            }
        }
    }

    private List<Particle> getAboveAndRightAdjacentParticles(int cellIndex, Particle particle) {
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

            newRow = (newRow + M) % M;
            newCol = (newCol + M) % M;

            if (newRow >= 0 && newRow < M && newCol >= 0 && newCol < M) {
                int neighborCellIndex = newRow * M + newCol;
                adjacentParticles.addAll(grid.get(neighborCellIndex));
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