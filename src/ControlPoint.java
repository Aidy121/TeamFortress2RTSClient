// Author: Aidan Fisher

import java.io.Serializable;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ControlPoint extends Entity implements Serializable {
	private static final long serialVersionUID = -6232864999078160124L;
	public static final int CAP_PROGRESS_REQUIRED = 10000;

	public static BufferedImage[] controlPoints = new BufferedImage[3];

	public int whoCapping = -1;
	public int capProgress = 0;

	public void render(Graphics2D g) {
		int image = team;
		if (image == -1) {
			image = 2;
		}
		Point p = Game.game.getScreenPos(this, 0, 0);
		Dimension dim = Game.game.getScreenSize(this);
		g.drawImage(controlPoints[image], p.x - dim.width / 2, p.y - dim.height / 2, dim.width, dim.height, null);
	}
}
