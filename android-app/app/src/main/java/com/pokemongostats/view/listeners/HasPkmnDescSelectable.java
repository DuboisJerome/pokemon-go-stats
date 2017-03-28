/**
 *
 */
package com.pokemongostats.view.listeners;

import com.pokemongostats.model.bean.PkmnDesc;

/**
 * @author Zapagon
 */
public interface HasPkmnDescSelectable {
    public void acceptSelectedVisitorPkmnDesc(
            final SelectedVisitor<PkmnDesc> visitor);

}
