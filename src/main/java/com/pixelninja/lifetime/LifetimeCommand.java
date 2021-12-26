package com.pixelninja.lifetime;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelninja.lifetime.access.Timeout;
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
import java.util.Objects;

public class LifetimeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("lifetime")
                .then(CommandManager.argument("players", EntityArgumentType.players())
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer())
                                    .executes(context ->
                                            executeAddTime(context.getSource(), EntityArgumentType.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "seconds")))))
                        .then(CommandManager.literal("subtract")
                                .then(CommandManager.argument("seconds", IntegerArgumentType.integer())
                                        .executes(context ->
                                                executeSubtractTime(context.getSource(), EntityArgumentType.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "seconds")))))
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
                )
        .then(CommandManager.literal("share")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("seconds", IntegerArgumentType.integer())
                                .executes(context -> shareTime(context.getSource(), EntityArgumentType.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "seconds"))))))
        );
    }

    private static int executeAddTime(ServerCommandSource source, Collection<ServerPlayerEntity> entities, int time) throws CommandSyntaxException {
        for (PlayerEntity entity : entities) {
            TimeComponent.KEY.get(entity).addTime(time);
            ((Timeout)entity).setTimedOut(false);
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
            ((Timeout)entity).setTimedOut(false);
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

    private static int getTime(ServerCommandSource source, Collection<ServerPlayerEntity> entities) {
        for(PlayerEntity entity : entities) {
            int seconds = TimeComponent.KEY.get(entity).getTime();
            Text text = new LiteralText(entity.getName().asString()).append(" has ").append(String.valueOf(seconds)).append(" seconds left.");
            source.sendFeedback(text, false);
            return seconds;
        }
        return 1;
    }

    private static int shareTime(ServerCommandSource source, PlayerEntity entity, int seconds) {
        PlayerEntity entity1 = (PlayerEntity) Objects.requireNonNull(source.getEntity());
        if (TimeComponent.KEY.get(entity1).getTime() > seconds) {
            TimeComponent.KEY.get(entity1).subtractTime(seconds);
            TimeComponent.KEY.get(entity).addTime(seconds);
            Text text =
                    new LiteralText(entity1.getName().asString()).append(" shared ").append(String.valueOf(seconds)).append(" seconds with ").append(entity.getName());
            source.sendFeedback(text, true);
            return 1;
        } else {
            source.sendFeedback(new LiteralText("You don't have enough time to share."), false);
            return 0;
        }
    }

}
