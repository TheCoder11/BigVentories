package com.somemone.bigventories.storage;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class OpenStorage {

    public UUID uuid;
    public ArrayList<Inventory> inventory;

    public OpenStorage ( ArrayList<Inventory> inventories, UUID uuid ) {
        this.inventory = inventories;
        this.uuid = uuid;
    }

    public boolean checkIfViewing (Player player) {

        return this.getViewers().contains(player);

    }

    public ArrayList<Player> getViewers () {
        ArrayList<Player> players = new ArrayList<>();

        for (Inventory inventory : this.inventory) {

            if (inventory.getViewers().size() > 0) {
                for (HumanEntity player : inventory.getViewers()) {
                    players.add((Player) player);
                }
            }

        }

        return players;

    }

}
