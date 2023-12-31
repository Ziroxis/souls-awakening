package com.yuanno.soulsawakening.abilities.elements.poison;

import com.yuanno.soulsawakening.ability.api.Ability;
import com.yuanno.soulsawakening.ability.api.IRightClickEmptyAbility;
import com.yuanno.soulsawakening.api.Beapi;
import com.yuanno.soulsawakening.init.ModValues;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.List;

public class AdrenalineCloudAbility extends Ability implements IRightClickEmptyAbility {

    public static final AdrenalineCloudAbility INSTANCE = new AdrenalineCloudAbility();
    public AdrenalineCloudAbility()
    {
        this.setName("Adrenaline Cloud");
        this.setCooldown(17);
        this.setMaxCooldown(17);
        this.setActivationType(ActivationType.SHIFT_RIGHT_CLICK);
        this.setState(STATE.READY);
        this.setPassive(false);
        this.setZanpakutoState(ModValues.STATE.SHIKAI);
    }

    @Override
    public void onRightClick(PlayerEntity player)
    {
        List<LivingEntity> targets = Beapi.getNearbyEntities(player.blockPosition(), player.level, 10, null, LivingEntity.class);
        targets.add(player);
        for (LivingEntity livingEntity : targets)
        {
            if (!livingEntity.hasEffect(Effects.MOVEMENT_SPEED))
                livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 240, 1));
            if (!livingEntity.hasEffect(Effects.DAMAGE_BOOST))
                livingEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 240, 1));
        }
    }}
