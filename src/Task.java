// Author: Aidan Fisher

import java.io.IOException;
import java.util.ArrayList;

public class Task {
	// Unit orders:
	public static void moveUnit(Unit unit, double centerX, double centerY, double newX, double newY, boolean stacked) {
		try {
			Client.out.writeUTF("MoveUnit");
			Client.out.writeShort(unit.id); // The id is used as it is unique
			Client.out.writeDouble(centerX);
			Client.out.writeDouble(centerY);
			Client.out.writeDouble(newX);
			Client.out.writeDouble(newY);
			Client.out.writeBoolean(stacked);
			Client.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setTarget(Unit unit, Entity target, boolean stacked) {
		try {
			Client.out.writeUTF("SetTarget");
			Client.out.writeShort(unit.id); // The id is used as it is unique
			Client.out.writeShort(target.id);
			Client.out.writeBoolean(stacked);
			Client.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addOrder(Unit unit, byte order, double atX, double atY, boolean stacked) {
		try {
			Client.out.writeUTF("AddOrder");
			Client.out.writeShort(unit.id); // The id is used as it is unique
			Client.out.writeByte(order);
			Client.out.writeDouble(atX);
			Client.out.writeDouble(atY);
			Client.out.writeBoolean(stacked);
			Client.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Currently this refers to uber. [Will need 'order' like addOrder has]
	public static void addOrder(Unit unit, boolean stacked) {
		try {
			Client.out.writeUTF("Uber");
			Client.out.writeShort(unit.id); // The id is used as it is unique
			Client.out.writeBoolean(stacked);
			Client.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
