package com.somemone.bigventories.storage;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ChunkStorage extends Storage {

    public Chunk location;

    public ChunkStorage(int rows, Chunk location) {
        super(rows);

        this.location = location;
    }

    public boolean checkIfInChunk ( Player player ) {
        return player.getLocation().getChunk() == this.location;
    }

}
