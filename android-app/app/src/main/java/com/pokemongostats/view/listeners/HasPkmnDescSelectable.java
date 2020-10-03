/**
 *
 */
package com.pokemongostats.view.listeners;

import com.pokemongostats.model.bean.PkmnDesc;

/**
 * @author Zapagon
 */
public interface HasPkmnDescSelectable {
    void acceptSelectedVisitorPkmnDesc(
            final SelectedVisitor<PkmnDesc> visitor);

}
