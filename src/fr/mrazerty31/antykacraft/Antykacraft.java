package fr.mrazerty31.antykacraft;

import java.util.logging.Logger;

import me.spoony.JSONChatLib.JSONChatClickEventType;
import me.spoony.JSONChatLib.JSONChatColor;
import me.spoony.JSONChatLib.JSONChatFormat;
import me.spoony.chatlib.ChatPart;
import me.spoony.chatlib.MessageSender;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.mrazerty31.antykacraft.antykastuff.kits.StuffKits;
import fr.mrazerty31.antykacraft.listener.AntykacraftListener;
import fr.mrazerty31.antykacraft.listener.PvPBoxListener;
import fr.mrazerty31.antykacraft.pvpbox.Kit;
import fr.mrazerty31.antykacraft.pvpbox.PvPBoxConfig;
import fr.mrazerty31.antykacraft.pvpbox.PvPBoxGui;
import fr.mrazerty31.antykacraft.pvpbox.PvPBoxItems;
import fr.mrazerty31.antykacraft.utils.City;
import fr.mrazerty31.antykacraft.utils.ConfigManager;
import fr.mrazerty31.antykacraft.utils.Teams;
import fr.mrazerty31.antykacraft.utils.Timers;
import fr.mrazerty31.antykacraft.utils.Utils;

public class Antykacraft extends JavaPlugin {

	public static ScoreboardManager sbManager;
	public static Antykacraft instance;
	public static Logger log;
	public static boolean tracking, manload;
	public static String prefix = "§6[Antykacraft] ";
	public static FileConfiguration config;

