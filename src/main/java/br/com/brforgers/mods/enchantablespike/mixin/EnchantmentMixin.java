package br.com.brforgers.mods.enchantablespike.mixin;

import br.com.brforgers.mods.enchantablespike.blocks.DiamondSpikeBlockItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Shadow @Final public EnchantmentTarget type;

    @Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
    public void isAcceptableItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.getItem() instanceof DiamondSpikeBlockItem) {
            cir.setReturnValue(this.type == EnchantmentTarget.WEAPON);
        }
    }
}
