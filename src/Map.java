// Author: Aidan Fisher

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.util.ArrayList;

public class Map {
	public transient BufferedImage mapImage;
	public transient BufferedImage mapView;

	public Map(BufferedImage mapImage, BufferedImage mapView) {
		this.mapImage = mapImage;
		this.mapView = mapView;
	}

	public boolean blocked(int x, int y) {
		if (inBounds(x, y)) {
			Color color = new Color(mapImage.getRGB((int) x, (int) y));
			if (color.getBlue() == 255 && color.getRed() == 255 && color.getGreen() == 255) {
				return true;
			}
			return false;
		}
		return true;
	}

	public double getMinZ(Point2D p) {
		return getMinZ(p.getX(), p.getY());
	}

	public boolean inBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < mapImage.getWidth() && y < mapImage.getHeight();
	}

	public double getMinZ(double x, double y) {
		if (inBounds((int) x, (int) y)) {
			Color color = new Color(mapImage.getRGB((int) x, (int) y));
			return color.getRed() / 20.0;
		} else {
			return 0;
		}
	}

	public void render(Graphics2D g) {
		Point a = Game.game.getScreenPos(new Point.Double(0, 0));
		Point b = Game.game.getScreenPos(new Point.Double(mapView.getWidth() / 4.0, mapView.getHeight() / 4.0));
		g.drawImage(mapView, a.x, a.y, b.x - a.x, b.y - a.y, null);
	}
}
