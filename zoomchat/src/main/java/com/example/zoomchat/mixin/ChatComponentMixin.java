package com.example.zoomchat.mixin;

import com.example.zoomchat.ZoomChatClient;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    /** While Z is held, report chat as focused so the HUD draws the full chat history. */
    @Inject(method = "isChatFocused", at = @At("HEAD"), cancellable = true)
    private void zoomchat$focused(CallbackInfoReturnable<Boolean> cir) {
        if (ZoomChatClient.chatPreviewActive) cir.setReturnValue(true);
    }
}
