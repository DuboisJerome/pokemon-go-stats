package com.pokemongostats.view.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import androidx.collection.LruCache;
import androidx.core.content.ContextCompat;

import com.pokemongostats.controller.TaskRunner;
import com.pokemongostats.model.bean.bdd.PkmnDesc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.function.Consumer;

public class PkmnDrawableCache {

	private static final int cacheSize = 8 * 1024 * 1024;
	private static final LruCache<String,Drawable> cachedPkmnDrawables = new LruCache<>(
			cacheSize);
	private static final String DEFAULT_DRAWABLE_ID = "DEFAULT_DRAWABLE_ID";

	private static String getCacheId(long pokedexNum, String form) {
		return pokedexNum + "_" + form.toLowerCase();
	}

	private static Drawable getDefaultDrawable(Context c) {
		Drawable d = cachedPkmnDrawables.get(DEFAULT_DRAWABLE_ID);
		if (d == null) {
			d = ContextCompat.getDrawable(c, android.R.drawable.ic_menu_help);
			if(d != null){
				cachedPkmnDrawables.put(DEFAULT_DRAWABLE_ID, d);
			}
		}
		return d;
	}

	private static Drawable get(Context c, long pokedexNum, String form) {
		if (c == null) {
			return null;
		}

		String id = PkmnDrawableCache.getCacheId(pokedexNum, form);

		Drawable d = cachedPkmnDrawables.get(id);
		if (d == null) {
			AssetManager assetManager = c.getAssets();
			try {
				InputStream ims = assetManager.open("iconspkmn/pokemon_" + id+".png");
				d = Drawable.createFromStream(ims, null);
				if(d != null) {
					cachedPkmnDrawables.put(id, d);
				}
			} catch (IOException ex) {
				// tente de recupérer la forme normal
				if (!form.toLowerCase(Locale.getDefault()).equals("normal")) {
					d = get(c, pokedexNum, "normal");
				}
			}
		}
		if (d == null) {
			d = getDefaultDrawable(c);
		}
		return d;
	}

	private static Drawable get(Context c, PkmnDesc p) {
		return get(c, p.getPokedexNum(), p.getForm());
	}

	public static void getAsync(Context c, PkmnDesc p, Consumer<Drawable> consumer) {
		WeakReference<Context> wc= new WeakReference<>(c);
		new TaskRunner().executeAsync(() -> PkmnDrawableCache.get(wc.get(), p),
				new TaskRunner.Callback<>() {
					@Override
					public void onComplete(Drawable d) {
						consumer.accept(d);
					}

					@Override
					public void onError(Exception e) {
						throw new RuntimeException(e);
					}
				});
	}
}