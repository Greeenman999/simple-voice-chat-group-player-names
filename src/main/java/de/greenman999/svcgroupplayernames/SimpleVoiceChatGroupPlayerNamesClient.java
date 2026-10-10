package de.greenman999.svcgroupplayernames;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.GroupPlayerIconOrientation;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.logging.Logger;

public class SimpleVoiceChatGroupPlayerNamesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        Logger.getLogger("svcgroupplayernames").info("Simple Voice Chat Group Player Names Client Initialized");

        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);

	}

    public static void renderPlayerNames(GuiGraphicsExtractor instance,
                                         int x,
                                         int y,
                                         int width,
                                         int height,
                                         PlayerState state,
                                         float scale,
                                         ClientVoicechat client) {
        instance.pose().pushMatrix();
        float invScale = 1.0f / scale;
        instance.pose().scale(invScale, invScale);

        int nameOffsetX = (int) (x + (width * scale) + (scale - 1) + 4 + scale - 1);
        int nameOffsetY = (int) ((y + scale - 1) + ((height * scale) / 2) - (float) (7 / 2) - 1);

        int hudX = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosX.get();
        int hudY = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosY.get();
        boolean horizontal = VoicechatClient.CLIENT_CONFIG.groupPlayerIconOrientation.get().equals(GroupPlayerIconOrientation.HORIZONTAL);
        if (horizontal) {
            instance.pose().rotate((float) (Math.PI / 2));
            if (hudX < 0 && hudY < 0) {
                nameOffsetX = (int) (-Minecraft.getInstance().font.width(state.getName()) - (height * scale) - (scale - 1) - 4 - (scale - 1));
                nameOffsetY = (int) (scale + (width * scale) / 2 - (float) (7 / 2) - 1);
            } else if (hudX < 0) {
                nameOffsetX = (int) ((int) (height * scale) + (scale - 1) + 4 + (scale - 1));
            } else if (hudY < 0) {
                nameOffsetY = (int) (y - (width * scale) + 7 + (2 - scale) + (width * scale) / 2 - (float) (7 / 2) - 1);
                nameOffsetX = (int) (-Minecraft.getInstance().font.width(state.getName()) - (height * scale) - (scale - 1) - 4 - (scale - 1));
            } else {
                nameOffsetY = (int) (y - (width * scale) - scale + (width * scale) / 2 - (float) (7 / 2) - 1);
            }
        } else {
            if (hudX < 0) {
                nameOffsetX = (int) (-Minecraft.getInstance().font.width(state.getName()) - (width * scale) - (scale - 1) - 4 - (scale - 1));
            }
            if (hudY < 0) {
                nameOffsetY = (int) (y - (width * scale) + 7 + (2 - scale) + ((height * scale) / 2) - (float) (7 / 2) - 1);
            }
        }

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        int transparencyWhenTalking = whiteWithAlpha(config.transparencyWhenTalking);
        int transparencyWhenNotTalking = whiteWithAlpha(config.transparencyWhenNotTalking);
        if (config.onlyShowNamesWhenTalking && !client.getTalkCache().isTalking(state.getUuid())) {
            instance.pose().popMatrix();
            return;
        }
        instance.text(Minecraft.getInstance().font, state.getName(), nameOffsetX, nameOffsetY, client.getTalkCache().isTalking(state.getUuid()) ? transparencyWhenTalking : transparencyWhenNotTalking, false);
        instance.pose().popMatrix();
    }

    public static int whiteWithAlpha(int percent) {
        percent = Math.max(0, Math.min(100, percent));
        int alpha = Math.round(percent / 100.0f * 255.0f);
        alpha = Math.max(0, Math.min(255, alpha));
        return ((alpha & 0xFF) << 24) | 0x00FFFFFF;
    }

}