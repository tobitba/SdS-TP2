import core.Grid;
import tools.ParticleGenerator;
import tools.PostProcessor;

import java.io.IOException;

public class OffLatticeSimulation {
    private final static String N = "N";
    private final static String L = "L";
    private final static String V = "V";
    private final static String NEIGHBOR_RADIUS = "rc";
    private final static String EPOCH = "epoch";
    private final static String NOISE = "noise";
    private final static String RANDOM_NEIGHBOR_DIRECTION = "rand-dir";
    private final static String OUTPUT_FILE = "output";


    public static void main(String[] args) {
        int n = Integer.parseInt(System.getProperty(N));
        double l = Double.parseDouble(System.getProperty(L));
        double v = Double.parseDouble(System.getProperty(V));
        double neighborRadius = Double.parseDouble(System.getProperty(NEIGHBOR_RADIUS));
        double noise = Double.parseDouble(System.getProperty(NOISE));
        int epoch = Integer.parseInt(System.getProperty(EPOCH));
        boolean randomNeighborDirection = Boolean.parseBoolean(System.getProperty(RANDOM_NEIGHBOR_DIRECTION));
        String outputFile = System.getProperty(OUTPUT_FILE);

        Grid grid = new Grid(l, epoch, neighborRadius);
        ParticleGenerator.generate(n, l, grid::addParticle, v, noise, randomNeighborDirection);
        long init =  System.currentTimeMillis();
        try(PostProcessor postProcessor  = new PostProcessor(outputFile)){
            grid.iterator().forEachRemaining(postProcessor::processEpoch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(System.currentTimeMillis() - init);


    }
}
