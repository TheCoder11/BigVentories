package com.somemone.bigventories.command;

import com.somemone.bigventories.Bigventories;
import com.somemone.bigventories.storage.ChunkStorage;
import com.somemone.bigventories.storage.GroupStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupStorageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        switch (args[0]) {

            case "upgrade":  // OwP not needed

                int rowsToAdd = 1;
                try {
                    rowsToAdd = Integer.parseInt(args[1]);
                } catch (NumberFormatException | NullPointerException ignored) { }

                if (Bigventories.groupStorages.size() > 0) {

                    for (GroupStorage upgs : Bigventories.groupStorages) {

                        if (upgs.name.equals(args[2])) {

                            upgs.rows = upgs.rows + rowsToAdd;

                        }

                    }

                    sender.sendMessage(ChatColor.RED + "There is no Chunk Storage in this zone! Create one first!");

                }

            case "invite":   // OwP needed

                if (args.length == 3) {

                    for (GroupStorage fgs : Bigventories.groupStorages) {

                        if (fgs.name == args[2] && fgs.owner == player) {

                            fgs.accessList.add(Bukkit.getPlayer(args[1]));
                            return true;

                        } else {

                            sender.sendMessage(ChatColor.RED + "You do not have access to this storage!");

                        }

                    }

                }
            case "create":   // OwP not needed

                if (args.length == 2) {
                    GroupStorage newGS = new GroupStorage( args[0], 1, player);

                    Bigventories.groupStorages.add(newGS);
                }
            case "remove":   // OwP needed, CANNOT REMOVE SELF

                if (args.length == 3) {

                    for (GroupStorage fgs : Bigventories.groupStorages) {

                        if (fgs.name == args[2] && fgs.owner == player) {

                            if (fgs.accessList.contains(Bukkit.getPlayer(args[1]))) {
                                fgs.accessList.remove(Bukkit.getPlayer(args[1]));
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED + "Player not in ");
                            }

                        } else {

                            sender.sendMessage(ChatColor.RED + "You do not have access to this storage!");

                        }

                    }

                }
            case "setowner": // OwP needed

                System.out.println("nou");
            default:         // To access inventory, OwP not needed


        }

        return true;
    }
}
