package de.greenman999.svcgroupplayernames;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.GroupPlayerIconOrientation;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.logging.Logger;

public class SimpleVoiceChatGroupPlayerNamesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        Logger.getLogger("assets/simple-voice-chat-group-player-names").info("Simple Voice Chat Group Player Names Client Initialized");
	}

    public static void renderPlayerNames(DrawContext drawContext,
                                  int x,
                                  int y,
                                  int width,
                                  int height,
                                  PlayerState state,
                                  float scale,
                                  ClientVoicechat client) {
        drawContext.getMatrices().pushMatrix();
        float invScale = 1.0f / scale;
        drawContext.getMatrices().scale(invScale, invScale);

        int nameOffsetX = (int) (x + (width * scale) + (scale - 1) + 4 + scale - 1);
        int nameOffsetY = (int) ((y + scale - 1) + ((height * scale) / 2) - (float) (7 / 2) - 1);

        int hudX = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosX.get();
        int hudY = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosY.get();
        boolean horizontal = VoicechatClient.CLIENT_CONFIG.groupPlayerIconOrientation.get().equals(GroupPlayerIconOrientation.HORIZONTAL);
        if (horizontal) {
            drawContext.getMatrices().rotate((float) (Math.PI / 2));
            if (hudX < 0 && hudY < 0) {
                nameOffsetX = (int) (-MinecraftClient.getInstance().textRenderer.getWidth(state.getName()) - (height * scale) - (scale - 1) - 4 - (scale - 1));
                nameOffsetY = (int) (scale + (width * scale) / 2 - (float) (7 / 2) - 1);
            } else if (hudX < 0) {
                nameOffsetX = (int) ((int) (height * scale) + (scale - 1) + 4 + (scale - 1));
            } else if (hudY < 0) {
                nameOffsetY = (int) (y - (width * scale) + 7 + (2 - scale) + (width * scale) / 2 - (float) (7 / 2) - 1);
                nameOffsetX = (int) (-MinecraftClient.getInstance().textRenderer.getWidth(state.getName()) - (height * scale) - (scale - 1) - 4 - (scale - 1));
            } else {
                nameOffsetY = (int) (y - (width * scale) - scale + (width * scale) / 2 - (float) (7 / 2) - 1);
            }
        } else {
            if (hudX < 0) {
                nameOffsetX = (int) (-MinecraftClient.getInstance().textRenderer.getWidth(state.getName()) - (width * scale) - (scale - 1) - 4 - (scale - 1));
            }
            if (hudY < 0) {
                nameOffsetY = (int) (y - (width * scale) + 7 + (2 - scale) + ((height * scale) / 2) - (float) (7 / 2) - 1);
            }
        }


        drawContext.drawText(MinecraftClient.getInstance().textRenderer, state.getName(), nameOffsetX, nameOffsetY, client.getTalkCache().isTalking(state.getUuid()) ? 0xFFFFFFFF : 0x7FFFFFFF, false);
        drawContext.getMatrices().popMatrix();
    }
}