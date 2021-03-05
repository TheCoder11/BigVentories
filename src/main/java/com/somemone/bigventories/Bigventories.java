package com.somemone.bigventories;

import com.somemone.bigventories.command.ChunkStorageCommand;
import com.somemone.bigventories.command.GroupStorageCommand;
import com.somemone.bigventories.command.PersonalStorageCommand;
import com.somemone.bigventories.listener.InventoryListener;
import com.somemone.bigventories.storage.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

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
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    public void saveStorages () {
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

            ItemStack[] items = (ItemStack[]) ps.items.toArray();
            String uuid = ps.uuid.toString();
            String owner = ps.owner.getUniqueId().toString();

            con.set("pstorages." + uuid + ".contents", items);
            con.set("pstorages." + uuid + ".owner", owner);
            con.set("pstorages." + uuid + ".rows", Integer.toString( ps.rows ));
            con.set("pstorages." + uuid + ".uuid", uuid);

        }

        for ( ChunkStorage cs : Bigventories.chunkStorages) {

            ItemStack[] items = (ItemStack[]) cs.items.toArray();
            String uuid = cs.uuid.toString();

            con.set("cstorages." + uuid + ".contents", items);
            con.set("cstorages." + uuid + ".xpos", cs.x);
            con.set("cstorages." + uuid + ".zpos", cs.z);
            con.set("cstorages." + uuid + ".uuid", uuid);
            con.set("cstorages." + uuid + ".rows", cs.rows);

        }
        for ( GroupStorage gs : Bigventories.groupStorages) {

            ItemStack[] items = (ItemStack[]) gs.items.toArray();
            String uuid = gs.uuid.toString();
            String owner = gs.owner.getUniqueId().toString();

            String[] 

            con.set("gstorages." + uuid + ".contents", items);
            con.set("gstorages." + uuid + ".rows", gs.rows);
            con.set("gstorages." + uuid + ".name", gs.name);
            con.set("gstorages." + uuid + ".players", gs.accessList.toArray());
            con.set("gstorages." + uuid + ".owner", owner);

        }
    }

    public void loadStorages () {

    }
}
