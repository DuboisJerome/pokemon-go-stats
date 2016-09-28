/**
 * 
 */
package com.pokemongostats.controller.utils;

import com.pokemongostats.model.HasID;

/**
 * @author Zapagon
 *
 */
public class DBHelper {

	private DBHelper() {
	}

	/**
	 * Ids to string
	 * 
	 * @param separator
	 * @param params
	 * @return
	 */
	public static <T> String arrayToStringWithSeparators(final T... params) {
		if (params != null && params.length > 0) {
			final StringBuilder b = new StringBuilder("");
			for (T t : params) {
				if (t != null) {
					b.append(String.valueOf(t));
					b.append(Constants.SEPARATOR);
				}
			}
			int idxLastComa = b.lastIndexOf(Constants.SEPARATOR);
			if (idxLastComa >= 0) {
				b.replace(idxLastComa, idxLastComa + 1, "");
			}
			return b.toString();
		}

		return "";
	}

	/**
	 * 
	 * @param hasID
	 * @return return null if (hasID == null || hasID.getId() == HasID.NO_ID)
	 *         else hasID.getID()
	 */
	public static Long getIdForDB(final HasID hasID) {
		return (hasID == null || hasID.getId() == HasID.NO_ID)
				? null
				: hasID.getId();
	}
}
