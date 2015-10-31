package fr.mrazerty31.antykacraft;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.mrazerty31.antykacraft.listener.AntykacraftListener;
import fr.mrazerty31.antykacraft.listener.PvPBoxListener;
import fr.mrazerty31.antykacraft.pvpbox.PvPBoxConfig;
import fr.mrazerty31.antykacraft.pvpbox.PvPBoxGui;
import fr.mrazerty31.antykacraft.pvpbox.PvPBoxItems;
import fr.mrazerty31.antykacraft.pvpbox.kits.Kit;
import fr.mrazerty31.antykacraft.pvpbox.spells.SpellUtil;
import fr.mrazerty31.antykacraft.utils.ConfigManager;
import fr.mrazerty31.antykacraft.utils.Recipes;
import fr.mrazerty31.antykacraft.utils.Teams;
import fr.mrazerty31.antykacraft.utils.TimeUtil;
import fr.mrazerty31.antykacraft.utils.Utils;
import fr.mrazerty31.antykacraft.utils.roleplay.City;
import fr.mrazerty31.antykacraft.utils.roleplay.Faction;
import fr.mrazerty31.antykacraft.utils.roleplay.Raid;
import fr.mrazerty31.antykacraft.utils.timer.EventTimer;

public class Antykacraft extends JavaPlugin {

	public static ScoreboardManager sbManager;
	public static Antykacraft instance;
	public static Logger log;
	public static boolean tracking, manload;
	public static String prefix = "§6[Antykacraft] ";
	public static FileConfiguration config;

	private void init() {
		instance = this;
		saveDefaultConfig();
		sbManager = this.getServer().getScoreboardManager();
		log = Logger.getLogger("Minecraft");
		config = this.getConfig();
		initClasses();
		tracking = false;
		manload = false;
		this.getServer().getPluginManager().registerEvents(new AntykacraftListener(), this);
		this.getServer().getPluginManager().registerEvents(new PvPBoxListener(), this);
		TimeUtil.startAnnounceTimer();
	}

