package autoreconnect;

import static org.apache.logging.log4j.LogManager.getRootLogger;

import java.awt.AWTException;
import java.awt.Robot;

import net.minecraft.client.network.ClientPlayerEntity;

public class ResetPlayer {

	// I simply do not care how this code is written
	// Whether it is efficient
	// Whether it is "Correct"
	// All that matters is that it works as intended

	private static void toggleBotton(int key) {
		try {
			System.setProperty("java.awt.headless", "false");

			final Robot robot = new Robot();

			getRootLogger().info("Attempting to press key 112 (1)"); // f1
			robot.keyPress(key);
			Thread.sleep(500);
			robot.keyRelease(key);
			Thread.sleep(1000);
			getRootLogger().info("Attempting to press key 112 (2)"); // f1
			robot.keyPress(key);
			Thread.sleep(500);
			robot.keyRelease(key);

		} catch (AWTException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void run(ClientPlayerEntity player) {
		AFKManager.isAfk = false;
		AFKManager.afkTime = 0;
		
		final Thread thread = new Thread(() -> {

			try {
				Thread.sleep(5000);
				getRootLogger().info("Running command /skyblock");
				player.sendChatMessage("/skyblock");

				Thread.sleep(5000);
				getRootLogger().info("Running command /is");
				player.sendChatMessage("/is");

				Thread.sleep(5000);
				toggleBotton(112);

			} catch (InterruptedException ignored) {
			}

		});

		thread.start();
	}
}
