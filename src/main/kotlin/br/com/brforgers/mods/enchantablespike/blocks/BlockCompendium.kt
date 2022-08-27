package br.com.brforgers.mods.enchantablespike.blocks

import br.com.brforgers.mods.enchantablespike.Enchantablespike.MOD_ID
import io.github.lucaargolo.kibe.CLIENT
import io.github.lucaargolo.kibe.blocks.*
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.item.BlockItem
import net.minecraft.util.Identifier
import java.util.function.Supplier
import kotlin.reflect.KClass

val blockRegistry = linkedMapOf<Block, BlockInfo<*>>()
fun getBlockId(block: Block) = blockRegistry[block]?.identifier
fun getEntityType(block: Block) = blockRegistry[block]?.entity as BlockEntityType<BlockEntity>

fun <T : BlockEntity> registerWithEntity(identifier: Identifier, block: Block, hasBlockItem: Boolean = true, blockItem: KClass<*>? = null, renderer: Supplier<KClass<*>>? = null, containers: List<ContainerInfo<*>> = listOf(), apiRegistrations: (BlockEntityType<T>) -> Unit = {}): Block {
    val bli = blockItem as? KClass<BlockItem>
    val ent = (block as? BlockEntityProvider)?.let { BlockEntityType.Builder.create({ blockPos, blockState -> block.createBlockEntity(blockPos, blockState) } , block).build(null) as BlockEntityType<T> }
    ent?.let { apiRegistrations(it) }
    val rnd = if(CLIENT) renderer?.let { it.get() as KClass<BlockEntityRenderer<T>> } else null
    val info = BlockInfo(identifier, block, hasBlockItem, bli, ent, rnd, containers)
    blockRegistry[block] = info
    return block
}

val DIAMOND_SPIKES = registerWithEntity<DiamondSpikeBlockEntity>(Identifier(MOD_ID, "diamond_spikes"), DiamondSpike(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK)), blockItem = DiamondSpikeBlockItem::class)

fun initBlocks() {
    blockRegistry.forEach{
        it.value.init()
    }
}

fun initBlocksClient() {
    blockRegistry.forEach{ it.value.initClient() }
}

