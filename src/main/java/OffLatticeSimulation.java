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


    public static void main(String[] args) {
        int n = Integer.parseInt(System.getProperty(N));
        double l = Double.parseDouble(System.getProperty(L));
        double v = Double.parseDouble(System.getProperty(V));
        double neighborRadius = Double.parseDouble(System.getProperty(NEIGHBOR_RADIUS));
        int epoch = Integer.parseInt(System.getProperty(EPOCH));

        Grid grid = new Grid(l,epoch,neighborRadius,true); //TODO: sacar el boolean boundPeriodicity, siempre va a ser asi
        ParticleGenerator.generate(n, l, particle -> grid.addParticle(particle, true), v);

        try(PostProcessor postProcessor  = new PostProcessor()){
            grid.iterator().forEachRemaining(postProcessor::processEpoch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
