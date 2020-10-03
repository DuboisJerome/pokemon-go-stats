/**
 *
 */
package com.pokemongostats.view.listeners;

import com.pokemongostats.model.bean.Move;

/**
 * @author Zapagon
 */
public interface HasMoveSelectable {
    void acceptSelectedVisitorMove(final SelectedVisitor<Move> visitor);
}
