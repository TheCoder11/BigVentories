package com.somemone.storageplus.command;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.OpenStorage;
import com.somemone.storageplus.storage.PersonalStorage;
import com.somemone.storageplus.util.FileHandler;
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

            PersonalStorage ps = FileHandler.loadPersonalStorage(player.getUniqueId());

            if (args.length == 0 && ps.isEmpty) {
                sender.sendMessage(ChatColor.RED + "You do not own a Personal Storage!");
                return true;
            }

            if (args.length == 0) {
                    if (StoragePlus.openStorages.size() > 0) {
                        for (OpenStorage os : StoragePlus.openStorages) {
                            if (os.uuid.equals(ps.uuid)) {
                                player.openInventory(os.inventory.get(0));

                                return true;
                            }
                        }
                    }

                    ArrayList<Inventory> inventories = ps.buildInventories();
                    OpenStorage openStorage = new OpenStorage(inventories, ps.uuid, true);
                    StoragePlus.openStorages.add(openStorage);
                    player.openInventory(openStorage.inventory.get(0));

                    return true;
            }

            switch (args[0]) {
                case "create":
                    if (StoragePlus.activeConfig.validateStorage(player, 1)) {
                        ps = new PersonalStorage(1, player.getUniqueId());
                        FileHandler.saveStorage(ps);
                        sender.sendMessage(ChatColor.GREEN + "Personal Storage Created!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have enough money!");
                    }
                    break;
                case "upgrade":
                    if (!ps.isEmpty) {
                        int rowsToAdd = 1;
                        try {
                            rowsToAdd = Integer.parseInt(args[1]);
                        } catch (NumberFormatException | NullPointerException ignored) { }

                        if (StoragePlus.activeConfig.validateStorage(player, rowsToAdd)) {
                            ps.addRows(rowsToAdd);
                            FileHandler.saveStorage(ps);
                            sender.sendMessage(ChatColor.GREEN + "Personal Storage successfully upgraded!");
                        } else {
                            sender.sendMessage(ChatColor.RED + "You don't have enough money!");
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
