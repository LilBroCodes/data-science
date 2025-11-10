package org.lilbrocodes.data_science.data.data;

import dan200.computercraft.shared.ModRegistry;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.advancement.AdvancementProvider;
import net.minecraft.data.server.advancement.AdvancementTabGenerator;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lilbrocodes.data_science.common.DataScience;
import org.lilbrocodes.data_science.common.registry.ModAdvancements;
import org.lilbrocodes.data_science.common.registry.ModBlocks;
import org.lilbrocodes.data_science.common.registry.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DataScienceAdvancementProvider implements AdvancementTabGenerator {
    @Override
    public void accept(RegistryWrapper.WrapperLookup lookup, Consumer<Advancement> exporter) {
        Advancement root = buildAdv(
                Advancement.Builder.create()
                        .display(
                                ModRegistry.Blocks.COMPUTER_ADVANCED.get(),
                                Text.translatable("advancements.data_science.root.title"),
                                Text.translatable("advancements.data_science.root.description"),
                                DataScience.identify("textures/gui/advancements_background.png"),
                                AdvancementFrame.TASK,
                                false, false, false
                        )
                        .criterion("got_iron", InventoryChangedCriterion.Conditions.items(Items.IRON_INGOT)),
                exporter,
                DataScience.identify("root")
        );

        Advancement grinding = buildAdv(
                Advancement.Builder.create()
                        .parent(root)
                        .display(
                                Blocks.GRINDSTONE,
                                Text.translatable("advancements.data_science.grinding.title"),
                                Text.translatable("advancements.data_science.grinding.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("code", new ImpossibleCriterion.Conditions()),
                exporter,
                ModAdvancements.GRINDING_AWAY
        );

        buildAdv(
                Advancement.Builder.create()
                        .parent(grinding)
                        .display(
                                ModItems.COPPER_DUST,
                                Text.translatable("advancements.data_science.crumble.title"),
                                Text.translatable("advancements.data_science.crumble.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("code", new ImpossibleCriterion.Conditions()),
                exporter,
                ModAdvancements.DIMINISHING_RETURNS
        );

        Advancement shattered = buildAdv(
                Advancement.Builder.create()
                        .parent(grinding)
                        .display(
                                ModItems.SHATTERED_PRISMARINE_CRYSTAL,
                                Text.translatable("advancements.data_science.shatter.title"),
                                Text.translatable("advancements.data_science.shatter.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("code", new ImpossibleCriterion.Conditions()),
                exporter,
                ModAdvancements.SHATTERED
        );

        buildAdv(
                Advancement.Builder.create()
                        .parent(shattered)
                        .display(
                                ModItems.ROUGH_PRISMARINE_CRYSTAL,
                                Text.translatable("advancements.data_science.unlucky.title"),
                                Text.translatable("advancements.data_science.unlucky.description"),
                                null,
                                AdvancementFrame.CHALLENGE,
                                true, true, true
                        )
                        .criterion("code", new ImpossibleCriterion.Conditions()),
                exporter,
                ModAdvancements.UNLUCKY
        );

        buildAdv(
                Advancement.Builder.create()
                        .parent(grinding)
                        .display(
                                ModItems.POLISHED_PRISMARINE_CRYSTAL,
                                Text.translatable("advancements.data_science.polished_crystal.title"),
                                Text.translatable("advancements.data_science.polished_crystal.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("polished", InventoryChangedCriterion.Conditions.items(ModItems.POLISHED_PRISMARINE_CRYSTAL)),
                exporter,
                DataScience.identify("polished_crystal")
        );

        Advancement basicCircuit = buildAdv(
                Advancement.Builder.create()
                        .parent(root)
                        .display(
                                ModItems.BASIC_CIRCUIT,
                                Text.translatable("advancements.data_science.basic_circuit.title"),
                                Text.translatable("advancements.data_science.basic_circuit.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("crafted_basic", InventoryChangedCriterion.Conditions.items(ModItems.BASIC_CIRCUIT)),
                exporter,
                DataScience.identify("basic_circuit")
        );

        Advancement advancedCircuit = buildAdv(
                Advancement.Builder.create()
                        .parent(basicCircuit)
                        .display(
                                ModItems.ADVANCED_CIRCUIT,
                                Text.translatable("advancements.data_science.advanced_circuit.title"),
                                Text.translatable("advancements.data_science.advanced_circuit.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("crafted_advanced", InventoryChangedCriterion.Conditions.items(ModItems.ADVANCED_CIRCUIT)),
                exporter,
                DataScience.identify("advanced_circuit")
        );

        buildAdv(
                Advancement.Builder.create()
                        .parent(advancedCircuit)
                        .display(
                                ModItems.ENVIRONMENTAL_SENSOR,
                                Text.translatable("advancements.data_science.environmental_sensor.title"),
                                Text.translatable("advancements.data_science.environmental_sensor.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("crafted_sensor", InventoryChangedCriterion.Conditions.items(ModItems.ENVIRONMENTAL_SENSOR)),
                exporter,
                DataScience.identify("environmental_sensor")
        );

        buildAdv(
                Advancement.Builder.create()
                        .parent(advancedCircuit)
                        .display(
                                ModItems.GRAPHER,
                                Text.translatable("advancements.data_science.grapher.title"),
                                Text.translatable("advancements.data_science.grapher.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("crafted_grapher", InventoryChangedCriterion.Conditions.items(ModItems.GRAPHER)),
                exporter,
                DataScience.identify("grapher")
        );

        Advancement redAlloyCraft = buildAdv(
                Advancement.Builder.create()
                        .parent(root)
                        .display(
                                ModItems.RED_ALLOY_DUST,
                                Text.translatable("advancements.data_science.red_alloy_craft.title"),
                                Text.translatable("advancements.data_science.red_alloy_craft.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("crafted_alloy", InventoryChangedCriterion.Conditions.items(ModItems.RED_ALLOY_DUST)),
                exporter,
                DataScience.identify("red_alloy_craft")
        );

        buildAdv(
                Advancement.Builder.create()
                        .parent(redAlloyCraft)
                        .display(
                                ModItems.RED_ALLOY_INGOT,
                                Text.translatable("advancements.data_science.red_alloy_smelt.title"),
                                Text.translatable("advancements.data_science.red_alloy_smelt.description"),
                                null,
                                AdvancementFrame.TASK,
                                true, true, false
                        )
                        .criterion("smelted_alloy", InventoryChangedCriterion.Conditions.items(ModItems.RED_ALLOY_INGOT)),
                exporter,
                DataScience.identify("red_alloy_smelt")
        );
    }

    private static Advancement buildAdv(Advancement.Builder builder, Consumer<Advancement> exporter, Identifier id) {
        Advancement adv = builder.build(id);
        exporter.accept(adv);
        return adv;
    }

    public static AdvancementProvider create(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        return new AdvancementProvider(
                output,
                registryLookupFuture,
                List.of(
                        new DataScienceAdvancementProvider()
                )
        );
    }
}
