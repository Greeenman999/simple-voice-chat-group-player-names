package de.greenman999.svcgroupplayernames;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.GroupPlayerIconOrientation;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class SimpleVoiceChatGroupPlayerNamesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
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

        }
        if (hudX < 0) {
            nameOffsetX = -MinecraftClient.getInstance().textRenderer.getWidth(state.getName());
        }

        drawContext.drawText(MinecraftClient.getInstance().textRenderer, state.getName(), nameOffsetX, nameOffsetY, client.getTalkCache().isTalking(state.getUuid()) ? 0xFFFFFFFF : 0x7FFFFFFF, false);

        /*int nameOffsetX = x + 22;
        int nameOffsetY = y + 5;

        nameOffsetY = (int) (y + ((float) height / 2) - (scale / 2));

        int hudX = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosX.get();
        int hudY = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosY.get();
        boolean horizontal = VoicechatClient.CLIENT_CONFIG.groupPlayerIconOrientation.get().equals(GroupPlayerIconOrientation.HORIZONTAL);
        if (horizontal) {
            drawContext.getMatrices().rotate((float) (Math.PI / 2));
            nameOffsetY = y - 14;
        }
        if (hudX < 0) {
            nameOffsetX = -MinecraftClient.getInstance().textRenderer.getWidth(state.getName()) - (int) (width * scale) - 6;
        }


        drawContext.drawText(MinecraftClient.getInstance().textRenderer, state.getName(), nameOffsetX, (int) (nameOffsetY * scale), client.getTalkCache().isTalking(state.getUuid()) ? 0xFFFFFFFF : 0x7FFFFFFF, false);*/

        drawContext.getMatrices().popMatrix();
    }
}