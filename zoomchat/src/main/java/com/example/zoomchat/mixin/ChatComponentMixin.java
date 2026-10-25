package com.example.zoomchat.mixin;

import com.example.zoomchat.ZoomChatClient;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    /**
     * The public draw method has exactly one boolean parameter. Forcing it to true while
     * Z is held is an experiment to see what it controls.
     */
    @ModifyVariable(
            method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0)
    private boolean zoomchat$forceFocused(boolean focused) {
        return focused || ZoomChatClient.chatPreviewActive;
    }
}
