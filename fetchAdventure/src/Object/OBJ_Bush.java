package Object;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_Bush extends SuperObject {

    public OBJ_Bush() {
        name = "Bush";
        collision = true; // blocks movement

        try {
            File f = new File("assets/Bush01.png");
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // default position (can be overridden by AssetSetter)
        worldX = 0;
        worldY = 0;
    }
}