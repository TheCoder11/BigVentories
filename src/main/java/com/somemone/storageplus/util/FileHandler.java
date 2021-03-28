package com.somemone.storageplus.util;

import com.somemone.storageplus.StoragePlus;
import com.somemone.storageplus.storage.*;
import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import sun.net.www.http.ChunkedInputStream;

import java.io.File;
import java.io.IOException;
import java.security.acl.Group;
import java.util.*;

public class FileHandler {

    public static void saveStorage (PersonalStorage ps) {
        YamlConfiguration con = new YamlConfiguration();

        con.set("owner", ps.owner.toString());
        con.set("items", ps.items);
        con.set("rows", ps.rows);
        con.set("uuid", ps.uuid.toString());
        con.set("type", "personal");

        File newFolder = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages", ps.uuid.toString() + ".yml");

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
        con.set("type", "chunk");

        File newFolder = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages", cs.uuid.toString() + ".yml");

        try {
            con.save(newFolder);
        } catch (IOException ignored) {}
    }

    public static void saveStorage (GroupStorage gs) {
        YamlConfiguration con = new YamlConfiguration();

        con.set("items", gs.items);
        con.set("rows", gs.rows);
        con.set("uuid", gs.uuid.toString());

        ArrayList<String> uuidStringLis = new ArrayList<>();
        for (UUID accessUUID : gs.accessList) {
            uuidStringLis.add(accessUUID.toString());
        }

        con.set("accesslist", uuidStringLis);
        con.set("owner", gs.owner.toString());
        con.set("name", gs.name);
        con.set("type", "group");

        File newFolder = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages", gs.uuid.toString() + ".yml");

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
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles();
        if (folders == null) return new PersonalStorage();

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.getString("type").equals("personal")) {
                if (Objects.equals(con.getString("owner"), owner.toString())) {
                    ArrayList<ItemStack> items = (ArrayList<ItemStack>) con.getList("items");
                    UUID id = UUID.fromString(con.getString("uuid"));
                    int rows = con.getInt("rows");

                    PersonalStorage ps = new PersonalStorage(rows, id, owner, items);
                    return ps;
                }
            }
        }

        return new PersonalStorage();
    }

    public static PersonalStorage loadPersonalStorageFromID (UUID id) {
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles();
        if (folders == null) return new PersonalStorage();

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.getString("type").equals("personal")) {
                if (Objects.equals(con.getString("uuid"), id.toString())) {
                    ArrayList<ItemStack> items = (ArrayList<ItemStack>) con.getList("items");
                    UUID owner = UUID.fromString(con.getString("owner"));
                    int rows = con.getInt("rows");

                    PersonalStorage ps = new PersonalStorage(rows, id, owner, items);
                    return ps;
                }
            }
        }

        return new PersonalStorage();
    }

    public static ChunkStorage loadChunkStorage (int x, int z) {
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles();

        if (folders.length == 0) return new ChunkStorage();

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.getString("type").equals("chunk")) {
                if (con.getInt("xpos") == x && con.getInt("zpos") == z) {

                    ArrayList<ItemStack> items = (ArrayList<ItemStack>) con.getList("items");
                    UUID id = UUID.fromString(con.getString("uuid"));
                    int rows = con.getInt("rows");

                    ChunkStorage cs = new ChunkStorage(rows, id, items, x, z);
                    return cs;

                }
            }
        }
        return new ChunkStorage();
    }

    public static ChunkStorage loadChunkStorage (UUID id) {
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles();

        if (folders.length == 0) return new ChunkStorage();

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.getString("type").equals("chunk")) {
                if (con.getString("uuid").equals(id.toString())) {

                    ArrayList<ItemStack> items = (ArrayList<ItemStack>) con.getList("items");
                    int x = con.getInt("xpos");
                    int z = con.getInt("zpos");
                    int rows = con.getInt("rows");

                    ChunkStorage cs = new ChunkStorage(rows, id, items, x, z);
                    return cs;

                }
            }
        }
        return new ChunkStorage();
    }

    public static GroupStorage loadGroupStorage (String name) {
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles();

        if (folders== null) return new GroupStorage();

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (Objects.equals(con.getString("type"), "group")) {

                if (con.getString("name").equals(name)) {

                    UUID uuid = UUID.fromString(con.getString("uuid"));

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
        return new GroupStorage();
    }

    public static GroupStorage loadGroupStorage (UUID id) {
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles();

        if (folders == null) return new GroupStorage();

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.getString("type").equals("group")) {

                if (con.getString("uuid").equals(id.toString())) {

                    String name = con.getString("name");

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

                    GroupStorage gs = new GroupStorage(name, rows, contents, owner, id, accessList);
                    return gs;

                }
            }
        }
        return new GroupStorage();
    }


    public static ArrayList<GroupStorage> loadAllowedGroupStorages (UUID prospect) {

        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles();
        ArrayList<GroupStorage> groupStorages = new ArrayList<>();

        if (folders == null) return groupStorages;

        for (File file : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(file);

            if (con.getString("type").equals("group")) {
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

    public static void deleteStorage (UUID storageID) {
        List<File> folders = Arrays.asList(new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles());


        if (folders.size() == 0) return;

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            if (con.contains("uuid")) {
                if (con.getString("uuid").equals(storageID.toString())) {
                    storageFile.delete();
                }
            }
        }

    }

    public static List<String> getGroupNames () {
        List<String> list = new ArrayList<>();
        File[] folders = new File(StoragePlus.getPlugin(StoragePlus.class).getDataFolder() + "/storages").listFiles();

        if (folders.length == 0) return list;

        for (File storageFile : folders) {
            YamlConfiguration con = YamlConfiguration.loadConfiguration(storageFile);

            list.add(con.getString("name"));
        }

        return list;
    }
}
