package me.sootysplash.vai.mixin;

import me.sootysplash.vai.Main;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsExtractorMixin {

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("RETURN"))
    private void onItemCountOld(Font font, ItemStack itemStack, int x, int y, String countText, CallbackInfo ci) {
        GuiGraphics gg = (GuiGraphics) (Object) this;
        gg.pose().pushPose();
        gg.pose().translate(0.0F, 0.0F, 200.0F);
        Main.doDraw(gg, font, itemStack, x, y, countText);
        gg.pose().popPose();
    }

}
