package com.somemone.bigventories;

import com.somemone.bigventories.storage.ChunkStorage;
import com.somemone.bigventories.storage.GroupStorage;
import com.somemone.bigventories.storage.PersonalStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class Bigventories extends JavaPlugin {

    public static HashMap<Player, ArrayList<Inventory>> currentInventoryLists;
    public static ArrayList<PersonalStorage> personalStorages;
    public static ArrayList<ChunkStorage> chunkStorages;
    public static ArrayList<GroupStorage> groupStorages;

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    public void saveStorages () {

    }

    public void loadStorages () {

    }
}
