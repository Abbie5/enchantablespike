package br.com.brforgers.mods.enchantablespike.blocks

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack

class DiamondSpikeBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun isEnchantable(stack: ItemStack): Boolean {
        return stack.count == 1
    }

    override fun getEnchantability(): Int {
        return 15
    }
}