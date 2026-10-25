package com.example.zoomchat;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class ZoomChatClient implements ClientModInitializer {
    /** Vanilla's FOV option bottoms out at 30, so the real zoom is limited by that. */
    private static final int MIN_FOV = 30;
    private static final double STEP = 1.15;

    /** Current zoom level (1.0 = no zoom). Kept between presses; changed with the scroll wheel. */
    private static double zoomFactor = 2.0;

    private static KeyMapping zoomKey;
    private static KeyMapping previewKey;

    private static boolean zooming = false;
    private static int savedFov = 70;

    /** Read by ChatComponentMixin. */
    public static boolean chatPreviewActive = false;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category =
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath("zoomchat", "main"));

        zoomKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.zoomchat.zoom", InputConstants.KEY_C, category));
        previewKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.zoomchat.preview_chat", InputConstants.KEY_Z, category));

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        ClientLifecycleEvents.CLIENT_STOPPING.register(mc -> stopZoom(mc));
    }

    private void tick(Minecraft mc) {
        boolean noScreen = mc.gui.screen() == null && mc.player != null;

        chatPreviewActive = noScreen && previewKey.isDown();

        boolean wantZoom = noScreen && zoomKey.isDown();
        if (wantZoom && !zooming) {
            savedFov = mc.options.fov().get();
            zooming = true;
        }
        if (wantZoom) {
            zoomFactor = Math.min(zoomFactor, maxFactor());
            int target = (int) Math.max(MIN_FOV, Math.round(savedFov / zoomFactor));
            if (mc.options.fov().get() != target) mc.options.fov().set(target);
        } else if (zooming) {
            stopZoom(mc);
        }
    }

    private static double maxFactor() {
        return Math.max(1.0, savedFov / (double) MIN_FOV);
    }

    /** Called by MouseHandlerMixin. Returns true if the scroll was used for zoom (and should be cancelled). */
    public static boolean onScroll(double yOffset) {
        if (!zooming || yOffset == 0) return false;
        double f = zoomFactor * Math.pow(STEP, Math.signum(yOffset));
        zoomFactor = Math.max(1.0, Math.min(maxFactor(), f));
        return true;
    }

    private static void stopZoom(Minecraft mc) {
        if (!zooming) return;
        zooming = false;
        mc.options.fov().set(savedFov);
    }
}
