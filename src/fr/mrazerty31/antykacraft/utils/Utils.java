package fr.mrazerty31.antykacraft.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Utils {

	public static String wordMaj(String word) {
		return word.replaceFirst(".", (word.charAt(0) + "").toUpperCase());
	}
	public static int calculateTicks(int seconds) {
		return seconds * 20;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}

	public static Vector setAngle(Player p, float ang, double mul) {
		double pitch = ((p.getLocation().getPitch() + 90) * Math.PI) / 180;
		double yaw  = ((p.getLocation().getYaw() + ang)  * Math.PI) / 180;
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		return new Vector(x, z, y).multiply(mul);
	}

	public static Vector getVectorToLocation(Location a, Location b, double multiplicator) {
		return a.subtract(b).toVector().multiply(multiplicator);
	}

	public static Vector getVectorToLocation(Location a, Location b) {
		return a.subtract(b).toVector();
	}
}