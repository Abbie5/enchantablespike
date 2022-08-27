package br.com.brforgers.mods.enchantablespike.blocks

import io.github.lucaargolo.kibe.blocks.miscellaneous.Spikes
import io.github.lucaargolo.kibe.utils.SpikeHelper
import br.com.brforgers.mods.enchantablespike.utils.FakePlayerEntity
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class DiamondSpike(settings: Settings) : BlockWithEntity(settings){
    init {
        defaultState = defaultState.with(Properties.FACING, Direction.UP)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.FACING)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return DiamondSpikeBlockEntity(this, pos, state)
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack
    ) {
        val blockEntity = world.getBlockEntity(pos)
        if (stack.hasEnchantments()) {
            if (blockEntity is DiamondSpikeBlockEntity) {
                blockEntity.addAllEnchantments(stack.enchantments)
            }
        }
    }

    override fun getDroppedStacks(state: BlockState?, builder: LootContext.Builder?): MutableList<ItemStack> {
        val blockEntity = builder?.getNullable(LootContextParameters.BLOCK_ENTITY)
        if (blockEntity is DiamondSpikeBlockEntity) {
            if (blockEntity.hasEnchantments()) {
                val stack = ItemStack(DIAMOND_SPIKES.asItem())
                stack.orCreateNbt
                stack.nbt?.put("Enchantments", blockEntity.getEnchantments())
                return mutableListOf(stack)
            }
        }

        return super.getDroppedStacks(state, builder)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(Properties.FACING, ctx.side)
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if(!world.isClient && entity is LivingEntity) {
            SpikeHelper.setSpike(entity, Spikes.Type.DIAMOND)
            val sword = ItemStack(Items.NETHERITE_SWORD).apply {
                val blockEntity = world.getBlockEntity(pos)
                if (blockEntity is DiamondSpikeBlockEntity) {
                    if (blockEntity.hasEnchantments()) {
                        EnchantmentHelper.fromNbt(blockEntity.getEnchantments())
                            .forEach { (enchantment, level) -> this.addEnchantment(enchantment, level) }
                    }
                }
            }
            val fakePlayer = FakePlayerEntity(world, sword)
            if (EnchantmentHelper.getFireAspect(fakePlayer) > 0 && !entity.isOnFire()) {
                entity.setOnFireFor(1)
            }
            entity.damage(DamageSource.player(fakePlayer), Spikes.Type.DIAMOND.damage + EnchantmentHelper.getAttackDamage(sword, entity.group))
            SpikeHelper.setSpike(entity, null)
        }
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun hasSidedTransparency(state: BlockState?): Boolean {
        return true
    }

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, ePos: ShapeContext) = getShape(state[Properties.FACING])

    override fun getCollisionShape(state: BlockState, view: BlockView, pos: BlockPos, ePos: ShapeContext) = getShape(state[Properties.FACING])

    override fun getCullingShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = EMPTY

    companion object {
        private val EMPTY = createCuboidShape(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        private val SHAPES = mutableMapOf<Direction, VoxelShape>()

        init {
            Direction.values().forEach {
                SHAPES[it] = when(it) {
                    Direction.UP -> createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)
                    Direction.DOWN -> createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0)
                    Direction.EAST -> createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0)
                    Direction.WEST -> createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0)
                    Direction.SOUTH -> createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0)
                    Direction.NORTH -> createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0)
                }
            }
        }

        private fun getShape(facing: Direction) = SHAPES[facing] ?: EMPTY
    }
}