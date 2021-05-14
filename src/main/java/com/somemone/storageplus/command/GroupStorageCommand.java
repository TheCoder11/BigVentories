package com.somemone.storageplus.command;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.GroupStorage;
import com.somemone.storageplus.storage.OpenStorage;
import com.somemone.storageplus.util.FileHandler;
import net.milkbowl.vault.chat.Chat;
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

            if (args.length == 0) return false;

            if (args.length == 1) {
                GroupStorage gs = FileHandler.loadGroupStorage(args[0]);
                for (GroupStorage gls : FileHandler.loadAllowedGroupStorages(player.getUniqueId())) {
                    if (gls.name.equals(gs.name)) {
                            if (StoragePlus.openStorages.size() > 0) {
                                for (OpenStorage os : StoragePlus.openStorages) {
                                    if (os.uuid.equals(gs.uuid)) {
                                        player.openInventory(os.inventory.get(0));

                                        return true;
                                    }
                                }
                            }

                            ArrayList<Inventory> inventories = gs.buildInventories();
                            OpenStorage openStorage = new OpenStorage(inventories, gs.uuid, true, gs.rows);
                            StoragePlus.openStorages.add(openStorage);
                            player.openInventory(openStorage.inventory.get(0));

                            return true;
                    }
                }
                sender.sendMessage(ChatColor.RED + "You do not have access to this storage!");

            }

            switch (args[0]) {
                case "upgrade":  // /gs upgrade group-name 3
                    if (args.length != 3) return false;

                    GroupStorage gs = FileHandler.loadGroupStorage(args[1]);

                    if (!gs.isEmpty) {
                        int rowsToAdd = 1;
                        try {
                            rowsToAdd = Integer.parseInt(args[2]);
                        } catch (NumberFormatException | NullPointerException ignored) { }

                        if (StoragePlus.activeConfig.validateStorage(player, rowsToAdd)) {
                            gs.addRows(rowsToAdd);
                            FileHandler.saveStorage(gs);
                            sender.sendMessage(ChatColor.GREEN + "Personal Storage successfully upgraded!");
                        } else {
                            sender.sendMessage(ChatColor.RED + "You don't have enough money!");
                        }
                    }
                    break;

                case "invite":   // /gs invite [group-name] [players]

                    if (args.length == 3) {

                        GroupStorage igs = FileHandler.loadGroupStorage(args[1]);

                        if (!igs.isEmpty) {
                            if (igs.owner.equals(player.getUniqueId())) {

                                Player requestedPlayer = Bukkit.getPlayer(args[2]);
                                if (requestedPlayer == null) return false;

                                if (FileHandler.loadAllowedGroupStorages(requestedPlayer.getUniqueId()).size() < StoragePlus.configHandler.getGroupStorageNum()) {

                                    if (StoragePlus.currentInvites.containsKey(requestedPlayer.getUniqueId())) {
                                        sender.sendMessage(ChatColor.RED + "This player already has an invitation!");
                                    } else {
                                        sender.sendMessage(ChatColor.GREEN + args[2] + " was invited to the group!");
                                        StoragePlus.currentInvites.put(requestedPlayer.getUniqueId(), igs.uuid);

                                        requestedPlayer.sendMessage("You have been invited to the group " + igs.name + "! Use /staccept to accept this request");
                                    }
                                }
                            }

                        } else {
                            sender.sendMessage(ChatColor.RED + "");
                        }
                    }
                    break;
                case "create":   // /gs create group-name
                    if (args.length != 2) return false;

                    if (!FileHandler.loadGroupStorage(args[1]).isEmpty) {
                        sender.sendMessage(ChatColor.RED + "This storage already exists!");
                        return true;
                    }

                    if (StoragePlus.activeConfig.validateStorage(player, 1)) {
                        GroupStorage cgs = new GroupStorage(args[1], 1, player.getUniqueId());
                        FileHandler.saveStorage(cgs);
                        sender.sendMessage(ChatColor.GREEN + "Group Storage successfully created!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have enough money!");
                    }

                    break;
                case "remove":   // /gs remove group-name player123

                    if (args.length != 3) return false;

                    GroupStorage rgs = FileHandler.loadGroupStorage(args[1]);

                    if (rgs.isEmpty) return false;

                    if (rgs.owner.equals(player.getUniqueId())) {

                        if (rgs.accessList.contains(Bukkit.getOfflinePlayer(args[2]).getUniqueId())) {
                            sender.sendMessage(String.valueOf(rgs.accessList.size()));
                            rgs.accessList.remove(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                            sender.sendMessage(String.valueOf(rgs.accessList.size()));
                            sender.sendMessage(ChatColor.GREEN + args[2] + " has been removed from the group");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Player not in group");
                        }

                        FileHandler.saveStorage(rgs);

                    }
                    break;
                case "setowner": // /gs setowner group-name player123
                    if (args.length != 3) return false;

                    GroupStorage sgs = FileHandler.loadGroupStorage(args[1]);

                    if (sgs.isEmpty) return false;

                    if (sgs.owner.equals(player.getUniqueId())) {
                        if (sgs.accessList.contains( Bukkit.getOfflinePlayer(args[2]).getUniqueId() )) {
                            sgs.owner = Bukkit.getOfflinePlayer(args[2]).getUniqueId();
                            FileHandler.saveStorage(sgs);
                        }
                    }
                    break;

                case "delete":

                    if (args.length != 2) return false;

                    GroupStorage dgs = FileHandler.loadGroupStorage(args[2]);

                    if (!dgs.isEmpty) {
                        if (dgs.owner.equals(player.getUniqueId())) {
                            FileHandler.deleteStorage(dgs.uuid);
                            sender.sendMessage(ChatColor.GREEN + "Group Storages successfully removed!");
                        }
                    }

                    break;
                case "leave":
                    if (args.length != 2) return false;
                    GroupStorage lgs = FileHandler.loadGroupStorage(args[1]);

                    if (!lgs.isEmpty) {
                        if (lgs.owner.equals(player.getUniqueId())) {
                            sender.sendMessage(ChatColor.GOLD + "You own this storage! Delete it to get rid of it!");
                            return true;
                        }
                        if (lgs.accessList.contains(player.getUniqueId())) {
                            lgs.accessList.remove(player.getUniqueId());
                            sender.sendMessage(ChatColor.GREEN + "You have left this storage!");
                            FileHandler.saveStorage(lgs);
                        }
                    }
                    break;
            }

        } else {
            sender.sendMessage("This is for players only!");
        }
        return true;
    }
}
