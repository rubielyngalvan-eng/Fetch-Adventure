package Entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {

	public int worldX,worldY;
	public int speed;
	
	public BufferedImage up_1,up_2,up_3,up_4,right_1,right_2,right_3,right_4,left_1,left_2,left_3,left_4,down_1,down_2,down_3,down_4;
	public String direction;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public Rectangle solidArea;
	public boolean collisionOn = false;
	
	
	
	
}