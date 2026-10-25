package com.example.zoomchat.mixin;

import com.example.zoomchat.ZoomChatClient;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    /**
     * ChatComponent#render has a boolean "focused" argument (true when the chat screen is open).
     * Forcing it to true while Z is held makes the HUD draw the full chat history without fading.
     * Ordinal 0 = the first boolean parameter.
     */
    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private boolean zoomchat$forceFocused(boolean focused) {
        return focused || ZoomChatClient.chatPreviewActive;
    }
}
