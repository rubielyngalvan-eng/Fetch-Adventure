package Object;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_Ball extends SuperObject {

    public OBJ_Ball() {
        name = "Ball";
        collision = false; // not a blocking obstacle

        try {
            File f = new File("assets/ballRED.png");
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Default position (can be overridden in AssetSetter)
        worldX = 0;
        worldY = 0;
    }
}