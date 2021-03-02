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

import java.util.ArrayList;

public class ChunkStorageCommand  implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length == 0) {
            if (Bigventories.chunkStorages.size() > 0) {

                for (ChunkStorage cs : Bigventories.chunkStorages) {

                    if ( cs.location == player.getLocation().getChunk()) {

                        for (OpenStorage os : Bigventories.openStorages) {
                            if (os.uuid == cs.uuid) {

                                os.viewers.add(player);
                                player.openInventory(os.inventory.get(0));
                                return true;

                            }
                        }

                        ArrayList<Inventory> inventories = cs.buildInventories();
                        OpenStorage os = new OpenStorage(inventories, cs.uuid);

                        Bigventories.openStorages.add(os);
                        player.openInventory( os.inventory.get(0) );

                        return true;

                    }

                }

                sender.sendMessage(ChatColor.RED + "There is no Chunk Storage in this zone!");

            }
        }

        switch (args[0]) {
            case "create":

                ChunkStorage newCS = new ChunkStorage(1, player.getLocation().getChunk());
                Bigventories.chunkStorages.add(newCS);
                break;

            case "upgrade":

                // NOTE: Must add Vault+Config!

                int rowsToAdd = 1;
                try {
                    rowsToAdd = Integer.parseInt(args[1]);
                } catch (NumberFormatException | NullPointerException ignored) { }

                if (Bigventories.chunkStorages.size() > 0) {

                    for (ChunkStorage upcs : Bigventories.chunkStorages) {

                        if ( upcs.location == player.getLocation().getChunk()) {

                            upcs.rows = upcs.rows + rowsToAdd;

                        }

                    }

                    sender.sendMessage(ChatColor.RED + "There is no Chunk Storage in this zone! Create one first!");

                }
                break;

        }



        return false;
    }
}
