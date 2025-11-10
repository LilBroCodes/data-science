package org.lilbrocodes.data_science.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public abstract class CommandUtil {
    public static <T extends Text> void feedback(CommandContext<ServerCommandSource> src, T message) {
        src.getSource().sendFeedback(() -> appendPrefix(message), false);
    }

    public static <T extends Text> void feedback(PlayerEntity player, T message) {
        player.sendMessage(appendPrefix(message), false);
    }

    public static Text appendPrefix(Text text) {
        MutableText prefix = Text.literal("")
                .append(Text.literal("[")
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA)))
                .append(Text.literal("Data Science").setStyle(Style.EMPTY.withColor(0xFFFFFF)))
                .append(Text.literal("] ")
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA)));

        return prefix.append(text);
    }

    public abstract void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment);
}
