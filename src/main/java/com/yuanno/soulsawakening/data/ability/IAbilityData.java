package com.yuanno.soulsawakening.data.ability;

import com.yuanno.soulsawakening.ability.api.Ability;

import java.util.List;

public interface IAbilityData {

    void addUnlockedAbility(Ability ability);
    void removeUnlockedAbility(Ability ability);
    List<Ability> getUnlockedAbilities();
    List<Ability> getActiveAbilities();
    <T extends Ability<T>> List<T> clearUnlockedAbilities();
    boolean loadUnlockedAbility(Ability abl);
    <T extends Ability> T getUnlockedAbility(T abl);

}
