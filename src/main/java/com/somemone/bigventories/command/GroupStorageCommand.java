package com.somemone.bigventories.command;

import com.somemone.bigventories.Bigventories;
import com.somemone.bigventories.storage.ChunkStorage;
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

        Player player = (Player) sender;

        if (args.length == 0) {
            if (Bigventories.groupStorages.size() > 0) {

                for (GroupStorage cs : Bigventories.groupStorages) {

                    if (cs.name.equals(args[0]) && cs.accessList.contains(player) ) {

                        //Check if inventory is open

                        for (OpenStorage os : Bigventories.openStorages) {

                            if (os.uuid == cs.uuid) {
                                os.viewers.add(player);
                                player.openInventory(os.inventory.get(0));
                                return true;
                            }

                        }

                        ArrayList<Inventory> inventories = cs.buildInventories();
                        OpenStorage os = new OpenStorage( inventories, cs.uuid);

                        Bigventories.openStorages.add(os);

                        player.openInventory( os.inventory.get(0) );

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
                } catch (NumberFormatException | NullPointerException ignored) { }

                if (Bigventories.groupStorages.size() > 0) {

                    for (GroupStorage upgs : Bigventories.groupStorages) {

                        if (upgs.name.equals(args[1])) {

                            upgs.rows = upgs.rows + rowsToAdd;

                        }

                    }

                    sender.sendMessage(ChatColor.RED + "There is no Chunk Storage in this zone! Create one first!");

                }

            case "invite":   // /gs invite player123

                if (args.length == 3) {

                    if (Bigventories.groupStorages.size() > 0) {
                        for (GroupStorage fgs : Bigventories.groupStorages) {

                            if (fgs.name.equals(args[2]) && fgs.owner == player) {

                                fgs.accessList.add(Bukkit.getPlayer(args[1]));
                                return true;

                            } else {

                                sender.sendMessage(ChatColor.RED + "You do not have access to this storage!");

                            }

                        }
                    }

                }
                break;
            case "create":   // /gs create group-name

                if (args.length == 2) {
                    GroupStorage newGS = new GroupStorage( args[1], 1, player);

                    if (Bigventories.groupStorages.size() > 0) {
                        for (GroupStorage fgs : Bigventories.groupStorages) {

                            if (fgs.name.equals(args[1])) {

                                sender.sendMessage(ChatColor.RED + "A Group Storage with this name already exists!");
                                return false;

                            }

                        }
                    }

                    Bigventories.groupStorages.add(newGS);
                }
                break;
            case "remove":   // /gs remove player123

                if (args.length == 3) {

                    if (Bigventories.groupStorages.size() > 0) {

                        for (GroupStorage fgs : Bigventories.groupStorages) {

                            if (fgs.name.equals(args[2]) && fgs.owner == player) {

                                if (fgs.accessList.contains(Bukkit.getPlayer(args[1]))) {
                                    fgs.accessList.remove(Bukkit.getPlayer(args[1]));
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

                    if (Bigventories.groupStorages.size() > 0) {
                        for (GroupStorage fgs : Bigventories.groupStorages) {

                            if (fgs.name.equals(args[1]) && fgs.owner == player) {

                                fgs.owner = Bukkit.getPlayer(args[2]);

                            }

                        }
                    }

                }
                break;
        }

        return true;
    }
}
