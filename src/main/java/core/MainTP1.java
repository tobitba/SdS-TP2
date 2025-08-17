package core;

import tools.GraphRenderer;
import tools.ParticleGenerator;
import tools.PostProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

//TODO: dejo esto solo para poder verlo... borrar antes de entragar
public class MainTP1 {

    private final static String STATIC_FILE = "static";
    private final static String DYNAMIC_FILE = "dynamic";
    private final static String GENERATE_PARTICLES = "generate";
    private final static String GENERATE_INPUT_FILES = "generate-input-files";
    private final static String N = "N";
    private final static String L = "L";
    private final static String ID = "ID";
    private final static String PARTICLE_RADIUS = "r";
    private final static String NEIGHBOR_RADIUS = "rc";
    private final static String BOUND_PERIODICITY = "bound-periodicity";
    private final static String FIXED_RADIUS = "fixed";
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
        boolean boundPeriodicity = Boolean.parseBoolean(System.getProperty(BOUND_PERIODICITY));
        boolean saveGraph = Boolean.parseBoolean(System.getProperty(SAVE_GRAPH));
        double neighborRadius = Double.parseDouble(System.getProperty(NEIGHBOR_RADIUS));
        Grid grid = new Grid(l, 100, neighborRadius, boundPeriodicity);

        if (generateParticles) {
            double particleRadius = Double.parseDouble(System.getProperty(PARTICLE_RADIUS));
            boolean fixedRadius = Boolean.parseBoolean(System.getProperty(FIXED_RADIUS));
            ParticleGenerator.generate(n, l, particle -> grid.addParticle(particle, true), 0.03);
        } else {
            parseInput(grid, n);
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

    private static void parseInput(Grid grid, int n) {
        Path staticPath = Paths.get(System.getProperty(STATIC_FILE));
        Path dynamicPath = Paths.get(System.getProperty(DYNAMIC_FILE));
        try (BufferedReader staticLines = Files.newBufferedReader(staticPath); BufferedReader dynamicLines = Files.newBufferedReader(dynamicPath)) {
            //InputParser.parseParticlesFiles(staticLines, dynamicLines, grid::addParticle, n);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
