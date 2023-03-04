package br.com.brforgers.mods.enchantablespike.utils

import br.com.brforgers.mods.enchantablespike.blocks.DIAMOND_SPIKES
import br.com.brforgers.mods.enchantablespike.blocks.getBlockId
import net.minecraft.block.Block
import net.minecraft.item.ItemConvertible
import net.minecraft.text.Text

import net.minecraft.util.Identifier

val tooltipRegistry = mutableMapOf<ItemConvertible, List<Text>>()

fun initTooltip() {
    registerTooltip(DIAMOND_SPIKES, 2)
    registerTooltip(io.github.lucaargolo.kibe.blocks.DIAMOND_SPIKES, 2, "kibe")
}

fun registerTooltip(item: ItemConvertible, modId: String = "enchantablespike") {
    val id = if (modId == "enchantablespike"){
        getIdentifier(item)
    } else {
        getKibeIdentifier(item)
    }
    tooltipRegistry[item] = arrayListOf(Text.translatable("tooltip.${modId}.lore.${id!!.path}"))
}

fun registerTooltip(item: ItemConvertible, number: Int, modId: String = "enchantablespike") {
    val id = if (modId == "enchantablespike"){
        getIdentifier(item)
    } else {
        getKibeIdentifier(item)
    }
    val list = mutableListOf<Text>()
    (1..number).forEach {
        list.add(Text.translatable("tooltip.${modId}.lore.${id!!.path}.${it}"))
    }
    tooltipRegistry[item] = list
}

private fun getIdentifier(item: ItemConvertible): Identifier? {
    return getBlockId(item as Block)
}

private fun getKibeIdentifier(item: ItemConvertible): Identifier? {
    return io.github.lucaargolo.kibe.blocks.getBlockId(item as Block)
}