package com.pixelninja.lifetime.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class TimeComponentImpl implements TimeComponent{

    private int time;
    private int ticks;
    PlayerEntity entity;

    public TimeComponentImpl(PlayerEntity entity) {
        this.entity = entity;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void addTime(int time) {
        this.time += time;
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        time = tag.getInt("Time");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Time", time);
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % 20 == 0) {
            time--;
        }
    }
}
