package com.yuanno.soulsawakening.networking.client;

import com.yuanno.soulsawakening.data.entity.EntityStatsCapability;
import com.yuanno.soulsawakening.data.entity.IEntityStats;
import com.yuanno.soulsawakening.events.stats.HakudaGainEvent;
import com.yuanno.soulsawakening.events.stats.HohoGainEvent;
import com.yuanno.soulsawakening.events.stats.HollowGainEvent;
import com.yuanno.soulsawakening.events.stats.ZanjutsuGainEvent;
import com.yuanno.soulsawakening.init.ModValues;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CSyncentityStatsHollowPacket {

    private INBT data;

    public CSyncentityStatsHollowPacket() {}

    public CSyncentityStatsHollowPacket(IEntityStats props)
    {
        this.data = new CompoundNBT();
        this.data = EntityStatsCapability.INSTANCE.getStorage().writeNBT(EntityStatsCapability.INSTANCE, props, null);
    }

    public void encode(PacketBuffer buffer)
    {
        buffer.writeNbt((CompoundNBT) data);
    }

    public static CSyncentityStatsHollowPacket decode(PacketBuffer buffer)
    {
        CSyncentityStatsHollowPacket msg = new CSyncentityStatsHollowPacket();
        msg.data = buffer.readNbt();
        return msg;
    }

    public static void handle(CSyncentityStatsHollowPacket message, final Supplier<NetworkEvent.Context> ctx)
    {
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
        {
            ctx.get().enqueueWork(() ->
            {
                PlayerEntity player = ctx.get().getSender();
                IEntityStats props = EntityStatsCapability.get(player);
                EntityStatsCapability.INSTANCE.getStorage().readNBT(EntityStatsCapability.INSTANCE, props, null, message.data);
                if (props.getRace().equals(ModValues.HOLLOW))
                {
                    HollowGainEvent hollowGainEvent = new HollowGainEvent(player);
                    if (MinecraftForge.EVENT_BUS.post(hollowGainEvent))
                        return;
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
