package swing.customcomponents;

import swing.customcomponents.RoundBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundButton extends JButton {
    private Shape shape;
    final int cornerRadius;
    private Color originalBackground;
    private Color originalForeground;
    private Color originalBorder;
    private Color hoverBackground;
    private Color hoverForeground;
    private Color hoverBorder;

    public RoundButton(String label, int cornerRadius) {
        super(label);

        setOpaque(false);

        setBackground(Color.WHITE);

        this.cornerRadius = cornerRadius;

        this.setFocusPainted(false);
        this.setContentAreaFilled(false);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                originalBackground = getBackground();
                originalForeground = getForeground();
                originalBorder = getBorder() != null ? ((RoundBorder)getBorder()).getColor() : originalForeground;

                hoverBackground = new Color(originalBackground.getRed(), originalBackground.getGreen(), originalBackground.getBlue(), 33);
                hoverForeground = originalForeground == Color.BLACK ? originalBorder : originalBackground;
                hoverBorder = originalBackground;
                setBackground(hoverBackground);
                setForeground(hoverForeground);
                setBorder(new RoundBorder(cornerRadius, hoverBorder));
                setIgnoreRepaint(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(originalBackground);
                setForeground(originalForeground);
                setBorder(new RoundBorder(cornerRadius, originalBorder));
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(originalForeground == Color.BLACK) {
                    setBorder(new RoundBorder(cornerRadius, hoverBorder.darker()));
                    setBackground(hoverBackground.darker());
                    setForeground(hoverForeground.darker());
                } else if (originalForeground == Color.WHITE){
                    setBackground(originalBackground.darker());
                    setForeground(originalForeground);
                    setBorder(new RoundBorder(cornerRadius, getBackground().darker()));
                } else {
                    setForeground(originalForeground.darker());
                }
                setIgnoreRepaint(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getForeground() == Color.BLACK) {
                    setBackground(originalBackground);
                    setForeground(originalForeground);
                    setBorder(new RoundBorder(cornerRadius, originalBorder));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getModel().isArmed() ? originalBackground : getBackground());
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, cornerRadius, cornerRadius);
        super.paintComponent(g);
    }
    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
        return shape.contains(x, y);
    }
    public void playSound(String filePath, boolean haveSignal) {
        if (haveSignal) {
            try {
                File soundFile = new File(filePath);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}