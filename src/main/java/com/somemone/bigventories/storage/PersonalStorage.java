package com.somemone.bigventories.storage;

import org.bukkit.entity.Player;

public class PersonalStorage extends Storage {

    public Player owner;

    public PersonalStorage(int rows, Player owner) {
        super(rows);

        this.owner = owner;
    }
}
