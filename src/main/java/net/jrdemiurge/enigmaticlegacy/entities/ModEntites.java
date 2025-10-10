package net.jrdemiurge.enigmaticlegacy.entities;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.entities.custom.PermanentItemEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntites {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, EnigmaticLegacy.MOD_ID);

    public static final Supplier<EntityType<PermanentItemEntity>> PERMANENT_ITEM_ENTITY =
            ENTITY_TYPES.register("permanent_item_entity", () -> EntityType.Builder.<PermanentItemEntity>of(PermanentItemEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .setTrackingRange(64)
                    .setUpdateInterval(2)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("permanent_item_entity"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
