package Object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Key extends SuperObject {

    public OBJ_Key() {
        name = "Key";
        collision = false;
        try {
            File f = new File("assets/key.png");
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
