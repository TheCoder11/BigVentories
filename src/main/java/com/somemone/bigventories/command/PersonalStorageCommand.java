package com.somemone.bigventories.command;

import com.somemone.bigventories.Bigventories;
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

        switch (args[0]) {
            case "create":

                for ( PersonalStorage ups : Bigventories.personalStorages) {
                    if (ups.owner == player) {
                        sender.sendMessage(ChatColor.RED + "You already have a personal storage!");
                    }
                }
                PersonalStorage cps = new PersonalStorage( 1, player );
                Bigventories.personalStorages.add(cps);

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

            default:
                if (Bigventories.personalStorages.size() > 0) {

                    for (PersonalStorage ps : Bigventories.personalStorages) {

                        if (ps.owner == player) {

                            ArrayList<Inventory> inventories = ps.buildInventories();

                            Bigventories.currentInventoryLists.put(player, inventories);

                            player.openInventory( inventories.get(0) );

                            return true;

                        }

                    }

                    sender.sendMessage(ChatColor.RED + "You do not own a Personal Storage!");

                }
        }

        return false;
    }
}
