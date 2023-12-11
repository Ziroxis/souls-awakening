package com.yuanno.soulsawakening.init;

import com.yuanno.soulsawakening.entity.BulkEntity;
import com.yuanno.soulsawakening.entity.CentipedeEntity;
import com.yuanno.soulsawakening.entity.ClawEntity;
import com.yuanno.soulsawakening.entity.JetEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event)
    {
        //MOBS
        event.put(ModEntities.CENTIPEDE.get(), CentipedeEntity.setCustomAttributes().build());
        event.put(ModEntities.CLAW.get(), ClawEntity.setCustomAttributes().build());
        event.put(ModEntities.JET.get(), JetEntity.setCustomAttributes().build());
        event.put(ModEntities.BULK.get(), BulkEntity.setCustomAttributes().build());
    }
}
