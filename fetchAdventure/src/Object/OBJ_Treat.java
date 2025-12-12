package Object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Treat extends SuperObject {

    public OBJ_Treat() {
        name = "Treat";
        collision = false;
        try {
            File f = new File("assets/treat 1.png");
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}