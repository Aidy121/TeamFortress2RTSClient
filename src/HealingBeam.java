// Author: Aidan Fisher

import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.io.Serializable;

public class HealingBeam extends Weapon implements Serializable {

	private static final long serialVersionUID = 5281447600197225348L;

	public static float MAX_HEALING_DISTANCE = 22.5f;

	public short unitHealing = Short.MAX_VALUE;

	public boolean ubering;

	public HealingBeam() {

	}

	public Point.Double getPointOfIntersection(double dirPointing, Point a, Point b) {
		//double dirBetween = Math.atan2(b.y - a.y, b.x - a.x);
		//double length = a.distance(b) / (2 * Math.cos(dirBetween - dirPointing));
		double length = 7.5 * Game.unitSize;
		return new Point.Double(a.x + length * Math.cos(dirPointing), a.y + length * Math.sin(dirPointing));
	}

	public void render(Graphics2D g) {
		if (unitHealing != Short.MAX_VALUE) {
			// Not just the gun.
			Unit parentUnit = (Unit) Game.game.getEntity(parent);
			Unit unitHealingUnit = (Unit) Game.game.getEntity(unitHealing);
			if (parentUnit != null && unitHealingUnit != null) {
				Point a = Game.game.getScreenPos(parentUnit, parentUnit.width * Math.cos(parentUnit.rotation + Math.PI / 14) * 0.79, parentUnit.width * Math.sin(parentUnit.rotation + Math.PI / 14)
						* 0.79); // width / length are interchangable
				Point b = Game.game.getScreenPos(unitHealingUnit, 0, 0);
				Point.Double c = getPointOfIntersection(parentUnit.rotation, a, b);

				QuadCurve2D q = new QuadCurve2D.Float();
				q.setCurve(a.x, a.y, c.x, c.y, b.x, b.y);
				if (parentUnit.team == Entity.BLUE) {
					g.setColor(new Color(60, 120, 200, 150));
				} else {
					g.setColor(new Color(250, 110, 130, 150));
				}
				g.setStroke(new BasicStroke((float) Game.unitSize / 2.1f));
				g.draw(q);
			}
		}
	}
}
