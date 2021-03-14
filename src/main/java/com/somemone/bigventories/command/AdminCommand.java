package com.somemone.bigventories.command;

import com.somemone.bigventories.StoragePlus;
import com.somemone.bigventories.item.VoucherItem;
import com.somemone.bigventories.storage.ChunkStorage;
import com.somemone.bigventories.storage.GroupStorage;
import com.somemone.bigventories.storage.OpenStorage;
import com.somemone.bigventories.storage.PersonalStorage;
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

            if (args.length == 4) {
                switch (args[1]) {

                    case "ps":
                        PersonalStorage ps = null;
                        for (PersonalStorage pes : StoragePlus.personalStorages) {
                            if (pes.owner == Bukkit.getPlayer(args[2])) {
                                ps = pes;
                            }
                        }

                        switch (args[0]) {
                            case "view":
                                if (sender.hasPermission("bva.view")) {
                                    if (StoragePlus.openStorages.size() > 0) {
                                        for (OpenStorage os : StoragePlus.openStorages) {
                                            if (os.uuid == ps.uuid) {
                                                player.openInventory(os.inventory.get(0));

                                                return true;
                                            }
                                        }
                                    }

                                    ArrayList<Inventory> inventories = ps.buildInventories();
                                    OpenStorage openStorage = new OpenStorage(inventories, ps.uuid);
                                    StoragePlus.openStorages.add(openStorage);
                                    player.openInventory(openStorage.inventory.get(0));

                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                                }
                                break;

                            case "delete":

                                if (sender.hasPermission("bva.delete")) {
                                    if (StoragePlus.openStorages.size() > 0) {

                                        for (OpenStorage os : StoragePlus.openStorages) {
                                            if (os.uuid == ps.uuid) {
                                                for (Player viewer : os.getViewers()) {
                                                    viewer.closeInventory();
                                                }
                                                StoragePlus.personalStorages.remove(ps);
                                                sender.sendMessage(ChatColor.GREEN + args[2] + "'s Personal Storage has been wiped!");
                                            }
                                        }
                                    }

                                } else {
                                    sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                                }
                                break;
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

                        ChunkStorage cs = null;
                        for (ChunkStorage chs : StoragePlus.chunkStorages) {
                            if (chs.x == x && chs.z == z) {
                                cs = chs;
                            }
                        }

                        if (cs == null) {
                            sender.sendMessage(ChatColor.RED + "A Chunk Storage does not exist on this ");
                        }

                        switch (args[0]) {
                            case "view":
                                if (sender.hasPermission("bva.view")) {
                                    if (StoragePlus.openStorages.size() > 0) {
                                        for (OpenStorage os : StoragePlus.openStorages) {
                                            if (os.uuid == cs.uuid) {
                                                player.openInventory(os.inventory.get(0));

                                                return true;
                                            }
                                        }
                                    }

                                    ArrayList<Inventory> inventories = cs.buildInventories();
                                    OpenStorage openStorage = new OpenStorage(inventories, cs.uuid);
                                    StoragePlus.openStorages.add(openStorage);
                                    player.openInventory(openStorage.inventory.get(0));

                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You do not have access to this command!");
                                }
                                break;
                            case "delete":

                                if (sender.hasPermission("bva.delete")) {
                                    if (StoragePlus.openStorages.size() > 0) {

                                        for (OpenStorage os : StoragePlus.openStorages) {
                                            if (os.uuid == cs.uuid) {
                                                for (Player viewer : os.getViewers()) {
                                                    viewer.closeInventory();
                                                }
                                                StoragePlus.chunkStorages.remove(cs);
                                                sender.sendMessage(ChatColor.GREEN + "Chunk Storage has been wiped!");
                                            }
                                        }
                                    }

                                } else {
                                    sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                                }
                                break;
                        }
                        break;
                    case "gs":

                        if (args.length == 3) {
                            GroupStorage gs = null;
                            for (GroupStorage grs : StoragePlus.groupStorages) {
                                if (grs.name == args[2]) {
                                    gs = grs;
                                }
                            }

                            switch (args[0]) {
                                case "view":
                                    if (sender.hasPermission("bva.view")) {
                                        if (StoragePlus.openStorages.size() > 0) {
                                            for (OpenStorage os : StoragePlus.openStorages) {
                                                if (os.uuid == gs.uuid) {
                                                    player.openInventory(os.inventory.get(0));

                                                    return true;
                                                }
                                            }
                                        }

                                        ArrayList<Inventory> inventories = gs.buildInventories();
                                        OpenStorage openStorage = new OpenStorage(inventories, gs.uuid);
                                        StoragePlus.openStorages.add(openStorage);
                                        player.openInventory(openStorage.inventory.get(0));

                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You do not have access to this command!");
                                    }
                                    break;

                                case "delete":
                                    if (sender.hasPermission("bva.delete")) {
                                        if (StoragePlus.openStorages.size() > 0) {

                                            for (OpenStorage os : StoragePlus.openStorages) {
                                                if (os.uuid == gs.uuid) {
                                                    for (Player viewer : os.getViewers()) {
                                                        viewer.closeInventory();
                                                    }
                                                    StoragePlus.groupStorages.remove(gs);
                                                    sender.sendMessage(ChatColor.GREEN + "Chunk Storage has been wiped!");
                                                }
                                            }
                                        }

                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                                    }
                                    break;
                                case "getplayers":
                                    if (sender.hasPermission("bva.getplayers")) {

                                        String playerList = "Players: " + ChatColor.GREEN + "";
                                        for (UUID uuid : gs.accessList) {
                                            if (uuid != null) {
                                                playerList = playerList + Bukkit.getOfflinePlayer(uuid).getName() + ", ";
                                            }
                                        }

                                    }
                            }
                        } else {
                            return false;
                        }
                        break;

                }
            } else if (args.length == 1 || args.length == 2) {
                switch (args[0]) {
                    case "save":
                        if (sender.hasPermission("bva.save")) {
                            try {
                                StoragePlus.saveStorages();
                                sender.sendMessage(ChatColor.GREEN + "Storages successfully saved!");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have permission!");
                        }
                        break;
                    case "load":
                        if (sender.hasPermission("bva.load")) {
                            try {
                                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Loading to last saved configuration...");

                                for (OpenStorage os : StoragePlus.openStorages) {
                                    for (Player viewer : os.getViewers()) {
                                        viewer.closeInventory();
                                    }
                                }

                                StoragePlus.openStorages = new ArrayList<>();
                                StoragePlus.personalStorages = new ArrayList<>();
                                StoragePlus.chunkStorages = new ArrayList<>();
                                StoragePlus.groupStorages = new ArrayList<>();

                                StoragePlus.loadStorages();
                            } catch (InvalidConfigurationException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case "voucher":
                        sender.sendMessage(ChatColor.GOLD + "Created Voucher!");

                        if (sender.hasPermission("bva.voucher")) {
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
