package com.somemone.bigventories.command;

import com.somemone.bigventories.StoragePlus;
import com.somemone.bigventories.storage.OpenStorage;
import com.somemone.bigventories.storage.PersonalStorage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class PersonalStorageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                if (StoragePlus.personalStorages.size() > 0) {

                    for (PersonalStorage ps : StoragePlus.personalStorages) {

                        if (ps.owner == (OfflinePlayer) player) {

                            // Check for open inventory

                            if (StoragePlus.openStorages.size() > 0) {
                                for (OpenStorage os : StoragePlus.openStorages) {
                                    if (os.uuid == ps.uuid) {
                                        player.openInventory(os.inventory.get(0));

                                        return true;
                                    }
                                }
                            }

                            ArrayList<Inventory> inventories = ps.buildInventories();

                            OpenStorage openStorage = new OpenStorage(inventories, ps.uuid);

                            StoragePlus.openStorages.add(openStorage);

                            player.openInventory(openStorage.inventory.get(0));

                            return true;

                        }

                    }
                }
                sender.sendMessage(ChatColor.RED + "You do not own a Personal Storage!");
                return true;
            }

            switch (args[0]) {
                case "create":

                    if (StoragePlus.personalStorages.size() > 0) {
                        for (PersonalStorage ups : StoragePlus.personalStorages) {
                            if (ups.owner == player) {
                                sender.sendMessage(ChatColor.RED + "You already have a personal storage!");
                                return true;
                            }
                        }
                    }

                    if (StoragePlus.configHandler.getPersonalStoragePrice(1) < StoragePlus.getEcon().getBalance(player) && StoragePlus.configHandler.getPersonalStoragePrice(1) != 0) {
                        StoragePlus.getEcon().withdrawPlayer(player, StoragePlus.configHandler.getPersonalStoragePrice(1));
                        PersonalStorage cps = new PersonalStorage(7, player);
                        StoragePlus.personalStorages.add(cps);

                        sender.sendMessage(ChatColor.GREEN + "Personal Storage successfully created!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Insufficient funds!");
                    }

                    break;

                case "upgrade":
                    int rowsToAdd = 1;
                    try {
                        rowsToAdd = Integer.parseInt(args[1]);
                    } catch (NumberFormatException | NullPointerException ignored) {
                    }

                    if (StoragePlus.personalStorages.size() > 0) {

                        for (PersonalStorage upps : StoragePlus.personalStorages) {

                            if (upps.owner == player) {

                                if (StoragePlus.configHandler.getPersonalStoragePrice(rowsToAdd) < StoragePlus.getEcon().getBalance(player) && StoragePlus.configHandler.getPersonalStoragePrice(1) != 0) {
                                    StoragePlus.getEcon().withdrawPlayer(player, StoragePlus.configHandler.getPersonalStoragePrice(rowsToAdd));
                                    upps.rows = upps.rows + rowsToAdd;
                                    sender.sendMessage(ChatColor.GREEN + "Personal Storage successfully upgraded!");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Insufficient funds!");
                                }

                            }

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
