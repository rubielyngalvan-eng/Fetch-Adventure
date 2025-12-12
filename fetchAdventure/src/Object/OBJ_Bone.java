package Object;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_Bone extends SuperObject {

    public OBJ_Bone() {
        name = "Bone";

        try {
            File f = new File("assets/bone.png");
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Default world position (can be overridden in AssetSetter)
        worldX = 7 ; // 10 tiles right
        worldY = 9  ; // 12 tiles down
    }
}