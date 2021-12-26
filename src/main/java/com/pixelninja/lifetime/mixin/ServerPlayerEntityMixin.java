package com.pixelninja.lifetime.mixin;

import com.mojang.authlib.GameProfile;
import com.pixelninja.lifetime.access.Timeout;
import com.pixelninja.lifetime.component.TimeComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements Timeout {

    boolean timedOut = false;

    @Shadow public abstract void setGameMode(@Nullable NbtCompound nbt);

    @Shadow public abstract boolean changeGameMode(GameMode gameMode);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "getPlayerListName", at = @At("HEAD"), cancellable = true)
    public void setName(CallbackInfoReturnable<Text> cir) {
            cir.setReturnValue(getDisplayName());
    }

   /* @Inject(method = "tick", at = @At("HEAD"))
    private void timerRunout(CallbackInfo ci) {
        if (TimeComponent.KEY.get(this).getTime() <= 0 && !timedOut){
            this.changeGameMode(GameMode.SPECTATOR);
            timedOut = true;
        }
    } */

    @Override
    public boolean isTimedOut() {
        return timedOut;
    }

    @Override
    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }
}


