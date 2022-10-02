package autoreconnect.mixin;

import static org.objectweb.asm.Opcodes.PUTFIELD;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import autoreconnect.AFKManager;
import autoreconnect.event.ScreenChangedListener;
import autoreconnect.event.ServerEntryChangedListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	public Screen currentScreen;
	private double oldX, oldY, oldZ;

	@Inject(method = "tick", at = @At("HEAD"))
	public void onTick(CallbackInfo ci) {

		MinecraftClient mc = MinecraftClient.getInstance();
		ClientPlayerEntity player = mc.player;
		
		if (mc != null && player != null) {
			if (player.getX() == oldX && player.getY() == oldY && player.getZ() == oldZ) {
				AFKManager.tickAfkStatus();
			} else {
				AFKManager.afkTime = 0;
			}

			this.oldX = player.getX();
			this.oldY = player.getY();
			this.oldZ = player.getZ();
		}
	}

	@Inject(at = @At("HEAD"), method = "setCurrentServerEntry")
	private void setCurrentServerEntry(ServerInfo serverInfo, CallbackInfo info) {
		ServerEntryChangedListener.EVENT.invoker().onServerEntryChanged(serverInfo);
	}

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", opcode = PUTFIELD), method = "setScreen")
	private void setScreen(Screen newScreen, CallbackInfo info) {
		// old and new screen must not be the same type, actually happens very often for
		// some reason
		if ((currentScreen == null ? null : currentScreen.getClass()) != (newScreen == null ? null
				: newScreen.getClass())) {
			ScreenChangedListener.EVENT.invoker().onScreenChanged(currentScreen, newScreen);
		}
	}
}