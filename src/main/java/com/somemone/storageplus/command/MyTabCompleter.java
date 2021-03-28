package com.somemone.storageplus.command;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.GroupStorage;
import com.somemone.storageplus.util.FileHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MyTabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player player = (Player) sender;

        List<String> list = new ArrayList<>();

        if (command.getName().equals("sta")) {
            if (args.length == 1) {
                list.addAll(Arrays.asList("save", "load", "view", "delete", "voucher", "get-players"));
            } else if (args.length == 2) {
                switch (args[0]) {
                    case "view":
                        list.addAll(Arrays.asList("ps", "cs", "gs"));
                        break;
                    case "delete":
                        list.addAll(Arrays.asList("ps", "cs", "gs"));
                        break;
                    case "getplayers":
                        list = FileHandler.getGroupNames();
                        break;
                }
            } else if (args.length == 3) {
                switch (args[2]) {
                    case "ps":
                        for (Player playerP : Bukkit.getOnlinePlayers()) {
                            list.add(playerP.getName());
                        }
                        break;
                    case "cs":
                        list.add("here");
                        break;
                    case "gs":
                        list = FileHandler.getGroupNames();
                        break;
                }
            }
        } else if (command.getAliases().contains("ps")) {
            if (args.length == 1) {
                list.addAll(Arrays.asList("create", "upgrade"));
            }
        } else if (command.getAliases().contains("cs")) {
            if (args.length == 1) {
                list.addAll(Arrays.asList("create", "upgrade"));
            }
        } else if (command.getAliases().contains("gs")) {
            if (args.length == 1) {
                list.addAll(Arrays.asList("create", "upgrade", "invite", "remove", "leave", "setowner", "delete"));
            }
            } else if (args.length == 3) {
                switch (args[0]) {
                    case "invite":
                        for (Player playerP : Bukkit.getOnlinePlayers()) {
                            list.add(playerP.getName());
                        }
                        break;
                    case "remove":
                        list = findPlayersInStorages(FileHandler.loadGroupStorage(args[2]));
                        break;
                    case "setowner":
                        list = findPlayersInStorages(FileHandler.loadGroupStorage(args[2]));
                        break;
                }
        }

        return list;
    }

    public List<String> findPlayersInStorages (GroupStorage gs) {
        List<String> list = new ArrayList<>();
        if (gs != null) {
            for (UUID uuid : gs.accessList) {
                list.add(Bukkit.getOfflinePlayer(uuid).getName());
            }
        }
        return list;
    }
}
