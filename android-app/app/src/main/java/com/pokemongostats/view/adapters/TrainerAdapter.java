/**
 *
 */
package com.pokemongostats.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.view.utils.TeamUtils;

import java.util.List;

/**
 * @author Zapagon
 */
public class TrainerAdapter extends ArrayAdapter<Trainer> {

    public TrainerAdapter(Context context, int textViewResourceId,
                          List<Trainer> list) {
        super(context, textViewResourceId, list);
    }

    public TrainerAdapter(Context applicationContext,
                          int simpleSpinnerItem) {
        super(applicationContext, simpleSpinnerItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, v, parent);
        return getTextViewAtPosition(textView, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View v, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, v,
                parent);
        return getTextViewAtPosition(textView, position);
    }

    /**
     * Change text view color & text from Trainer at position
     *
     * @param textView
     * @param position
     * @return textView
     */
    private TextView getTextViewAtPosition(TextView textView, int position) {
        Trainer t = getItem(position);
        // color
        Integer idColor = TeamUtils.getTeamIdColor(t.getTeam());
        if (idColor != null) {
            textView.setTextColor(
                    getContext().getResources().getColor(idColor));
        }
        // text
        textView.setText(t.getName() + " - " + t.getLevel());
        return textView;
    }

}