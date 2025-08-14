package CellIndexMethod;

import java.util.Random;
import java.util.function.Consumer;

public class ParticleGenerator {

    public static void generate(int particleNumber, double gridSize, Consumer<Particle> consumer, boolean generateInputFiles, double radius , boolean fixRadius) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        double x;
        double y;
        double r = radius;
        for (int i = 0; i < particleNumber; i++) {
            x = random.nextDouble() * gridSize;
            y = random.nextDouble() * gridSize;
            if(!fixRadius){
                r = random.nextDouble(0.1, radius);
            }
            consumer.accept(new Particle(x, y, r, 1));
        }
    }


}
