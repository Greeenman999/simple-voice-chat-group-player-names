package de.greenman999.svcgroupplayernames.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import de.greenman999.svcgroupplayernames.SimpleVoiceChatGroupPlayerNamesClient;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.GroupChatManager;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GroupChatManager.class)
public class GroupChatManagerMixin {

    @Definition(id = "drawTexture", method = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIII)V")
    @Definition(id = "GUI_TEXTURED", field = "Lnet/minecraft/client/gl/RenderPipelines;GUI_TEXTURED:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    @Definition(id = "body", method = "Lnet/minecraft/entity/player/SkinTextures;body()Lnet/minecraft/util/AssetInfo$TextureAsset;")
    @Definition(id = "texturePath", method = "Lnet/minecraft/util/AssetInfo$TextureAsset;texturePath()Lnet/minecraft/util/Identifier;")
    @Expression("?.drawTexture(GUI_TEXTURED, ?.body().texturePath(), ?, ?, ?, ?, ?, ?, ?, ?)")
    @WrapOperation(
            method = "renderIcons",
            at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1)
    )
    private static void renderPlayerNames(DrawContext drawContext,
                                          RenderPipeline pipeline,
                                          Identifier sprite,
                                          int x,
                                          int y,
                                          float u,
                                          float v,
                                          int width,
                                          int height,
                                          int textureWidth,
                                          int textureHeight,
                                          Operation<Void> original,
                                          @SuppressWarnings("LocalMayBeArgsOnly") @Local PlayerState state,
                                          @SuppressWarnings("LocalMayBeArgsOnly") @Local float scale,
                                          @SuppressWarnings("LocalMayBeArgsOnly") @Local ClientVoicechat client) {
        original.call(drawContext, pipeline, sprite, x, y, u, v, width, height, textureWidth, textureHeight);

        SimpleVoiceChatGroupPlayerNamesClient.renderPlayerNames(drawContext, x, y, width, height, state, scale, client);
    }
}