package com.pixelninja.lifetime.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class TimeComponentImpl implements TimeComponent{

    private int time = 1;
    private int ticks;
    PlayerEntity entity;
    boolean isCountingDown;
    boolean isPaused = true;

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
    public void subtractTime(int time) {
        this.time -= time;
    }

    @Override
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        this.time = tag.getInt("Time");
        this.isPaused = tag.getBoolean("Paused");
        this.ticks = tag.getInt("Ticks");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Time", time);
        tag.putInt("Ticks", ticks);
        tag.putBoolean("Paused", this.isPaused);
    }

    @Override
    public void tick() {
        if (!isPaused){
            ticks++;
        }
        if (time == 0) {
            isCountingDown = false;
        } else if (time > 0 && !isPaused) {
            isCountingDown = true;
        }
        if (ticks % 20 == 0 && isCountingDown) {
            time--;
        }
    }
}
