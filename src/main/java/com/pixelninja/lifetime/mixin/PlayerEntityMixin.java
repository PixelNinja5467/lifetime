package com.pixelninja.lifetime.mixin;

import com.pixelninja.lifetime.access.Timeout;
import com.pixelninja.lifetime.component.TimeComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Timeout {

    boolean timedOut = false;
    int ticks = 0;

    @Shadow public abstract Text getName();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    public void displayTime(CallbackInfoReturnable<Text> cir) {
        cir.setReturnValue(TimeComponent.KEY.get(this).getTimeAsText());
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void timerRunout(CallbackInfo ci) {
        ticks++;
         if (ticks % 20 == 0) {
            sendMessage(TimeComponent.KEY.get(this).getTimeAsText(), false);
        }
        if (TimeComponent.KEY.get(this).getTime() <= 0 && !timedOut) {
            /*DamageSource damageSource = DamageSourceAccessor.createDamageSource("timeout");
            ((DamageSourceAccessor)damageSource).setBypassesArmor(true);
            this.damage(DamageSourceAccessor.createDamageSource("timeout"), 20.0f); */
            Objects.requireNonNull(this.getServer()).sendSystemMessage(new LiteralText(this.getName().asString()).append(" ran out of time."), this.getUuid());
            Objects.requireNonNull(this.getServer().getPlayerManager().getPlayer(this.getUuid())).changeGameMode(GameMode.SPECTATOR);
            timedOut = true;
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void saveData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("TimedOut", timedOut);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    public void loadData(NbtCompound nbt, CallbackInfo ci) {
        timedOut = nbt.getBoolean("TimedOut");
    }

    @Override
    public boolean isTimedOut() {
        return timedOut;
    }

    @Override
    public void setTimedOut(boolean timeOut) {
        this.timedOut = timeOut;
    }


}
