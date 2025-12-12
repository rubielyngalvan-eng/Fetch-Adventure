package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;
public class HomePage extends JPanel {

    Image bgImage;
    Rectangle playBtn;
    boolean hover = false;

    JFrame window;
    GamePanel gamePanel;

    public HomePage(JFrame window, GamePanel gamePanel) {
        this.window = window;
        this.gamePanel = gamePanel;
        
            // match the GamePanel size so switching doesn't resize the window
            setPreferredSize(gamePanel.getPreferredSize());

        // Load homepage image (optional) from local assets folder
        try {
            File f = new File("assets/home.png");
            if (f.exists()) {
                bgImage = ImageIO.read(f);
            }
        } catch (Exception ignored) {
        }
            // Play button: centered and sized relative to the panel
            Dimension sz = gamePanel.getPreferredSize();
            // make button narrower/taller so it doesn't fully cover artwork
            int btnW = Math.min(450, sz.width - 300);
            int btnH = 72;
            int bx = (sz.width - btnW) / 2;
            // nudge lower so it matches the artwork's built-in play area
            int by = (int) (sz.height * 0.68);
            playBtn = new Rectangle(bx, by, btnW, btnH);

        // Mouse click to start game
        addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                if (playBtn.contains(e.getPoint())) {
                    goToGame();
                }
            }
        });

        // Hover effect
        addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                hover = playBtn.contains(e.getPoint());
                repaint();
            }
        });
    }

    private void goToGame() {
        window.remove(this);      // remove homepage 
        window.add(gamePanel);    // show game screen
        window.revalidate();
        window.repaint();

        gamePanel.setupGame();
        gamePanel.startGameThread();
        gamePanel.requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw home.png if available
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            // fallback background
            g2.setColor(new Color(30, 40, 70));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Play Button
        Color original = hover ? Color.GREEN : Color.ORANGE;
        Color darker = original.darker();  // automatically returns a darker shade
        Color c = hover ? darker : original;
        g2.setColor(c);
        g2.fill(playBtn);


        // Border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(4));
        g2.draw(playBtn);

        // PLAY text
        g2.setFont(new Font("Arial", Font.BOLD, 45));
        drawCenteredText(g2, "PLAY", playBtn);
    }

    private void drawCenteredText(Graphics2D g2, String text, Rectangle rect) {
        FontMetrics fm = g2.getFontMetrics();
        int x = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int y = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, x, y);
    }
}