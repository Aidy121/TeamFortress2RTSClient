// Author: Aidan Fisher

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Kit extends Entity implements Serializable {
	private static final long serialVersionUID = 8108685360484118074L;

	public static BufferedImage[] kitImages = new BufferedImage[6];

	public int type; // 0, 1, 2, 3, 4, 5. Can also do stuff like resupply lockers in the future.

	public int respawnTimer = -1; // Currently spawned == -1

	public void render(Graphics2D g) {
		if (respawnTimer == -1) {
			Point p = Game.game.getScreenPos(this, 0, 0);
			Dimension dim = Game.game.getScreenSize(this);
			g.drawImage(kitImages[type], p.x - dim.width / 2, p.y - dim.height / 2, dim.width, dim.height, null);
		}
	}
}
