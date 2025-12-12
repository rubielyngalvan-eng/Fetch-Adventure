package Object;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Main.GamePanel;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    // Draw object relative to player
    public void draw(Graphics2D g2, GamePanel gp) {
        // Draw object using world coordinates (no camera)
        int x = worldX;
        int y = worldY;

        // Only draw if inside the visible screen
        if (x + gp.tileSize > 0 && x < gp.screenWidth && y + gp.tileSize > 0 && y < gp.screenHeight) {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }
    }
}