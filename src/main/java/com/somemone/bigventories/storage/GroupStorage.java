package com.somemone.bigventories.storage;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GroupStorage extends Storage {

    public Player owner;
    public ArrayList<Player> accessList;
    public String name;

    public GroupStorage(String name, int rows, Player owner) {
        super(rows);

        this.owner = owner;
        this.accessList = new ArrayList<>();
        this.accessList.add(owner);
        this.name = name;
    }

    public void addPlayer ( Player added ) {
        this.accessList.add(added);
    }

    public void removePlayer ( Player removed ) {
        this.accessList.add(removed);
    }

    public boolean checkAccess (Player checking) {
        return this.accessList.contains(checking);
    }
}
