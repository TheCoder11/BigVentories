package com.somemone.storageplus.util;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.ChunkStorage;
import com.somemone.storageplus.storage.GroupStorage;
import com.somemone.storageplus.storage.PersonalStorage;
import com.somemone.storageplus.storage.Storage;
import org.bukkit.ChunkSnapshot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import sun.net.www.http.ChunkedInputStream;

import java.io.File;
import java.io.IOException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.UUID;

public class FileHandler {

    public static void saveStorage (PersonalStorage ps) {
        YamlConfiguration con = new YamlConfiguration();

        con.set("owner", ps.owner.toString());
        con.set("items", ps.items);
        con.set("rows", ps.rows);
        con.set("uuid", ps.uuid.toString());

        File newFolder = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/Storages/Personal Storages", ps.uuid.toString() + ".yml");

        try {
            con.save(newFolder);
        } catch (IOException ignored) {}
    }

    public static void saveStorage (ChunkStorage cs) {
        YamlConfiguration con = new YamlConfiguration();

        con.set("items", cs.items);
        con.set("rows", cs.rows);
        con.set("uuid", cs.uuid.toString());
        con.set("xpos", cs.x);
        con.set("zpos", cs.z);

        File newFolder = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/Storages/Chunk Storages", cs.uuid.toString() + ".yml");

        try {
            con.save(newFolder);
        } catch (IOException ignored) {}
    }

    public static void saveStorage (GroupStorage gs) {
        YamlConfiguration con = new YamlConfiguration();

        con.set("items", gs.items);
        con.set("rows", gs.rows);
        con.set("uuid", gs.uuid.toString());
        con.set("accesslist", gs.accessList);
        con.set("owner", gs.owner.toString());
        con.set("name", gs.name);

        File newFolder = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/Storages/Group Storages", gs.uuid.toString() + ".yml");

        try {
            con.save(newFolder);
        } catch (IOException ignored) {}
    }

    /**
     *  Returns Personal Storage associated with player UUID provided
     *
     * @param owner
     */
    public static PersonalStorage loadPersonalStorage (UUID owner) {
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/Storages/Personal Storages").listFiles();
        if (folders.length == 0) return null;

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.contains("owner") || !con.contains("name")) {
                if (con.get("owner").equals(owner.toString())) {
                    ArrayList<ItemStack> items = (ArrayList<ItemStack>) con.getList("items");
                    UUID id = UUID.fromString(con.getString("uuid"));
                    int rows = con.getInt("rows");

                    PersonalStorage ps = new PersonalStorage(rows, id, owner, items);
                    return ps;
                }
            }
        }

        return null;
    }

    public static ChunkStorage loadChunkStorage (int x, int z) {
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/Storages/Chunk Storages").listFiles();

        if (folders.length == 0) return null;

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.contains("x")) {
                if (con.getInt("x") == x && con.getInt("z") == z) {

                    ArrayList<ItemStack> items = (ArrayList<ItemStack>) con.getList("items");
                    UUID id = UUID.fromString(con.getString("uuid"));
                    int rows = con.getInt("rows");

                    ChunkStorage cs = new ChunkStorage(rows, id, items, x, z);
                    return cs;

                }
            }
        }
        return null;
    }

    public static GroupStorage loadGroupStorage (String name) {
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/Storages/Group Storages").listFiles();

        if (folders.length == 0) return null;

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.contains("name")) {

                if (con.getString("name").equals(name)) {

                    UUID uuid = UUID.fromString(con.getString("name"));

                    ArrayList<ItemStack> contents = (ArrayList<ItemStack>) con.getList("contents");

                    int rows = con.getInt("rows");

                    UUID owner = UUID.fromString(con.getString("owner"));

                    ArrayList<String> accessStringList = (ArrayList<String>) con.getStringList("accesslist");

                    ArrayList<UUID> accessList = new ArrayList<>();
                    for (String player : accessStringList) {
                        if ( !(accessList.contains( UUID.fromString(player)))) {
                            accessList.add(UUID.fromString(player));
                        }
                    }

                    GroupStorage gs = new GroupStorage(name, rows, contents, owner, uuid, accessList);
                    return gs;

                }
            }
        }
        return null;
    }

    public static ArrayList<GroupStorage> loadAllowedGroupStorages (UUID prospect) {

        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/Storages/Group Storages").listFiles();

        if (folders.length == 0) return null;

        ArrayList<GroupStorage> groupStorages = new ArrayList<>();

        for (File file : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(file);

            if (con.contains("name")) {
                ArrayList<String> accessStringList = (ArrayList<String>) con.getStringList("accesslist");

                ArrayList<UUID> accessList = new ArrayList<>();
                for (String player : accessStringList) {
                    if ( !(accessList.contains( UUID.fromString(player)))) {
                        accessList.add(UUID.fromString(player));
                    }
                }

                if (accessList.contains(prospect)) {

                    UUID uuid = UUID.fromString(con.getString("uuid"));

                    String name = con.getString("name");

                    ArrayList<ItemStack> contents = (ArrayList<ItemStack>) con.getList("contents");

                    int rows = con.getInt("rows");

                    UUID owner = UUID.fromString(con.getString("owner"));

                    GroupStorage gs = new GroupStorage(name, rows, contents, owner, uuid, accessList);
                    groupStorages.add(gs);

                }
            }
        }

        return groupStorages;

    }

}
