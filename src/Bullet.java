// Author: Aidan Fisher

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;

public class Bullet extends Entity implements Serializable {

	private static final long serialVersionUID = 3185021257460712779L;

	public static HashMap<String, BufferedImage> bulletImages = new HashMap<String, BufferedImage>();

	double speed;
	double distance;
	int maxDistance;
	double fullDamage;
	double splashDistance;
	boolean particle;

	String bulletImage;

	public Bullet() {

	}

	public double interpDistance() {
		return distance - speed * (Component.timePerTick - Component.timeSinceLastTick);
	}

	public void render(Graphics2D g) {
		if (dieTime != 0) {
			//Interpolates x & y & z. Rotation maintains, however.
			Point p = Game.game.getScreenPos(this, 0, 0);
			Dimension dim = Game.game.getScreenSize(this);

			AffineTransform current = g.getTransform();
			g.rotate(rotation, p.x, p.y); // x & y being the center.

			double distanceTravelled = interpDistance();

			if (this.bulletImage.equals("Fire.png")) { // Who cares about cutting off fire.
				if (distanceTravelled / maxDistance < 0.335) {
					g.drawImage(bulletImages.get("BeforeFire.png"), p.x - dim.width, p.y - dim.height / 2, dim.width, dim.height, null);
				} else if (distanceTravelled / maxDistance < 0.355) {
					g.drawImage(bulletImages.get("InitialIgnition.png"), p.x - dim.width, p.y - dim.height / 2, dim.width, dim.height, null);
				} else if (distanceTravelled / maxDistance < 0.5) {
					g.drawImage(bulletImages.get("TransitionFire.png"), p.x - dim.width, p.y - dim.height / 2, dim.width, dim.height, null);
				} else {
					g.drawImage(bulletImages.get(bulletImage), p.x - dim.width, p.y - dim.height / 2, dim.width, dim.height, null);
				}
			} else if (distanceTravelled < this.width) {
				// Cut off the bullet.
				int imageWidth = bulletImages.get(bulletImage).getWidth();
				g.drawImage(bulletImages.get(bulletImage), p.x - (int) (distanceTravelled * Game.unitSize), p.y - dim.height / 2, p.x, p.y + dim.height / 2, (int) ((this.width - distanceTravelled)
						* imageWidth / this.width), 0, imageWidth, bulletImages.get(bulletImage).getHeight(), null);
			} else {
				g.drawImage(bulletImages.get(bulletImage), p.x - dim.width, p.y - dim.height / 2, dim.width, dim.height, null);
			}
			g.setTransform(current);
		}
	}
}
