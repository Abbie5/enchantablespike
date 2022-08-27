package br.com.brforgers.mods.enchantablespike.blocks

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.BlockPos

class DiamondSpikeBlockEntity(block: DiamondSpike, pos: BlockPos, state: BlockState): BlockEntity(getEntityType(block), pos, state){
    private val enchantments = NbtList();

    override fun writeNbt(tag: NbtCompound) {
        if(enchantments.isNotEmpty()){
            tag.put("Enchantments", enchantments)
        }
        super.writeNbt(tag)
    }

    override fun readNbt(tag: NbtCompound) {
        if(tag.contains("Enchantments")){
            enchantments.addAll(tag.getList("Enchantments", NbtElement.COMPOUND_TYPE.toInt()))
        }
        super.readNbt(tag)
    }

    fun addAllEnchantments(enchantments: NbtList){
        this.enchantments.addAll(enchantments)
    }

    fun getEnchantments(): NbtList{
        return enchantments
    }

    fun hasEnchantments(): Boolean{
        return enchantments.isNotEmpty()
    }
}