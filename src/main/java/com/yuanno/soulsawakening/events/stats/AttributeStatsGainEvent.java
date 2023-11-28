package com.yuanno.soulsawakening.events.stats;

import com.yuanno.soulsawakening.Main;
import com.yuanno.soulsawakening.data.entity.EntityStatsCapability;
import com.yuanno.soulsawakening.data.entity.IEntityStats;
import com.yuanno.soulsawakening.init.ModAttributes;
import com.yuanno.soulsawakening.init.ModValues;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.play.server.SUpdateHealthPacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class AttributeStatsGainEvent {
    public static final UUID ZANJUTSU_ATTACK_BONUS_ID = UUID.fromString("e19608b6-8b16-11ee-b9d1-0242ac120002");
    public static final UUID HOHO_SPEED_BONUS_ID = UUID.fromString("d85b324a-8b2e-11ee-b9d1-0242ac120002");

    @SubscribeEvent
    public static void hohoGainEvent(HohoGainEvent event)
    {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide)
            return;

        IEntityStats entityStats = EntityStatsCapability.get(player);

        double hohoPoints = entityStats.getHohoPoints() / 10000;
        AttributeModifier attributeModifier = new AttributeModifier("Hoho Speed Bonus", hohoPoints, AttributeModifier.Operation.ADDITION);
        if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(attributeModifier)) {
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(attributeModifier);
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(attributeModifier);
        }
        else
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(attributeModifier);

    }
    @SubscribeEvent
    public static void hakudaGainEvent(HakudaGainEvent event)
    {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide)
            return;
        ModifiableAttributeInstance maxHpAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        IEntityStats entityStats = EntityStatsCapability.get(player);
        maxHpAttribute.setBaseValue(20 + entityStats.getHakudaPoints());
        player.setHealth((float) maxHpAttribute.getValue());
        ((ServerPlayerEntity) player).connection.send(new SUpdateHealthPacket(player.getHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));
    }
    @SubscribeEvent
    public static void zanjutsuGainEvent(ZanjutsuGainEvent event)
    {
        // might add a way to gain the damage here instead of somewhere else
    }

    @SubscribeEvent
    public static void onAttributeItemChange(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();

        if (event.getSlotType() != EquipmentSlotType.MAINHAND || itemStack.isEmpty() || !itemStack.hasTag())
            return;

        if (itemStack.getItem().asItem() instanceof SwordItem)
        {
            int zanjutsuBonus = itemStack.getTag().getInt("zanjutsuBonus");
            AttributeModifier mod = new AttributeModifier(ZANJUTSU_ATTACK_BONUS_ID, "Zanjutsu Bonus", zanjutsuBonus, AttributeModifier.Operation.ADDITION);
            if (itemStack.getTag().getBoolean("hasZanjutsuBonus"))
            {
                event.addModifier(Attributes.ATTACK_DAMAGE, mod);
            }
            else if (!itemStack.getTag().getBoolean("hasZanjutsuBonus"))
            {
                event.removeModifier(Attributes.ATTACK_DAMAGE, mod);
            }
        }
    }

    @SubscribeEvent
    public static void onItemChange(LivingEquipmentChangeEvent event)
    {
        LivingEntity entity = event.getEntityLiving();
        ItemStack stack = event.getTo();

        if (!(entity instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) entity;
        IEntityStats entityStats = EntityStatsCapability.get(player);
        if (stack.isEmpty())
            return;
        if (!(stack.getItem().asItem() instanceof SwordItem))
            return;
        if(!stack.hasTag()) {
            stack.getOrCreateTag();
        }
        if (entityStats.getRace().equals(ModValues.FULLBRINGER) || entityStats.getRace().equals(ModValues.SHINIGAMI)) {
            stack.getTag().putBoolean("hasZanjutsuBonus", true);
            stack.getTag().putInt("zanjutsuBonus", (int) Math.floor(entityStats.getZanjutsuPoints()));
        }
        else {
            stack.getTag().remove("hasZanjutsuBonus");
            stack.getTag().remove("zanjutsuBonus");
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onToolTipRender(ItemTooltipEvent event)
    {
        PlayerEntity player = event.getPlayer();

        if (player == null) {
            return;
        }
        IEntityStats entityStats = EntityStatsCapability.get(player);
        if (event.getItemStack().getItem() instanceof SwordItem && (entityStats.getRace().equals(ModValues.SHINIGAMI) || entityStats.getRace().equals(ModValues.FULLBRINGER)) && entityStats.getZanjutsuPoints() > 0)
        {
            StringTextComponent damageBonus = new StringTextComponent(TextFormatting.WHITE + "" + new TranslationTextComponent("Zanjutsu Bonus").getString());
            if (!event.getToolTip().contains(damageBonus)) {
                event.getToolTip().add(new StringTextComponent(""));
                event.getToolTip().add(damageBonus);
            }
        }
    }
}
