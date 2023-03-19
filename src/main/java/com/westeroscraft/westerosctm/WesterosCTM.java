package com.westeroscraft.westerosctm;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("westerosctm")
public class WesterosCTM
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public WesterosCTM() {
    	// We are only client
    	DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
    	    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    	    modBus.addListener(this::setup);
    	    modBus.addListener(this::doClientStuff);
            // Register ourselves for server and other game events we are interested in
            MinecraftForge.EVENT_BUS.register(this);
    	});
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        //LOGGER.info("HELLO FROM PREINIT");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
       // LOGGER.info("Got game settings {}", event.getContainer().options);
    }

}
