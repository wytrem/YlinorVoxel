package com.ylinor.client.world;

import com.ylinor.library.api.world.storage.CacheWorldStorage;
import java.io.File;

public class HardCacheWorldStorage extends CacheWorldStorage
{
    public HardCacheWorldStorage(File folder, boolean writable)
    {
        super(folder, writable);
    }
}
