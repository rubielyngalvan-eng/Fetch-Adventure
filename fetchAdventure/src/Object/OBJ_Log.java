package Object;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_Log extends SuperObject {

    public OBJ_Log() {
        // Use consistent capitalized name and mark as a blocking obstacle
        name = "Log";
        collision = true; // non-passable

        try {
            File f = new File("assets/log.png");
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Default world position (can be overridden in AssetSetter)
        worldX = 7; // tile-based placement should be done in AssetSetter (multiply by gp.tileSize)
        worldY = 9;
    }
}