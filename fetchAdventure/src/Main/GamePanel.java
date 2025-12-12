package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;

import Entity.PlayerDog;
import Object.SuperObject;
import Tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    // screen settings
    public final int originalTileSize = 18;  // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;   //54x54 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //FPS
    int FPS = 60;
    // game states
    public int gameState; // current state
    public final int playState = 1;
    public final int endState = 0;
    public int currentMap = 1;
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public boolean mapChanging = false;
    public int mapChangeCooldown = 30; // number of update cycles to ignore further map changes
    public int mapChangeTimer = 0;

    public CollisionChecker cChecker = new CollisionChecker(this);
    public SuperObject obj[] = new SuperObject[20];  // the 10 is kung pila ka item ma display in at the same time
    public PlayerDog player = new PlayerDog(this, keyH);
    public AssetSetter aSetter = new AssetSetter(this);
    public AudioManager audioManager = new AudioManager();
    // ending screen background
    public java.awt.image.BufferedImage endingBg;
    // exit button bounds (set when rendering ending screen)
    private java.awt.Rectangle exitButtonRect;
    // hover state for exit button
    private boolean exitHover = false;
    // simple counter for collected bones
    public int boneCount = 0;
    // simple counter for collected treats
    public int treatCount = 0;
    // HUD icon for bone (optional)
    public java.awt.image.BufferedImage boneIcon;
    // cached small HUD icons (pre-scaled)
    public java.awt.image.BufferedImage boneIconSmall;
    // scissors state + icon
    public boolean hasScissors = false;
    public java.awt.image.BufferedImage scissorsIcon;
    public java.awt.image.BufferedImage scissorsIconSmall;
    // treat icon + counter
    public java.awt.image.BufferedImage treatIcon;
    public java.awt.image.BufferedImage treatIconSmall;
    // key state + icon
    public boolean hasKey = false;
    public java.awt.image.BufferedImage keyIcon;
    public java.awt.image.BufferedImage keyIconSmall;

    // ball state (required for final map completion)
    public boolean hasBall = false;
    public java.awt.image.BufferedImage ballIcon;
    public java.awt.image.BufferedImage ballIconSmall;

    // speed boost from treats
    public boolean speedBoosted = false;
    public int speedBoostTimer = 0; // frames remaining for speed boost
    public final int speedBoostDuration = 300; // ~5 seconds at 60 FPS

    // temporary on-screen message
    public String message = "";
    public int messageTimer = 0; // frames remaining to show message

    // victory flag and timer
    public boolean victoryAchieved = false;
    public int victoryTimer = 0;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.GREEN);
        this.setDoubleBuffered(true);  // all drawings from this component will be done in an offscreen painting buffer
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.gameState = playState; // start in play state by default

        // Mouse listener for ending screen exit button
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameState == endState && exitButtonRect != null) {
                    if (exitButtonRect.contains(e.getX(), e.getY())) {
                        // Exit the game
                        System.exit(0);
                    }
                }
            }
        });

        // Mouse motion listener for hover effect on ending-screen button
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (gameState == endState && exitButtonRect != null) {
                    boolean nowHover = exitButtonRect.contains(e.getX(), e.getY());
                    if (nowHover != exitHover) {
                        exitHover = nowHover;
                        repaint();
                    }
                } else if (exitHover) {
                    exitHover = false;
                    repaint();
                }
            }
        });

    }

    // Draw a temporary centered message near the bottom of the screen
    private void drawMessage(Graphics2D g2, String msg) {
        Font prev = g2.getFont();
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(msg);
        int x = (screenWidth - textWidth) / 2;
        int y = screenHeight - 80;

        // background
        java.awt.Color bg = new java.awt.Color(0, 0, 0, 160);
        g2.setColor(bg);
        g2.fillRoundRect(x - 12, y - fm.getAscent(), textWidth + 24, fm.getHeight() + 10, 12, 12);

        // text
        g2.setColor(java.awt.Color.WHITE);
        g2.drawString(msg, x, y + 4);

        g2.setFont(prev);
    }

    public void setupGame() {
        aSetter.setObject();

        // Start background music
        audioManager.playBackgroundMusic("assets/backgroundmusic.wav");

        // Try to load a bone icon for the HUD
        try {
            java.io.File f = new java.io.File("assets/bone.png");
            if (f.exists()) {
                boneIcon = ImageIO.read(f);
                // prepare small HUD version
                boneIconSmall = getScaledBufferedImage(boneIcon, 20, 20);
            }
            java.io.File f2 = new java.io.File("assets/cut.png");
            if (f2.exists()) {
                scissorsIcon = ImageIO.read(f2);
                scissorsIconSmall = getScaledBufferedImage(scissorsIcon, 20, 20);
            }
            java.io.File f3 = new java.io.File("assets/treat 1.png");
            if (f3.exists()) {
                treatIcon = ImageIO.read(f3);
                treatIconSmall = getScaledBufferedImage(treatIcon, 20, 20);
            }
            java.io.File f4 = new java.io.File("assets/key.png");
            if (f4.exists()) {
                keyIcon = ImageIO.read(f4);
                keyIconSmall = getScaledBufferedImage(keyIcon, 20, 20);
            }
            java.io.File f5 = new java.io.File("assets/ballRED.png");
            if (f5.exists()) {
                ballIcon = ImageIO.read(f5);
                ballIconSmall = getScaledBufferedImage(ballIcon, 20, 20);
            }
            java.io.File f6 = new java.io.File("assets/ending.png");
            if (f6.exists()) {
                endingBg = ImageIO.read(f6);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }

    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            //currentTime = System.nanoTime();
            //1.update informatiom such as character position 
            update();
            //2. draw the screen with the updated information for example the dog is in x:100,y:100 the e move down siyag ka isa, the updated info position will be x;100,y;100+5(pixels)=105
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void update() {
        if (gameState == playState) {
            player.update();

            // map change cooldown timer
            if (mapChanging) {
                mapChangeTimer--;
                if (mapChangeTimer <= 0) {
                    mapChanging = false;
                    mapChangeTimer = 0;
                }
            }

            // update temporary message timer
            if (messageTimer > 0) {
                messageTimer--;
                if (messageTimer == 0) {
                    message = "";
                }
            }

            // update speed boost timer
            if (speedBoostTimer > 0) {
                speedBoostTimer--;
                if (speedBoostTimer == 0) {
                    speedBoosted = false;
                }
            }
            // no titleState handling here; HomePage handles the title screen

        }

    }

    public void nextMap() {
        // Check if currently on map 5 (final map) with ball - trigger victory
        if (currentMap == 5 && hasBall) {
            System.out.println("VICTORY! You completed the game!");
            audioManager.playSoundEffect("assets/victory.wav");
            gameState = 0; // End game state (or whatever end state you want)
            return;
        }

        currentMap++;

        // Load the next map 
        String nextMapPath = "assets/map0" + currentMap + ".txt";
        java.io.File f = new java.io.File(nextMapPath);
        if (!f.exists()) {
            // No more maps available — revert the counter and log
            currentMap--;
            System.err.println("Next map not found: " + nextMapPath);
            // activate cooldown to avoid repeated attempts
            mapChanging = true;
            mapChangeTimer = mapChangeCooldown;
            return;
        }

        tileM.loadNextMap(nextMapPath);

        // Reset objects for the new map so objects from the previous map don't persist
        aSetter.setObject();

        // Reset per-map items: scissors only last for the current level
        this.hasScissors = false;
        // Inform the player the scissors are reset when moving to a new level
        this.message = "Scissors are reset on new level";
        this.messageTimer = 120; // show for ~2 seconds at 60fps

        // NOTE: Player position is set by AssetSetter.setObject() for each map.
        // Do not overwrite it here so per-map starting positions are preserved.
        // start cooldown to avoid immediate retrigger
        mapChanging = true;
        mapChangeTimer = mapChangeCooldown;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (gameState == playState) {
            tileM.draw(g2);

            for (SuperObject o : obj) {
                if (o != null) {
                    o.draw(g2, this);
                }
            }

            player.draw(g2);

            // draw HUD (bone count)
            drawHUD(g2);

            // draw temporary message if present
            if (!message.isEmpty()) {
                drawMessage(g2, message);
            }
        } else if (gameState == endState) {
            // Draw ending screen
            if (endingBg != null) {
                g2.drawImage(getScaledBufferedImage(endingBg, screenWidth, screenHeight), 0, 0, null);
            } else {
                // fallback background
                g2.setColor(new Color(20, 20, 30));
                g2.fillRect(0, 0, screenWidth, screenHeight);
            }

            // Draw summary text
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(java.awt.Color.WHITE);
            String title = "You Win!";
            int titleW = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, (screenWidth - titleW) / 2, screenHeight / 3);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 24));
            String bones = "Bones collected: " + boneCount;
            int bW = g2.getFontMetrics().stringWidth(bones);
            g2.drawString(bones, (screenWidth - bW) / 2, screenHeight / 3 + 50);

                // Draw exit button (lowered) with hover color change
                int btnW = 160;
                int btnH = 48;
                int btnX = (screenWidth - btnW) / 2;
                // moved lower
                int btnY = screenHeight / 2 + 100;
                exitButtonRect = new java.awt.Rectangle(btnX, btnY, btnW, btnH);

                // base color: yellow; hover color: green
                java.awt.Color base = new java.awt.Color(255, 203, 0); // warm yellow
                java.awt.Color hover = new java.awt.Color(40, 180, 60); // green on hover
                g2.setColor(exitHover ? hover : base);
                g2.fillRoundRect(btnX, btnY, btnW, btnH, 12, 12);
                g2.setColor(java.awt.Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 20));
                String btnText = "Exit";
                int tx = g2.getFontMetrics().stringWidth(btnText);
                g2.drawString(btnText, btnX + (btnW - tx) / 2, btnY + (btnH + g2.getFontMetrics().getAscent()) / 2 - 6);
        }


        g2.dispose();
    }

    // Draw a simple HUD showing collected bones
    private void drawHUD(Graphics2D g2) {
        // Compact HUD layout: bone (with count), treat (with count if any), key and scissors as icons
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        int startX = 12;
        int startY = 12; // top padding
        int iconSize = 20;
        int innerGap = 6; // gap between icon and text
        int itemGap = 12; // gap between items

        // compute total width
        int totalWidth = 0;
        // bone (always)
        int boneTextWidth = fm.stringWidth("x " + boneCount);
        totalWidth += iconSize + innerGap + boneTextWidth;

        // treat
        boolean showTreat = treatCount > 0;
        int treatTextWidth = 0;
        if (showTreat) {
            treatTextWidth = fm.stringWidth("x " + treatCount);
            totalWidth += itemGap + iconSize + innerGap + treatTextWidth;
        }

        // key (icon only)
        if (hasKey) {
            totalWidth += itemGap + iconSize;
        }

        // scissors (icon only)
        if (hasScissors) {
            totalWidth += itemGap + iconSize;
        }

        // ball (icon only)
        if (hasBall) {
            totalWidth += itemGap + iconSize;
        }

        int bgX = startX - 8;
        int bgY = startY - 6;
        int bgH = iconSize + 12;
        int bgW = totalWidth + 24;
        java.awt.Color bg = new java.awt.Color(0, 0, 0, 160);
        g2.setColor(bg);
        g2.fillRoundRect(bgX, bgY, bgW, bgH, 10, 10);

        int x = startX;
        int y = startY + (bgH - iconSize) / 2;
        g2.setColor(java.awt.Color.WHITE);

        // bone
        if (boneIconSmall != null) {
            g2.drawImage(boneIconSmall, x, y, null);
        } else if (boneIcon != null) {
            g2.drawImage(getScaledBufferedImage(boneIcon, iconSize, iconSize), x, y, null);
        } else {
            g2.fillOval(x, y, iconSize, iconSize);
        }
        x += iconSize + innerGap;
        g2.drawString("x " + boneCount, x, y + iconSize - 4);
        x += boneTextWidth;

        // treat
        if (showTreat) {
            x += itemGap;
            if (treatIconSmall != null) {
                g2.drawImage(treatIconSmall, x, y, null);
            } else if (treatIcon != null) {
                g2.drawImage(getScaledBufferedImage(treatIcon, iconSize, iconSize), x, y, null);
            } else {
                g2.fillOval(x, y, iconSize, iconSize);
            }
            x += iconSize + innerGap;
            g2.drawString("x " + treatCount, x, y + iconSize - 4);
            x += treatTextWidth;
        }

        // key icon only
        if (hasKey) {
            x += itemGap;
            if (keyIconSmall != null) {
                g2.drawImage(keyIconSmall, x, y, null);
            } else if (keyIcon != null) {
                g2.drawImage(getScaledBufferedImage(keyIcon, iconSize, iconSize), x, y, null);
            } else {
                g2.fillRect(x, y, iconSize, iconSize);
            }
            x += iconSize;
        }

        // scissors icon only
        if (hasScissors) {
            x += itemGap;
            if (scissorsIconSmall != null) {
                g2.drawImage(scissorsIconSmall, x, y, null);
            } else if (scissorsIcon != null) {
                g2.drawImage(getScaledBufferedImage(scissorsIcon, iconSize, iconSize), x, y, null);
            } else {
                g2.fillRect(x, y, iconSize, iconSize);
            }
            x += iconSize;
        }

        // ball icon only
        if (hasBall) {
            x += itemGap;
            if (ballIconSmall != null) {
                g2.drawImage(ballIconSmall, x, y, null);
            } else if (ballIcon != null) {
                g2.drawImage(getScaledBufferedImage(ballIcon, iconSize, iconSize), x, y, null);
            } else {
                g2.fillOval(x, y, iconSize, iconSize);
            }
            x += iconSize;
        }
    }

    // Helper to create a scaled BufferedImage from a source
    private java.awt.image.BufferedImage getScaledBufferedImage(java.awt.image.BufferedImage src, int w, int h) {
        if (src == null) {
            return null;
        }
        java.awt.image.BufferedImage resized = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return resized;
    }

}