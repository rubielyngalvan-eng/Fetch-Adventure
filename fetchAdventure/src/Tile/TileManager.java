package Tile;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileManager {

	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];

	// Data structures manager
	public final DataStructuresManager ds = new DataStructuresManager();

	public TileManager(GamePanel gp) {
		this.gp = gp;

		tile = new Tile[12];
		mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

		getTileImage();
		loadMap("assets/map01.txt");

		tile[0].collision = true; // grass
		tile[1].collision = false; // path
		tile[2].collision = true; // wall
		tile[3].collision = true; // tree
		tile[4].collision = true; // water
		tile[5].collision = false; // stair
		tile[6].collision = true; // Trunk
		tile[7].collision = true; // PinkTree
		tile[8].collision = true; // FruitTREE


		// sync the data structures initially
		ds.syncTilesArray(tile);
		ds.buildTileIdTree(tile);
		ds.syncMapRows(mapTileNum);
		ds.buildGraphFromMap(mapTileNum, gp.maxScreenCol, gp.maxScreenRow, tile);
	}

	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(new File("assets/FlowerBushTrunk.png"));

			tile[1] = new Tile();
			tile[1].image = ImageIO.read(new File("assets/sand.png"));

			tile[2] = new Tile();
			tile[2].image = ImageIO.read(new File("assets/wall.png"));

			tile[3] = new Tile();
			tile[3].image = ImageIO.read(new File("assets/treeBG.png"));

			tile[4] = new Tile();
			tile[4].image = ImageIO.read(new File("assets/water.png"));

			tile[5] = new Tile();
			tile[5].image = ImageIO.read(new File("assets/stair.png"));

			tile[6] = new Tile();
			tile[6].image = ImageIO.read(new File("assets/Screenshot 2025-11-24 153112.png"));

			tile[7] = new Tile();
			tile[7].image = ImageIO.read(new File("assets/TREE222.png"));
			
			tile[8] = new Tile();
			tile[8].image = ImageIO.read(new File("assets/tree33.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void loadMap(String filePatch) {
		// before loading: push current map to history
		ds.pushMapToHistory(mapTileNum);

		InputStream is = null;
		try {
			// Prefer filesystem assets path
			File f = new File(filePatch);
			if (f.exists()) {
				is = new FileInputStream(f);
			} else {
				// Fallback to classpath resource
				is = getClass().getResourceAsStream(filePatch);
			}

			if (is == null) {
				throw new IOException("Map file not found: " + filePatch);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				int col = 0;
				int row = 0;

				while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
					String line = br.readLine();
					if (line == null) break;

					String[] numbers = line.trim().split("\\s+");

					if (numbers.length == 1 && numbers[0].length() >= gp.maxScreenCol) {
						// map line without spaces, each character is a tile id
						String s = numbers[0];
						for (int i = 0; i < gp.maxScreenCol && i < s.length(); i++) {
							int num = Character.getNumericValue(s.charAt(i));
							mapTileNum[col][row] = num;
							col++;
						}
					} else {
						while (col < gp.maxScreenCol && col < numbers.length) {
							int num = Integer.parseInt(numbers[col]);
							mapTileNum[col][row] = num;
							col++;
						}
					}

					if (col == gp.maxScreenCol) {
						col = 0;
						row++;
					}
				}
			}

			// AFTER loading, update the data structures:
			ds.syncTilesArray(tile);
			ds.syncMapRows(mapTileNum);
			ds.buildTileIdTree(tile); //to quickly check if tile id 5 exists
			ds.buildGraphFromMap(mapTileNum, gp.maxScreenCol, gp.maxScreenRow, tile);

		} catch (Exception e) {
			System.err.println("Failed to load map '" + filePatch + "': " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException ignored) {
			}
		}
	}

	public void loadNextMap(String mapPath) {
		loadMap(mapPath);
	}

	public void draw(Graphics2D g2) { // helper to allow external callers (GamePanel) to load the next map
		// Draw tiles without camera (fixed to screen coordinates starting at 0,0)
		for (int row = 0; row < gp.maxScreenRow; row++) {
			for (int col = 0; col < gp.maxScreenCol; col++) {
				int tileNum = mapTileNum[col][row];
				int x = col * gp.tileSize;
				int y = row * gp.tileSize;

				// Only draw if inside the visible screen
				if (x + gp.tileSize > 0 && x < gp.screenWidth && y + gp.tileSize > 0 && y < gp.screenHeight) {
					g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
				}
			}
		}
	}

}