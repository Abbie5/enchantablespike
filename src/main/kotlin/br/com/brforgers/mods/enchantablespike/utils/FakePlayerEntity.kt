package br.com.brforgers.mods.enchantablespike.utils

import com.mojang.authlib.GameProfile
import io.github.lucaargolo.kibe.FAKE_PLAYER_UUID
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FakePlayerEntity(world: World, var item: ItemStack = ItemStack.EMPTY): PlayerEntity(world, BlockPos.ORIGIN, 0f, GameProfile(FAKE_PLAYER_UUID, "fake")) {

    override fun isSpectator() = true
    override fun isCreative() = true

    override fun damage(source: DamageSource?, amount: Float) = false

    override fun getEquippedStack(slot: EquipmentSlot): ItemStack = if (slot == EquipmentSlot.MAINHAND) item else ItemStack.EMPTY
}