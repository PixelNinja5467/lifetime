package com.pixelninja.lifetime.mixin;

import com.mojang.authlib.GameProfile;
import com.pixelninja.lifetime.component.TimeComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "getPlayerListName", at = @At("HEAD"), cancellable = true)
    public void setName(CallbackInfoReturnable<Text> cir) {
            int time = TimeComponent.KEY.get(this).getTime();
            Text text = Text.of(getDisplayName().asString() + time);
            cir.setReturnValue(text);
    }

}


