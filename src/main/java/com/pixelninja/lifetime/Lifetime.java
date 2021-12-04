package com.pixelninja.lifetime;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Lifetime implements ModInitializer {

    public String MOD_ID = "lifetime";

    public Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {

    }
}
