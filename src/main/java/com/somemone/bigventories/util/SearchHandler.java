package com.somemone.bigventories.util;

import com.somemone.bigventories.Bigventories;
import com.somemone.bigventories.storage.ChunkStorage;
import com.somemone.bigventories.storage.GroupStorage;
import com.somemone.bigventories.storage.PersonalStorage;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class SearchHandler {

    public PersonalStorage searchPersonalStorage (Player player) {
        for (PersonalStorage ps : Bigventories.personalStorages) {
            if (ps.owner == player) {
                return ps;
            }
        }

        return null;
    }

    public PersonalStorage searchPersonalStorage (UUID uuid) {
        for (PersonalStorage ps : Bigventories.personalStorages) {
            if (ps.uuid == uuid) {
                return ps;
            }
        }
        return null;
    }


    public ChunkStorage searchChunkStorage (int x, int z) {
        for (ChunkStorage cs : Bigventories.chunkStorages) {
            if (cs.x == x && cs.z == z) {
                return cs;
            }
        }
        return null;
    }

    public ChunkStorage searchChunkStorage (Chunk chunk) {
        for (ChunkStorage cs : Bigventories.chunkStorages) {
            if (cs.checkChunk(chunk)) {
                return cs;
            }
        }
        return null;
    }

    public ChunkStorage searchChunkStorage (UUID uuid) {
        for (ChunkStorage cs : Bigventories.chunkStorages) {
            if (cs.uuid == uuid) {
                return cs;
            }
        }
        return null;
    }


    public GroupStorage searchGroupStorage (Player owner) {
        for (GroupStorage gs : Bigventories.groupStorages) {
            if (gs.owner == owner.getUniqueId()) {
                return gs;
            }
        }
        return null;
    }

    public ArrayList<GroupStorage> searchGroupStorages (UUID uuid) {
        ArrayList<GroupStorage> list = new ArrayList<>();

        for (GroupStorage gs : Bigventories.groupStorages) {
            if (gs.accessList.contains(list)) {
                list.add(gs);
            }
        }
        return list;
    }

    public GroupStorage searchGroupStorage (UUID uuid) {
        for (GroupStorage gs : Bigventories.groupStorages) {
            if (gs.uuid == uuid) {
                return gs;
            }
        }
        return null;
    }

    public GroupStorage searchGroupStorage (String name) {
        for (GroupStorage gs : Bigventories.groupStorages) {
            if (gs.name == name) {
                return gs;
            }
        }
        return null;
    }
}
