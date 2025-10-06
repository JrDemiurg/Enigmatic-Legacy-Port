package net.jrdemiurge.enigmaticlegacy.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EnderMan.class)
public interface EnderManAccessor {
    @Invoker("teleportTowards")
    boolean callTeleportTowards(Entity target);
}
