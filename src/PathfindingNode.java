// Author: Aidan Fisher

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class PathfindingNode implements Serializable {
	// Also lists orders: (All include pathfinding, even if already there)
	public static final byte PATH_FINDING = 0;
	public static final byte DISPENSER_BUILD = 1;
	public static final byte SENTRY_BUILD = 2;

	private static final long serialVersionUID = 394100569780330688L;

	public double x;
	public double y;

	public PathfindingNode() {
		// Nodes can't be created client side. (Also only x & y data are saved)
	}

	// For sniper rifle:
	public static Point.Double lineTo(Point.Double start, double rotation) {
		// Basically, the goal is to acquire a list of grid squares that are passed through.
		// note 0, 90, special cases, do happen.
		int xSign;
		if (Math.cos(rotation) >= 0) {
			xSign = 1;
		} else {
			xSign = -1;
		}
		int ySign;
		if (Math.sin(rotation) >= 0) {
			ySign = 1;
		} else {
			ySign = -1;
		}
		if ((rotation >= Math.PI / 4 && rotation < 3 * Math.PI / 4) || (rotation >= 5 * Math.PI / 4 && rotation < 7 * Math.PI / 4)) {
			double m = Math.cos(rotation) / Math.sin(rotation);
			double b = start.x - m * start.y;
			Point checkPoint = new Point((int) Math.floor(start.x), (int) Math.floor(start.y));
			if (xSign == -1) {
				checkPoint.x += 1;
			}
			if (ySign == -1) {
				checkPoint.y += 1;
			}
			double lastInterX = start.x;
			double lastInterY = start.y;
			while (true) {
				// Check if this point is "done"
				if (Game.map.blocked(checkPoint.x, checkPoint.y)) {
					return new Point.Double(lastInterX - xSign / 2.0 + 0.5, lastInterY - ySign / 2.0 + 0.4);
				}

				// Check point to right:
				checkPoint = new Point(checkPoint.x, checkPoint.y + ySign);
				double x0 = m * checkPoint.y + b;
				if ((xSign == 1 && x0 >= checkPoint.x && x0 <= checkPoint.x + 1) || (xSign == -1 && x0 <= checkPoint.x && x0 >= checkPoint.x - 1)) {
					lastInterX = x0;
					lastInterY = checkPoint.y;
					continue;
				}

				// Check point to above, (it MUST intersect, but, we need "lastInterX, lastInterY"
				checkPoint = new Point(checkPoint.x + xSign, checkPoint.y - ySign /* reverted last check*/);
				double y0 = (checkPoint.x - b) / m;
				//System.out.println((xSign == 1 && x0 >= checkPoint.x && x0 <= checkPoint.x + 1) || (xSign == -1 && x0 <= checkPoint.x && x0 >= checkPoint.x - 1));
				// Intersection check is pointless.
				lastInterX = checkPoint.x;
				lastInterY = y0;
			}
		} else {

			double m = Math.sin(rotation) / Math.cos(rotation);
			double b = start.y - m * start.x;
			Point checkPoint = new Point((int) Math.floor(start.x), (int) Math.floor(start.y));
			if (xSign == -1) {
				checkPoint.x += 1;
			}
			if (ySign == -1) {
				checkPoint.y += 1;
			}
			double lastInterX = start.x;
			double lastInterY = start.y;
			while (true) {
				// Check if this point is "done"
				if (Game.map.blocked(checkPoint.x, checkPoint.y)) {
					return new Point.Double(lastInterX - xSign / 2.0 + 0.5, lastInterY - ySign / 2.0 + 0.4);
				}

				// Check point to right:
				checkPoint = new Point(checkPoint.x + xSign, checkPoint.y);
				double y0 = m * checkPoint.x + b;
				if ((ySign == 1 && y0 >= checkPoint.y && y0 <= checkPoint.y + 1) || (ySign == -1 && y0 <= checkPoint.y && y0 >= checkPoint.y - 1)) {
					lastInterX = checkPoint.x;
					lastInterY = y0;
					continue;
				}

				// Check point to above, (it MUST intersect, but, we need "lastInterX, lastInterY"
				checkPoint = new Point(checkPoint.x - xSign /* reverted last check*/, checkPoint.y + ySign);
				double x0 = (checkPoint.y - b) / m;
				//System.out.println((xSign == 1 && x0 >= checkPoint.x && x0 <= checkPoint.x + 1) || (xSign == -1 && x0 <= checkPoint.x && x0 >= checkPoint.x - 1));
				// Intersection check is pointless.
				lastInterX = x0;
				lastInterY = checkPoint.y;
			}
		}
	}
}
