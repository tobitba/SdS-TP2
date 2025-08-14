package CellIndexMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Parser implementation for a particle neighbor simulator.
 * This class receives two {@code BufferedReader} instances that read from the static and dynamic
 * input files used in the simulation. It also receives a {@code Consumer<CellIndexMethod.Particle>} to define
 * how each parsed particle should be handled, along with the total number of particles to parse.
 */

public class InputParser {


    public static void parseParticlesFiles(BufferedReader staticBufferedReader, BufferedReader dynamicBufferedReader, Consumer<Particle> consumer, int n) throws IOException {
        String props, pos;
        int i = 0;
        while ((pos = dynamicBufferedReader.readLine()) != null && (props = staticBufferedReader.readLine()) != null && i < n) {
            String[] dynamicLine = pos.split("\\s+");
            String[] staticLine = props.split("\\s+");
            consumer.accept(new Particle(
                    Double.parseDouble(dynamicLine[1]),
                    Double.parseDouble(dynamicLine[2]),
                    Double.parseDouble(staticLine[1]),
                    Double.parseDouble(staticLine[2])));
            i++;
        }
        if (dynamicBufferedReader.readLine() != null || staticBufferedReader.readLine() != null || i < n) {
            throw new RuntimeException("Error parsing files. Wrong number of particles" + i);
        }
    }



}
