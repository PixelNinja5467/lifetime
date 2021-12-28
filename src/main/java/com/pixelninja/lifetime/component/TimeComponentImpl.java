package com.pixelninja.lifetime.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TimeComponentImpl implements TimeComponent{

    int time;
    int ticks;
    PlayerEntity entity;
    boolean isCountingDown;
    boolean isPaused = true;

    public TimeComponentImpl(PlayerEntity entity) {
        this.entity = entity;
        this.time = 1;
    }

    @Override
    public int getTime() {
        return this.time;
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
        tag.putInt("Time", getTime());
        tag.putInt("Ticks", ticks);
        tag.putBoolean("Paused", this.isPaused);
    }

    @Override
    public void tick() {
        if (!isPaused){
            ticks++;
        }
        if (getTime() == 0) {
            isCountingDown = false;
        } else if (isPaused) {
            isCountingDown = false;
        } else if (time > 0) {
            isCountingDown = true;
        }
        if (ticks % 20 == 0 && isCountingDown) {
            time--;
        }
    }

    @Override
    public Text getTimeAsText() {
        int seconds = getTime() % 60;
        int remainingMinutes = getTime() / 60;
        int minutes = remainingMinutes % 60;
        int hours = remainingMinutes / 60;

        String timeLeft = hours + ":" + minutes + ":" + seconds;


        Formatting color;
        if (remainingMinutes < 15) {
            color = Formatting.DARK_RED;
        } else if (remainingMinutes > 15 && hours < 2) {
            color = Formatting.GOLD;
        } else {
            color = Formatting.DARK_GREEN;
        }

        return new LiteralText(timeLeft).formatted(color);
    }


}
