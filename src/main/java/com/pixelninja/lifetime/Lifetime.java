package com.pixelninja.lifetime;

import com.pixelninja.lifetime.component.TimeComponent;
import com.pixelninja.lifetime.component.TimeComponentImpl;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.util.Identifier;

public class Lifetime implements ModInitializer, EntityComponentInitializer {

    public static Identifier identifier(String path) {
        return new Identifier("lifetime", path);
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> LifetimeCommand.register(dispatcher)));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TimeComponent.KEY, TimeComponentImpl::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
