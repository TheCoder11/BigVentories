package com.somemone.bigventories;

import com.somemone.bigventories.command.ChunkStorageCommand;
import com.somemone.bigventories.command.GroupStorageCommand;
import com.somemone.bigventories.command.PersonalStorageCommand;
import com.somemone.bigventories.listener.InventoryListener;
import com.somemone.bigventories.storage.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Bigventories extends JavaPlugin {

    public static ArrayList<OpenStorage> openStorages;

    public static ArrayList<PersonalStorage> personalStorages;
    public static ArrayList<ChunkStorage> chunkStorages;
    public static ArrayList<GroupStorage> groupStorages;

    public static ArrayList<Player> closedByPlugin;

    @Override
    public void onEnable() {
        
        personalStorages = new ArrayList<>();
        chunkStorages = new ArrayList<>();
        groupStorages = new ArrayList<>();
        openStorages = new ArrayList<>();
        closedByPlugin = new ArrayList<>();

         getServer().getPluginManager().registerEvents(new InventoryListener(), this);

         getCommand("pstorage").setExecutor(new PersonalStorageCommand());
         getCommand("cstorage").setExecutor(new ChunkStorageCommand());
         getCommand("gstorage").setExecutor(new GroupStorageCommand());

        try {
            loadStorages();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
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



    public void saveStorages () throws IOException {
        // Closing all OpenStorages to permanent storage

        for (OpenStorage os : Bigventories.openStorages) {

            Storage st = null;
            for (PersonalStorage ps : Bigventories.personalStorages) {
                if (ps.uuid == os.uuid) {
                    st = ps;
                }
            }
            for (ChunkStorage cs : Bigventories.chunkStorages) {
                if (cs.uuid == os.uuid) {
                    st = cs;
                }
            }
            for (GroupStorage gs : Bigventories.groupStorages) {
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

        for ( PersonalStorage ps : Bigventories.personalStorages) {

            ItemStack[] items = ps.items.toArray(new ItemStack[0]);
            String uuid = ps.uuid.toString();
            String owner = ps.owner.getUniqueId().toString();

            con.set("pstorages." + uuid + ".contents", items);
            con.set("pstorages." + uuid + ".owner", owner);
            con.set("pstorages." + uuid + ".rows", ps.rows);

        }

        for ( ChunkStorage cs : Bigventories.chunkStorages) {

            ItemStack[] items = cs.items.toArray(new ItemStack[0]);
            String uuid = cs.uuid.toString();

            con.set("cstorages." + uuid + ".contents", items);
            con.set("cstorages." + uuid + ".xpos", cs.x);
            con.set("cstorages." + uuid + ".zpos", cs.z);
            con.set("cstorages." + uuid + ".rows", cs.rows);

        }
        for ( GroupStorage gs : Bigventories.groupStorages) {

            ItemStack[] items = gs.items.toArray(new ItemStack[0]);
            String uuid = gs.uuid.toString();
            String owner = gs.owner.getUniqueId().toString();

            List<String> uuids = new ArrayList<>();
            for (Player player : gs.accessList) {
                uuids.add( player.getUniqueId().toString() );
            }


            con.set("gstorages." + uuid + ".contents", items);
            con.set("gstorages." + uuid + ".rows", gs.rows);
            con.set("gstorages." + uuid + ".name", gs.name);
            con.set("gstorages." + uuid + ".players", uuids);
            con.set("gstorages." + uuid + ".owner", owner);

        }

        con.save( new File (this.getDataFolder(), "storages.yml"));
    }

    public void loadStorages () throws IOException, InvalidConfigurationException {
        YamlConfiguration con = new YamlConfiguration();
        con.load( new File( this.getDataFolder(), "storages.yml"));

        // load PersonalStorages

        ConfigurationSection pStorages = con.getConfigurationSection("pstorages");

        ArrayList<String> keys = new ArrayList<>();
        if ( pStorages.getKeys(false) != null) {
            keys.addAll(pStorages.getKeys(false));

            for (String key : keys) {

                UUID uuid = UUID.fromString(key);

                Player owner = Bukkit.getPlayer(UUID.fromString(pStorages.getString(key + ".owner")));

                ArrayList<ItemStack> contents = (ArrayList<ItemStack>) pStorages.getList(key + ".contents");

                int rows = pStorages.getInt(key + ".rows");

                Bigventories.personalStorages.add(new PersonalStorage(rows, uuid, owner, contents));

            }
        }
        // load ChunkStorages

        ConfigurationSection cStorages = con.getConfigurationSection("cstorages");

        keys = new ArrayList<>();
        if ( cStorages.getKeys(false) != null) {
            keys.addAll(cStorages.getKeys(false));

            for (String key : keys) {

                UUID uuid = UUID.fromString(key);

                ArrayList<ItemStack> contents = (ArrayList<ItemStack>) cStorages.getList(key + ".contents");

                int rows = cStorages.getInt(key + ".rows");

                int x = cStorages.getInt(key + ".xpos");

                int z = cStorages.getInt(key + ".zpos");

                Bigventories.chunkStorages.add(new ChunkStorage(rows, uuid, contents, x, z));

            }
        }
        // load GroupStorages

        ConfigurationSection gStorages = con.getConfigurationSection("gstorages");

        keys = new ArrayList<>();
        if ( gStorages.getKeys(false) != null) {
            keys.addAll(gStorages.getKeys(false));

            for (String key : keys) {

                UUID uuid = UUID.fromString(key);

                ArrayList<ItemStack> contents = (ArrayList<ItemStack>) gStorages.getList(key + ".contents");

                int rows = gStorages.getInt(key + ".rows");

                Player owner = Bukkit.getPlayer(UUID.fromString(gStorages.getString(key + ".owner")));

                ArrayList<String> accessStringList = (ArrayList<String>) gStorages.getStringList(key + ".players");

                ArrayList<Player> accessList = new ArrayList<>();
                for (String player : accessStringList) {
                    accessList.add(Bukkit.getPlayer(UUID.fromString(player)));
                }

                String name = gStorages.getString(key + ".name");

                Bigventories.groupStorages.add(new GroupStorage(name, rows, contents, owner, uuid, accessList));

            }
        }

    }
}
