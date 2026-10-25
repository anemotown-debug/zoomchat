package com.example.zoomchat.mixin;

import com.example.zoomchat.ZoomChatClient;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    /** While Z is held, report chat as focused. */
    @Inject(method = "isChatFocused", at = @At("HEAD"), cancellable = true)
    private void zoomchat$focused(CallbackInfoReturnable<Boolean> cir) {
        if (ZoomChatClient.chatPreviewActive) cir.setReturnValue(true);
    }

    /**
     * The public draw method has exactly one boolean parameter, which I believe is "focused".
     * Forcing it to true while Z is held should draw the full chat history without fading.
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
