// Author: Aidan Fisher

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Building extends Entity implements Serializable {
	private static final long serialVersionUID = -1082224572906664999L;

	public static final float DISPENSER_HEAL_DIST = 7f;
	public static final float REPAIR_DIST = 6f;
	public static final float MAX_SENTRY_DIST = 50f; // Fairly far.

	public static BufferedImage[][][] bImage = new BufferedImage[2][3][2];
	public static BufferedImage sentryStand;
	public static BufferedImage sentryL3Stand;

	public double sentryRotation; // Only used for sentries.

	public byte type;
	public int level;

	public int upgradeMetal;

	double health;
	int maxHealth;

	public void render(Graphics2D g) {
		Point p = Game.game.getScreenPos(this, 0, 0);
		Dimension dim = Game.game.getScreenSize(this);

		if (this.type == PathfindingNode.DISPENSER_BUILD) {
			if (team == Entity.BLUE) {
				g.setColor(new Color(75, 75, 185, 60));
			} else if (team == Entity.RED) {
				g.setColor(new Color(185, 75, 75, 60));
			}
			g.setStroke(new BasicStroke((float) Game.unitSize / 6));
			g.drawOval(p.x - (int) (dim.width * DISPENSER_HEAL_DIST / this.width), p.y - (int) (dim.height * DISPENSER_HEAL_DIST / this.width),
					(int) (dim.width * DISPENSER_HEAL_DIST * 2 / this.width), (int) (dim.height * DISPENSER_HEAL_DIST * 2 / this.width));
			g.fillOval(p.x - (int) (dim.width * DISPENSER_HEAL_DIST / this.width), p.y - (int) (dim.height * DISPENSER_HEAL_DIST / this.width),
					(int) (dim.width * DISPENSER_HEAL_DIST * 2 / this.width), (int) (dim.height * DISPENSER_HEAL_DIST * 2 / this.width));
		}

		if (Component.enemyHighlighted == this.id) {
			if (team == Entity.BLUE) {
				g.setColor(new Color(75, 75, 185, 188));
			} else if (team == Entity.RED) {
				g.setColor(new Color(185, 75, 75, 188));
			}
			g.fillOval(p.x - (int) (dim.width * 0.7), p.y - (int) (dim.height * 0.7), (int) (dim.width * 1.4), (int) (dim.height * 1.4));
		}

		AffineTransform current = g.getTransform();
		g.rotate(rotation, p.x, p.y); // x & y being the center.
		if (team == Entity.BLUE) {
			g.setColor(new Color(0, 0, 175, 150));
		} else if (team == Entity.RED) {
			g.setColor(new Color(175, 0, 0, 150));
		}
		if (this.type == PathfindingNode.SENTRY_BUILD) {
			if (this.level == 2) {
				g.drawImage(sentryL3Stand, p.x - dim.width, p.y - dim.height, dim.width * 2, dim.height * 2, null);
			} else {
				g.drawImage(sentryStand, p.x - dim.width, p.y - dim.height, dim.width * 2, dim.height * 2, null);
			}
		}

		// Dispensers should show auras.
		if (this.type == PathfindingNode.SENTRY_BUILD) {
			g.rotate(sentryRotation, p.x, p.y);
			g.drawImage(bImage[this.type - 1][level][team], p.x - dim.width, p.y - dim.height, dim.width * 2, dim.height * 2, null);
		} else {
			g.drawImage(bImage[this.type - 1][level][team], p.x - dim.width, p.y - dim.height, dim.width * 2, dim.height * 2, null);
		}

		g.setTransform(current);

		drawHealthBar(p, dim, g);
	}

	public void drawHealthBar(Point p, Dimension dim, Graphics2D g) {
		if (dieTime < 0) {
			if (team == Entity.BLUE) {
				g.setColor(new Color(64, 120, 200));
			} else if (team == Entity.RED) {
				g.setColor(new Color(175, 100, 100));
			}
			g.fillRect(p.x - dim.width / 2, p.y - dim.height, dim.width, dim.height / 8);

			if (team == Entity.BLUE) {
				g.setColor(new Color(128, 200, 255));
			} else if (team == Entity.RED) {
				g.setColor(new Color(255, 150, 150));
			}
			g.fillRect(p.x - dim.width / 2, p.y - dim.height, (int) (dim.width * Math.min(health, maxHealth) / maxHealth), dim.height / 8);
		}
		if (level != 2) {
			if (team == Entity.BLUE) {
				g.setColor(new Color(104, 104, 104));
			} else if (team == Entity.RED) {
				g.setColor(new Color(104, 104, 104));
			}
			g.fillRect(p.x - dim.width / 2, p.y - dim.height + dim.height / 8, dim.width, (int) (Game.unitSize / 4));

			if (team == Entity.BLUE) {
				g.setColor(new Color(20, 80, 200));
			} else if (team == Entity.RED) {
				g.setColor(new Color(200, 50, 50));
			}
			g.fillRect(p.x - dim.width / 2, p.y - dim.height + dim.height / 8, (int) (dim.width * upgradeMetal / 200), (int) (Game.unitSize / 4));
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Verdana", Font.PLAIN, (int) (Game.unitSize / 6)));
			g.drawString("Upgrade Progress", (int) (p.x - Game.unitSize / 1.3), (int) (p.y - dim.height + dim.height / 8 + (int) (Game.unitSize / 5.5)));
		}
	}

}
