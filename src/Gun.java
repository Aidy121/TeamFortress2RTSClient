// Author: Aidan Fisher

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public class Gun extends Weapon implements Serializable {
	private static final long serialVersionUID = -5801926527271533028L;

	public final static int SNIPER_CHARGE_TIME = 20;
	public final static int SNIPER_PAUSE_TIME = 160;

	// Guns shoot projectiles; generally fast-> and *always* have tracers behind them [Projectiles can be "hitscan" weapons, "splash" damage: rockets, pills] 
	String gunName;

	boolean tryingToFire = false;
	double bulletSpeed;
	int fireRate; // ticks to fire
	int coolDown; // ticks until can fire again

	double maxMissAngle; // Will be between 0 and this angle.
	int numBullets;
	double damage;
	int distance;

	double forwardOffset;

	int maxDistance;
	double splashDistance;

	public void render(Graphics2D g) {
		Unit parentUnit = (Unit) Game.game.getEntity(parent);
		if (gunName.equals("Sniper Rifle") && parentUnit != null) {
			// Draw laser:
			Point p = Game.game.getScreenPos(parentUnit, 0, 0);
			Point n = Game.game.getScreenPos(PathfindingNode.lineTo(new Point.Double(parentUnit.x, parentUnit.y), parentUnit.rotation));
			if (coolDown < SNIPER_CHARGE_TIME) {
				if (parentUnit.team == Entity.BLUE) {
					g.setColor(new Color(30, 70, 180, 255));
				} else {
					g.setColor(new Color(170, 60, 70, 255));
				}
				g.setStroke(new BasicStroke((float) Game.unitSize * 0.15f));
			} else if (coolDown > SNIPER_PAUSE_TIME) {
				if (parentUnit.team == Entity.BLUE) {
					g.setColor(new Color(30, 70, 180, 15));
				} else {
					g.setColor(new Color(170, 60, 70, 15));
				}
				g.setStroke(new BasicStroke((float) Game.unitSize * 0.25f));
			} else {
				if (parentUnit.team == Entity.BLUE) {
					g.setColor(new Color(30, 70, 180, 75));
				} else {
					g.setColor(new Color(170, 60, 70, 75));
				}
				//g.setStroke(new BasicStroke((float) Game.unitSize * 0.05f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, (float) Game.unitSize, new float[] { (float) Game.unitSize }, 0.0f));
				g.setStroke(new BasicStroke((float) Game.unitSize * 0.1f));
			}
			g.drawLine(p.x, p.y, n.x, n.y);
		}
	}
}
