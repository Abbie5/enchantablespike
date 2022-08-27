package br.com.brforgers.mods.enchantablespike

import br.com.brforgers.mods.enchantablespike.blocks.DIAMOND_SPIKES
import br.com.brforgers.mods.enchantablespike.blocks.initBlocks
import br.com.brforgers.mods.enchantablespike.utils.initTooltip
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.fabricmc.fabric.api.loot.v2.LootTableEvents.Replace
import net.fabricmc.fabric.api.loot.v2.LootTableSource
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootManager
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


object Enchantablespike : ModInitializer {
    const val MOD_ID = "enchantablespike"
    var logger: Logger = LogManager.getLogger("EnchantableSpike")

    override fun onInitialize(){
        logger.info("EnchantableSpike!")
        initTooltip()
        initBlocks()
        LootTableEvents.REPLACE.register(Replace { resourceManager: ResourceManager?, lootManager: LootManager?, id: Identifier?, original: LootTable, source: LootTableSource ->
            if (source.isBuiltin && io.github.lucaargolo.kibe.blocks.DIAMOND_SPIKES.lootTableId.equals(id)) {
                LootTable.builder().pool(LootPool.builder().with(
                    ItemEntry.builder(br.com.brforgers.mods.enchantablespike.blocks.DIAMOND_SPIKES.asItem()))).build()
            } else {
                original
            }
        })
        FabricItemGroupBuilder.create(
            Identifier(MOD_ID, "tab")
        )
            .icon { ItemStack(DIAMOND_SPIKES) }
            .appendItems { stacks: MutableList<ItemStack?> ->
                stacks.add(ItemStack(DIAMOND_SPIKES))
            }
            .build()
    }
}
