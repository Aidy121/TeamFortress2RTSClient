// Author: Aidan Fisher

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Explosion extends Entity implements Serializable {
	private static final long serialVersionUID = 3619998093427673624L;

	public static BufferedImage explosion;

	public static int ticksPerFrame = 3;

	public short stuckTo;

	public void render(Graphics2D g) {
		Point p;
		if (stuckTo != Short.MAX_VALUE) {
			Entity entity = Game.game.getEntity(stuckTo);
			if (entity != null) {
				p = Game.game.getScreenPos(new Point.Double(entity.x + this.x, entity.y + this.y)); // Doesn't have render timing consideration yet!!
			} else {
				return;
			}
		} else {
			p = Game.game.getScreenPos(this, 0, 0);
		}
		g.drawImage(explosion, (int) (p.x - width * Game.unitSize), (int) (p.y - length * Game.unitSize), (int) (p.x + width * Game.unitSize), (int) (p.y + length * Game.unitSize),
				(dieTime / ticksPerFrame) * 128, 0, (dieTime / ticksPerFrame) * 128 + 128, 128, null);
	}
}
