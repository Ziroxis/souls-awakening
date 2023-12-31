package com.yuanno.soulsawakening.abilities.elements.heal;

import com.yuanno.soulsawakening.ability.api.Ability;
import com.yuanno.soulsawakening.ability.api.IRightClickEmptyAbility;
import com.yuanno.soulsawakening.init.ModParticleTypes;
import com.yuanno.soulsawakening.init.ModValues;
import com.yuanno.soulsawakening.particles.ParticleEffect;
import com.yuanno.soulsawakening.particles.api.HoveringParticleEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class SelfHealingAbility extends Ability implements IRightClickEmptyAbility {
    public static final SelfHealingAbility INSTANCE = new SelfHealingAbility();
    public static final ParticleEffect PARTICLES_HOVER = new HoveringParticleEffect(3, 4);

    public SelfHealingAbility()
    {
        this.setName("Self Healing");
        this.setCooldown(10);
        this.setMaxCooldown(10);
        this.setPassive(false);
        this.setActivationType(ActivationType.RIGHT_CLICK_EMPTY);
        this.setZanpakutoState(ModValues.STATE.SHIKAI);
    }

    @Override
    public void onRightClick(PlayerEntity user)
    {
        PARTICLES_HOVER.spawn(user.level, user.getX(), user.getY(), user.getZ(), 0, 0, 0, ModParticleTypes.HEALING.get());
        if (user.hasEffect(Effects.ABSORPTION))
        {
            user.removeEffect(Effects.ABSORPTION);
            user.addEffect(new EffectInstance(Effects.ABSORPTION, 120, 1));
        }
        else
            user.addEffect(new EffectInstance(Effects.ABSORPTION, 120, 1));
        float missingHealth = user.getMaxHealth() - user.getHealth();
        user.heal(missingHealth / 2 + 4);

    }
}
