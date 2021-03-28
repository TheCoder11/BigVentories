package com.somemone.storageplus.command;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.item.VoucherItem;
import com.somemone.storageplus.storage.ChunkStorage;
import com.somemone.storageplus.storage.GroupStorage;
import com.somemone.storageplus.storage.OpenStorage;
import com.somemone.storageplus.storage.PersonalStorage;
import com.somemone.storageplus.util.FileHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 3) {
                switch (args[1]) {

                    case "ps":
                        PersonalStorage ps = FileHandler.loadPersonalStorage(player.getUniqueId());

                        if (ps != null) {
                            switch (args[0]) {
                                case "view":
                                    if (sender.hasPermission("sta.view")) {
                                        if (StoragePlus.openStorages.size() > 0) {
                                            for (OpenStorage os : StoragePlus.openStorages) {
                                                if (os.uuid == ps.uuid) {
                                                    player.openInventory(os.inventory.get(0));

                                                    return true;
                                                }
                                            }
                                        }

                                        sender.sendMessage(String.valueOf(ps.uuid));
                                        ArrayList<Inventory> inventories = ps.buildInventories();
                                        OpenStorage openStorage = new OpenStorage(inventories, ps.uuid, true);
                                        StoragePlus.openStorages.add(openStorage);
                                        player.openInventory(openStorage.inventory.get(0));

                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                                    }
                                    break;

                                case "delete":

                                    if (sender.hasPermission("sta.delete")) {
                                        if (StoragePlus.openStorages.size() > 0) {

                                            for (OpenStorage os : StoragePlus.openStorages) {
                                                if (os.uuid == ps.uuid) {
                                                    for (Player viewer : os.getViewers()) {
                                                        viewer.closeInventory();
                                                    }
                                                }
                                            }
                                        }
                                        FileHandler.deleteStorage(ps.uuid);
                                        sender.sendMessage(ChatColor.GREEN + args[2] + "'s Personal Storage has been wiped!");

                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                                    }
                                    break;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Cannot find that player's Personal Storage!");
                        }
                        break;
                    case "cs":

                        int x = 0;
                        int z = 0;

                        if (args[2].equals("here")) {
                            x = ((Player) sender).getLocation().getChunk().getX();
                            z = ((Player) sender).getLocation().getChunk().getZ();
                        } else if (args.length == 4) {
                            try {
                                x = Integer.parseInt(args[2]);
                                z = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        } else {
                            return false;
                        }

                        ChunkStorage cs = FileHandler.loadChunkStorage(x, z);

                        if (!cs.isEmpty) {
                            sender.sendMessage(ChatColor.RED + "A Chunk Storage does not exist on this chunk");
                        }

                        switch (args[0]) {
                            case "view":
                                if (sender.hasPermission("sta.view")) {
                                    if (StoragePlus.openStorages.size() > 0) {
                                        for (OpenStorage os : StoragePlus.openStorages) {
                                            if (os.uuid == cs.uuid) {
                                                player.openInventory(os.inventory.get(0));

                                                return true;
                                            }
                                        }
                                    }

                                    ArrayList<Inventory> inventories = cs.buildInventories();
                                    OpenStorage openStorage = new OpenStorage(inventories, cs.uuid, true);
                                    StoragePlus.openStorages.add(openStorage);
                                    player.openInventory(openStorage.inventory.get(0));

                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You do not have access to this command!");
                                }
                                break;
                            case "delete":

                                if (sender.hasPermission("sta.delete")) {
                                    if (StoragePlus.openStorages.size() > 0) {
                                        for (OpenStorage os : StoragePlus.openStorages) {
                                            if (os.uuid == cs.uuid) {
                                                for (Player viewer : os.getViewers()) {
                                                    viewer.closeInventory();
                                                }
                                            }
                                        }
                                    }
                                    FileHandler.deleteStorage(cs.uuid);
                                    sender.sendMessage(ChatColor.GREEN + "Chunk Storage has been wiped!");

                                } else {
                                    sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                                }
                                break;
                        }
                        break;
                    case "gs":

                        if (args.length == 3) {
                            GroupStorage gs = FileHandler.loadGroupStorage(args[2]);

                            switch (args[0]) {
                                case "view":
                                    if (sender.hasPermission("sta.view")) {
                                        if (StoragePlus.openStorages.size() > 0) {
                                            for (OpenStorage os : StoragePlus.openStorages) {
                                                if (os.uuid == gs.uuid) {
                                                    player.openInventory(os.inventory.get(0));

                                                    return true;
                                                }
                                            }
                                        }

                                        ArrayList<Inventory> inventories = gs.buildInventories();
                                        OpenStorage openStorage = new OpenStorage(inventories, gs.uuid, true);
                                        StoragePlus.openStorages.add(openStorage);
                                        player.openInventory(openStorage.inventory.get(0));

                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You do not have access to this command!");
                                    }
                                    break;

                                case "delete":
                                    if (sender.hasPermission("sta.delete")) {
                                        if (StoragePlus.openStorages.size() > 0) {
                                            for (OpenStorage os : StoragePlus.openStorages) {
                                                if (os.uuid == gs.uuid) {
                                                    for (Player viewer : os.getViewers()) {
                                                        viewer.closeInventory();
                                                    }
                                                }
                                            }
                                        }

                                        FileHandler.deleteStorage(gs.uuid);
                                        sender.sendMessage(ChatColor.GREEN + "Group Storage has been wiped!");

                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                                    }
                                    break;
                                case "getplayers":
                                    if (sender.hasPermission("sta.getplayers")) {

                                        String playerList = "Players: " + ChatColor.GREEN + "";
                                        for (UUID uuid : gs.accessList) {
                                            if (uuid != null) {
                                                playerList = playerList + Bukkit.getOfflinePlayer(uuid).getName() + ", ";
                                            }
                                        }

                                        sender.sendMessage(playerList);

                                    }
                            }
                        } else {
                            return false;
                        }
                        break;

                }
            } else if (args.length == 1 || args.length == 2) {
                switch (args[0]) {
                    case "voucher":
                        sender.sendMessage(ChatColor.GOLD + "Created Voucher!");

                        if (sender.hasPermission("sta.voucher")) {
                            if (args.length == 2) {
                                ItemStack item = new VoucherItem( Integer.parseInt(args[1]) ).getItem();
                                player.getInventory().addItem(item);
                            } else {
                                return false;
                            }

                        }
                }
            }

        } else {
            sender.sendMessage("Only players can use this command!");
        }

        return true;
    }
}
