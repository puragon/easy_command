package com.example.easy_command;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EasyCommandMod.MODID)
public class EasyCommandMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "easy_command";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> "1.0",
            s -> s.equals("1.0"),
            s -> s.equals("1.0")
    );


    public EasyCommandMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        NETWORK.registerMessage(
                0,
                ActionRequestPacker.class,
                ActionRequestPacker::toBytes,
                ActionRequestPacker::new,
                ActionRequestPacker::handle
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Config.load();
        });
    }
}
