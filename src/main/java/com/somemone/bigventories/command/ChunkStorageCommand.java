package com.somemone.bigventories.command;

import com.somemone.bigventories.Bigventories;
import com.somemone.bigventories.storage.ChunkStorage;
import com.somemone.bigventories.storage.OpenStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ChunkStorageCommand  implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                if (Bigventories.chunkStorages.size() > 0) {

                    for (ChunkStorage cs : Bigventories.chunkStorages) {

                        if (cs.checkChunk(player.getLocation().getChunk())) {

                            for (OpenStorage os : Bigventories.openStorages) {
                                if (os.uuid == cs.uuid) {

                                    player.openInventory(os.inventory.get(0));
                                    return true;

                                }
                            }


                            ArrayList<Inventory> inventories = cs.buildInventories();
                            OpenStorage os = new OpenStorage(inventories, cs.uuid);
                            Bigventories.openStorages.add(os);
                            player.openInventory(os.inventory.get(0));

                            return true;

                        }

                    }

                    sender.sendMessage(ChatColor.RED + "There is no Chunk Storage in this zone!");
                    return true;

                }
            }

            switch (args[0]) {
                case "create":

                    ChunkStorage newCS = new ChunkStorage(1, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());

                    for (ChunkStorage cs : Bigventories.chunkStorages) {

                        if (cs.x == newCS.x && cs.z == newCS.z) {

                            sender.sendMessage(ChatColor.RED + "A Chunk Storage already exists in this area!");
                            return true;

                        }

                    }

                    sender.sendMessage(String.valueOf( Bigventories.configHandler.getPersonalStoragePrice(1) ));
                    sender.sendMessage(String.valueOf( Bigventories.plugin.getConfig().getBoolean("personal-storage.purchaseable") ));

                    if (Bigventories.configHandler.getPersonalStoragePrice(1) < Bigventories.getEcon().getBalance(player) && Bigventories.configHandler.getPersonalStoragePrice(1) != 0) {
                        Bigventories.getEcon().withdrawPlayer(player, Bigventories.configHandler.getPersonalStoragePrice(1));
                        Bigventories.chunkStorages.add(newCS);
                        sender.sendMessage(ChatColor.GREEN + "Chunk Storage successfully created!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Insufficient Funds");
                    }

                    break;

                case "upgrade":

                    // NOTE: Must add Vault+Config!

                    int rowsToAdd = 1;
                    try {
                        rowsToAdd = Integer.parseInt(args[1]);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                    }

                    if (Bigventories.chunkStorages.size() > 0) {

                        for (ChunkStorage upcs : Bigventories.chunkStorages) {

                            if (upcs.checkChunk(player.getLocation().getChunk())) {

                                if (Bigventories.getEcon().getBalance(player) > Bigventories.configHandler.getChunkStoragePrice(rowsToAdd) && Bigventories.configHandler.getChunkStoragePrice(1) > 0) {
                                    Bigventories.getEcon().withdrawPlayer(player, Bigventories.configHandler.getChunkStoragePrice(rowsToAdd));
                                    upcs.rows = upcs.rows + rowsToAdd;
                                    sender.sendMessage(ChatColor.GREEN + "Chunk Storage successfully upgraded!");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Insufficient Funds");
                                }

                                return true;
                            }

                        }

                        sender.sendMessage(ChatColor.RED + "There is no Chunk Storage in this zone! Create one first!");

                    }
                    break;

            }

        } else {
            sender.sendMessage("This is for players only");
        }

        return true;
    }
}
