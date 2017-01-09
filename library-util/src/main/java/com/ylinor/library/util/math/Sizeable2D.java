package com.ylinor.library.util.math;

/**
 * Définition d'un objet ayant une taille dans un plan en deux dimensions.
 *
 * @author Litarvan
 * @since 1.0.0
 */
public interface Sizeable2D {
    /**
     * @return La longueur de l'objet sur l'axe X
     */
    int getSizeX();

    /**
     * @return La longueur de l'objet sur l'axe Y
     */
    int getSizeY();
}
