package net.citizensnpcs.npc.entity;

import net.citizensnpcs.npc.CitizensMobNPC;
import net.citizensnpcs.npc.CitizensNPCManager;

import net.minecraft.server.EntityEnderman;
import net.minecraft.server.PathfinderGoalSelector;
import net.minecraft.server.World;

import org.bukkit.entity.Enderman;

public class CitizensEndermanNPC extends CitizensMobNPC {

    public CitizensEndermanNPC(CitizensNPCManager manager, int id, String name) {
        super(manager, id, name, EntityEndermanNPC.class);
    }

    @Override
    public Enderman getBukkitEntity() {
        return (Enderman) getHandle().getBukkitEntity();
    }

    public static class EntityEndermanNPC extends EntityEnderman {

        public EntityEndermanNPC(World world) {
            super(world);
            goalSelector = new PathfinderGoalSelector();
            targetSelector = new PathfinderGoalSelector();
        }

        @Override
        public void d_() {
        }

        @Override
        public void e() {
        }
    }
}