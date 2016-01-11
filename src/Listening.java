// Author: Aidan Fisher

import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Listening implements MouseListener, MouseWheelListener, KeyListener, WindowListener, MouseMotionListener {

	public static Point pressLocation = new Point(-1, -1);

	public static boolean mouse1Down = false;
	public static boolean up = false;
	public static boolean down = false;
	public static boolean left = false;
	public static boolean right = false;
	public static boolean shiftDown = false;

	public static boolean pathFinding = false;
	public static Point releaseLocation = null;

	private void zoomIn(double zoomRate, MouseEvent e) {
		Game.sX += e.getX() / Game.unitSize / (1.0 + zoomRate);
		Game.sY += e.getY() / Game.unitSize / (1.0 + zoomRate);
		Game.unitSize = Game.unitSize * (1.0 + (1.0 / zoomRate));
	}

	private void zoomOut(double zoomRate, MouseEvent e) {
		Game.sX -= e.getX() / Game.unitSize * (1.0 / zoomRate);
		Game.sY -= e.getY() / Game.unitSize * (1.0 / zoomRate);
		Game.unitSize = Game.unitSize / (1.0 + 1.0 / zoomRate);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftDown = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			// Set to Build a dispenser:
			ArrayList<Unit> selectedUnits = Game.game.getEntities(Component.selected);
			for (Unit unit : selectedUnits) {
				if (unit.classNumber == 6) { // Must be engineer.
					Component.nextOrder = PathfindingNode.DISPENSER_BUILD;
					break;
				}
			}

		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			// Set to Build a sentry:
			ArrayList<Unit> selectedUnits = Game.game.getEntities(Component.selected);
			for (Unit unit : selectedUnits) {
				if (unit.classNumber == 6) { // Must be engineer.
					Component.nextOrder = PathfindingNode.SENTRY_BUILD;
					break;
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			Component.teamControlling = Entity.RED;
		} else if (e.getKeyCode() == KeyEvent.VK_B) {
			Component.teamControlling = Entity.BLUE;
		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftDown = false;
		} else if (e.getKeyCode() == KeyEvent.VK_U) { // Ubercharge.
			ArrayList<Unit> selectedUnits = Game.game.getEntities(Component.selected);
			for (Unit unit : selectedUnits) {
				if (unit.classNumber == 7) { // Must be medic.
					Task.addOrder(unit, false);
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) { // doesn't work, use pressed.

	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (Game.game != null) {
			if (e.getWheelRotation() > 0) {
				zoomOut(5, e);
			} else if (e.getWheelRotation() < 0) {
				zoomIn(5, e);
			}
		}
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		// Must be "default" for it to consider the mouse to be down. (For selecting)
		if (e.getButton() == MouseEvent.BUTTON1 && Component.nextOrder == PathfindingNode.PATH_FINDING) {
			mouse1Down = true;
			pressLocation = e.getPoint();
		}
	}

	public static Point.Double getAveragePos(ArrayList<Unit> units) {
		Point.Double currentPoint = new Point.Double(0, 0);
		for (Unit unit : units) {
			currentPoint.x += unit.x;
			currentPoint.y += unit.y;
		}
		currentPoint.x /= units.size();
		currentPoint.y /= units.size();
		return currentPoint;
	}

	public void mouseReleased(MouseEvent e) {
		if (Game.game != null) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				releaseLocation = e.getPoint();
				mouse1Down = false;
			}

			if (e.getButton() == MouseEvent.BUTTON3) {
				// cancel.
				if (Component.nextOrder != PathfindingNode.PATH_FINDING) {
					Component.nextOrder = PathfindingNode.PATH_FINDING;
				} else {
					pathFinding = true;
					releaseLocation = e.getPoint();
				}
			}
		}
	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		// Send this information to server:
		try {
			if (Client.out != null) {
				Client.out.writeUTF("Exiting");
				Client.out.flush();
			}
		} catch (Exception el) {
		}
	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowDeactivated(WindowEvent e) {

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
	}
}
