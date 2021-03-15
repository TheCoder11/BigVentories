package com.somemone.bigventories.command;

import com.somemone.bigventories.StoragePlus;
import com.somemone.bigventories.storage.GroupStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (StoragePlus.currentInvites.containsKey(player.getUniqueId())) {

                UUID groupUUID = StoragePlus.currentInvites.get(player.getUniqueId());
                for (GroupStorage gs : StoragePlus.groupStorages) {

                    if (gs.uuid == groupUUID) {
                        gs.addPlayer(player);
                        player.sendMessage(ChatColor.GOLD + "You have been added to group " + gs.name + "!");
                    }

                }

            } else {
                sender.sendMessage(ChatColor.GOLD + "You are not currently invited to any group storages!");
            }
        }

        return true;
    }
}
