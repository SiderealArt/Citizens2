package net.citizensnpcs.nms.v1_16_R1.entity.nonliving;

import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.event.NPCPushEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.nms.v1_16_R1.entity.MobEntityController;
import net.citizensnpcs.nms.v1_16_R1.util.NMSImpl;
import net.citizensnpcs.npc.CitizensNPC;
import net.citizensnpcs.npc.ai.NPCHolder;
import net.citizensnpcs.util.Util;
import net.minecraft.server.v1_16_R1.EntityMinecartMobSpawner;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import net.minecraft.server.v1_16_R1.World;

public class MinecartSpawnerController extends MobEntityController {
    public MinecartSpawnerController() {
        super(EntityMinecartSpawnerNPC.class);
    }

    @Override
    public Minecart getBukkitEntity() {
        return (Minecart) super.getBukkitEntity();
    }

    public static class EntityMinecartSpawnerNPC extends EntityMinecartMobSpawner implements NPCHolder {
        private final CitizensNPC npc;

        public EntityMinecartSpawnerNPC(EntityTypes<? extends EntityMinecartMobSpawner> types, World world) {
            this(types, world, null);
        }

        public EntityMinecartSpawnerNPC(EntityTypes<? extends EntityMinecartMobSpawner> types, World world, NPC npc) {
            super(types, world);
            this.npc = (CitizensNPC) npc;
        }

        @Override
        public void collide(net.minecraft.server.v1_16_R1.Entity entity) {
            // this method is called by both the entities involved - cancelling
            // it will not stop the NPC from moving.
            super.collide(entity);
            if (npc != null) {
                Util.callCollisionEvent(npc, entity.getBukkitEntity());
            }
        }

        @Override
        public boolean d(NBTTagCompound save) {
            return npc == null ? super.d(save) : false;
        }

        @Override
        public NPC getNPC() {
            return npc;
        }

        @Override
        public void h(double x, double y, double z) {
            if (npc == null) {
                super.h(x, y, z);
                return;
            }
            if (NPCPushEvent.getHandlerList().getRegisteredListeners().length == 0) {
                if (!npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true))
                    super.h(x, y, z);
                return;
            }
            Vector vector = new Vector(x, y, z);
            NPCPushEvent event = Util.callPushEvent(npc, vector);
            if (!event.isCancelled()) {
                vector = event.getCollisionVector();
                super.h(vector.getX(), vector.getY(), vector.getZ());
            }
            // when another entity collides, this method is called to push the
            // NPC so we prevent it from doing anything if the event is
            // cancelled.
        }

        @Override
        public void tick() {
            super.tick();
            if (npc != null) {
                npc.update();
                NMSImpl.minecartItemLogic(this);
            }
        }

    }
}