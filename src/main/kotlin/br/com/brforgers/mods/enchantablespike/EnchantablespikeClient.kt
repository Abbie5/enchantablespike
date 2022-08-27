package br.com.brforgers.mods.enchantablespike

import br.com.brforgers.mods.enchantablespike.blocks.initBlocksClient
import net.fabricmc.api.ClientModInitializer

object EnchantablespikeClient : ClientModInitializer {
    override fun onInitializeClient() {
        initBlocksClient()
    }
}