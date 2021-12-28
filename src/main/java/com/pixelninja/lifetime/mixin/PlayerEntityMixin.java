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
import net.minecraft.text.Texts;
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
             Text playerName = new LiteralText(this.getName().asString()).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
             Text dash = new LiteralText(" - ").setStyle(Style.EMPTY.withColor(Formatting.BLACK));
             //(timeLeftString).formatted(color).append(dash).append(playerName)
             Text message = new LiteralText("").append(TimeComponent.KEY.get(this).getTimeAsText()).append(dash).append(playerName);
             this.sendMessage(message, true);
        }
        if (TimeComponent.KEY.get(this).getTime() <= 0 && !timedOut) {
            sendSystemMessage(new LiteralText(this.getName().asString()).append(" ran out of time.").formatted(Formatting.DARK_RED), this.getUuid());
            Objects.requireNonNull(Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayer(this.getUuid())).changeGameMode(GameMode.SPECTATOR);
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
