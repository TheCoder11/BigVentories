package com.somemone.bigventories.command;

import com.somemone.bigventories.StoragePlus;
import com.somemone.bigventories.storage.GroupStorage;
import com.somemone.bigventories.storage.OpenStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class GroupStorageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1) {
                if (StoragePlus.groupStorages.size() > 0) {

                    for (GroupStorage cs : StoragePlus.groupStorages) {

                        if (cs.name.equals(args[0]) && cs.accessList.contains(player.getUniqueId())) {

                            //Check if inventory is open

                            for (OpenStorage os : StoragePlus.openStorages) {

                                if (os.uuid == cs.uuid) {
                                    player.openInventory(os.inventory.get(0));
                                    return true;
                                }

                            }

                            ArrayList<Inventory> inventories = cs.buildInventories();
                            OpenStorage os = new OpenStorage(inventories, cs.uuid);

                            StoragePlus.openStorages.add(os);

                            player.openInventory(os.inventory.get(0));

                            return true;

                        }

                    }

                    sender.sendMessage(ChatColor.RED + "You do not have access to this inventory!");

                }
            }

            switch (args[0]) {

                case "upgrade":  // /gs upgrade group-name 3

                    int rowsToAdd = 1;
                    try {
                        rowsToAdd = Integer.parseInt(args[2]);
                    } catch (NumberFormatException | NullPointerException ignored) {
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return false;
                    }

                    if (StoragePlus.groupStorages.size() > 0) {

                        for (GroupStorage upgs : StoragePlus.groupStorages) {

                            if (upgs.name.equals(args[1])) {
                                if (StoragePlus.configHandler.getGroupStoragePrice(rowsToAdd) < StoragePlus.getEcon().getBalance(player) && StoragePlus.configHandler.getGroupStoragePrice(rowsToAdd) != 0) {
                                    StoragePlus.getEcon().withdrawPlayer(player, StoragePlus.configHandler.getGroupStoragePrice(rowsToAdd));
                                    upgs.rows = upgs.rows + rowsToAdd;
                                    sender.sendMessage(ChatColor.GREEN + "Group Storage was upgraded!");

                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Insufficient funds!");
                                }


                            }

                        }

                        sender.sendMessage(ChatColor.RED + "There is no Chunk Storage in this zone! Create one first!");

                    }
                    break;

                case "invite":   // /gs invite [group-name] [players]

                    if (args.length == 3) {

                        if (StoragePlus.groupStorages.size() > 0) {

                            for (GroupStorage fgs : StoragePlus.groupStorages) {
                                if (fgs.name.equals(args[1])) {
                                    if (fgs.owner == player.getUniqueId()) {

                                        if (StoragePlus.getInvitedGroupStorages(Bukkit.getOfflinePlayer(args[2]).getUniqueId()) <= StoragePlus.configHandler.getGroupStorageNum()) {
                                            sender.sendMessage(ChatColor.GREEN + args[2] + " was invited to the Group!");
                                            fgs.accessList.add(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                                        } else {
                                            sender.sendMessage(ChatColor.RED + args[2] + " has too many group storages!");
                                        }
                                        return true;

                                    } else {

                                        sender.sendMessage(ChatColor.RED + "You do not have access to this storage!");

                                    }

                                }
                            }
                        }

                    }
                    break;
                case "create":   // /gs create group-name

                    if (args.length == 2) {
                        GroupStorage newGS = new GroupStorage(args[1], 1, player.getUniqueId());

                        if (StoragePlus.groupStorages.size() > 0) {
                            for (GroupStorage fgs : StoragePlus.groupStorages) {

                                if (fgs.name.equals(args[1])) {

                                    sender.sendMessage(ChatColor.RED + "A Group Storage with this name already exists!");
                                    return true;

                                }

                            }
                        }

                        if (StoragePlus.configHandler.getGroupStoragePrice(1) < StoragePlus.getEcon().getBalance(player) && StoragePlus.configHandler.getGroupStoragePrice(1) != 0) {
                            StoragePlus.getEcon().withdrawPlayer(player, StoragePlus.configHandler.getGroupStoragePrice(1));
                            newGS.accessList.add(player.getUniqueId());
                            StoragePlus.groupStorages.add(newGS);
                            sender.sendMessage(ChatColor.GREEN + "Group Storage successfully created!");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Insufficient Funds!");
                        }

                    }
                    break;
                case "remove":   // /gs remove player123

                    if (args.length == 3) {

                        if (StoragePlus.groupStorages.size() > 0) {

                            for (GroupStorage fgs : StoragePlus.groupStorages) {

                                if (fgs.name.equals(args[2]) && fgs.owner == player.getUniqueId()) {

                                    if (fgs.accessList.contains(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                                        fgs.accessList.remove(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                                        sender.sendMessage(ChatColor.GREEN + args[1] + " has been removed from the Group!");
                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Player not in group");
                                    }

                                } else {

                                    sender.sendMessage(ChatColor.RED + "You do not have access to this storage!");

                                }

                            }

                        }

                    }
                    break;
                case "setowner": // /gs setowner group-name player123

                    if (args.length == 3) {

                        if (StoragePlus.groupStorages.size() > 0) {
                            for (GroupStorage fgs : StoragePlus.groupStorages) {

                                if (fgs.name.equals(args[1]) && fgs.owner == player.getUniqueId()) {

                                    fgs.owner = Bukkit.getOfflinePlayer(args[2]).getUniqueId();
                                    sender.sendMessage(ChatColor.GREEN + args[2] + " is now the owner of the Group!");

                                }

                            }
                        }

                    }
                    break;

                case "delete":

                    if (args.length == 2) {

                        if (StoragePlus.groupStorages.size() > 0) {
                            GroupStorage storageToDelete = null;
                            for (GroupStorage fgs : StoragePlus.groupStorages) {
                                if (fgs.name.equals(args[1]) && fgs.owner == player.getUniqueId()) {
                                    storageToDelete = fgs;
                                }
                            }

                            if (storageToDelete != null) {
                                StoragePlus.groupStorages.remove(storageToDelete);
                                sender.sendMessage(ChatColor.GREEN + storageToDelete.name + " successfully removed!");
                            } else {
                                sender.sendMessage(ChatColor.RED + "You do not have access to this group!");
                            }
                        }

                    }
                    break;
                case "leave":

                    if (args.length == 2) {

                        if (StoragePlus.groupStorages.size() > 0) {
                            for (GroupStorage gs : StoragePlus.groupStorages) {
                                if (gs.name.equals(args[1]) && gs.accessList.contains( player.getUniqueId() )) {
                                    gs.accessList.remove(player.getUniqueId());
                                    sender.sendMessage(ChatColor.GREEN + "You have left " + gs.name);
                                    return true;
                                }
                            }
                            sender.sendMessage(ChatColor.RED + "You are not in this group!");
                        }
                    }
            }

        } else {
            sender.sendMessage("This is for players only!");
        }
        return true;
    }
}
