package me.sootysplash.vai.mixin;

import me.sootysplash.vai.Main;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {

    @Inject(method = "itemCount", at = @At("RETURN"))
    private void onItemCount(Font font, ItemStack itemStack, int x, int y, String countText, CallbackInfo ci) {
        Main.doDraw((GuiGraphicsExtractor) (Object) this, font, itemStack, x, y, countText);
    }

}
