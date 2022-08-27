package br.com.brforgers.mods.enchantablespike.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(at=@At("TAIL"),method="deserialize", cancellable = true)
    private static void init(
            Identifier id, JsonObject json, CallbackInfoReturnable<Recipe<?>> cir
    ) {
        if (id.equals(new Identifier("kibe:diamond_spikes"))) {
            json.getAsJsonObject("result").addProperty("item", "enchantablespike:diamond_spikes");
            String string = JsonHelper.getString(json, "type");
            cir.setReturnValue(Registry.RECIPE_SERIALIZER.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported recipe type '" + string + "'")).read(id, json));
        }
    }
}

