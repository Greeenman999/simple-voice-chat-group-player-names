package de.greenman999.svcgroupplayernames;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import me.shedaniel.autoconfig.ConfigManager;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;
import me.shedaniel.autoconfig.gui.DefaultGuiProviders;
import me.shedaniel.autoconfig.gui.DefaultGuiTransformers;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigManager<ModConfig> manager = (ConfigManager<ModConfig>) AutoConfig.getConfigHolder(ModConfig.class);
            GuiRegistry registry = new GuiRegistry();
            DefaultGuiProviders.apply(registry);
            DefaultGuiTransformers.apply(registry);
            ConfigScreenProvider<ModConfig> provider = new ConfigScreenProvider<>(manager, registry, parent);
            return provider.get();
        };
    }
}