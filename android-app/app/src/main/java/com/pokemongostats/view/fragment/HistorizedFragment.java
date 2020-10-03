package com.pokemongostats.view.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pokemongostats.controller.HistoryService;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.commands.CompensableCommand;

/**
 * @param <T> Bean representing all information of this fragment
 * @author Zapagon
 */
public abstract class HistorizedFragment<T> extends Fragment {

    protected T currentItem;

    protected View currentView;

    private HistoryService<CompensableCommand> historyService;

    /**
     * @return the historyService
     */
    public HistoryService<CompensableCommand> getHistoryService() {
        return historyService;
    }

    /**
     * @param historyService the historyService to set
     */
    public void setHistoryService(HistoryService<CompensableCommand> historyService) {
        this.historyService = historyService;
    }

    /**
     * @return the currentItem
     */
    public T getCurrentItem() {
        return currentItem;
    }

    /**
     * @param item the currentItem to set
     */
    public void setCurrentItem(T item) {
        currentItem = item;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TagUtils.SAVE, "onSaveInstanceState " + this.getClass().getName()
                + " item: " + currentItem);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TagUtils.SAVE, "onActivityCreated " + this.getClass().getName()
                + " item: " + currentItem);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TagUtils.DEBUG, "onCreate " + this.getClass().getName() + " item: "
                + currentItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TagUtils.DEBUG, "onCreateView " + this.getClass().getName() + " item: "
                + currentItem);
        return v;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    /**
     * Change visible view with given item
     *
     * @param item Item to show
     */
    public void showItem(final T item) {
        if (item == null || item.equals(currentItem)) {
            return;
        }

        CompensableCommand cmd = new ChangeItemCommand(item);
        Log.d(TagUtils.DEBUG, "showItem: " + cmd);
        cmd.execute();
        if(historyService != null){
            historyService.add(cmd);
        } else {
            Log.i(TagUtils.DEBUG, "No history service");
        }

        updateView();
    }

    /**
     * Update current view
     */
    public final void updateView() {
        if (currentView == null) {
            Log.d(TagUtils.DEBUG, "updateView FAIL " + this.getClass().getName());
            return;
        } else {
            Log.d(TagUtils.DEBUG, "updateView OK " + this.getClass().getName());
        }
        updateViewImpl();
    }

    /**
     * Update current view
     */
    protected abstract void updateViewImpl();

    public class ChangeItemCommand implements CompensableCommand {
        private T lastItem, newItem;

        ChangeItemCommand(T newItem) {
            this.lastItem = currentItem;
            this.newItem = newItem;
        }

        @Override
        public void execute() {
            Log.d(TagUtils.HIST, "=== Before execute " + this);
            currentItem = newItem;
            Log.d(TagUtils.HIST, "=== After execute " + this);
        }

        @Override
        public void compensate() {
            Log.d(TagUtils.HIST, "=== Before compensate " + this);
            currentItem = lastItem;
            Log.d(TagUtils.HIST, "=== After compensate " + this);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "ChangeItemCommand [lastItem=" + lastItem + ", newItem="
                    + newItem + "]";
        }

    }
}
