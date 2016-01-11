// Author: Aidan Fisher

import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import java.io.Serializable;

public class Entity implements Serializable {

	private static final long serialVersionUID = 7979875947947389831L;
	public final static int BLUE = 0;
	public final static int RED = 1;

	public short id;

	// This location is the center bottom of the player. Players are cylinders.
	public double x;
	public double y;
	public double z;

	public double interpX;
	public double interpY;
	public double interpZ;

	public double rotation; // xy rot
	public double upDownRotation; // up/down rot: From -Math.PI/2 to +Math.PI/2. 0 is horizontal.
	public double width;
	public double length;
	public double height;

	public boolean onGround = false;

	public int team;

	public int dieTime = -1;

	public boolean effectedByGravity = true;

	public Entity() {

	}

	// It should be noted that the game only handles 2 teams right now.
	public int getEnemy() {
		if (this.team == 0) {
			return 1;
		} else {
			return 0;
		}
	}

	public boolean withinSelectDistance(Point.Double other) {
		return Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y)) < this.width * 0.7;
	}

	// In x / y coordinate plane
	public double distancexy(Entity other) {
		return Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y));
	}

	// Uses center of mass:
	public double getDistance(Entity other) {
		return Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y) + (this.z + this.height / 2 - other.z - other.height / 2)
				* (this.z + this.height / 2 - other.z - other.height / 2));
	}

	public void render(Graphics2D g) {
	}
}
