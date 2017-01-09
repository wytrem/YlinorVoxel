package com.ylinor.library.api.world.provider;

import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.World;


/**
 * Un Chunk Provider
 *
 * Un Chunk Provider est un objet fournissant un chunk aux potitions fournies,
 * seulement si le chunk n'a pas déjà été chargé. Il peut aussi décharger des
 * chunks
 *
 * Il peut par exemple le charger depuis des fichiers, depuis le network, etc...
 *
 * @author Litarvan
 * @since 1.0.0
 */
public interface IChunkProvider {
    /**
     * Fournit le chunk à la position donnée
     *
     * @param pos La position du chunk à fournir
     *
     * @return Le chunk à fournir
     */
    Chunk provide(World world, int x, int z);
}
