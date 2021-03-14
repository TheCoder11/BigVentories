package com.somemone.bigventories;

import com.somemone.bigventories.command.AdminCommand;
import com.somemone.bigventories.command.ChunkStorageCommand;
import com.somemone.bigventories.command.GroupStorageCommand;
import com.somemone.bigventories.command.PersonalStorageCommand;
import com.somemone.bigventories.listener.InventoryListener;
import com.somemone.bigventories.listener.VoucherListener;
import com.somemone.bigventories.storage.*;
import com.somemone.bigventories.util.ConfigHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public final class StoragePlus extends JavaPlugin {

    public static ArrayList<OpenStorage> openStorages;
    public static ConfigHandler configHandler;

    public static ArrayList<PersonalStorage> personalStorages;
    public static ArrayList<ChunkStorage> chunkStorages;
    public static ArrayList<GroupStorage> groupStorages;
    public static StoragePlus plugin;

    public static ArrayList<Player> closedByPlugin;

    public static File dataFolder;

    private static Economy econ = null;

    @Override
    public void onEnable() {
        
        personalStorages = new ArrayList<>();
        chunkStorages = new ArrayList<>();
        groupStorages = new ArrayList<>();
        openStorages = new ArrayList<>();
        closedByPlugin = new ArrayList<>();
        dataFolder = this.getDataFolder();
        configHandler = new ConfigHandler(getConfig());
        plugin = this;

         getServer().getPluginManager().registerEvents(new InventoryListener(), this);
         getServer().getPluginManager().registerEvents(new VoucherListener(), this);

         getCommand("pstorage").setExecutor(new PersonalStorageCommand());
         getCommand("cstorage").setExecutor(new ChunkStorageCommand());
         getCommand("gstorage").setExecutor(new GroupStorageCommand());
         getCommand("bvadmin").setExecutor(new AdminCommand());

        try {
            loadStorages();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (!setupEconomy()) {
            Logger.getLogger("Minecraft").severe("Bigventories disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic

        try {
            saveStorages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void saveStorages () throws IOException {
        // Closing all OpenStorages to permanent storage

        for (OpenStorage os : StoragePlus.openStorages) {

            Storage st = null;
            for (PersonalStorage ps : StoragePlus.personalStorages) {
                if (ps.uuid == os.uuid) {
                    st = ps;
                }
            }
            for (ChunkStorage cs : StoragePlus.chunkStorages) {
                if (cs.uuid == os.uuid) {
                    st = cs;
                }
            }
            for (GroupStorage gs : StoragePlus.groupStorages) {
                if (gs.uuid == os.uuid) {
                    st = gs;
                }
            }

            if (st != null) {

                ArrayList<ItemStack> totalItems = new ArrayList<>();

                for (Inventory inv : os.inventory) {
                    ArrayList<ItemStack> items = new ArrayList<>();
                    if (inv.getContents().length > 0) {
                        for (ItemStack item : inv.getContents()) {
                            items.add(item);
                        }
                    }

                    int removed = 0;
                    while (removed < 9) {
                        items.remove(items.size() - 1);
                    }

                    totalItems.addAll(items);
                }

                st.items = totalItems;

            }

        }

        YamlConfiguration con = new YamlConfiguration();

        for ( PersonalStorage ps : StoragePlus.personalStorages) {

            ItemStack[] items = ps.items.toArray(new ItemStack[0]);
            String uuid = ps.uuid.toString();
            String owner = "";
            try {
                owner = ps.owner.getUniqueId().toString();
            } catch (NullPointerException ignored) {}

            con.set("pstorages." + uuid + ".contents", items);
            con.set("pstorages." + uuid + ".owner", owner);
            con.set("pstorages." + uuid + ".rows", ps.rows);

        }

        for ( ChunkStorage cs : StoragePlus.chunkStorages) {

            ItemStack[] items = cs.items.toArray(new ItemStack[0]);
            String uuid = cs.uuid.toString();

            con.set("cstorages." + uuid + ".contents", items);
            con.set("cstorages." + uuid + ".xpos", cs.x);
            con.set("cstorages." + uuid + ".zpos", cs.z);
            con.set("cstorages." + uuid + ".rows", cs.rows);

        }
        for ( GroupStorage gs : StoragePlus.groupStorages) {

            ItemStack[] items = gs.items.toArray(new ItemStack[0]);
            String uuid = gs.uuid.toString();
            String owner = gs.owner.toString();

            List<String> uuids = new ArrayList<>();
            for (UUID puuid : gs.accessList) {
                if (puuid != null) {
                    uuids.add(puuid.toString());
                }
            }


            con.set("gstorages." + uuid + ".contents", items);
            con.set("gstorages." + uuid + ".rows", gs.rows);
            con.set("gstorages." + uuid + ".name", gs.name);
            con.set("gstorages." + uuid + ".players", uuids);
            con.set("gstorages." + uuid + ".owner", owner);

        }

        con.save( new File (StoragePlus.dataFolder, "storages.yml"));
    }

    public static void loadStorages () throws IOException, InvalidConfigurationException {
        YamlConfiguration con = new YamlConfiguration();
        con.load( new File( StoragePlus.dataFolder, "storages.yml"));
        ArrayList<String> keys;

        // load PersonalStorages

        if (con.contains("pstorages")) {
            ConfigurationSection pStorages = con.getConfigurationSection("pstorages");

            keys = new ArrayList<>();
            if (pStorages.getKeys(false) != null) {
                keys.addAll(pStorages.getKeys(false));

                for (String key : keys) {

                    UUID uuid = UUID.fromString(key);

                    OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(pStorages.getString(key + ".owner")));

                    ArrayList<ItemStack> contents = (ArrayList<ItemStack>) pStorages.getList(key + ".contents");

                    int rows = pStorages.getInt(key + ".rows");

                    StoragePlus.personalStorages.add(new PersonalStorage(rows, uuid, owner, contents));

                }
            }
        }
        // load ChunkStorages

        if (con.contains("cstorages")) {
            ConfigurationSection cStorages = con.getConfigurationSection("cstorages");

            keys = new ArrayList<>();
            if (cStorages.getKeys(false) != null) {
                keys.addAll(cStorages.getKeys(false));

                for (String key : keys) {

                    UUID uuid = UUID.fromString(key);

                    ArrayList<ItemStack> contents = (ArrayList<ItemStack>) cStorages.getList(key + ".contents");

                    int rows = cStorages.getInt(key + ".rows");

                    int x = cStorages.getInt(key + ".xpos");

                    int z = cStorages.getInt(key + ".zpos");

                    StoragePlus.chunkStorages.add(new ChunkStorage(rows, uuid, contents, x, z));

                }
            }
        }
        // load GroupStorages

        ConfigurationSection gStorages = con.getConfigurationSection("gstorages");

        if (con.contains("gstorages")) {
            keys = new ArrayList<>();
            if (gStorages.getKeys(false) != null) {
                keys.addAll(gStorages.getKeys(false));

                for (String key : keys) {

                    UUID uuid = UUID.fromString(key);

                    ArrayList<ItemStack> contents = (ArrayList<ItemStack>) gStorages.getList(key + ".contents");

                    int rows = gStorages.getInt(key + ".rows");

                    UUID owner = UUID.fromString(gStorages.getString(key + ".owner"));

                    ArrayList<String> accessStringList = (ArrayList<String>) gStorages.getStringList(key + ".players");

                    ArrayList<UUID> accessList = new ArrayList<>();
                    for (String player : accessStringList) {
                        if ( !(accessList.contains( UUID.fromString(player)))) {
                            accessList.add(UUID.fromString(player));
                        }
                    }

                    String name = gStorages.getString(key + ".name");

                    StoragePlus.groupStorages.add(new GroupStorage(name, rows, contents, owner, uuid, accessList));

                }
            }
        }

    }

    public static Economy getEcon() {
        return econ;
    }

    public static int getInvitedGroupStorages ( UUID uuid ) {

        int invited = 0;
        for (GroupStorage gs : StoragePlus.groupStorages) {
            if (gs.accessList.contains( uuid )) {
                invited++;
            }
        }

        return invited;

    }

    private boolean setupEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;

    }

}
