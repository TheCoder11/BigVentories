package com.somemone.bigventories.storage;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ChunkStorage extends Storage {

    public int x;
    public int z;

    public ChunkStorage(int rows, int x, int z) {
        super(rows);

        this.x = x;
        this.z = z;
    }

    public boolean checkChunk( Chunk chunk ) {
        return chunk.getX() == this.x && chunk.getZ() == this.z;
    }

}
