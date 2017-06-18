package simulation;

/**
 * Created by zahar on 18/06/17.
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KeyListenerTester extends JPanel implements KeyListener {

    JLabel label;

    public KeyListenerTester(String s) {
//        super(s);

        JFrame F = new JFrame();
        JPanel p = new JPanel();
        label = new JLabel("Key Listener!");
        add(label);
        F.add(this);
        addKeyListener(this);
        setSize(200, 100);
        setVisible(true);

    }

    @Override
    public void keyTyped(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("Right key typed");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("Left key typed");
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("Right key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("Left key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("Up key pressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("Right key Released");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("Left key Released");
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("Up key Released");
        }
    }

    public static void main(String[] args) {
        new KeyListenerTester("Key Listener Tester");
    }
}
