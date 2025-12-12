package Main;

import Object.OBJ_Bone;
import Object.OBJ_Bush;
import Object.OBJ_Scissors;
import Object.OBJ_Treat;
import Object.OBJ_Key;
import Object.OBJ_Gate;
import Object.OBJ_Log;
import Object.OBJ_Ball;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        // Clear existing objects first so leftover objects from previous map don't persist
        for (int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }

        int placed = 0;

        // Place objects based on the current map number
        switch (gp.currentMap) {
            case 1:
                // Player is created once in GamePanel, just reposition on map change
                gp.player.worldX = 2 * gp.tileSize;
                gp.player.worldY = 2 * gp.tileSize;

                gp.obj[0] = new OBJ_Bone();
                gp.obj[0].worldX = 9 * gp.tileSize;
                gp.obj[0].worldY = 7 * gp.tileSize;
                placed++;

                // add a blocking bush
                gp.obj[3] = new OBJ_Bush();
                gp.obj[3].worldX = 6 * gp.tileSize;
                gp.obj[3].worldY = 4 * gp.tileSize;
                placed++;

                gp.obj[1] = new OBJ_Bone();
                gp.obj[1].worldX = 12 * gp.tileSize;
                gp.obj[1].worldY = 8 * gp.tileSize;
                placed++;

                gp.obj[2] = new OBJ_Bone();
                gp.obj[2].worldX = 5 * gp.tileSize;
                gp.obj[2].worldY = 7 * gp.tileSize;
                placed++;

                gp.obj[6] = new OBJ_Scissors();
                gp.obj[6].worldX = 4 * gp.tileSize;
                gp.obj[6].worldY = 5 * gp.tileSize;
                placed++;

                // place a treat on map 1
                gp.obj[4] = new OBJ_Treat();
                gp.obj[4].worldX = 8 * gp.tileSize;
                gp.obj[4].worldY = 4 * gp.tileSize;
                placed++;

                // place a locked gate near the top-left exit (example)
                gp.obj[7] = new OBJ_Gate();
                gp.obj[7].worldX = 13 * gp.tileSize;
                gp.obj[7].worldY = 8 * gp.tileSize;
                placed++;

                // place a key somewhere else on the map
                gp.obj[8] = new OBJ_Key();
                gp.obj[8].worldX = 10 * gp.tileSize;
                gp.obj[8].worldY = 2 * gp.tileSize;
                placed++;

                // place a fallen log as an extra obstacle
                gp.obj[9] = new OBJ_Log();
                gp.obj[9].worldX = 9 * gp.tileSize;
                gp.obj[9].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[10] = new OBJ_Log();
                gp.obj[10].worldX = 13 * gp.tileSize;
                gp.obj[10].worldY = 7 * gp.tileSize;
                placed++;

                gp.obj[11] = new OBJ_Log();
                gp.obj[11].worldX = 1 * gp.tileSize;
                gp.obj[11].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[12] = new OBJ_Log();
                gp.obj[12].worldX = 3 * gp.tileSize;
                gp.obj[12].worldY = 6 * gp.tileSize;
                placed++;

                gp.obj[13] = new OBJ_Log();
                gp.obj[13].worldX = 8 * gp.tileSize;
                gp.obj[13].worldY = 7 * gp.tileSize;
                placed++;

                break;

            case 2:
                // Reposition player for map 2
                gp.player.worldX = 2 * gp.tileSize;
                gp.player.worldY = 7 * gp.tileSize;

                gp.obj[0] = new OBJ_Bone();
                gp.obj[0].worldX = 2 * gp.tileSize;
                gp.obj[0].worldY = 4 * gp.tileSize;
                placed++;

                gp.obj[1] = new OBJ_Bone();
                gp.obj[1].worldX = 6 * gp.tileSize;
                gp.obj[1].worldY = 6 * gp.tileSize;
                placed++;

                gp.obj[2] = new OBJ_Bone();
                gp.obj[2].worldX = 10 * gp.tileSize;
                gp.obj[2].worldY = 9 * gp.tileSize;
                placed++;

                // add a blocking bush
                gp.obj[3] = new OBJ_Bush();
                gp.obj[3].worldX = 2 * gp.tileSize;
                gp.obj[3].worldY = 5 * gp.tileSize;
                placed++;

                // place a bush that blocks a path
                gp.obj[4] = new OBJ_Bush();
                gp.obj[4].worldX = 5 * gp.tileSize;
                gp.obj[4].worldY = 2 * gp.tileSize;
                placed++;

                // scissors to pick up on map 2
                gp.obj[5] = new OBJ_Scissors();
                gp.obj[5].worldX = 2 * gp.tileSize;
                gp.obj[5].worldY = 9 * gp.tileSize;
                placed++;
                // place a treat on map 2
                gp.obj[6] = new OBJ_Treat();
                gp.obj[6].worldX = 5 * gp.tileSize;
                gp.obj[6].worldY = 9 * gp.tileSize;
                placed++;
                // map 2: optional key and gate (example)

                gp.obj[12] = new OBJ_Gate();
                gp.obj[12].worldX = 13 * gp.tileSize;
                gp.obj[12].worldY = 10 * gp.tileSize;
                placed++;

                gp.obj[13] = new OBJ_Gate();
                gp.obj[13].worldX = 9 * gp.tileSize;
                gp.obj[13].worldY = 9 * gp.tileSize;
                placed++;

                gp.obj[8] = new OBJ_Key();
                gp.obj[8].worldX = 12 * gp.tileSize;
                gp.obj[8].worldY = 5 * gp.tileSize;
                placed++;

                gp.obj[7] = new OBJ_Key();
                gp.obj[7].worldX = 2 * gp.tileSize;
                gp.obj[7].worldY = 2 * gp.tileSize;
                placed++;

                // small log obstacle
                gp.obj[9] = new OBJ_Log();
                gp.obj[9].worldX = 10 * gp.tileSize;
                gp.obj[9].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[10] = new OBJ_Log();
                gp.obj[10].worldX = 1 * gp.tileSize;
                gp.obj[10].worldY = 7 * gp.tileSize;
                placed++;

                gp.obj[11] = new OBJ_Log();
                gp.obj[11].worldX = 2 * gp.tileSize;
                gp.obj[11].worldY = 8 * gp.tileSize;
                placed++;

                break;

            case 3:
                // Reposition player for map 3
                gp.player.worldX = 3 * gp.tileSize;
                gp.player.worldY = 2 * gp.tileSize;

                gp.obj[0] = new OBJ_Bone();
                gp.obj[0].worldX = 6 * gp.tileSize;
                gp.obj[0].worldY = 5 * gp.tileSize;
                placed++;

                gp.obj[1] = new OBJ_Bone();
                gp.obj[1].worldX = 11 * gp.tileSize;
                gp.obj[1].worldY = 2 * gp.tileSize;
                placed++;
                // small cluster of bushes
                gp.obj[2] = new OBJ_Bush();
                gp.obj[2].worldX = 9 * gp.tileSize;
                gp.obj[2].worldY = 5 * gp.tileSize;
                placed++;

                gp.obj[4] = new OBJ_Bush();
                gp.obj[4].worldX = 13 * gp.tileSize;
                gp.obj[4].worldY = 6 * gp.tileSize;
                placed++;

                // treat in map 3
                gp.obj[3] = new OBJ_Treat();
                gp.obj[3].worldX = 9 * gp.tileSize;
                gp.obj[3].worldY = 8 * gp.tileSize;
                placed++;
                // optional gate/key for map 3
                gp.obj[7] = new OBJ_Gate();
                gp.obj[7].worldX = 14 * gp.tileSize;
                gp.obj[7].worldY = 6 * gp.tileSize;
                placed++;
                gp.obj[8] = new OBJ_Key();
                gp.obj[8].worldX = 9 * gp.tileSize;
                gp.obj[8].worldY = 7 * gp.tileSize;
                placed++;
                // add a log obstacle for map 3
                gp.obj[9] = new OBJ_Log();
                gp.obj[9].worldX = 4 * gp.tileSize;
                gp.obj[9].worldY = 6 * gp.tileSize;
                placed++;

                gp.obj[10] = new OBJ_Log();
                gp.obj[10].worldX = 2 * gp.tileSize;
                gp.obj[10].worldY = 9 * gp.tileSize;
                placed++;

                gp.obj[5] = new OBJ_Scissors();
                gp.obj[5].worldX = 9 * gp.tileSize;
                gp.obj[5].worldY = 6 * gp.tileSize;
                placed++;

                gp.obj[11] = new OBJ_Log();
                gp.obj[11].worldX = 1 * gp.tileSize;
                gp.obj[11].worldY = 2 * gp.tileSize;
                placed++;
                break;

            case 4:
                // Reposition player for map 4
                gp.player.worldX = 2 * gp.tileSize;
                gp.player.worldY = 9 * gp.tileSize;

                gp.obj[0] = new OBJ_Bone();
                gp.obj[0].worldX = 2 * gp.tileSize;
                gp.obj[0].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[1] = new OBJ_Bone();
                gp.obj[1].worldX = 4 * gp.tileSize;
                gp.obj[1].worldY = 8 * gp.tileSize;
                placed++;

                gp.obj[2] = new OBJ_Bone();
                gp.obj[2].worldX = 9 * gp.tileSize;
                gp.obj[2].worldY = 9 * gp.tileSize;
                placed++;

                gp.obj[3] = new OBJ_Bone();
                gp.obj[3].worldX = 13 * gp.tileSize;
                gp.obj[3].worldY = 6 * gp.tileSize;
                placed++;

                gp.obj[4] = new OBJ_Bone();
                gp.obj[4].worldX = 6 * gp.tileSize;
                gp.obj[4].worldY = 3 * gp.tileSize;
                placed++;
                // add some bushes on map 4
                gp.obj[5] = new OBJ_Bush();
                gp.obj[5].worldX = 5 * gp.tileSize;
                gp.obj[5].worldY = 9 * gp.tileSize;
                placed++;
                // treat on map 4
                gp.obj[6] = new OBJ_Treat();
                gp.obj[6].worldX = 3 * gp.tileSize;
                gp.obj[6].worldY = 2 * gp.tileSize;
                placed++;
                // gate/key on map 4
                gp.obj[7] = new OBJ_Gate();
                gp.obj[7].worldX = 14 * gp.tileSize;
                gp.obj[7].worldY = 6 * gp.tileSize;
                placed++;

                gp.obj[8] = new OBJ_Key();
                gp.obj[8].worldX = 6 * gp.tileSize;
                gp.obj[8].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[9] = new OBJ_Scissors();
                gp.obj[9].worldX = 4 * gp.tileSize;
                gp.obj[9].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[10] = new OBJ_Log();
                gp.obj[10].worldX = 5 * gp.tileSize;
                gp.obj[10].worldY = 5 * gp.tileSize;
                placed++;

                gp.obj[11] = new OBJ_Bush();
                gp.obj[11].worldX = 6 * gp.tileSize;
                gp.obj[11].worldY = 4 * gp.tileSize;
                placed++;

                gp.obj[12] = new OBJ_Log();
                gp.obj[12].worldX = 7 * gp.tileSize;
                gp.obj[12].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[13] = new OBJ_Key();
                gp.obj[13].worldX = 8 * gp.tileSize;
                gp.obj[13].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[14] = new OBJ_Log();
                gp.obj[14].worldX = 13 * gp.tileSize;
                gp.obj[14].worldY = 9 * gp.tileSize;
                placed++;

                gp.obj[15] = new OBJ_Bush();
                gp.obj[15].worldX = 10 * gp.tileSize;
                gp.obj[15].worldY = 8 * gp.tileSize;
                placed++;

                gp.obj[16] = new OBJ_Gate();
                gp.obj[16].worldX = 10 * gp.tileSize;
                gp.obj[16].worldY = 3 * gp.tileSize;
                placed++;

                gp.obj[17] = new OBJ_Log();
                gp.obj[17].worldX = 1 * gp.tileSize;
                gp.obj[17].worldY = 9 * gp.tileSize;
                placed++;

                break;

            case 5:
                // Reposition player for map 5 (final map)
                gp.player.worldX = 2 * gp.tileSize;
                gp.player.worldY = 2 * gp.tileSize;

                gp.obj[0] = new OBJ_Bone();
                gp.obj[0].worldX = 6 * gp.tileSize;
                gp.obj[0].worldY = 5 * gp.tileSize;
                placed++;

                gp.obj[1] = new OBJ_Bone();
                gp.obj[1].worldX = 10 * gp.tileSize;
                gp.obj[1].worldY = 8 * gp.tileSize;
                placed++;

                gp.obj[2] = new OBJ_Treat();
                gp.obj[2].worldX = 7 * gp.tileSize;
                gp.obj[2].worldY = 9 * gp.tileSize;
                placed++;

                // ball object on map 5 (final map) - required to complete the game
                gp.obj[3] = new OBJ_Ball();
                gp.obj[3].worldX = 13 * gp.tileSize;
                gp.obj[3].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[4] = new OBJ_Log();
                gp.obj[4].worldX = 1 * gp.tileSize;
                gp.obj[4].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[5] = new OBJ_Bone();
                gp.obj[5].worldX = 11 * gp.tileSize;
                gp.obj[5].worldY = 5 * gp.tileSize;
                placed++;
                
                gp.obj[6] = new OBJ_Scissors();
                gp.obj[6].worldX = 5 * gp.tileSize;
                gp.obj[6].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[7] = new OBJ_Gate();
                gp.obj[7].worldX = 10 * gp.tileSize;
                gp.obj[7].worldY = 9 * gp.tileSize;
                placed++;

                gp.obj[8] = new OBJ_Log();
                gp.obj[8].worldX = 10 * gp.tileSize;
                gp.obj[8].worldY = 7 * gp.tileSize;
                placed++;

                gp.obj[9] = new OBJ_Log();
                gp.obj[9].worldX = 11 * gp.tileSize;
                gp.obj[9].worldY = 2 * gp.tileSize;
                placed++;

                gp.obj[10] = new OBJ_Bush();
                gp.obj[10].worldX = 10 * gp.tileSize;
                gp.obj[10].worldY = 5 * gp.tileSize;
                placed++;

                gp.obj[11] = new OBJ_Key();
                gp.obj[11].worldX = 13 * gp.tileSize;
                gp.obj[11].worldY = 5 * gp.tileSize;
                placed++;

                
                break;

            default:
                // no objects on other maps by default
                break;
        }

        System.out.println("AssetSetter: placed " + placed + " objects for map " + gp.currentMap);
    }
}