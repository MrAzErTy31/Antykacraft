package fr.mrazerty31.antykacraft.pvpbox;

import org.bukkit.entity.Player;

interface IRightAbility {
	public void run(Player p);
} 

interface IHitAbility {
	public void run(Player p, Player d, double damage);
}

interface ILeftAbility {
	public void run(Player p);
}

public abstract class MainAbility {
	protected long cooldown;

	public MainAbility(long cdTicks) {
		this.cooldown = cdTicks;
	}

	public MainAbility(int seconds) {
		this.cooldown = seconds*20;
	}

	public MainAbility() {
		this.cooldown = 0;
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	
	public static abstract class RightAbility extends MainAbility implements IRightAbility {
		public RightAbility(long cdTicks) {
			this.cooldown = cdTicks;
		} public RightAbility(int seconds) {
			this.cooldown = seconds*20;
		} public RightAbility() {
			this.cooldown = 0;
		}
	} public static abstract class HitAbility extends MainAbility implements IHitAbility {
		public HitAbility(long cdTicks) {
			this.cooldown = cdTicks;
		} public HitAbility(int seconds) {
			this.cooldown = seconds*20;
		} public HitAbility() {
			this.cooldown = 0;
		}
	} public static abstract class LeftAbility extends MainAbility implements ILeftAbility {
		public LeftAbility(long cdTicks) {
			this.cooldown = cdTicks;
		} public LeftAbility(int seconds) {
			this.cooldown = seconds*20;
		} public LeftAbility() {
			this.cooldown = 0;
		}
	}
}
