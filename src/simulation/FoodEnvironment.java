package simulation;

import gui.SwarmPanel;
import gui.TestComponent;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class FoodEnvironment {

	private static int maxX = 100;
	private static int maxY = 100;

	private Area freeArea;
	
	private SwarmPanel panel;

	List<Being> beings = new ArrayList<Being>();

	public FoodEnvironment() {
		Polygon worldArea = new Polygon();
		worldArea.addPoint(0, 0);
		worldArea.addPoint(maxX, 0);
		worldArea.addPoint(maxX, maxY);
		worldArea.addPoint(0, maxY);
		freeArea = new Area(worldArea);
	}

	public void addBeing(Being being) {
		
	}

	public Area getAreaFrom(Geometry geom) {
		GeneralPath path = new GeneralPath();
		Coordinate[] coords = geom.getCoordinates();
		if (coords.length > 0) {
			path.moveTo(coords[0].x, coords[0].y);
			for (int i = 1; i < coords.length; i++) {
				path.lineTo(coords[i].x, coords[i].y);
			}
			path.closePath();
		}

		return new Area(path);
	}
	
	private void startGui() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panel = new SwarmPanel();
				panel.init(new Agent[0]);
				Coordinate[] coordinates = new Coordinate[] {
						new Coordinate(20, 20), new Coordinate(100, 100),
						new Coordinate(200, 20),
						new Coordinate(20, 20)
				};
				Geometry g1 = new GeometryFactory().createLinearRing(coordinates);
				Geometry buffer = g1.buffer(5.0);
				System.out.println("G1 is simple " + g1.isSimple());
				System.out.println("G1 is valid " + g1.isValid());
				//System.out.println("Buffer is simple " + buffer.isSimple());
				//System.out.println("Buffer is valid " + buffer.isValid());
				TestComponent comp1 = new TestComponent(getAreaFrom(g1));
				TestComponent comp2 = new TestComponent(getAreaFrom(buffer));
				comp1.setLocation(20, 50);
				panel.add(comp1);
				comp2.setLocation(20, 50);
				panel.add(comp2);
//				Rectangle2D rec = new Rectangle2D.Double(20, 20, 200, 250);
//				panel.add(new TestComponent(new Area(rec)));
				
//				final JFrame frame = new JFrame();
//				frame.add(panel);
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				frame.setSize(new Dimension(500, 500));
//				frame.setVisible(true);
				
			}
		});
	}
	
	public static void main(String[] args) {
		new FoodEnvironment().startGui();
	}
}
