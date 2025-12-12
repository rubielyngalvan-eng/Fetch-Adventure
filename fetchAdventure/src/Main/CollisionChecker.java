package Main;

import Entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // Check object collisions (blocks movement for objects with collision=true)
    public void checkObject(Entity entity) {
        int nextLeft = entity.worldX + entity.solidArea.x;
        int nextRight = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int nextTop = entity.worldY + entity.solidArea.y;
        int nextBottom = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        switch (entity.direction) {
            case "up":
                nextTop -= entity.speed;
                nextBottom -= entity.speed;
                break;
            case "down":
                nextTop += entity.speed;
                nextBottom += entity.speed;
                break;
            case "left":
                nextLeft -= entity.speed;
                nextRight -= entity.speed;
                break;
            case "right":
                nextLeft += entity.speed;
                nextRight += entity.speed;
                break;
        }

        java.awt.Rectangle entityRect = new java.awt.Rectangle(nextLeft, nextTop, nextRight - nextLeft, nextBottom - nextTop);

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                // object's collision area is tile-sized
                java.awt.Rectangle objRect = new java.awt.Rectangle(gp.obj[i].worldX, gp.obj[i].worldY, gp.tileSize, gp.tileSize);
                if (entityRect.intersects(objRect)) {
                    if (gp.obj[i].collision) {
                        entity.collisionOn = true;
                        return; // blocked by object
                    }
                }
            }
        }
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;
        
        //Prevent out-of-bounds before using mapTileNum[][]
        entityLeftCol = Math.max(0, Math.min(entityLeftCol, gp.maxScreenCol - 1));
        entityRightCol = Math.max(0, Math.min(entityRightCol, gp.maxScreenCol - 1));
        entityTopRow = Math.max(0, Math.min(entityTopRow, gp.maxScreenRow - 1));
        entityBottomRow = Math.max(0, Math.min(entityBottomRow, gp.maxScreenRow - 1));
        
        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                entityTopRow = Math.max(0, Math.min(entityTopRow, gp.maxScreenRow - 1));
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                entityBottomRow = Math.max(0, Math.min(entityBottomRow, gp.maxScreenRow - 1));
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                entityLeftCol = Math.max(0, Math.min(entityLeftCol, gp.maxScreenCol - 1));
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                entityRightCol = Math.max(0, Math.min(entityRightCol, gp.maxScreenCol - 1));
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
}