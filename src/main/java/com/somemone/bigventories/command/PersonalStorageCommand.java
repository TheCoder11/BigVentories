package com.somemone.bigventories.command;

import com.somemone.bigventories.Bigventories;
import com.somemone.bigventories.storage.OpenStorage;
import com.somemone.bigventories.storage.PersonalStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class PersonalStorageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length == 0) {
            if (Bigventories.personalStorages.size() > 0) {

                for (PersonalStorage ps : Bigventories.personalStorages) {

                    if (ps.owner == player) {

                        // Check for open inventory

                        if (Bigventories.openStorages.size() > 0) {
                            for (OpenStorage os : Bigventories.openStorages) {
                                if (os.uuid == ps.uuid) {
                                    sender.sendMessage("Found an Open Storage!");
                                    player.openInventory(os.inventory.get(0));

                                    return true;
                                }
                            }
                        }

                        ArrayList<Inventory> inventories = ps.buildInventories();

                        OpenStorage openStorage = new OpenStorage( inventories, ps.uuid );

                        Bigventories.openStorages.add(openStorage);

                        player.openInventory( openStorage.inventory.get(0) );

                        return true;

                    }

                }
            }
            sender.sendMessage(ChatColor.RED + "You do not own a Personal Storage!");
            return true;
        }

        switch (args[0]) {
            case "create":

                if (Bigventories.personalStorages.size() > 0) {
                    for (PersonalStorage ups : Bigventories.personalStorages) {
                        if (ups.owner == player) {
                            sender.sendMessage(ChatColor.RED + "You already have a personal storage!");
                            break;
                        }
                    }
                }

                PersonalStorage cps = new PersonalStorage(7, player);
                Bigventories.personalStorages.add(cps);
                sender.sendMessage(ChatColor.GREEN + "Personal Storage successfully created!");
                break;

            case "upgrade":
                int rowsToAdd = 1;
                try {
                    rowsToAdd = Integer.parseInt(args[1]);
                } catch (NumberFormatException | NullPointerException ignored) { }

                if (Bigventories.personalStorages.size() > 0) {

                    for (PersonalStorage upps : Bigventories.personalStorages) {

                        if (upps.owner == player) {

                            upps.rows = upps.rows + rowsToAdd;

                        }

                    }

                }

                break;


        }

        return true;
    }
}
