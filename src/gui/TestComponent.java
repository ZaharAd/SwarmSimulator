package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class TestComponent extends JComponent {
	
	Area area;
	
	public TestComponent(Area area) {
		this.area = area;
		setOpaque(false);
		setVisible(true);
		Rectangle bounds = area.getBounds();
		setSize(bounds.width+50, bounds.height+50);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		g2.draw(area);
	}
	
}