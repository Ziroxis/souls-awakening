package com.yuanno.soulsawakening.items.blueprints;

import com.yuanno.soulsawakening.abilities.elements.fire.FireAttackAbility;
import com.yuanno.soulsawakening.abilities.elements.fire.FireBallAbility;
import com.yuanno.soulsawakening.abilities.elements.fire.FireWaveAbility;
import com.yuanno.soulsawakening.abilities.elements.heal.HealingTouchingAbility;
import com.yuanno.soulsawakening.abilities.elements.heal.RevitilazingAuraAbility;
import com.yuanno.soulsawakening.abilities.elements.heal.SelfHealingAbility;
import com.yuanno.soulsawakening.abilities.elements.lunar.LunarBlessingAbility;
import com.yuanno.soulsawakening.abilities.elements.lunar.LunarCrescentAbility;
import com.yuanno.soulsawakening.abilities.elements.lunar.LunarWaveAbility;
import com.yuanno.soulsawakening.abilities.elements.normal.NormalBuffAbility;
import com.yuanno.soulsawakening.abilities.elements.poison.AdrenalineCloudAbility;
import com.yuanno.soulsawakening.abilities.elements.poison.PoisonAttackAbility;
import com.yuanno.soulsawakening.abilities.elements.poison.VenomousCloudAbility;
import com.yuanno.soulsawakening.abilities.elements.shadow.DarkStepAbility;
import com.yuanno.soulsawakening.abilities.elements.shadow.ShadowAttackAbility;
import com.yuanno.soulsawakening.abilities.elements.shadow.UmbralCloakAbility;
import com.yuanno.soulsawakening.abilities.elements.thunder.LightningStepAbility;
import com.yuanno.soulsawakening.abilities.elements.thunder.ThunderAttackAbility;
import com.yuanno.soulsawakening.abilities.elements.thunder.ThunderStrikeAbility;
import com.yuanno.soulsawakening.abilities.elements.water.AquaSlashAbility;
import com.yuanno.soulsawakening.abilities.elements.water.TidalWaveAbility;
import com.yuanno.soulsawakening.abilities.elements.water.WaterPrisonAbility;
import com.yuanno.soulsawakening.abilities.elements.wind.GaleForceAbility;
import com.yuanno.soulsawakening.abilities.elements.wind.WhirldWindDanceAbility;
import com.yuanno.soulsawakening.abilities.elements.wind.WindAttackAbility;
import com.yuanno.soulsawakening.data.ability.AbilityDataCapability;
import com.yuanno.soulsawakening.data.ability.IAbilityData;
import com.yuanno.soulsawakening.data.entity.EntityStatsCapability;
import com.yuanno.soulsawakening.data.entity.IEntityStats;
import com.yuanno.soulsawakening.init.ModItemGroup;
import com.yuanno.soulsawakening.init.ModResources;
import com.yuanno.soulsawakening.init.ModTiers;
import com.yuanno.soulsawakening.init.ModValues;
import com.yuanno.soulsawakening.networking.PacketHandler;
import com.yuanno.soulsawakening.networking.server.SSyncAbilityDataPacket;
import com.yuanno.soulsawakening.networking.server.SSyncEntityStatsPacket;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ZanpakutoItem extends SwordItem {
    private ELEMENT zanpakutoElement = ELEMENT.NONE;
    private ModResources.STATE zanpakutoState = ModResources.STATE.SEALED;
    private TYPE zanpakutoType;
    private ItemStack stack;

    public ZanpakutoItem() {
        super(ModTiers.WEAPON, 7, 0.5f, new Item.Properties().rarity(Rarity.RARE).tab(ModItemGroup.SOULS_AWAKENINGS_WEAPONS).stacksTo(1));
        this.zanpakutoState = ModResources.STATE.SEALED;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        // Store the item stack
        this.stack = stack;

    }
    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity owner) {
        String currentOwner = itemStack.getOrCreateTag().getString("owner");
        if (owner instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) owner;
            IEntityStats entityStats = EntityStatsCapability.get(player);
            int zanpakutoDamage = (int) entityStats.getZanjutsuPoints() + 7;
            this.setDamage(itemStack, (int) Math.floor(zanpakutoDamage));
        }
        if (!currentOwner.isEmpty()) {
            super.hurtEnemy(itemStack, target, owner);
            return true;
        }
        else
            return false;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {


        ItemStack itemStack = player.getItemInHand(hand);
        IEntityStats entityStats = EntityStatsCapability.get(player);
        if (!itemStack.hasTag())
            itemStack.setTag(new CompoundNBT());

        String currentOwner = itemStack.getOrCreateTag().getString("owner");
        if (currentOwner.isEmpty() && !player.level.isClientSide) {
            ELEMENT element = ELEMENT.DARK;
            IAbilityData abilityData = AbilityDataCapability.get(player);
            itemStack.getTag().putString("owner", player.getDisplayName().getString());
            itemStack.getTag().putString("zanpakutoElement", element.name());
            itemStack.getTag().putString("zanpakutoType", TYPE.getRandomType().name());
            itemStack.getTag().putString("zanpakutoState", ModResources.STATE.SEALED.name());
            if (entityStats.getRace().equals(ModValues.SPIRIT)) {
                entityStats.setRace(ModValues.SHINIGAMI);
            }
            else if (entityStats.getRace().equals(ModValues.HUMAN))
            {
                entityStats.setRace(ModValues.FULLBRINGER);
            }
            entityStats.setHohoPoints(0);
            entityStats.addAvailableStats(0);
            entityStats.setHakudaPoints(0);
            entityStats.addAvailableStats(0);
            entityStats.setZanjutsuPoints(0);
            entityStats.addAvailableStats(0);
            switch (element)
            {
                case DARK:
                    abilityData.addUnlockedAbility(DarkStepAbility.INSTANCE);
                    abilityData.addUnlockedAbility(ShadowAttackAbility.INSTANCE);
                    abilityData.addUnlockedAbility(UmbralCloakAbility.INSTANCE);
                    break;
                case FIRE:
                    abilityData.addUnlockedAbility(FireAttackAbility.INSTANCE);
                    abilityData.addUnlockedAbility(FireWaveAbility.INSTANCE);
                    abilityData.addUnlockedAbility(FireBallAbility.INSTANCE);
                    break;
                case HEAL:
                    abilityData.addUnlockedAbility(HealingTouchingAbility.INSTANCE);
                    abilityData.addUnlockedAbility(RevitilazingAuraAbility.INSTANCE);
                    abilityData.addUnlockedAbility(SelfHealingAbility.INSTANCE);
                    break;
                case LIGHTNING:
                    abilityData.addUnlockedAbility(LightningStepAbility.INSTANCE);
                    abilityData.addUnlockedAbility(ThunderAttackAbility.INSTANCE);
                    abilityData.addUnlockedAbility(ThunderStrikeAbility.INSTANCE);
                    break;
                case LUNAR:
                    abilityData.addUnlockedAbility(LunarBlessingAbility.INSTANCE);
                    abilityData.addUnlockedAbility(LunarCrescentAbility.INSTANCE);
                    abilityData.addUnlockedAbility(LunarWaveAbility.INSTANCE);
                    break;
                case NORMAL:
                    abilityData.addUnlockedAbility(NormalBuffAbility.INSTANCE);
                    break;
                case POISON:
                    abilityData.addUnlockedAbility(PoisonAttackAbility.INSTANCE);
                    abilityData.addUnlockedAbility(VenomousCloudAbility.INSTANCE);
                    abilityData.addUnlockedAbility(AdrenalineCloudAbility.INSTANCE);
                    break;
                case WATER:
                    abilityData.addUnlockedAbility(AquaSlashAbility.INSTANCE);
                    abilityData.addUnlockedAbility(TidalWaveAbility.INSTANCE);
                    abilityData.addUnlockedAbility(WaterPrisonAbility.INSTANCE);
                    break;
                case WIND:
                    abilityData.addUnlockedAbility(GaleForceAbility.INSTANCE);
                    abilityData.addUnlockedAbility(WhirldWindDanceAbility.INSTANCE);
                    abilityData.addUnlockedAbility(WindAttackAbility.INSTANCE);
                    break;
            }
            PacketHandler.sendTo(new SSyncEntityStatsPacket(player.getId(), entityStats), player);
            PacketHandler.sendTo(new SSyncAbilityDataPacket(player.getId(), abilityData), player);
            return ActionResult.success(itemStack);
        }
        else if (!currentOwner.equals(player.getDisplayName().getString()) || !entityStats.getRace().equals(ModValues.SHINIGAMI))
            return ActionResult.fail(itemStack);

        return ActionResult.success(itemStack);

    }



    public void setOwner(PlayerEntity player, ItemStack itemStack)
    {
        if (!itemStack.hasTag())
            itemStack.setTag(new CompoundNBT());
        String currentOwner = itemStack.getOrCreateTag().getString("owner");
        if (currentOwner.isEmpty())
            itemStack.getTag().putString("owner", player.getDisplayName().getString());
        else
            return;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag par4)
    {
        if (itemStack.getTag().getString("owner").isEmpty())
            return;
        else
        {
            String currentOwner = itemStack.getTag().getString("owner");
            list.add(new StringTextComponent("§4Owner: " + currentOwner));
        }
    }

    public enum TYPE {
        TYPE_1, TYPE_2;

        public static TYPE getRandomType()
        {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    public enum ELEMENT {
        NONE, DARK, FIRE, HEAL, LIGHTNING, LUNAR, NORMAL, POISON, WATER, WIND;

        public static ELEMENT getRandomElement()
        {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }




    public ModResources.STATE getNextZanpakutoState(ModResources.STATE currentState) {
        ModResources.STATE[] states = ModResources.STATE.values();
        int currentIndex = currentState.ordinal();
        int nextIndex = (currentIndex + 1) % states.length;  // Calculate the next index in a circular manner
        return states[nextIndex];
    }
    public ELEMENT getZanpakutoElement() {
        String elementName = stack.getTag().getString("zanpakutoElement");
        return ELEMENT.valueOf(elementName);
    }

    public TYPE getZanpakutoType() {
        String typeName = stack.getTag().getString("zanpakutoType");
        return TYPE.valueOf(typeName);
    }

    public ModResources.STATE getZanpakutoState() {
        if (stack != null) {
            String stateName = stack.getTag().getString("zanpakutoState");

            // Handle cases where the state name is invalid or not present
            try {
                return ModResources.STATE.valueOf(stateName);
            } catch (IllegalArgumentException e) {
                // Log the error or handle it accordingly
                // For now, we'll return SEALED in case of an invalid state
                return ModResources.STATE.SEALED;
            }
        } else {
            return ModResources.STATE.SEALED;
        }
    }

    public void setZanpakutoElement(ELEMENT element) {
        stack.getTag().putString("zanpakutoElement", element.name());
    }

    public void setZanpakutoType(TYPE type) {
        stack.getTag().putString("zanpakutoType", type.name());
    }

    public void setZanpakutoState(ModResources.STATE state) {
        stack.getTag().putString("zanpakutoState", state.name());
    }

}
