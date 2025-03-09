import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class NeumorphicToggleButton extends JPanel {
    private boolean isToggled = false;

    public NeumorphicToggleButton() {
        setPreferredSize(new Dimension(140, 140));
        setBackground(new Color(40, 40, 40)); // Background color
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isToggled = !isToggled;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Outer Neumorphic Effect
        g2.setColor(new Color(30, 30, 30));
        g2.fillOval(10, 10, 120, 120);

        // Soft Shadow (Dark Side)
        g2.setColor(new Color(20, 20, 20, 150));
        g2.fillOval(14, 14, 120, 120);

        // Soft Shadow (Light Side)
        g2.setColor(new Color(60, 60, 60, 150));
        g2.fillOval(6, 6, 120, 120);

        // Inner Circle Toggle Effect
        g2.setColor(isToggled ? new Color(77, 124, 255) : new Color(165, 165, 165));
        g2.fillOval(50, 50, 40, 40);

        // Inner Glow Effect
        if (isToggled) {
            g2.setColor(new Color(77, 124, 255, 80));
            g2.fillOval(40, 40, 60, 60);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Neumorphic Toggle Button");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLayout(new BorderLayout());
            frame.add(new NeumorphicToggleButton(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
