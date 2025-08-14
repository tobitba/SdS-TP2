package CellIndexMethod;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PostProcessor {
    // runtime is in milliseconds
    public static void process(List<Particle> particles, long runtime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for (Particle particle : particles) {
                writer.write(particle.stringNeighborhoods());
            }
            writer.newLine();
            writer.write("Time elapsed: " + runtime + "ms");
        } catch (IOException ignored) {
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("rawtime.txt", true))) {
            writer.write(String.valueOf(runtime));
            writer.newLine();
        } catch (IOException ignored) {
        }
    }
}
