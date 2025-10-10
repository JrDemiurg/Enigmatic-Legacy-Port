package net.jrdemiurge.enigmaticlegacy.network;

import io.netty.buffer.ByteBuf;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EnderRingKeyPayload(boolean pressed) implements CustomPacketPayload {

    public static final Type<EnderRingKeyPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "ender_ring_key"));

    public static final StreamCodec<ByteBuf, EnderRingKeyPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, EnderRingKeyPayload::pressed,
            EnderRingKeyPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnServer(final EnderRingKeyPayload msg, final IPayloadContext ctx) {
        // Если хочешь — можно обернуть в ctx.enqueueWork(...)
        if (!msg.pressed()) return;

        if (!(ctx.player() instanceof ServerPlayer player)) return;

        if (!ModUtils.isTheCursedOne(player)) {
            return;
        }

        MenuProvider provider = new SimpleMenuProvider(
                (id, inv, p) -> new ChestMenu(MenuType.GENERIC_9x3, id, inv, p.getEnderChestInventory(), 3) {
                    @Override
                    public void removed(Player p2) {
                        super.removed(p2);
                        if (!p2.level().isClientSide) {
                            p2.level().playSound(null, p2.blockPosition(),
                                    SoundEvents.ENDER_CHEST_CLOSE, SoundSource.PLAYERS,
                                    1.0F, (float) (0.8F + (Math.random() * 0.2)));
                        }
                    }
                },
                Component.translatable("container.enderchest")
        );

        player.openMenu(provider);

        player.level().playSound(null, player.blockPosition(),
                SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS,
                1.0F, (float) (0.8F + (Math.random() * 0.2)));
    }
}
