package com.github.desktopgame.simplenote;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModMain.MODID, name=ModMain.MODNAME, version = ModMain.VERSION)
public class ModMain {
    public static final String MODNAME = "SimpleNote";
    public static final String MODID = "com.github.desktopgame.simplenote";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new NoteCommand());
	}
}