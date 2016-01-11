// Author: Aidan Fisher

import java.applet.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Component extends Applet implements Runnable {

	public static double ticksPerSecond = 100;
	public static double timePerTick = 1 / ticksPerSecond;

	public static Dimension dimension = new Dimension(1200, 800);
	public static float hudSize = 1;

	private static final long serialVersionUID = 1L;

	public static double zoom = 1;

	public static String name = "Team Fortress 2 RTS";

	public static boolean disperseQueue = false;

	public static boolean isRunning = false;
	public int frames = 0;

	public static BufferedImage[] cursors = new BufferedImage[4];

	private Image screen;

	public static byte nextOrder = PathfindingNode.PATH_FINDING;

	public static long lastTick = 0;
	public static double timeSinceLastTick = 0; // Percentage of time since last tick to next tick.

	public static int teamControlling = Entity.BLUE;
	public static ArrayList<Short> selected = new ArrayList<Short>();
	public static short enemyHighlighted = Short.MAX_VALUE;

	public Component() {
		setPreferredSize(dimension);
		addKeyListener(new Listening());
		addMouseMotionListener(new Listening());
		addMouseListener(new Listening());
		addMouseWheelListener(new Listening());
		try {
			Unit.image[0][Entity.BLUE] = ImageIO.read(new File("res/classes/scoutBlue.png"));
			Unit.image[0][Entity.RED] = ImageIO.read(new File("res/classes/scoutRed.png"));
			Unit.image[1][Entity.BLUE] = ImageIO.read(new File("res/classes/soldierBlue.png"));
			Unit.image[1][Entity.RED] = ImageIO.read(new File("res/classes/soldierRed.png"));
			Unit.image[2][Entity.BLUE] = ImageIO.read(new File("res/classes/PyroBlue.png"));
			Unit.image[2][Entity.RED] = ImageIO.read(new File("res/classes/PyroRed.png"));
			Unit.image[3][Entity.BLUE] = ImageIO.read(new File("res/classes/DemomanBlue.png"));
			Unit.image[3][Entity.RED] = ImageIO.read(new File("res/classes/DemomanRed.png"));
			Unit.image[4][Entity.BLUE] = ImageIO.read(new File("res/classes/HeavyBlue.png"));
			Unit.image[4][Entity.RED] = ImageIO.read(new File("res/classes/HeavyRed.png"));
			Unit.image[5][Entity.BLUE] = ImageIO.read(new File("res/classes/EngineerBlue.png"));
			Unit.image[5][Entity.RED] = ImageIO.read(new File("res/classes/EngineerRed.png"));
			Unit.image[6][Entity.BLUE] = ImageIO.read(new File("res/classes/MedicBlue.png"));
			Unit.image[6][Entity.RED] = ImageIO.read(new File("res/classes/MedicRed.png"));
			Unit.image[7][Entity.BLUE] = ImageIO.read(new File("res/classes/SniperBlue.png"));
			Unit.image[7][Entity.RED] = ImageIO.read(new File("res/classes/SniperRed.png"));

			Unit.uberImage[0][Entity.BLUE] = ImageIO.read(new File("res/uberclasses/scoutBlue.png"));
			Unit.uberImage[0][Entity.RED] = ImageIO.read(new File("res/uberclasses/scoutRed.png"));
			Unit.uberImage[1][Entity.BLUE] = ImageIO.read(new File("res/uberclasses/soldierBlue.png"));
			Unit.uberImage[1][Entity.RED] = ImageIO.read(new File("res/uberclasses/soldierRed.png"));
			Unit.uberImage[2][Entity.BLUE] = ImageIO.read(new File("res/uberclasses/PyroBlue.png"));
			Unit.uberImage[2][Entity.RED] = ImageIO.read(new File("res/uberclasses/PyroRed.png"));
			Unit.uberImage[3][Entity.BLUE] = ImageIO.read(new File("res/uberclasses/DemomanBlue.png"));
			Unit.uberImage[3][Entity.RED] = ImageIO.read(new File("res/uberclasses/DemomanRed.png"));
			Unit.uberImage[4][Entity.BLUE] = ImageIO.read(new File("res/uberclasses/HeavyBlue.png"));
			Unit.uberImage[4][Entity.RED] = ImageIO.read(new File("res/uberclasses/HeavyRed.png"));
			Unit.uberImage[5][Entity.BLUE] = ImageIO.read(new File("res/uberclasses/EngineerBlue.png"));
			Unit.uberImage[5][Entity.RED] = ImageIO.read(new File("res/uberclasses/EngineerRed.png"));
			Unit.uberImage[6][Entity.BLUE] = ImageIO.read(new File("res/uberclasses/MedicBlue.png"));
			Unit.uberImage[6][Entity.RED] = ImageIO.read(new File("res/uberclasses/MedicRed.png"));
			Unit.uberImage[7][Entity.BLUE] = ImageIO.read(new File("res/uberclasses/SniperBlue.png"));
			Unit.uberImage[7][Entity.RED] = ImageIO.read(new File("res/uberclasses/SniperRed.png"));

			Building.bImage[0][0][Entity.BLUE] = ImageIO.read(new File("res/buildings/DispenserL1Blue.png"));
			Building.bImage[0][1][Entity.BLUE] = ImageIO.read(new File("res/buildings/DispenserL2Blue.png"));
			Building.bImage[0][2][Entity.BLUE] = ImageIO.read(new File("res/buildings/DispenserL3Blue.png"));
			Building.bImage[0][0][Entity.RED] = ImageIO.read(new File("res/buildings/DispenserL1Red.png"));
			Building.bImage[0][1][Entity.RED] = ImageIO.read(new File("res/buildings/DispenserL2Red.png"));
			Building.bImage[0][2][Entity.RED] = ImageIO.read(new File("res/buildings/DispenserL3Red.png"));

			Building.sentryStand = ImageIO.read(new File("res/buildings/SentryStand.png"));
			Building.sentryL3Stand = ImageIO.read(new File("res/buildings/SentryL3Stand.png"));

			Building.bImage[1][0][Entity.BLUE] = ImageIO.read(new File("res/buildings/SentryL1Blue.png"));
			Building.bImage[1][1][Entity.BLUE] = ImageIO.read(new File("res/buildings/SentryL2Blue.png"));
			Building.bImage[1][2][Entity.BLUE] = ImageIO.read(new File("res/buildings/SentryL3Blue.png"));
			Building.bImage[1][0][Entity.RED] = ImageIO.read(new File("res/buildings/SentryL1Red.png"));
			Building.bImage[1][1][Entity.RED] = ImageIO.read(new File("res/buildings/SentryL2Red.png"));
			Building.bImage[1][2][Entity.RED] = ImageIO.read(new File("res/buildings/SentryL3Red.png"));

			Explosion.explosion = ImageIO.read(new File("res/explosion.png"));

			Bullet.bulletImages.put("Rocket.png", ImageIO.read(new File("res/projectiles/Rocket.png")));
			Bullet.bulletImages.put("LightBullet.png", ImageIO.read(new File("res/projectiles/LightBullet.png")));
			Bullet.bulletImages.put("LightBulletSniper.png", ImageIO.read(new File("res/projectiles/LightBulletSniper.png")));
			Bullet.bulletImages.put("BlueGrenade.png", ImageIO.read(new File("res/projectiles/BlueGrenade.png")));
			Bullet.bulletImages.put("RedGrenade.png", ImageIO.read(new File("res/projectiles/RedGrenade.png")));

			// Flames are projectiles.
			Bullet.bulletImages.put("Fire.png", ImageIO.read(new File("res/flames/Fire.png")));
			Bullet.bulletImages.put("TransitionFire.png", ImageIO.read(new File("res/flames/TransitionFire.png")));
			Bullet.bulletImages.put("InitialIgnition.png", ImageIO.read(new File("res/flames/InitialIgnition.png")));
			Bullet.bulletImages.put("BeforeFire.png", ImageIO.read(new File("res/flames/BeforeFire.png")));

			Bullet.bulletImages.put("OnFire.png", ImageIO.read(new File("res/OnFire.png")));

			Kit.kitImages[0] = ImageIO.read(new File("res/kits/SmallHealthPack.png"));
			Kit.kitImages[1] = ImageIO.read(new File("res/kits/MediumHealthPack.png"));
			Kit.kitImages[2] = ImageIO.read(new File("res/kits/FullHealthPack.png"));
			Kit.kitImages[3] = ImageIO.read(new File("res/kits/SmallAmmoPack.png"));
			Kit.kitImages[4] = ImageIO.read(new File("res/kits/MediumAmmoPack.png"));
			Kit.kitImages[5] = ImageIO.read(new File("res/kits/LargeAmmoPack.png"));

			ControlPoint.controlPoints[Entity.BLUE] = ImageIO.read(new File("res/controlpoints/Blue.png"));
			ControlPoint.controlPoints[Entity.RED] = ImageIO.read(new File("res/controlpoints/Red.png"));
			ControlPoint.controlPoints[2] = ImageIO.read(new File("res/controlpoints/Neutral.png"));

			cursors[0] = ImageIO.read(new File("res/cursors/enemyUnitMouseOver.png"));
			cursors[1] = ImageIO.read(new File("res/cursors/NormalCursor.png"));
			cursors[2] = ImageIO.read(new File("res/cursors/PlacingCursor.png"));
			cursors[3] = ImageIO.read(new File("res/cursors/PlacingSentryCursor.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {

		try {
			Client.connect(JOptionPane.showInputDialog("Connect to IP Address: (Port 7788)"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		playerConnect();

		try {
			BufferedImage mapData = ImageIO.read(new File("res/MapData.png"));
			BufferedImage mapView = ImageIO.read(new File("res/MapView.png"));
			Game.map = new Map(mapData, mapView);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Starting game loop
		isRunning = true;

		new Thread(this).start();
	}

	public static void playerConnect() {
		String name = JOptionPane.showInputDialog("Enter your username: ");
		if (name == null) {
			name = "N/A";
		}
		try {
			Client.out.writeUTF("newObject");
			Client.out.writeObject(name);
			Client.out.flush();
		} catch (Exception e) {
			System.out.println("Era PC: " + e);
		}
		String input = (String) JOptionPane.showInputDialog(null, "Choose Team:", "Team", JOptionPane.QUESTION_MESSAGE, null, new String[] { "BLU", "RED" }, "BLU");
		if (input.equals("BLU")) {
			teamControlling = Entity.BLUE;
		} else {
			teamControlling = Entity.RED;
		}
	}

	public void stop() {
		isRunning = false;
	}

	public static void main(String args[]) {

		Component component = new Component();

		JFrame frame = new JFrame();
		frame.add(component);
		frame.setTitle(name);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new Listening());

		component.start();
	}

	public void inputChecks() {
		if (Listening.releaseLocation != null) {
			if (nextOrder != PathfindingNode.PATH_FINDING) {
				// Do order:
				Point.Double b = Game.game.getGamePos(Listening.releaseLocation);
				ArrayList<Unit> selectedUnits = Game.game.getEntities(selected);
				for (Unit unit : selectedUnits) {
					if (unit.classNumber == 6) { // Must be engineer.
						Task.addOrder(unit, nextOrder, b.x, b.y, false);
					}
				}
				nextOrder = PathfindingNode.PATH_FINDING;
			} else {
				if (Listening.pathFinding) {
					Listening.pathFinding = false;
					Point.Double b = Game.game.getGamePos(Listening.releaseLocation);
					ArrayList<Unit> selectedUnits = Game.game.getEntities(selected);
					for (Entity entity : Game.game.units.get(Math.abs(teamControlling - 1))) {
						if (entity.withinSelectDistance(b)) {
							for (Unit unit : selectedUnits) {
								Task.setTarget(unit, entity, false);
							}
							return;
						}
					}

					if (selectedUnits.size() >= 2) {
						Point.Double averagePoint = Listening.getAveragePos(selectedUnits);
						for (Unit unit : selectedUnits) {
							//Task.moveUnit(unit, (int) (b.x + Math.cos(angle) * IDEAL_DISTANCE), (int) (b.y + Math.sin(angle) * IDEAL_DISTANCE), false);
							// , b.x + unit.x - averagePoint.x, b.y + unit.y - averagePoint.y
							Task.moveUnit(unit, b.x, b.y, b.x + unit.x - averagePoint.x, b.y + unit.y - averagePoint.y, false);
						}
					} else if (selectedUnits.size() == 1) {
						Task.moveUnit(selectedUnits.get(0), b.x, b.y, b.x, b.y, false);
					}
				} else {
					if (!Listening.shiftDown) {
						selected.clear();
					}
					Point.Double a = Game.game.getGamePos(Listening.pressLocation);
					Point.Double b = Game.game.getGamePos(Listening.releaseLocation);
					for (Entity entity : Game.game.units.get(teamControlling)) {
						if (entity.getClass() == Unit.class) {
							Unit unit = (Unit) entity;
							double x = (Math.min(a.x, b.x) > unit.x) ? Math.min(a.x, b.x) : (Math.max(a.x, b.x) < unit.x) ? Math.max(a.x, b.x) : unit.x;
							double y = (Math.min(a.y, b.y) > unit.y) ? Math.min(a.y, b.y) : (Math.max(a.y, b.y) < unit.y) ? Math.max(a.y, b.y) : unit.y;

							if ((unit.x - x) * (unit.x - x) + (unit.y - y) * (unit.y - y) < (unit.width / 2.0) * (unit.width / 2.0)) {
								selected.add(unit.id);
							}
						}
					}
				}
			}
			Listening.releaseLocation = null;
		}
	}

	public void tick() {
		// Remember to update last tickfor (

		if (Listening.up) {
			Game.sY -= 10 / Game.unitSize;
		}
		if (Listening.down) {
			Game.sY += 10 / Game.unitSize;
		}
		if (Listening.left) {
			Game.sX -= 10 / Game.unitSize;
		}
		if (Listening.right) {
			Game.sX += 10 / Game.unitSize;
		}
	}

	public void render() {
		enemyHighlighted = Short.MAX_VALUE;
		Point mousePos = getMousePosition();
		if (mousePos != null && Game.game != null) {
			if (nextOrder == PathfindingNode.DISPENSER_BUILD) {
				setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursors[2], new Point(16, 16), "Placing D"));
			} else if (nextOrder == PathfindingNode.SENTRY_BUILD) {
				setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursors[3], new Point(16, 16), "Placing S"));
			} else {
				boolean setToDefault = true;
				for (Entity entity : Game.game.units.get(Math.abs(teamControlling - 1))) {
					if (entity.withinSelectDistance(Game.game.getGamePos(mousePos))) {
						enemyHighlighted = entity.id;
						setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursors[0], new Point(16, 16), "Enemy Unit"));
						setToDefault = false;
						break;
					}
				}
				if (setToDefault) {
					setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursors[1], new Point(16, 16), "Normal"));
				}
			}
		}

		inputChecks();

		timeSinceLastTick = (System.nanoTime() - lastTick) / (1000000000.0);
		if (timeSinceLastTick > timePerTick) {
			timeSinceLastTick = timePerTick; // Do not extrapolate.
		}
		((VolatileImage) screen).validate(getGraphicsConfiguration());

		Graphics2D g = (Graphics2D) screen.getGraphics();

		g.setColor(Color.gray);
		g.fillRect(0, 0, dimension.width, dimension.height);

		if (Game.game != null) {

			Game.map.render(g);

			for (Entity entity : Game.game.entities) {
				entity.render(g);
			}

			if ((Listening.pressLocation.x != -1 || Listening.pressLocation.y != -1) && mousePos != null && Listening.mouse1Down) {
				g.setColor(new Color(255, 255, 255, 128));
				g.setStroke(new BasicStroke(3));
				g.fillRect(Math.min(mousePos.x, Listening.pressLocation.x), Math.min(mousePos.y, Listening.pressLocation.y),
						Math.max(mousePos.x, Listening.pressLocation.x) - Math.min(mousePos.x, Listening.pressLocation.x),
						Math.max(mousePos.y, Listening.pressLocation.y) - Math.min(mousePos.y, Listening.pressLocation.y));
				g.drawRect(Math.min(mousePos.x, Listening.pressLocation.x), Math.min(mousePos.y, Listening.pressLocation.y),
						Math.max(mousePos.x, Listening.pressLocation.x) - Math.min(mousePos.x, Listening.pressLocation.x),
						Math.max(mousePos.y, Listening.pressLocation.y) - Math.min(mousePos.y, Listening.pressLocation.y));
			}

			ArrayList<Unit> selectedUnits = Game.game.getEntities(selected);
			int heightValue = dimension.height - 5;
			for (Unit unit : selectedUnits) {
				heightValue = unit.renderHud(heightValue, g);
			}

			Game.game.render(g); // Game hud.
		}

		g = (Graphics2D) getGraphics();

		g.drawImage(screen, 0, 0, dimension.width, dimension.height, 0, 0, dimension.width, dimension.height, null);
		g.dispose();
	}

	public void run() {
		screen = createVolatileImage(dimension.width, dimension.height);
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / /*Just in case*/(double) ticksPerSecond;
		long lastTimer = System.currentTimeMillis();
		// Just in case:
		lastTick = System.nanoTime();
		while (isRunning) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (unprocessed >= 1) {
				tick();
				unprocessed -= 1;
			}
			{
				render();
				frames++;
				if (unprocessed < 1) {
					try {
						Thread.sleep((int) ((1 - unprocessed) * nsPerTick) / 1000000, (int) ((1 - unprocessed) * nsPerTick) % 1000000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				//System.out.println("Fps: " + frames);
				frames = 0;
			}
		}
	}
}
