package com.somemone.storageplus.command;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.GroupStorage;
import com.somemone.storageplus.storage.Storage;
import com.somemone.storageplus.util.FileHandler;
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

                GroupStorage gs = FileHandler.loadGroupStorage(groupUUID);

                if (!gs.isEmpty) return false;

                gs.addPlayer(player);
                player.sendMessage(ChatColor.GOLD + "You have been added to group " + gs.name + "!");
                StoragePlus.currentInvites.remove(player.getUniqueId());

                FileHandler.saveStorage(gs);

            } else {
                sender.sendMessage(ChatColor.GOLD + "You are not currently invited to any group storages!");
            }
        }

        return true;
    }
}
