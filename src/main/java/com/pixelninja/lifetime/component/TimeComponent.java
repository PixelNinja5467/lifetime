package com.pixelninja.lifetime.component;

import com.pixelninja.lifetime.Lifetime;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;

public interface TimeComponent extends CommonTickingComponent {
    ComponentKey<TimeComponent> KEY = ComponentRegistry.getOrCreate(Lifetime.identifier("time"), TimeComponent.class);

    int getTime();
    void setTime(int time);
    void addTime(int time);
    void subtractTime(int time);
    void setPaused(boolean paused);

}
