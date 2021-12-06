package com.pixelninja.lifetime;

import com.pixelninja.lifetime.component.TimeComponent;
import com.pixelninja.lifetime.component.TimeComponentImpl;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Lifetime implements ModInitializer, EntityComponentInitializer {

    public static Identifier identifier(String path) {
        return new Identifier("lifetime", path);
    }

    TimeEssenceBottle TIME_ESSENCE_BOTTLE = new TimeEssenceBottle(new FabricItemSettings().maxCount(1).group(ItemGroup.BREWING), 1200);

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, identifier("time_essence_bottle"), TIME_ESSENCE_BOTTLE);
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> LifetimeCommand.register(dispatcher)));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TimeComponent.KEY, TimeComponentImpl::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
