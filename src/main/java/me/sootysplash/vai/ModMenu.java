package me.sootysplash.vai;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            Config config = Config.getInstance();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.nullToEmpty("Config"))
                    .setSavingRunnable(config::save);

            ConfigEntryBuilder cfgent = builder.entryBuilder();
            ConfigCategory behavior = builder.getOrCreateCategory(Component.nullToEmpty("Behavior"));


            behavior.addEntry(cfgent.startBooleanToggle(Component.nullToEmpty("Enabled"), config.enabled)
                    .setDefaultValue(true)
                    .setTooltip(Component.nullToEmpty("Draw ammo indicators?"))
                    .setSaveConsumer(newValue -> config.enabled = newValue)
                    .build());


            behavior.addEntry(cfgent.startBooleanToggle(Component.nullToEmpty("Use Tipped Arrow Colors"), config.tippedArrowColors)
                    .setDefaultValue(false)
                    .setTooltip(Component.nullToEmpty("Color ammo indicators based on the tipped arrow effect?"))
                    .setSaveConsumer(newValue -> config.tippedArrowColors = newValue)
                    .build());


            behavior.addEntry(cfgent.startBooleanToggle(Component.nullToEmpty("Count Crossbow Fireworks"), config.countFireworks)
                    .setDefaultValue(true)
                    .setTooltip(Component.nullToEmpty("Count usable fireworks for crossbows??"))
                    .setSaveConsumer(newValue -> config.countFireworks = newValue)
                    .build());


            behavior.addEntry(cfgent.startAlphaColorField(Component.nullToEmpty("Default Ammo Color"), config.ammoColor)
                    .setDefaultValue(Color.YELLOW.getRGB())
                    .setTooltip(Component.nullToEmpty("The color for plain/untipped arrows"))
                    .setSaveConsumer(newValue -> config.ammoColor = newValue)
                    .build());


            behavior.addEntry(cfgent.startIntField(Component.nullToEmpty("Max Ammo Stack"), config.maxAmmoStack)
                    .setDefaultValue(99)
                    .setMin(1)
                    .setTooltip(Component.nullToEmpty("Highest count of ammo to display"))
                    .setSaveConsumer(newValue -> config.maxAmmoStack = newValue)
                    .build());


            behavior.addEntry(cfgent.startIntField(Component.nullToEmpty("Only Show Ammo Below"), config.onlyShowAmmoBelow)
                    .setDefaultValue(9999)
                    .setMin(1)
                    .setTooltip(Component.nullToEmpty("Only draw ammo counter when total ammo is below this value"))
                    .setSaveConsumer(newValue -> config.onlyShowAmmoBelow = newValue)
                    .build());


            return builder.build();
        };
    }
}
