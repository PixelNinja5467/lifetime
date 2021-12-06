package com.pixelninja.lifetime.mixin;

import com.pixelninja.lifetime.component.TimeComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract Text getName();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    public void displayTime(CallbackInfoReturnable<Text> cir) {
        int time = TimeComponent.KEY.get(this).getTime();
        Formatting color;
        if (time < 60) {
            color = Formatting.DARK_RED;
        } else if (time > 60 && time < 600) {
            color = Formatting.GOLD;
        } else {
            color = Formatting.DARK_GREEN;
        }
        Text timeLeft = new LiteralText(time + " Seconds Left").setStyle(Style.EMPTY.withColor(color));
        Text dash = new LiteralText(" - ").setStyle(Style.EMPTY.withColor(Formatting.BLACK));
        Text text = new LiteralText(getName().asString()).append(dash).append(timeLeft);
        cir.setReturnValue(text);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void timerRunout(CallbackInfo ci) {
        if (TimeComponent.KEY.get(this).getTime() <= 0) {
            this.kill();
        }
    }

}
