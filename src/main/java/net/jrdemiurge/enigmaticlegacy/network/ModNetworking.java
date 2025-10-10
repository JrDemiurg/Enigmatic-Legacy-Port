package net.jrdemiurge.enigmaticlegacy.network;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public final class ModNetworking {
    private ModNetworking() {}

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.MAIN);

        registrar.playToServer(
                EnderRingKeyPayload.TYPE,
                EnderRingKeyPayload.STREAM_CODEC,
                EnderRingKeyPayload::handleOnServer
        );
    }
}
