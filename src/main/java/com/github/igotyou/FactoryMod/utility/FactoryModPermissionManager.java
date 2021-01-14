package com.github.igotyou.FactoryMod.utility;


import vg.civcraft.mc.namelayer.core.DefaultPermissionLevel;
import vg.civcraft.mc.namelayer.core.PermissionTracker;
import vg.civcraft.mc.namelayer.core.PermissionType;
import vg.civcraft.mc.namelayer.mc.GroupAPI;

public class FactoryModPermissionManager {

	public final static String useFactory = "USE_FACTORY";
	public final static String upgradeFactory = "UPGRADE_FACTORY";

	private PermissionTracker permTracker;

	public FactoryModPermissionManager(PermissionTracker permTracker) {
		this.permTracker = permTracker;
		setup();
	}
	private static void setup() {
		GroupAPI.registerPermission(useFactory, DefaultPermissionLevel.MEMBER, "Allows a player to use factories reinforced under this group.");
		GroupAPI.registerPermission(upgradeFactory, DefaultPermissionLevel.MOD, "Allows a player to upgrade/make changes to a factory.");
	}
	
	public PermissionType getUseFactory() {
		return permTracker.getPermission(useFactory);
	}
	
	public PermissionType getUpgradeFactory() {
		return permTracker.getPermission(upgradeFactory);
	}

}
