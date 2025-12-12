package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Main.KeyHandler;

public class PlayerDog extends Entity {
	
	
	GamePanel gp;
	KeyHandler keyH;
	public int screenX;
	public int screenY;
	
	
	public PlayerDog(GamePanel gp, KeyHandler keyH) {
	
		this.gp = gp;
		this.keyH = keyH;
		screenX = gp.screenWidth / 2 - gp.tileSize / 2;
	    screenY = gp.screenHeight / 2 - gp.tileSize / 2;
		 try {
			 // attempt to load a placeholder dog image if present
			 File f = new File("assets/dog.png");
			 if (f.exists()) {
				 up_1 = ImageIO.read(f);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }

		 
		//collision
		solidArea = new Rectangle();
		solidArea.x= 15;
		solidArea.y=15;
		solidArea.width=32;
		solidArea.height=32;
		
		
		
		setDefaultValues();
		getPlayerImage();
		
		
	}

	// temporary rectangles reused each frame to avoid allocations
	private java.awt.Rectangle tmpPlayerRect = new java.awt.Rectangle();
	private java.awt.Rectangle tmpObjRect = new java.awt.Rectangle();
	private java.awt.Rectangle tmpTargetRect = new java.awt.Rectangle();
	public void setDefaultValues(){
		worldX=100;
		worldY=100;
		speed=3;
		direction = "down";
	}
	
	public void getPlayerImage() {
		
		try {
			
			up_1=ImageIO.read(new File("assets/up_1.png"));
			up_2=ImageIO.read(new File("assets/up_2.png"));
			up_3=ImageIO.read(new File("assets/up_3.png"));
			up_4=ImageIO.read(new File("assets/up_4.png"));
			down_1=ImageIO.read(new File("assets/down_1.png"));
			down_2=ImageIO.read(new File("assets/down_2.png"));
			down_3=ImageIO.read(new File("assets/down_3.png"));
			down_4=ImageIO.read(new File("assets/down_4.png"));
			right_1=ImageIO.read(new File("assets/right_1.png"));
			right_2=ImageIO.read(new File("assets/right_2.png"));
			right_3=ImageIO.read(new File("assets/right_3.png"));
			right_4=ImageIO.read(new File("assets/right_4.png"));
			left_1=ImageIO.read(new File("assets/left_1.png"));
			left_2=ImageIO.read(new File("assets/left_2.png"));
			left_3=ImageIO.read(new File("assets/left_3.png"));
			left_4=ImageIO.read(new File("assets/left_4.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void update() {
		
		if(keyH.upPressed == true || keyH.downPressed == true || 
				keyH.leftPressed == true || keyH.rightPressed == true ) {
			
			if(keyH.upPressed ) {
				direction = "up";
				
			}
			else if(keyH.downPressed ) {
				direction = "down";
				
			}
			else if(keyH.leftPressed) {
				direction = "left";
				
			}
			else if(keyH.rightPressed ) {
				direction = "right";
				
			}
			int playerCol = worldX / gp.tileSize;
			int playerRow = worldY / gp.tileSize;

			if (playerCol >= 0 && playerCol < gp.maxScreenCol && playerRow >= 0 && playerRow < gp.maxScreenRow) {
			    int tileNum = gp.tileM.mapTileNum[playerCol][playerRow];


			    if (tileNum == 5 && !gp.mapChanging) { // 5 = stair tile
			        // Check if on final map and require ball
			        if (gp.currentMap == 5 && !gp.hasBall) { // map 5 = final map
			            System.out.println("You need the ball to complete the game!");
			        } else {
			            gp.nextMap(); // go to next map or trigger victory
			        }
			    }
			}
			
			
				//check tile collision
				collisionOn = false; // reset before checking
	            gp.cChecker.checkTile(this); // call collision system
			// also check object collisions (e.g., bushes)
			gp.cChecker.checkObject(this);
			
			if (!collisionOn) {
				int currentSpeed = gp.speedBoosted ? speed + 5 : speed; // +5 speed boost
				int dx = 0, dy = 0;
				switch (direction) {
					case "up": dy = -1; break;
					case "down": dy = 1; break;
					case "left": dx = -1; break;
					case "right": dx = 1; break;
				}

				// Move one pixel at a time to avoid skipping over collidable objects
				for (int s = 0; s < currentSpeed; s++) {
					worldX += dx;
					worldY += dy;
					// Re-check collisions at each incremental step
					collisionOn = false;
					gp.cChecker.checkTile(this);
					gp.cChecker.checkObject(this);
					if (collisionOn) {
						// undo this step and stop further movement
						worldX -= dx;
						worldY -= dy;
						break;
					}
				}
			}

			// Check for object pickups after movement (reuse tmp rectangles)
			tmpPlayerRect.setBounds(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);
			for (int i = 0; i < gp.obj.length; i++) {
				if (gp.obj[i] != null) {
					tmpObjRect.setBounds(gp.obj[i].worldX, gp.obj[i].worldY, gp.tileSize, gp.tileSize);
					if (tmpPlayerRect.intersects(tmpObjRect)) {
						// If it's a bone, collect it and remove from the world
						if ("Bone".equals(gp.obj[i].name)) {
							gp.boneCount++;
							System.out.println("Picked up bone; total=" + gp.boneCount);
							} else if ("Scissors".equals(gp.obj[i].name)) {
							gp.hasScissors = true;
							System.out.println("Picked up scissors; you can now cut bushes.");
							// show brief on-screen instruction
							gp.message = "Press SPACE to cut bushes";
							gp.messageTimer = 120; // show for ~2 seconds at 60fps
							} else if ("Treat".equals(gp.obj[i].name)) {
							gp.treatCount++;
							gp.speedBoosted = true;
							gp.speedBoostTimer = gp.speedBoostDuration;
							System.out.println("Picked up treat; total=" + gp.treatCount + " - SPEED BOOST!");
							gp.message = "Speed Boost!";
							gp.messageTimer = 120;
							} else if ("Key".equals(gp.obj[i].name)) {
								gp.hasKey = true;
								System.out.println("Picked up key; you can now unlock gates.");
								gp.message = "Press SPACE to use the key";
								gp.messageTimer = 120;
						} else if ("Ball".equals(gp.obj[i].name)) {
								gp.hasBall = true;
								System.out.println("Picked up ball! You can now complete the final map.");
								gp.message = "You collected the ball!";
								gp.messageTimer = 120;
						}
						gp.obj[i] = null; // remove the object from the map
					}
				}
			}

			// Handle action (cut) - consume the key press here so the player must press again
			if (keyH.actionPressed) {
				keyH.actionPressed = false; // consume
				// First, if player has a key, try to unlock a gate in front
				boolean actionHandled = false;
				if (gp.hasKey) {
					int tx = worldX;
					int ty = worldY;
					switch (direction) {
						case "up": ty -= gp.tileSize; break;
						case "down": ty += gp.tileSize; break;
						case "left": tx -= gp.tileSize; break;
						case "right": tx += gp.tileSize; break;
					}
					tmpTargetRect.setBounds(tx, ty, gp.tileSize, gp.tileSize);
					for (int i = 0; i < gp.obj.length; i++) {
						if (gp.obj[i] != null && "Gate".equals(gp.obj[i].name)) {
							tmpObjRect.setBounds(gp.obj[i].worldX, gp.obj[i].worldY, gp.tileSize, gp.tileSize);
							if (tmpTargetRect.intersects(tmpObjRect)) {
								gp.obj[i] = null; // unlock and remove gate
								gp.hasKey = false; // consume key
								gp.message = "You unlocked the gate.";
								gp.messageTimer = 120;
								System.out.println("Unlocked gate with key.");
								actionHandled = true;
								break;
							}
						}
					}
				}

				if (!actionHandled && gp.hasScissors) {
					// determine a target rect in front of the player
					int tx = worldX;
					int ty = worldY;
					switch (direction) {
						case "up": ty -= gp.tileSize; break;
						case "down": ty += gp.tileSize; break;
						case "left": tx -= gp.tileSize; break;
						case "right": tx += gp.tileSize; break;
					}
					tmpTargetRect.setBounds(tx, ty, gp.tileSize, gp.tileSize);
					for (int i = 0; i < gp.obj.length; i++) {
						if (gp.obj[i] != null && "Bush".equals(gp.obj[i].name)) {
							tmpObjRect.setBounds(gp.obj[i].worldX, gp.obj[i].worldY, gp.tileSize, gp.tileSize);
						if (tmpTargetRect.intersects(tmpObjRect)) {
							gp.obj[i] = null; // cut the bush
							System.out.println("Cut the bush with scissors.");
							// Play bush cutting sound effect
							gp.audioManager.playSoundEffect("assets/bush.wav");
							break;
						}
						}
					}
				} else if (!actionHandled) {
					System.out.println("Nothing to use here or you need a tool (scissors/key).");
				}
			}
			
			spriteCounter++;
			if(spriteCounter > 10) {
				if(spriteNum == 1) {
					spriteNum =2;	
				}
				else if(spriteNum == 2) {
					spriteNum =1;
				}
				spriteCounter =0 ;
			}
		}
		
	
		
	}
	public void draw(Graphics2D g2) {
	//	g2.setColor(Color.white);
	//	g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		
		BufferedImage image = null;
		
		switch(direction) {
		case "up":
			if(spriteNum == 1) image = up_1;
			if(spriteNum == 2) image = up_2;
			if(spriteNum == 3) image = up_3;
			if(spriteNum == 4) image = up_4;
			break;
		case "down":
			if(spriteNum == 1) {
				image = down_1;
			}
			if(spriteNum == 2) {
				image = down_2;
			}
			if(spriteNum == 3) {
				image = down_3;
			}
			if(spriteNum == 4) {
				image = down_4;
			}
			break;
		case "left":
			if(spriteNum == 1) {
				image = left_1;
			}
			if(spriteNum == 2) {
				image = left_2;
			}
			if(spriteNum == 3) {
				image = left_3;
			}
			if(spriteNum == 4) {
				image = left_4;
			}
			
			break;
		case "right":
			if(spriteNum == 1) {
				image = right_1;
			} 
			if(spriteNum == 2) {
				image = right_2;
			}
			if(spriteNum == 3) {
				image = right_3;
			}
			if(spriteNum == 4) {
				image = right_4;
			}
			break;
		}
		// Draw player at the screen position (camera-centered), not world coords
		// Draw using world coordinates (no camera)
		g2.drawImage(image, worldX, worldY, gp.tileSize, gp.tileSize , null);
	}
}