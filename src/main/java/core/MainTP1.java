package core;

import tools.GraphRenderer;
import tools.ParticleGenerator;
import tools.PostProcessor;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

//TODO: dejo esto solo para poder verlo... borrar antes de entragar
public class MainTP1 {

    private final static String GENERATE_PARTICLES = "generate";
    private final static String N = "N";
    private final static String L = "L";
    private final static String ID = "ID";
    private final static String NEIGHBOR_RADIUS = "rc";
    private final static String SHOW_GRAPH = "graph";
    private final static String SAVE_GRAPH = "save";
    private final static String SHOW_IDS = "show-ids";

    public static void main(String[] args) throws IOException {
        boolean generateParticles = Boolean.parseBoolean(System.getProperty(GENERATE_PARTICLES));
        boolean showGraph = Boolean.parseBoolean(System.getProperty(SHOW_GRAPH));
        boolean showIDS = Boolean.parseBoolean(System.getProperty(SHOW_IDS));
        int n = Integer.parseInt(System.getProperty(N));
        int id = Integer.parseInt(System.getProperty(ID));
        double l = Double.parseDouble(System.getProperty(L));
        boolean saveGraph = Boolean.parseBoolean(System.getProperty(SAVE_GRAPH));
        double neighborRadius = Double.parseDouble(System.getProperty(NEIGHBOR_RADIUS));
        Grid grid = new Grid(l, 100, neighborRadius);

        if (generateParticles) {
            ParticleGenerator.generate(n, l, particle -> grid.addParticle(particle, true), 0.03,0);
        } else {
            System.out.println("Turn generateParticles to True");
        }

        grid.performCellIndexMethod();
        if (saveGraph){
            try {
                GraphRenderer.saveGridImage(grid, id, showIDS);
            } catch (IOException e) {
                throw new RuntimeException("Error al intentar manejar el archivo xx");
            }
        }
        if (showGraph) {
            PostProcessor postProcessor = new PostProcessor();
            Iterator<List<Particle>> iter = grid.iterator();
            for (int i = 0; iter.hasNext(); i++) {
                postProcessor.processEpoch(iter.next());
                if (i == 0)
                    GraphRenderer.show(grid, id, showIDS); // print first grid
            }
            GraphRenderer.show(grid, id, showIDS); // print last grid
            postProcessor.close();
        }
    }
}
