package me.filoghost.chestcommands.util;

import org.bukkit.Bukkit;

public final class NMSUtils {

	private static final String NMS_VERSION;

	static {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		NMS_VERSION = packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	private NMSUtils() {
	}

	public static String getNMSVersion() {
		return NMS_VERSION;
	}

	public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + NMSUtils.getNMSVersion() + "." + name);
	}

	public static Class<?> getCraftBukkitClass(String name) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + NMSUtils.getNMSVersion() + "." + name);
	}

}
