package com.pixelninja.lifetime;

import com.pixelninja.lifetime.component.TimeComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class TimeEssenceBottle extends Item {

    int time;

    public TimeEssenceBottle(Settings settings, int time) {
        super(settings);
        this.time = time;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        TimeComponent.KEY.get(user).addTime(time);
        stack.decrement(1);
        ((PlayerEntity)user).getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
        return super.finishUsing(stack, world, user);
    }
}
