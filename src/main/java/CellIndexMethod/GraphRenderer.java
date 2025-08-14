package CellIndexMethod;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphRenderer extends JPanel {
    private final Grid grid;
    private final int selected;
    private static final int PANEL_SIZE = 700;
    private final boolean showIDS;

    public GraphRenderer(Grid grid, int selected, boolean showIDS) {
        this.grid = grid;
        this.selected = selected;
        this.showIDS = showIDS;
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int M = grid.getM();
        double L = grid.getL();
        int cellSize = PANEL_SIZE / M;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1) Draw particles first, with stronger (less transparent) colors
        for (Particle p : grid.getParticles()) {
            int alpha = 180; // nearly opaque
            Color fill;
            if (p.getId() == selected) {
                fill = new Color(28, 104, 255, alpha);  // yellow
            } else if (grid.getParticles()
                    .stream()
                    .filter(pp -> pp.getId() == selected)
                    .findFirst()
                    .map(target -> target.getNeighbors().contains(p))
                    .orElse(false)) {
                fill = new Color(255, 165,   0, alpha);  // orange
            } else {
                fill = new Color(255,   0,   0, alpha);  // red
            }
            g2.setColor(fill);

            int px = (int) ((p.getX() / L) * PANEL_SIZE);
            int py = (int) (((L - p.getY()) / L) * PANEL_SIZE);
            int r  = (int) ((p.getRad() / L) * PANEL_SIZE);

            g2.fillOval(px - r, py - r, 2 * r, 2 * r);

            String idStr = String.valueOf(p.getId());
// choose your desired font first
            Font idFont = new Font("SansSerif", Font.BOLD, 20);
            g2.setFont(idFont);

// measure the text
            FontMetrics fm = g2.getFontMetrics();
            int textW = fm.stringWidth(idStr);
            int textH = fm.getAscent();  // from baseline up

// try to center on (px,py)
            int tx = px - textW/2;
            int ty = py + textH/2;

// clamp to panel (so it never goes off)
            tx = Math.max(tx, 0);
            tx = Math.min(tx, PANEL_SIZE - textW);
            ty = Math.max(ty, textH);
            ty = Math.min(ty, PANEL_SIZE);

            g2.setColor(Color.BLACK);
            if (showIDS)
                g2.drawString(idStr, tx, ty);
        }

        // 2) Draw the grid on top, with black lines and larger labels
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        for (int row = 0; row < M; row++) {
            for (int col = 0; col < M; col++) {
                int x = col * cellSize;
                int y = (M - 1 - row) * cellSize;
                int idx = row * M + col;
                g2.drawRect(x, y, cellSize, cellSize);
                if (showIDS)
                    g2.drawString(String.valueOf(idx), x + 6, y + 16);
            }
        }

        g2.dispose();
    }

    //Makes a graphic of the grid, highlighting a certain particle and its neighbors
    public static void show(Grid grid, int selected, boolean showIDS) {
        JFrame frame = new JFrame("CellIndexMethod.Grid Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GraphRenderer(grid, selected, showIDS));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void saveGridImage(Grid grid, int selected, boolean showIDS) throws IOException {
        GraphRenderer renderer = new GraphRenderer(grid, selected, showIDS);
        renderer.setSize(PANEL_SIZE, PANEL_SIZE);

        BufferedImage image = new BufferedImage(
                PANEL_SIZE, PANEL_SIZE,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = image.createGraphics();
        renderer.paint(g2d); // llama al paintComponent internamente
        g2d.dispose();
        ImageIO.write(image, "png", new File("graph.png"));
    }



}