	@Override
	public void onEnable() {
		this.getServer().getScheduler().cancelTasks(this);
		init();
	}

	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
	}

	void initClasses() {
		ConfigManager.init();
		SpellUtil.linkSpells();
		PvPBoxItems.init();
		Kit.init();
		Recipes.init();
		Faction.init();
		City.init();
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		/* PvPBox */
		if(cmd.getName().equalsIgnoreCase("pvpbox")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.getWorld().getName().equalsIgnoreCase("event")) {
					if(args.length == 1) { /* 1 Argument */
						if(args[0].equalsIgnoreCase("list")) {
							if(p.hasPermission("antykacraft.pvpbox.list")) {
								p.sendMessage(PvPBoxConfig.getPvPBoxArenaList());
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("reload")) {
							if(p.hasPermission("antykacraft.pvpbox.reload")) {
								reloadConfig();
								p.sendMessage(PVPBOX_PREF + "§aConfig redemarrée avec succés !");
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("stats")) {
							if(p.hasPermission("antykacraft.pvpbox.stats")) {
								p.sendMessage(PvPBoxConfig.getOfflinePlayerPvPBoxStats(p.getName(), true));
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("rank")) {
							if(p.hasPermission("antykacraft.pvpbox.rank")) {
								p.sendMessage(PvPBoxConfig.getPvPBoxRank(5));
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("trank")) {
							p.sendMessage(PvPBoxConfig.getRatioPvPBoxRank(5));
						} else if(args[0].equalsIgnoreCase("debug")) {
							if(p.hasPermission("antykacraft.pvpbox.debug")) {
								Kit.debug = Kit.debug == false ? true : false;
								p.sendMessage(Kit.debug == true ? "§aDebug Mode ON !" : "§cDebug Mode OFF !");
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("choose")) {
							if(p.hasPermission("antykacraft.pvpbox.choose")) {
								if(PvPBoxListener.playerKits.containsKey(p)) {
									Kit.resetPvPBoxPlayer(p);
								} else p.sendMessage(PVPBOX_PREF + "§cNope.");
							}
						}else p.sendMessage(PVPBOX_PREF + "§cArgument inconnu.");
					} else if(args.length == 2) { /* 2 Arguments */
						if(args[0].equalsIgnoreCase("create")) {
							if(p.hasPermission("antykacraft.pvpbox.create")) {
								PvPBoxConfig.createNewPvPBoxArena(p, args[1]);
								p.sendMessage(PVPBOX_PREF + "§aL'arène PvPBox \"" + Utils.wordMaj(args[1]) + "\" a bien été crée !");
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("setdefault")) {
							if(p.hasPermission("antykacraft.pvpbox.setdefault")) {
								if(PvPBoxConfig.pvpBoxArenaExist(args[1])) {
									PvPBoxConfig.setDefaultPvPBoxArena(args[1]);
									p.sendMessage(PVPBOX_PREF + "§aL'arène \"" + Utils.wordMaj(args[1]) + "\" a bien été définie par défaut !");
								} else if(args[1].equalsIgnoreCase("none")) {
									this.getConfig().set("antykacraft.pvpbox.defaultArena", "none");
									p.sendMessage(PVPBOX_PREF + "§aAucune arène n'a été sélectionnée.");
								} else p.sendMessage("§cL'arene spécifiée n'existe pas !");
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("remove")) {
							if(p.hasPermission("antykacraft.pvpbox.remove")) {
								if(PvPBoxConfig.pvpBoxArenaExist(args[1])) {
									PvPBoxConfig.removePvPBoxArena(args[1]);
									p.sendMessage(PVPBOX_PREF + "§aL'arène \"" + Utils.wordMaj(args[1]) + "\" a bien été supprimée");
								}
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("stats")) {
							if(p.hasPermission("antykacraft.pvpbox.stats.others")) {
								if(args[1] != "") 
									sender.sendMessage(PvPBoxConfig.getOfflinePlayerPvPBoxStats(args[1], false));
							} else p.sendMessage(PVPBOX_PERM);
						} else if(args[0].equalsIgnoreCase("rank")) {
							int i = 0;
							try {i = Integer.valueOf(args[1]);}
							catch(Exception e) {sender.sendMessage(PVPBOX_PREF + "§cVous devez indiquer un nombre en 2è argument !");}
							if(i >= 5) {
								sender.sendMessage(PvPBoxConfig.getPvPBoxRank(i));
							} else sender.sendMessage(PVPBOX_PREF + "§cLa limite du classement doit être supérieure ou égale à 5.");
						} else if(args[0].equalsIgnoreCase("trank")) {
							int i = 0;
							try {i = Integer.valueOf(args[1]);}
							catch(Exception e) {sender.sendMessage(PVPBOX_PREF + "§cVous devez indiquer un nombre en 2è argument !");}
							if(i >= 5) {
								sender.sendMessage(PvPBoxConfig.getRatioPvPBoxRank(i));
							} else sender.sendMessage(PVPBOX_PREF + "§cLa limite du classement doit être supérieure ou égale à 5.");
						}
					} else if(args.length == 0) {
						p.sendMessage(PVPBOX_PREF + "§eAide en cours d'édition ...");
					} else return false;
				} else p.sendMessage(PVPBOX_PREF + "§cVous devez être dans la map event pour effectuer cette commande.");
			} else {
				if(!(sender instanceof Player)) {
					if(args.length == 1) {
						Player p = this.getServer().getPlayer(args[0]);
						if(p != null) {
							if(PvPBoxConfig.getDefaultPvPBoxArena() != "none") {
								p.openInventory(PvPBoxGui.pvpBoxKitSelector(p));
							} else p.sendMessage(PVPBOX_PREF + "§cIl n'y a aucune arène PvPBox de séléctionnée ...");
						}
					} else sender.sendMessage(prefix + "§cCommande inconnue.");
				} else sender.sendMessage(prefix + "§cVous devez être un joueur pour executer cette commande.");
			}
		} 
		/* AntykaStuff */
		else if(cmd.getName().equalsIgnoreCase("pvpstuff")) {
			if(!(sender instanceof Player)) {
				if(args.length == 1) {
					Player p = this.getServer().getPlayer(args[0]);
					if(p != null) StuffKits.pvpStuff(p);
				}
			} else {
				if(sender.isOp() == true) sender.sendMessage(prefix + "§cLa commande doit être executée via command block.");
			}
		} else if(cmd.getName().equalsIgnoreCase("vip")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.vip")) {
					if(args.length == 2) {
						Player giveIt = Bukkit.getPlayer(args[0]);
						if(giveIt != null) {
							String armorType = args[1];
							switch(armorType) {
							case "leather":
								StuffKits.vipArmor("leather", giveIt);
								break;
							case "iron":
								StuffKits.vipArmor("iron", giveIt);
								break;
							case "gold":
								StuffKits.vipArmor("gold", giveIt);
								break;
							case "diamond":
								StuffKits.vipArmor("diamond", giveIt);
								break;
							default:
								p.sendMessage(prefix + "§cErreur : Le type d'armure spécifié est incorrect.");
								break;	
							}
							p.sendMessage(prefix + "§a" + giveIt.getName() + " a bien reçu son armure VIP !");
						} else {
							sender.sendMessage(prefix + "§cErreur : Le joueur spécifié n'est pas en ligne.");
						}
					} else {
						p.sendMessage(prefix + "§cErreur : Nombre d'argument incorrect.");
					}
				} else p.sendMessage("§cTu n'as pas la permission d'utiliser cette commande.");
			}
		}
		/* Antykacraft Général */

		else if(cmd.getName().equalsIgnoreCase("warnr")) {
			if(sender.hasPermission("antykacraft.reloadwarn")) {
				Bukkit.broadcastMessage("§6[Antykacraft] " + "§eUn reload va avoir lieu veuillez vous deconnecter s'il vous plaît merci.");
			} else sender.sendMessage(prefix + "§cTu n'as pas la permission d'utiliser cette commande.");
		} else if(cmd.getName().equalsIgnoreCase("ping")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.ping")) { // Ping du sender
					if(args.length == 0) {
						int ping = ((CraftPlayer) p).getHandle().ping;
						p.sendMessage(prefix + "§aVotre ping est de " + ping + "ms.");
					} else if(args.length == 1) {
						if(p.hasPermission("antykacraft.ping.others")) { // Ping d'un autre joueur
							Player pinged = Bukkit.getPlayer(args[0]);
							if(pinged != null) {
								int ping = ((CraftPlayer) p).getHandle().ping;
								p.sendMessage(prefix + "§aLe ping de " + pinged.getName() + " est de " + ping + "ms.");
							} else p.sendMessage(prefix + "§cLe joueur spécifié n'est pas en ligne.");
						} else sender.sendMessage(prefix + "§cTu n'as pas la permission d'utiliser cette commande.");
					} else sender.sendMessage(prefix + "§cNombre d'arguments incorrect.");
				} else sender.sendMessage(prefix + "§cTu n'as pas la permission d'utiliser cette commande.");
			} else sender.sendMessage(prefix + "§cVous devez être un joueur pour executer cette commande.");
		} else if(cmd.getName().equalsIgnoreCase("raid")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.raid")) {
					if(args.length == 1) {
						if(Faction.isFaction(args[0])) {
							Faction.raid(p, Faction.getPlayerFaction(p), Faction.getFaction(args[0].toLowerCase()));
						} else if(args[0].equalsIgnoreCase("reset")) { // Reset des raids
							if(p.hasPermission("antykacraft.raid.reset")) {
								for(Faction f : Faction.getFactions()) f.setHasRaid(false);
								p.sendMessage(prefix + "§aLes raids d'aujourd'hui ont été reset !");
							} else p.sendMessage(PERM);
						} else if(args[0].equalsIgnoreCase("stop")) {
							if(p.hasPermission("antykacraft.raid.stop")) {
								for(Raid r : Raid.getRaids()) r.stop();
								p.sendMessage(prefix + "§aLe raid à été stoppé avec succés !");
								Bukkit.broadcastMessage(prefix + "§eFin du raid forcé par " + p.getName());
							} else p.sendMessage(PERM);
						} else p.sendMessage(prefix + "§cFaction inconnue.");
					} else p.sendMessage(prefix + "§cErreur : /raid <faction>");
				} else p.sendMessage(PERM);
			} else sender.sendMessage(prefix + "§cVous devez être un joueur pour executer cette commande.");
		} else if(cmd.getName().equalsIgnoreCase("timer")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.event.timer")) {
					if(p.getWorld().getName().equals("event")) {
						if(args.length == 1) {
							int i = 0;
							try {
								i = Integer.parseInt(args[0]);
								EventTimer timer = new EventTimer(i);
								timer.start();
								p.sendMessage(prefix + "§a Lancement du timer pour " + i + " minute(s).");
							} catch(Exception e) {
								if(args[0].equalsIgnoreCase("stop")) {
									try {
										EventTimer.getCurrentTimer().stop();
										for(Player pl : Bukkit.getWorld("event").getPlayers()) {
											pl.sendMessage("§6[Antykacraft] §eTimer stoppé par "+p.getName());
										}
									} catch(NullPointerException npe) {
										p.sendMessage("§6[Antykacraft] §cAucun timer n'est en cours !");
									}
								} else {
									p.sendMessage(prefix + "§cL'argument doit être un entier !");
								}
							}
						} else p.sendMessage(prefix + "§cNombre d'arguments incorrect.");
					} else p.sendMessage(PERM);
				} else p.sendMessage(prefix + "§cTu dois être dans la map event pour éxécuter cette commande.");
			} else sender.sendMessage(prefix + "§cVous devez être un joueur pour executer cette commande.");
		} else if(cmd.getName().equalsIgnoreCase("faction")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(args.length >= 1) {
					String msgStr = "";
					for(int i = 0; i < args.length; i++) msgStr += args[i] + " ";
					try {
						Team t = Teams.getTeam(p);
						for(OfflinePlayer pl : t.getPlayers()) {
							try {
								Player pla = (Player) pl;
								pla.sendMessage("§a[" + p.getName() + " ► " + t.getDisplayName() + "]§r " + msgStr);
							} catch(Exception e) {}
						} log.info("[" + p.getName() + " ► " + t.getDisplayName() + "]" + msgStr);
					} catch(NullPointerException npe) {
						p.sendMessage("§6[AntykaCraft] §cVous n'appartenez à aucune faction !");
					}
				} else p.sendMessage(prefix + "§cVeuillez entrer le message.");
			}
		} else if(cmd.getName().equalsIgnoreCase("balancefac")) {
			for(Faction f : Faction.getFactions())
				f.updateMoney();
			sender.sendMessage(Faction.balance());
		} else if(cmd.getName().equalsIgnoreCase("faccount")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				try {
					Faction f = Faction.getPlayerFaction(p);
					if(args.length == 0) {
						p.sendMessage(prefix + "§aLe trésor de l'empire " + f.getEmpireName() + " s'élève à §6" + f.getAccount() + " Drachmes§a.");
					} else if(args.length == 2) { // Add et Remove
						if(args[0].equalsIgnoreCase("give")) {
							if(p.hasPermission("antykacraft.faction.account.give")) {
								try {
									int amount = Integer.parseInt(args[1]);
									int money = Economy.getMoneyExact(p.getName()).intValue();
									if(amount > 0) {
										if(money > 1000) {
											if(money >= amount) {
												if((money - amount) >= 1000) {
													try {
														Economy.setMoney(p.getName(), money - amount);
														f.addAccount(amount);
														p.sendMessage(prefix + "§aVotre don de §6" + amount + " Drachmes§a pour " + f.getDisplayName() + " a été effectué !");
													} catch (NoLoanPermittedException e) {
														e.printStackTrace();
													}
												} else p.sendMessage(prefix + "§cN'abusez pas ! Vous allez être ruiné !");
											} else p.sendMessage(prefix + "§cVous n'avez pas assez d'argent !");
										} else p.sendMessage(prefix + "§cVous devez avoir plus de 1000 Drachmes pour faire un don !");
									} else p.sendMessage(prefix + "§cMerci d'être honnête.");
								} catch (UserDoesNotExistException e) {
									e.printStackTrace();
								} catch(IllegalArgumentException iae) {
									p.sendMessage(prefix + "§cVeuillez indiquer un nombre ...");
								}

							} else p.sendMessage(PERM);
						} else if(args[0].equalsIgnoreCase("take")) {
							if(p.hasPermission("antykacraft.faction.account.take")) {
								try {
									int amount = Integer.parseInt(args[1]);
									int money = f.getAccount();
									if(money > 0) {
										if(money >= amount) {
											try {
												f.removeAccount(amount);
												Economy.setMoney(p.getName(), Economy.getMoney(p.getName()) + amount);
											} catch (NoLoanPermittedException e) {
												e.printStackTrace();
											}
											p.sendMessage(prefix + "§aVous avez pris §a" + amount + " Drachmes au coffre de " + f.getDisplayName() + " !");
										} else p.sendMessage(prefix + "§cLe coffre de " + f.getDisplayName() + " contient seulement " + f.getBalance());
									}
								} catch (UserDoesNotExistException e) {
									e.printStackTrace();
								} catch(IllegalArgumentException iae) {
									p.sendMessage(prefix + "§cVeuillez indiquer un nombre ...");
								}
							} else p.sendMessage(PERM);
						}
					} else if(args.length == 1) {
						if(args[0].equalsIgnoreCase("give")) {
							if(p.hasPermission("antykacraft.faction.account.give")) {
								p.sendMessage(prefix + "§cVeuillez indiquer la somme à donner à la faction.");
							} else p.sendMessage(PERM);
						} else if(args[0].equalsIgnoreCase("take")) {
							if(p.hasPermission("antykacraft.faction.account.take")) {
								p.sendMessage(prefix + "§cVeuillez indiquer la somme à prendre à la faction.");
							} else p.sendMessage(PERM);
						} else p.sendMessage(prefix + "§cArgument inconnu ...");
					}
				} catch(NullPointerException npe) {
					p.sendMessage(prefix + "§cVous n'appartenez à aucune faction !");
				}
			}
		} else if(cmd.getName().equalsIgnoreCase("annonce")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.announce")) {
					if(args.length == 0) {
						p.sendMessage(prefix + "§aL'annonce paramétrée est \"" + ConfigManager.getAnnounce() + "\"");
					} else if(args.length == 1) {
						boolean b;
						try {
							b = Boolean.parseBoolean(args[0]);
							ConfigManager.setAnnounceToggle(b);
							p.sendMessage(prefix + "§aL'annonce a bien été reglée sur \"" + b + "\"");
						} catch(Exception e) {
							p.sendMessage(prefix + "§cVeuillez indiquer une valeur correcte ! (true/false)");
						}
					} else if(args.length >= 2) {
						if(args[0].equalsIgnoreCase("set")) {
							String message = "";
							for(int i = 0;i < args.length; i++) {
								if(i >= 1) message += args[i] + " ";
							} if(message != "") {
								ConfigManager.setAnnounce(message);
								p.sendMessage(prefix + "§aLe message d'annonce a bien été modifié !");
							} else p.sendMessage(prefix + "§cVeuillez entrer un message !");
						} else p.sendMessage(prefix + "§cArgument inconnu.");
					} else p.sendMessage(prefix + "§cMauvaise utilisation.");
				} else p.sendMessage(PERM);
			}
		}
		return true;	
	}

	String PVPBOX_PREF = "§6[PvPBox] ";
	String PVPBOX_PERM = PVPBOX_PREF + "§6Vous n'avez pas la permission d'utiliser cette commande.";
	String PERM = prefix + "§cVous n'avez pas la permission d'éxecuter cette commande.";
}
