package com.somemone.storageplus.storage;

import org.bukkit.Chunk;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class ChunkStorage extends Storage {

    public int x;
    public int z;

    public ChunkStorage(int rows, int x, int z) {
        super(rows);

        this.x = x;
        this.z = z;
    }

    public ChunkStorage(int rows, UUID uuid, ArrayList<ItemStack> items, int x, int z) {
        super(rows, uuid, items);

        this.x = x;
        this.z = z;
    }

    public boolean checkChunk( Chunk chunk ) {
        return chunk.getX() == this.x && chunk.getZ() == this.z;
    }

}
