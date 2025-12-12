package Object;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Gate extends SuperObject {

    public OBJ_Gate() {
        name = "Gate";
        collision = true; // blocks movement until unlocked
        try {
            File f = new File("assets/gate.png");
            if (f.exists()) {
                image = ImageIO.read(f);
            }
        } catch (IOException e) {
            // ignore
        }
    }
}