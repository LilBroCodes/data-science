package org.lilbrocodes.data_science.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lilbrocodes.data_science.common.recipes.analysis.MaterialCollector;
import org.lilbrocodes.data_science.common.recipes.analysis.MaterialCollector.CollectedMaterials;

import java.util.concurrent.CompletableFuture;

public class RecipeTreeCommand extends CommandUtil {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("recipe_tree")
                        .then(CommandManager.argument("max_depth", IntegerArgumentType.integer(1))
                                .then(CommandManager.argument("recipe", StringArgumentType.greedyString())
                                        .suggests(this::getRecipes)
                                        .executes(ctx -> execute(ctx, StringArgumentType.getString(ctx, "recipe"), IntegerArgumentType.getInteger(ctx, "max_depth")))
                                )
                        )
        );
    }

    private int execute(CommandContext<ServerCommandSource> ctx, String recipeId, Integer maxDepth) {
        ServerCommandSource source = ctx.getSource();
        Identifier id = Identifier.tryParse(recipeId);

        if (id == null) {
            source.sendError(Text.literal("§cInvalid recipe ID: " + recipeId));
            return 0;
        }

        MinecraftServer server = source.getServer();
        Recipe<?> recipe = server.getRecipeManager().get(id).orElse(null);
        if (recipe == null) {
            source.sendError(Text.literal("§cRecipe not found: " + recipeId));
            return 0;
        }

        feedback(ctx, Text.literal("§aFound recipe: §f" + recipe.getId()));
        feedback(ctx, Text.literal("§aCollecting materials..."));
        MaterialCollector collector = new MaterialCollector(server.getRegistryManager(), recipe);
        CollectedMaterials materials = collector.developNoCombines(maxDepth);

        feedback(ctx, Text.literal("§6Ingredients:"));
        for (ItemStack stack : materials.ingredients()) {
            feedback(ctx, Text.literal(" - §f" + stack.getCount() + "x " + stack.getName().getString()));
        }

        if (!materials.catalysts().isEmpty()) {
            feedback(ctx, Text.literal("§6Catalysts:"));
            for (Ingredient ing : materials.catalysts()) {
                StringBuilder names = new StringBuilder();
                for (ItemStack stack : ing.getMatchingStacks()) {
                    if (!names.isEmpty()) names.append(", ");
                    names.append(stack.getName().getString());
                }
                feedback(ctx, Text.literal(" - §f" + names));
            }
        }

        if (!materials.totalResults().isEmpty()) {
            feedback(ctx, Text.literal("§6Results:"));
            for (var output : materials.totalResults()) {
                feedback(ctx, Text.literal(" - §f" + output.stack().getName().getString() + " §7(Chance: " + (int)(output.chance() * 100) + "%)"));
            }
        }

        feedback(ctx, Text.literal("§6Total cook time: §f" + materials.totalCookTime() + " ticks"));

        return 1;
    }

    public CompletableFuture<Suggestions> getRecipes(CommandContext<ServerCommandSource> ctx, SuggestionsBuilder builder) {
        var server = ctx.getSource().getServer();

        for (Recipe<?> recipe : server.getRecipeManager().values()) {
            builder.suggest(recipe.getId().toString());
        }

        return builder.buildFuture();
    }
}
