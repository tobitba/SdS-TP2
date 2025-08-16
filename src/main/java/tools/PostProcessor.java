package tools;

import core.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PostProcessor {
    private static final String OUTPUT_FILE_NAME = "dynamicOutput.txt";
    private final BufferedWriter writer;

    public PostProcessor() {
        try {
            writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException("Error opening file");
        }
    }

    public void processEpoch(List<Particle> particles, int epoch) {
        try {
            writer.write(String.valueOf(epoch));
            writer.newLine();
            particles.forEach(this::processParticle);
        } catch (IOException e) {
            throw new RuntimeException("Error writing on output file");
        }
    }

    private void processParticle(Particle particle) {
        try {
            writer.write(particle.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error writing on output file");
        }
    }

}
