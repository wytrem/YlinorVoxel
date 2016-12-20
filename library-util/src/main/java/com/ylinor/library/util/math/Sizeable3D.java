package com.ylinor.library.util.math;

/**
 * Définition d'un objet ayant une taille dans un plan
 * en trois dimensions.
 *
 * @author Litarvan
 * @since 1.0.0
 */
public interface Sizeable3D extends Sizeable2D
{
    /**
     * @return La longueur de l'objet sur l'axe Z
     */
    int getSizeZ();
}
