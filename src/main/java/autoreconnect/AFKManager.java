package autoreconnect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

public class AFKManager {

	public static boolean isAfk = false;
	private static boolean wasAfk = false;
	public static long afkTime = 0;
	private static long lastUpdate = 0;

	@SuppressWarnings("resource")
	public static void tickAfkStatus() {
		
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		
		if (System.nanoTime() - lastUpdate > 1e+9) {
			afkTime++;

			boolean afk = afkTime > 20;

			if (afk && !wasAfk) {
				AutoReconnect.LOGGER.debug("AutoAFK on.");
				
				if (MinecraftClient.getInstance().player != null) {
					MinecraftClient.getInstance().player.sendSystemMessage(new TranslatableText("afkpeace.afkmode.on"), Util.NIL_UUID);
					ResetPlayer.run(player);
				}
				
				isAfk = true;
			
			} else if (!afk && wasAfk) {
				AutoReconnect.LOGGER.debug("AutoAFK off.");
				if (MinecraftClient.getInstance().player != null) {
					MinecraftClient.getInstance().player.sendSystemMessage(new TranslatableText("afkpeace.afkmode.off"), Util.NIL_UUID);
				}
				
				isAfk = false;
			}
			wasAfk = afk;
			lastUpdate = System.nanoTime();
		}
	}
}
