// Author: Aidan Fisher

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class Game implements Serializable {

	private static final long serialVersionUID = -6842270011743467561L;

	public static Game game = null;

	public static double unitSize = 32; // pixels for 1 unit.
	public static double sX = 0;
	public static double sY = 0;

	public static Map map;

	public int[] ticksLeftToWin;

	public int currentTick = 0;
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<ArrayList<Entity>> units = new ArrayList<ArrayList<Entity>>();

	public Point getScreenPos(Entity e, double xOffset, double yOffset) {
		// Includes extrapolation:
		double rX = e.x - e.interpX * (Component.timePerTick - Component.timeSinceLastTick) / Component.timePerTick + xOffset;
		double rY = e.y - e.interpY * (Component.timePerTick - Component.timeSinceLastTick) / Component.timePerTick + yOffset;
		double rZ = e.z - e.interpZ * (Component.timePerTick - Component.timeSinceLastTick) / Component.timePerTick;
		return new Point((int) ((rX - sX) * unitSize), (int) ((rY - sY) * unitSize));
	}

	// To be used with "selected"
	public ArrayList<Unit> getEntities(ArrayList<Short> unitIDs) {
		ArrayList<Unit> entities = new ArrayList<Unit>(unitIDs.size());
		for (int i = 0; i < unitIDs.size(); i++) {
			Entity entity = getEntity(unitIDs.get(i));
			if (entity != null) {
				entities.add((Unit) entity);
			} else {
				// Takes the liberty of removing the id:
				unitIDs.remove(i);
				i--;
			}
		}
		return entities;
	}

	public Entity getEntity(short id) {
		for (Entity entity : entities) {
			if (entity.id == id) {
				return entity;
			}
		}
		return null;
	}

	// Not ready for 3d.
	public Point getScreenPos(Point.Double e) {
		return new Point((int) ((e.x - sX) * unitSize), (int) ((e.y - sY) * unitSize));
	}

	public Dimension getScreenSize(Entity e) {
		return new Dimension((int) (e.width * unitSize), (int) (e.length * unitSize));
	}

	public Point.Double getGamePos(Point p) {
		return new Point.Double((p.x / unitSize) + sX, (p.y / unitSize) + sY);
	}

	public void render(Graphics2D g) {
		g.setFont(new Font("Verdana", Font.PLAIN, 22));
		g.setStroke(new BasicStroke(2));
		g.setColor(new Color(40, 40, 40));

		g.setColor(new Color(64, 160, 255));
		g.fillRoundRect(Component.dimension.width / 2 - 85, 4, 80, 24, 5, 5);
		g.setColor(new Color(255, 80, 80));
		g.fillRoundRect(Component.dimension.width / 2 + 5, 4, 80, 24, 5, 5);
		g.setColor(new Color(255, 255, 255));
		String blueLine;
		if (ticksLeftToWin[0] < 0) {
			blueLine = "Winner!";
		} else {
			blueLine = String.format("%01d:%02d", ticksLeftToWin[0] / 6000, Math.abs((ticksLeftToWin[0] / 100) % 60));
		}
		g.drawString(blueLine, Component.dimension.width / 2 - 80, 25);
		String redLine;
		if (ticksLeftToWin[1] < 0) {
			redLine = "Red Wins!";
		} else {
			redLine = String.format("%01d:%02d", ticksLeftToWin[1] / 6000, Math.abs((ticksLeftToWin[1] / 100) % 60));
		}
		g.drawString(redLine, Component.dimension.width / 2 + 10, 25);
	}
}
