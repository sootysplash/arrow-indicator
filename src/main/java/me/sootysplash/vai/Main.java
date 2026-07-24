package me.sootysplash.vai;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main implements ModInitializer {

    public static final Minecraft mc = Minecraft.getInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger("VanillaArrowIndicator");

    @Override
    public void onInitialize() {
        LOGGER.info(Main.LOGGER.getName() + " | Sootysplash was here!");
    }

    private record ProjItemData(ProjectileWeaponItem pwi, AtomicInteger ammoCount, AtomicReference<ItemStack> firstAmmo) {}

    private static final List<ProjItemData> projItems = List.of(
            new ProjItemData((ProjectileWeaponItem) Items.BOW, new AtomicInteger(), new AtomicReference<>()),
            new ProjItemData((ProjectileWeaponItem) Items.CROSSBOW, new AtomicInteger(), new AtomicReference<>())
    );

    public static void onTick() {
        LocalPlayer lp = mc.player;
        if (lp == null) {
            return;
        }
        for (ProjItemData projItemData : projItems) {
            projItemData.ammoCount().set(0);
            projItemData.firstAmmo().set(mc.player.getProjectile(new ItemStack(projItemData.pwi())));
        }
        Inventory inventory = lp.getInventory();
        for (ItemStack is : inventory) {
            for (ProjItemData projItemData : projItems) {
                if (!projItemData.pwi().getAllSupportedProjectiles().test(is)) {
                    continue;
                }
                projItemData.ammoCount().addAndGet(is.count());
            }
        }

        if (Config.getInstance().countFireworks) {
            for (InteractionHand ih : InteractionHand.values()) {
                ItemStack is = lp.getItemInHand(ih);
                for (ProjItemData projItemData : projItems) {
                    if (projItemData.pwi().getAllSupportedProjectiles().test(is)) {
                        continue;
                    }
                    if (projItemData.pwi().getSupportedHeldProjectiles().test(is)) {
                        projItemData.ammoCount().addAndGet(is.count());
                    }
                }
            }
        }
    }

    public static void doDraw(GuiGraphicsExtractor gge, Font font, ItemStack itemStack, int x, int y, String countText) {
        ProjItemData projItemDataCast = null;
        for (ProjItemData projItemData : projItems) {
            if (projItemData.pwi() == itemStack.getItem()) {
                projItemDataCast = projItemData;
            }
        }
        if (projItemDataCast == null) {
            return;
        }
        int ammoC = projItemDataCast.ammoCount().get();
        if (ammoC == 0) {
            return;
        }
        boolean renderingExisting = itemStack.getCount() != 1 || countText != null;
        if (renderingExisting) {
            return;
        }
        Config config = Config.getInstance();
        if (ammoC > config.onlyShowAmmoBelow) {
            return;
        }
        int drawColor = config.ammoColor;
        PotionContents tip = projItemDataCast.firstAmmo().get().get(DataComponents.POTION_CONTENTS);
        if (tip != null && config.tippedArrowColors) {
            drawColor = tip.getColor();
        }
        String text = String.valueOf(Math.min(ammoC, Config.getInstance().maxAmmoStack));
        gge.text(font, text, x + 19 - 2 - font.width(text), y + 6 + 3, drawColor, true);
    }
}
