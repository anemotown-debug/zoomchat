package com.example.zoomchat.mixin;

import com.example.zoomchat.ZoomChatClient;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    /** While C is held, the scroll wheel changes zoom instead of switching hotbar slots. */
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void zoomchat$scroll(long window, double xOffset, double yOffset, CallbackInfo ci) {
        if (ZoomChatClient.onScroll(yOffset)) ci.cancel();
    }
}
