package com.somemone.storageplus.command;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.ChunkStorage;
import com.somemone.storageplus.storage.OpenStorage;
import com.somemone.storageplus.util.FileHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class ChunkStorageCommand  implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            ChunkStorage cs = FileHandler.loadChunkStorage(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());

            if (!cs.isEmpty) {
                if (args.length == 0) {
                    for (OpenStorage os : StoragePlus.openStorages) {
                        if (os.uuid.equals(cs.uuid)) {

                            player.openInventory(os.inventory.get(0));
                            return true;

                        }
                    }

                    ArrayList<Inventory> inventories = cs.buildInventories();
                    OpenStorage os = new OpenStorage(inventories, cs.uuid, true, cs.rows);
                    StoragePlus.openStorages.add(os);
                    player.openInventory(os.inventory.get(0));

                    return true;

                }
            }

            if (args.length == 0) return false;

            switch (args[0]) {
                case "create":
                    if (StoragePlus.activeConfig.validateStorage(player, 1)) {
                        cs = new ChunkStorage(1, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                        FileHandler.saveStorage(cs);
                        sender.sendMessage(ChatColor.GREEN + "Chunk Storage has been created");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have enough money!");
                    }

                    break;

                case "upgrade":
                    if (!cs.isEmpty) {
                        int rowsToAdd = 1;
                        try {
                            rowsToAdd = Integer.parseInt(args[1]);
                        } catch (NumberFormatException | NullPointerException ignored) { }

                        if (StoragePlus.activeConfig.validateStorage(player, rowsToAdd)) {
                            cs.addRows(rowsToAdd);
                            FileHandler.saveStorage(cs);
                            sender.sendMessage(ChatColor.GREEN + "Personal Storage successfully upgraded!");
                        } else {
                            sender.sendMessage(ChatColor.RED + "You don't have enough money!");
                        }
                    }
                    break;
            }

        } else {
            sender.sendMessage("This is for players only");
        }

        return true;
    }
}
