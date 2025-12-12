package Object;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_Scissors extends SuperObject {

    public OBJ_Scissors() {
        name = "Scissors";
        collision = false; // can be picked up

        try {
            File f = new File("assets/cut.png");
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        worldX = 0;
        worldY = 0;
    }
}