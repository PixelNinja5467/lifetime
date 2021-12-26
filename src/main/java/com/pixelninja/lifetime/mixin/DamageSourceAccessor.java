package com.pixelninja.lifetime.mixin;

import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(DamageSource.class)
public interface DamageSourceAccessor {

    @Invoker("<init>")
    static DamageSource createDamageSource(String name) {
        throw new RuntimeException("DamageSource exception ¯\\_(ツ)_/¯");
    }

    @Accessor
    boolean getBypassesArmor();

    @Accessor("bypassesArmor")
    void setBypassesArmor(boolean bypass);

}
