// Author: Aidan Fisher

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Unit extends Entity implements Serializable {

	private static final long serialVersionUID = -9046331796902423546L;

	public static BufferedImage[][] image = new BufferedImage[9][2];
	public static BufferedImage[][] uberImage = new BufferedImage[9][2];
	public static String[] classNames = { "Scout", "Soldier", "Pyro", "Demoman", "Heavy", "Engineer", "Medic", "Sniper", "Spy" }; // Corresponds to the classes that spawn

	public double speed;
	public int maxHealth;
	public double health;

	public int metal; // Only used for engineer (and medic)
	public int uber;
	public int fireTicksLeft;

	public short classNumber;

	public Weapon weapon;

	public ArrayList<PathfindingNode> currentPath = new ArrayList<PathfindingNode>(); // Generally just 1 node. Last node is goal location. First node is location going to right now.

	// Current path forces the ai to walk in a relatively straight line to its destination. It will not look around while travelling, but will do checks at corners & when it finishes. 
	// It takes priority over preferredLocation, and the last node is set to the prefferedLocation. While travelling, the bot will engage with other enemies, but unlike preferred location,
	// will give a *much* larger priority to moving towards its final destination. Preferred Location almost doesn't matter when the unit is engaged.

	public Unit() {

	}

	public void render(Graphics2D g) {
		Point p = Game.game.getScreenPos(this, 0, 0);
		Dimension dim = Game.game.getScreenSize(this);

		if (!onGround) {
			g.setColor(new Color(128, 128, 128, 64));
			g.fillOval((int) (p.x - (dim.width / 2) * ((z - Game.map.getMinZ(p)) / 10.0 + 1)), (int) (p.y - (dim.height / 2) * ((z - Game.map.getMinZ(p)) / 10.0 + 1)),
					(int) ((dim.width) * ((z - Game.map.getMinZ(p)) / 10.0 + 1)), (int) ((dim.height) * ((z - Game.map.getMinZ(p)) / 10.0 + 1)));
		}

		if (Component.selected.contains(this.id)) {
			g.setColor(new Color(200, 200, 200, 188));
			g.fillOval(p.x - (int) (dim.width * 0.7), p.y - (int) (dim.height * 0.7), (int) (dim.width * 1.4), (int) (dim.height * 1.4));
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

		if (uber != -1) {
			if (this.classNumber == 3 || this.classNumber == 6 || this.classNumber == 8) {
				g.drawImage(uberImage[classNumber - 1][team], p.x - (int) (dim.width * 1.6), p.y - (int) (dim.height * 1.6), (int) (dim.width * 3.2), (int) (dim.height * 3.2), null);
			} else {
				g.drawImage(uberImage[classNumber - 1][team], p.x - dim.width, p.y - dim.height, dim.width * 2, dim.height * 2, null);
			}
		} else {
			if (this.classNumber == 3 || this.classNumber == 6 || this.classNumber == 8) {
				g.drawImage(image[classNumber - 1][team], p.x - (int) (dim.width * 1.6), p.y - (int) (dim.height * 1.6), (int) (dim.width * 3.2), (int) (dim.height * 3.2), null);
			} else {
				g.drawImage(image[classNumber - 1][team], p.x - dim.width, p.y - dim.height, dim.width * 2, dim.height * 2, null);
			}
		}

		if (fireTicksLeft > 0) {
			g.drawImage(Bullet.bulletImages.get("OnFire.png"), p.x - dim.width, p.y - dim.height, dim.width * 2, dim.height * 2, null);
		}

		g.setTransform(current);

		if (weapon != null) {
			weapon.render(g);
		}

		if (currentPath.size() > 0) {
			g.setStroke(new BasicStroke(0.3f * (float) Game.unitSize));
			g.setColor(new Color(64, 64, 64, 120));
			Point screenPos = Game.game.getScreenPos(new Point.Double(currentPath.get(0).x, currentPath.get(0).y));
			g.drawOval((int) (screenPos.x - Game.unitSize), (int) (screenPos.y - Game.unitSize), (int) (Game.unitSize * 2), (int) (Game.unitSize * 2));
			g.fillOval((int) (screenPos.x - Game.unitSize), (int) (screenPos.y - Game.unitSize), (int) (Game.unitSize * 2), (int) (Game.unitSize * 2));
		}

		drawHealthBar(new Point(p.x, p.y - dim.height), new Dimension(dim.width, dim.height / 8), g);
	}

	public void drawHealthBar(Point p, Dimension dim, Graphics2D g) {
		if (dieTime < 0) {
			if (team == Entity.BLUE) {
				g.setColor(new Color(64, 120, 200));
			} else if (team == Entity.RED) {
				g.setColor(new Color(175, 100, 100));
			}
			g.fillRect(p.x - dim.width / 2, p.y, dim.width, dim.height);

			if (team == Entity.BLUE) {
				g.setColor(new Color(128, 200, 255));
			} else if (team == Entity.RED) {
				g.setColor(new Color(255, 150, 150));
			}
			g.fillRect(p.x - dim.width / 2, p.y, (int) (dim.width * Math.min(health, maxHealth) / maxHealth), dim.height);

			if (health > maxHealth) {
				if (team == Entity.BLUE) {
					g.setColor(new Color(180, 250, 255));
				} else if (team == Entity.RED) {
					g.setColor(new Color(255, 220, 220));
				}
				g.fillRect(p.x + dim.width / 2, p.y, (int) (dim.width * (health - maxHealth) / maxHealth), dim.height);
			}

			if (classNumber == 6) {
				if (team == Entity.BLUE) {
					g.setColor(new Color(70, 70, 70));
				} else if (team == Entity.RED) {
					g.setColor(new Color(70, 70, 70));
				}
				g.fillRect(p.x - dim.width / 2, p.y + dim.height, dim.width, dim.height);

				if (team == Entity.BLUE) {
					g.setColor(new Color(30, 90, 130));
				} else if (team == Entity.RED) {
					g.setColor(new Color(130, 70, 70));
				}
				g.fillRect(p.x - dim.width / 2, p.y + dim.height, (int) (dim.width * metal / 200), dim.height);

				g.setColor(new Color(0, 0, 0));
				g.setFont(new Font("Verdana", Font.BOLD, (int) (dim.height * 0.8)));
				g.drawString("Metal", (int) (p.x - dim.width / 6), (int) (p.y + dim.height * 1.82));
			}
			if (classNumber == 7) {
				if (team == Entity.BLUE) {
					g.setColor(new Color(104, 104, 104));
				} else if (team == Entity.RED) {
					g.setColor(new Color(104, 104, 104));
				}
				g.fillRect(p.x - dim.width / 2, p.y + dim.height, dim.width, dim.height);

				if (metal == 8000 || ((HealingBeam) weapon).ubering) {
					if (team == Entity.BLUE) {
						g.setColor(new Color(140, 140, 255));
					} else if (team == Entity.RED) {
						g.setColor(new Color(255, 180, 180));
					}
				} else {
					if (team == Entity.BLUE) {
						g.setColor(new Color(20, 80, 200));
					} else if (team == Entity.RED) {
						g.setColor(new Color(200, 50, 50));
					}
				}
				g.fillRect(p.x - dim.width / 2, p.y + dim.height, (int) (dim.width * metal / 8000), dim.height);

				g.setColor(new Color(0, 0, 0));
				g.setFont(new Font("Verdana", Font.BOLD, (int) (dim.height * 0.8)));
				g.drawString("Ubercharge", (int) (p.x - dim.width / 3.2), (int) (p.y + dim.height * 1.82));
			}
		}
	}

	// Renders at pixel location, and returns where next pixel location should be.
	public int renderHud(int pixelY, Graphics2D g) {
		int height;
		if (classNumber == 6 || classNumber == 7) {
			height = 50; // Default.
		} else {
			height = 30; // Default.
		}
		g.setColor(new Color(32, 32, 32));
		g.setStroke(new BasicStroke(2));
		g.drawRect(5, pixelY - height, 255, height);
		g.setColor(new Color(182, 182, 182));
		g.fillRect(5, pixelY - height, 255, height);
		drawHealthBar(new Point(90, pixelY - height + 5), new Dimension(160, 20), g);
		g.setFont(new Font("Verdana", Font.PLAIN, 16));
		g.setColor(new Color(0, 0, 0));
		g.drawString(classNames[classNumber - 1] + " (" + ((int) (health * 100 / maxHealth) + "%)"), 10, pixelY - height + 20);
		return pixelY - height;
	}
}
