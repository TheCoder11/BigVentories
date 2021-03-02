package com.somemone.bigventories;

import com.somemone.bigventories.command.ChunkStorageCommand;
import com.somemone.bigventories.command.GroupStorageCommand;
import com.somemone.bigventories.command.PersonalStorageCommand;
import com.somemone.bigventories.listener.InventoryListener;
import com.somemone.bigventories.storage.ChunkStorage;
import com.somemone.bigventories.storage.GroupStorage;
import com.somemone.bigventories.storage.OpenStorage;
import com.somemone.bigventories.storage.PersonalStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class Bigventories extends JavaPlugin {

    public static HashMap<Player, HashMap<String, ArrayList<Inventory>>> currentInventoryLists;
    public static ArrayList<OpenStorage> openStorages;
    public static ArrayList<PersonalStorage> personalStorages;
    public static ArrayList<ChunkStorage> chunkStorages;
    public static ArrayList<GroupStorage> groupStorages;

    @Override
    public void onEnable() {

        currentInventoryLists = new HashMap<>();
        personalStorages = new ArrayList<>();
        chunkStorages = new ArrayList<>();
        groupStorages = new ArrayList<>();

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

    }

    public void loadStorages () {

    }
}
