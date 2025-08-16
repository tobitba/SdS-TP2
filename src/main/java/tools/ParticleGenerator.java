package tools;

import core.Particle;

import java.util.Random;
import java.util.function.Consumer;

public class ParticleGenerator {

    public static void generate(int particleNumber, double gridSize, Consumer<Particle> consumer, double radius , boolean fixRadius, double speed) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        double x;
        double y;
        double direction;
        double r = radius;
        for (int i = 0; i < particleNumber; i++) {
            x = random.nextDouble() * gridSize;
            y = random.nextDouble() * gridSize;
            direction = random.nextDouble(-Math.PI, Math.PI);
            if(!fixRadius){
                r = random.nextDouble(0.1, radius);
            }
            consumer.accept(new Particle(x, y, r, direction));
        }
        Particle.setSpeed(speed);
    }


}
