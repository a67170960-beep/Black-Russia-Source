package com.smarteist.autoimageslider;

import android.view.View;
import android.view.ViewGroup;

/**
 * Small local adapter API compatible with the subset used by the launcher.
 * It keeps the stories screen buildable without the unavailable JitPack artifact.
 */
public abstract class SliderViewAdapter<VH extends SliderViewAdapter.ViewHolder> {
    private Runnable dataSetChangedListener;

    public abstract VH onCreateViewHolder(ViewGroup parent);

    public abstract void onBindViewHolder(VH holder, int position);

    public abstract int getCount();

    public void notifyDataSetChanged() {
        if (dataSetChangedListener != null) {
            dataSetChangedListener.run();
        }
    }

    void setDataSetChangedListener(Runnable listener) {
        this.dataSetChangedListener = listener;
    }

    public abstract static class ViewHolder {
        public final View itemView;

        protected ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }
}
