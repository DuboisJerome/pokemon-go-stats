package com.pokemongostats.view.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import androidx.collection.LruCache;
import androidx.core.content.ContextCompat;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.PkmnDesc;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.function.Consumer;

public class PkmnDrawableCache {

    private static final int cacheSize = 8 * 1024 * 1024;
    private static final LruCache<String, Drawable> cachedPkmnDrawables = new LruCache<>(
            cacheSize);
    private static final String DEFAULT_DRAWABLE_ID = "DEFAULT_DRAWABLE_ID";

    private static Drawable get(String id) {
        return cachedPkmnDrawables.get(id);
    }

    private static Drawable get(PkmnDesc p) {
        return get(getCacheId(p));
    }

    private static String getCacheId(final PkmnDesc p) {
        return getCacheId(p.getPokedexNum(), p.getForm());
    }

    private static String getCacheId(long pokedexNum, String form) {
        return pokedexNum + "_" + form.toLowerCase();
    }

    private static void put(String id, Drawable d) {
        cachedPkmnDrawables.put(id, d);
    }

    private static void put(PkmnDesc p, Drawable d) {
        cachedPkmnDrawables.put(getCacheId(p), d);
    }

    private static Drawable getDefaultDrawable(Context c) {
        Drawable d = get(DEFAULT_DRAWABLE_ID);
        if (d == null) {
            d = ContextCompat.getDrawable(c, R.drawable.pokeball_close);
            put(DEFAULT_DRAWABLE_ID, d);
        }
        return d;
    }

    private static Drawable get(Context c, long pokedexNum, String form) {
        if(c == null){
            return null;
        }
        String id = PkmnDrawableCache.getCacheId(pokedexNum, form);
        // pre execute
        String uri = "@drawable/pokemon_" + id;
        String packageName = c.getPackageName();
        int imgRes = c.getResources().getIdentifier(uri, null, packageName);

        Drawable d = PkmnDrawableCache.get(id);
        if (d == null) {
            try {
                d = ContextCompat.getDrawable(c, imgRes);
                PkmnDrawableCache.put(id, d);
            } catch (Resources.NotFoundException e) {
                // tente de recupérer la forme normal
                if (!form.toLowerCase(Locale.getDefault()).equals("normal")) {
                    d = get(c, pokedexNum, "normal");
                }
            }
        }
        if(d == null){
            d = getDefaultDrawable(c);
        }
        return d;
    }

    private static Drawable get(Context c, PkmnDesc  p) {
        return get(c, p.getPokedexNum(), p.getForm());
    }

    public static void getAsync(Context c, PkmnDesc p, Consumer<Drawable> consumer){
        new PkmnDrawableAsyncTask(c, p, consumer).execute();
    }

    static class PkmnDrawableAsyncTask extends AsyncTask<Object, Object, Drawable> {
        private WeakReference<Context> c;
        private PkmnDesc pkmnDesc;
        private Consumer<Drawable> consumer;

        private PkmnDrawableAsyncTask(Context c, PkmnDesc pkmnDesc, Consumer<Drawable> consumer) {
            this.c = new WeakReference<>(c);;
            this.pkmnDesc = pkmnDesc;
            this.consumer = consumer;
        }

        @Override
        protected void onPreExecute() {
            // Load drawable
            //Drawable d = PkmnDrawableCache.get(c, pkmnDesc);
        }

        @Override
        protected Drawable doInBackground(Object[] objects) {
            return PkmnDrawableCache.get(c.get(), pkmnDesc);
        }

        @Override
        protected void onPostExecute(Drawable d) {
            consumer.accept(d);
        }
    }
}
