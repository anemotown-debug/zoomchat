package com.example.zoomchat.mixin;

import com.example.zoomchat.ZoomChatClient;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    /**
     * The private draw method builds a fade calculator with AlphaCalculator.timeBased(tick).
     * While Z is held, passing tick = 0 makes every line look brand new, so old chat
     * lines are drawn at full strength instead of being faded out.
     */
    @ModifyArg(
            method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/ChatComponent$AlphaCalculator;timeBased(I)Lnet/minecraft/client/gui/components/ChatComponent$AlphaCalculator;"),
            index = 0)
    private int zoomchat$freshTick(int tick) {
        return ZoomChatClient.chatPreviewActive ? 0 : tick;
    }
}