	private void init() {
		saveDefaultConfig();
		sbManager = this.getServer().getScoreboardManager();
		log = Logger.getLogger("Minecraft");
		config = this.getConfig();
		initClasses();
		tracking = false;
		manload = false;
		instance = this;
		City.init();
		this.getServer().getPluginManager().registerEvents(new AntykacraftListener(), this);
		this.getServer().getPluginManager().registerEvents(new PvPBoxListener(), this);
		Timers.startAnnounceTimer();
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
		Recipes.init();
		PvPBoxItems.init();
		Kit.init();
		Timers.init();
		ConfigManager.init();
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
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("reload")) {
							if(p.hasPermission("antykacraft.pvpbox.reload")) {
								reloadConfig();
								p.sendMessage("§6[PvPBox] " + ChatColor.GREEN + "Config redemarrée avec succés !");
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("stats")) {
							if(p.hasPermission("antykacraft.pvpbox.stats")) {
								p.sendMessage(PvPBoxConfig.getOfflinePlayerPvPBoxStats(p.getName(), true));
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("rank")) {
							if(p.hasPermission("antykacraft.pvpbox.rank")) {
								p.sendMessage(PvPBoxConfig.getPvPBoxRank(5));
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("trank")) {
							p.sendMessage(PvPBoxConfig.getRatioPvPBoxRank(5));
						} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Argument inconnu.");
					} else if(args.length == 2) { /* 2 Arguments */
						if(args[0].equalsIgnoreCase("create")) {
							if(p.hasPermission("antykacraft.pvpbox.create")) {
								PvPBoxConfig.createNewPvPBoxArena(p, args[1]);
								p.sendMessage("§6[PvPBox] " + ChatColor.GREEN + "L'arène PvPBox \"" + Utils.wordMaj(args[1]) + "\" a bien été crée !");
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("setdefault")) {
							if(p.hasPermission("antykacraft.pvpbox.setdefault")) {
								if(PvPBoxConfig.pvpBoxArenaExist(args[1])) {
									PvPBoxConfig.setDefaultPvPBoxArena(args[1]);
									p.sendMessage("§6[PvPBox] " + ChatColor.GREEN + "L'arène \"" + Utils.wordMaj(args[1]) + "\" a bien été définie par défaut !");
								} else if(args[1].equalsIgnoreCase("none")) {
									this.getConfig().set("antykacraft.pvpbox.defaultArena", "none");
									p.sendMessage("§6[PvPBox] " + ChatColor.GREEN + "Aucune arène n'a été sélectionnée.");
								} else p.sendMessage(ChatColor.RED + "L'arene spécifiée n'existe pas !");
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("remove")) {
							if(p.hasPermission("antykacraft.pvpbox.remove")) {
								if(PvPBoxConfig.pvpBoxArenaExist(args[1])) {
									PvPBoxConfig.removePvPBoxArena(args[1]);
									p.sendMessage("§6[PvPBox] " + ChatColor.GREEN + "L'arène \"" + Utils.wordMaj(args[1]) + "\" a bien été supprimée");
								}
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("stats")) {
							if(p.hasPermission("antykacraft.pvpbox.stats.others")) {
								if(args[1] != "") 
									sender.sendMessage(PvPBoxConfig.getOfflinePlayerPvPBoxStats(args[1], false));
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("rank")) {
							int i = 0;
							try {i = Integer.valueOf(args[1]);}
							catch(Exception e) {sender.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous devez indiquer un nombre en 2è argument !");}
							if(i >= 5) {
								sender.sendMessage(PvPBoxConfig.getPvPBoxRank(i));
							} else sender.sendMessage("§6[PvPBox] " + ChatColor.RED + "La limite du classement doit être supérieure ou égale à 5.");
						} else if(args[0].equalsIgnoreCase("trank")) {
							int i = 0;
							try {i = Integer.valueOf(args[1]);}
							catch(Exception e) {sender.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous devez indiquer un nombre en 2è argument !");}
							if(i >= 5) {
								sender.sendMessage(PvPBoxConfig.getRatioPvPBoxRank(i));
							} else sender.sendMessage("§6[PvPBox] " + ChatColor.RED + "La limite du classement doit être supérieure ou égale à 5.");
						}
					} else if(args.length == 0) {
						p.sendMessage("§6[PvPBox] " + ChatColor.YELLOW + "Aide en cours d'édition ...");
					} else return false;
				} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Vous devez être dans la map event pour effectuer cette commande.");
			} else {
				if(!(sender instanceof Player)) {
					if(args.length == 1) {
						Player p = this.getServer().getPlayer(args[0]);
						if(p != null) {
							if(PvPBoxConfig.getDefaultPvPBoxArena() != "none") {
								p.openInventory(PvPBoxGui.pvpBoxKitSelector(p));
							} else p.sendMessage("§6[PvPBox] " + ChatColor.RED + "Il n'y a aucune arène PvPBox de séléctionnée ...");
						}
					} else sender.sendMessage(prefix + ChatColor.RED + "Commande inconnue.");
				} else sender.sendMessage(prefix + ChatColor.RED + "Vous devez être un joueur pour executer cette commande.");
			}
		} 
		/* AntykaStuff */
		else if(cmd.getName().equalsIgnoreCase("antykastuff")) {
			if(!(sender instanceof Player)) {
				if(args.length == 2) {
					Player giveTo = Bukkit.getPlayer(args[1]);
					if(giveTo != null) {
						String city = args[0];
						if(city.equalsIgnoreCase("thebes") || city.equalsIgnoreCase("tikal") || city.equalsIgnoreCase("athenes")) {
							StuffKits.antykaStuff(city, giveTo);
							giveTo.sendMessage(prefix + ChatColor.GREEN + "Tu as bien reçu le stuff de la ville de \"" + Utils.wordMaj(city.toLowerCase()) + "\" !");
						}
					}
				}
			} else {
				if(sender.isOp() == true) sender.sendMessage(prefix + ChatColor.RED + "La commande doit être executée via command block.");
			}
		} else if(cmd.getName().equalsIgnoreCase("pvpstuff")) {
			if(!(sender instanceof Player)) {
				if(args.length == 1) {
					Player p = this.getServer().getPlayer(args[0]);
					if(p != null) StuffKits.pvpStuff(p);
				}
			} else {
				if(sender.isOp() == true) sender.sendMessage(prefix + ChatColor.RED + "La commande doit être executée via command block.");
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
								p.sendMessage(prefix + ChatColor.RED + "Erreur : Le type d'armure spécifié est incorrect.");
								break;	
							}
							p.sendMessage(prefix + ChatColor.GREEN + giveIt.getName() + " à bien reçu son armure VIP !");
						} else {
							sender.sendMessage(prefix + ChatColor.RED + "Erreur : Le joueur spécifié n'est pas en ligne.");
						}
					} else {
						p.sendMessage(prefix + ChatColor.RED + "Erreur : Nombre d'argument incorrect.");
					}
				} else p.sendMessage(ChatColor.RED + "Tu n'as pas la permission d'utiliser cette commande.");
			}
		} else if(cmd.getName().equalsIgnoreCase("stuffbw")) {
			if(!(sender instanceof Player)) {
				if(args.length == 1) {
					Player giveIt = Bukkit.getPlayer(args[0]);
					if(giveIt != null) {
						StuffKits.bwKit(giveIt);
						giveIt.sendMessage(prefix + ChatColor.GREEN + "Kit reçu !");
					}
				}
			} else {
				if(sender.isOp() == true) sender.sendMessage(prefix + ChatColor.RED + "La commande doit être éxecutée via command block.");
			}
		}

		/* Antykacraft Général */

		else if(cmd.getName().equalsIgnoreCase("warnr")) {
			if(sender.hasPermission("antykacraft.reloadwarn")) {
				Bukkit.broadcastMessage("§6[Antykacraft] " + ChatColor.YELLOW + "Un reload va avoir lieu veuillez vous deconnecter s'il vous plaît merci.");
			} else sender.sendMessage(prefix + ChatColor.RED + "Tu n'as pas la permission d'utiliser cette commande.");
		} else if(cmd.getName().equalsIgnoreCase("aide")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.aide")) {
					MessageSender.sendMessage(p, new ChatPart("Bienvenue sur Antykacraft ! Pour devenir membre, tu dois faire ta candidature sur "),
							new ChatPart("le Forum", JSONChatColor.AQUA, new JSONChatFormat[] {JSONChatFormat.UNDERLINED}).setClickEvent(JSONChatClickEventType.OPEN_URL, "http://www.antykacraft.forumactif.org"));
					p.sendMessage(ChatColor.AQUA + "Bon jeu sur Antykacraft !");
				} else p.sendMessage(prefix + ChatColor.RED + "Cette commande est inutile aux membres.");
			}
		} else if(cmd.getName().equalsIgnoreCase("ping")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.ping")) { // Ping du sender
					if(args.length == 0) {
						int ping = ((CraftPlayer) p).getHandle().ping;
						p.sendMessage(prefix + ChatColor.GREEN + "Votre ping est de " + ping + "ms.");
					} else if(args.length == 1) {
						if(p.hasPermission("antykacraft.ping.others")) { // Ping d'un autre joueur
							Player pinged = Bukkit.getPlayer(args[0]);
							if(pinged != null) {
								int ping = ((CraftPlayer) p).getHandle().ping;
								p.sendMessage(prefix + ChatColor.GREEN + "Le ping de " + pinged.getName() + " est de " + ping + "ms.");
							} else p.sendMessage(prefix + ChatColor.RED + "Le joueur spécifié n'est pas en ligne.");
						} else sender.sendMessage(prefix + ChatColor.RED + "Tu n'as pas la permission d'utiliser cette commande.");
					} else sender.sendMessage(prefix + ChatColor.RED + "Nombre d'arguments incorrect.");
				} else sender.sendMessage(prefix + ChatColor.RED + "Tu n'as pas la permission d'utiliser cette commande.");
			} else sender.sendMessage(prefix + ChatColor.RED + "Vous devez être un joueur pour executer cette commande.");
		} else if(cmd.getName().equalsIgnoreCase("raid")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.raid")) {
					if(args.length == 1) {
						if(City.isCity(args[0])) {
							Utils.raid(p, args[0]);
						} else if(args[0].equalsIgnoreCase("reset")) { // Reset des raids
							if(p.hasPermission("antykacraft.raid.reset")) {
								for(City c : City.cities) c.setRaid(false);
								p.sendMessage(prefix + ChatColor.GREEN + "Les raids d'aujourd'hui ont été reset !");
							} else p.sendMessage(prefix + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else if(args[0].equalsIgnoreCase("stop")) {
							if(p.hasPermission("antykacraft.raid.stop")) {
								Bukkit.getScheduler().cancelTasks(this);
								Timers.sb.clearSlot(DisplaySlot.SIDEBAR);
								for(Player pl : Bukkit.getOnlinePlayers()) pl.setScoreboard(sbManager.getMainScoreboard());
								try{Timers.sb.getObjective("time").unregister();}
								catch(Exception e){}
								finally{p.sendMessage(prefix + ChatColor.GREEN + "Le raid à été stoppé avec succés !");}
								Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + "Fin du raid forcé par " + p.getName());
							} else p.sendMessage(prefix + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
						} else p.sendMessage(prefix + ChatColor.RED + "Ville inconnue.");
					} else p.sendMessage(prefix + ChatColor.RED + "Erreur : /raid <ville>");
				} else p.sendMessage(prefix + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
			} else sender.sendMessage(prefix + ChatColor.RED + "Vous devez être un joueur pour executer cette commande.");
		} else if(cmd.getName().equalsIgnoreCase("timer")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.event.timer")) {
					if(p.getWorld().getName().equals("event")) {
						if(args.length == 1) {
							int i = 0;
							try{i = Integer.parseInt(args[0]);}
							catch(Exception e){p.sendMessage(prefix + ChatColor.RED + "L'argument doit être un entier !");}
							Timers.startEventTimer(p, i);
							p.sendMessage(prefix + ChatColor.GREEN + " Lancement du timer pour " + i + " minute(s).");
						} else p.sendMessage(prefix + ChatColor.RED  +  "Nombre d'arguments incorrect.");
					} else p.sendMessage(prefix + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
				} else p.sendMessage(prefix + ChatColor.RED  + "Tu dois être dans la map event pour éxécuter cette commande.");
			} else sender.sendMessage(prefix + ChatColor.RED + "Vous devez être un joueur pour executer cette commande.");
		} else if(cmd.getName().equalsIgnoreCase("stoptimer")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("antykacraft.event.timer")) {
					if(p.getWorld().getName().equals("event")) {
						Bukkit.getScheduler().cancelTasks(this);
						Timers.sb.clearSlot(DisplaySlot.SIDEBAR);
						for(Player pl : Bukkit.getOnlinePlayers()) pl.setScoreboard(sbManager.getMainScoreboard());
						try{Timers.sb.getObjective("time").unregister();}
						catch(Exception e){}
						finally{p.sendMessage(prefix + ChatColor.GREEN + "Le timer à été stoppé avec succés !");}
					} else p.sendMessage(prefix + ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
				} else p.sendMessage(prefix + ChatColor.RED  + "Tu dois être dans la map event pour éxécuter cette commande.");
			} else sender.sendMessage(prefix + ChatColor.RED + "Vous devez être un joueur pour executer cette commande.");
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
			for(City c : City.cities)
				c.updateMoney();
			sender.sendMessage(City.balanceCity());
		} else if(cmd.getName().equalsIgnoreCase("faccount")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				try {
					City c = City.getPlayerCity(p);
					if(args.length == 0) {
						p.sendMessage(prefix + "§aLe trésor de l'empire " + c.getEmpire() + " s'élève à §6" + c.getMoney() + " Drachmes§a.");
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
														c.addMoney(amount);
														p.sendMessage(prefix + "§aVotre don de §6" + amount + " Drachmes§a pour " + c.getDisplayName() + " a été effectué !");
														c.saveMoney();
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

							} else p.sendMessage(prefix + "§cVous n'avez pas la permission d'éxecuter cette commande.");
						} else if(args[0].equalsIgnoreCase("take")) {
							if(p.hasPermission("antykacraft.faction.account.take")) {
								try {
									int amount = Integer.parseInt(args[1]);
									int money = c.getMoney();
									if(money > 0) {
										if(money >= amount) {
											try {
												c.removeMoney(amount);
												Economy.setMoney(p.getName(), Economy.getMoney(p.getName()) + amount);
												c.saveMoney();
											} catch (NoLoanPermittedException e) {
												e.printStackTrace();
											}
											p.sendMessage(prefix + "§aVous avez pris §a" + amount + " Drachmes au coffre de " + c.getDisplayName() + " !");
										} else p.sendMessage(prefix + "§cLe coffre de " + c.getDisplayName() + " contient seulement " + c.getMoney());
									}
								} catch (UserDoesNotExistException e) {
									e.printStackTrace();
								} catch(IllegalArgumentException iae) {
									p.sendMessage(prefix + "§cVeuillez indiquer un nombre ...");
								}
							} else p.sendMessage(prefix + "§cVous n'avez pas la permission d'éxecuter cette commande.");
						}
					} else if(args.length == 1) {
						if(args[0].equalsIgnoreCase("give")) {
							if(p.hasPermission("antykacraft.faction.account.give")) {
								p.sendMessage(prefix + "§cVeuillez indiquer la somme à donner à la faction.");
							} else p.sendMessage(prefix + "§cVous n'avez pas la permission d'éxecuter cette commande.");
						} else if(args[0].equalsIgnoreCase("take")) {
							if(p.hasPermission("antykacraft.faction.account.take")) {
								p.sendMessage(prefix + "§cVeuillez indiquer la somme à prendre à la faction.");
							} else p.sendMessage(prefix + "§cVous n'avez pas la permission d'éxecuter cette commande.");
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
				} else p.sendMessage(prefix + "§cVous n'avez pas la permission d'éxecuter cette commande.");
			}
		}
		return true;	
	}
}
