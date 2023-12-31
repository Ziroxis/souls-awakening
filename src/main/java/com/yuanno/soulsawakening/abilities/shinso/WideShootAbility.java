package com.yuanno.soulsawakening.abilities.shinso;

import com.yuanno.soulsawakening.ability.api.Ability;
import com.yuanno.soulsawakening.ability.api.IRightClickEmptyAbility;
import com.yuanno.soulsawakening.entities.projectiles.shinso.WideBladeProjectile;
import com.yuanno.soulsawakening.init.ModValues;
import net.minecraft.entity.player.PlayerEntity;

public class WideShootAbility extends Ability implements IRightClickEmptyAbility {
    public static final WideShootAbility INSTANCE = new WideShootAbility();

    public WideShootAbility()
    {
        this.setName("Wide Shoot");
        this.setCooldown(20);
        this.setMaxCooldown(20);
        this.setActivationType(ActivationType.SHIFT_RIGHT_CLICK);
        this.setZanpakutoState(ModValues.STATE.SHIKAI);
    }

    @Override
    public void onShiftRightClick(PlayerEntity user)
    {
        WideBladeProjectile wideBladeProjectile = new WideBladeProjectile(user.level, user);
        user.level.addFreshEntity(wideBladeProjectile);
        wideBladeProjectile.shootFromRotation(user, user.xRot, user.yRot, 0, 5f, 1);
    }
}
