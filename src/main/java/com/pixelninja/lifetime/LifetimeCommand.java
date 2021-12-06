package com.pixelninja.lifetime;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelninja.lifetime.component.TimeComponent;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.Collection;

public class LifetimeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("lifetime")
        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("players", EntityArgumentType.players())
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer())
                                    .executes(context ->
                                            executeAddTime(context.getSource(), EntityArgumentType.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "seconds")))))
                        .then(CommandManager.literal("subtract")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer())
                                        .executes(context ->
                                                executeSubtractTime(context.getSource(), EntityArgumentType.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "integer")))))
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer())
                                        .executes(context ->
                                                executeSetTime(context.getSource(), EntityArgumentType.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "seconds")))))
                        .then(CommandManager.literal("pause")
                            .executes(context ->
                                    executePauseTime(context.getSource(), EntityArgumentType.getPlayers(context, "players"))))
                        .then(CommandManager.literal("play")
                            .executes(context ->
                                    executePlayTime(context.getSource(), EntityArgumentType.getPlayers(context, "players"))))
                ));
    }

    private static int executeAddTime(ServerCommandSource source, Collection<ServerPlayerEntity> entities, int time) throws CommandSyntaxException {
        for (PlayerEntity entity : entities) {
            TimeComponent.KEY.get(entity).addTime(time);
            Text text = new LiteralText("Added ").append(String.valueOf(time)).append(" seconds to ").append(entity.getName()).append("'s Lifetime");
            source.sendFeedback(text, true);
        }
        return 1;
    }

    private static int executeSubtractTime(ServerCommandSource source, Collection<ServerPlayerEntity> entities, int time) throws CommandSyntaxException {
        for (PlayerEntity entity : entities) {
            TimeComponent.KEY.get(entity).subtractTime(time);
            Text text = new LiteralText("Subtracted ").append(String.valueOf(time)).append(" seconds from ").append(entity.getName()).append("'s Lifetime");
            source.sendFeedback(text, true);
        }
        return 1;
    }

    private static int executeSetTime(ServerCommandSource source, Collection<ServerPlayerEntity> entities, int time) throws CommandSyntaxException {
        for (PlayerEntity entity : entities) {
            TimeComponent.KEY.get(entity).setTime(time);
            Text text = new LiteralText("Set ").append(entity.getName()).append("'s Lifetime to ").append(String.valueOf(time)).append(" Seconds");
            source.sendFeedback(text, true);
        }
        return 1;
    }

    private static int executePauseTime(ServerCommandSource source, Collection<ServerPlayerEntity> entities) {
        for (PlayerEntity entity : entities) {
            TimeComponent.KEY.get(entity).setPaused(true);
            Text text = new LiteralText("Paused ").append(entity.getName()).append("'s Lifetime");
            source.sendFeedback(text, true);
        }
        return 1;
    }

    private static int executePlayTime(ServerCommandSource source, Collection<ServerPlayerEntity> entities) {
        for (PlayerEntity entity : entities) {
            TimeComponent.KEY.get(entity).setPaused(false);
            Text text = new LiteralText("Continued ").append(entity.getName()).append("'s Lifetime");
            source.sendFeedback(text, true);
        }
        return 1;
    }

}
