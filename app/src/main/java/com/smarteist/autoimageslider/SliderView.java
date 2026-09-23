package com.smarteist.autoimageslider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Lightweight single-page-at-a-time slider used by the stories screen.
 * The public methods mirror the methods called by the original launcher code.
 */
public class SliderView extends FrameLayout {
    private SliderViewAdapter adapter;
    private int currentPage;
    private CurrentPageListener currentPageListener;
    private final SliderPager sliderPager = new SliderPager();
    private final PagerIndicator pagerIndicator = new PagerIndicator();

    public SliderView(Context context) {
        super(context);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSliderAdapter(SliderViewAdapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            adapter.setDataSetChangedListener(new Runnable() {
                @Override
                public void run() {
                    renderCurrentPage();
                }
            });
        }
        currentPage = 0;
        renderCurrentPage();
    }

    public void setSliderTransformAnimation(int animation) {
        // Kept for source compatibility; this local implementation does not animate.
    }

    public void setCurrentPageListener(CurrentPageListener listener) {
        this.currentPageListener = listener;
    }

    public int getCurrentPagePosition() {
        return currentPage;
    }

    public void setCurrentPagePosition(int position) {
        int next = position;
        if (adapter == null || adapter.getCount() == 0) {
            next = 0;
        } else {
            next = Math.max(0, Math.min(position, adapter.getCount() - 1));
        }
        boolean changed = next != currentPage;
        currentPage = next;
        renderCurrentPage();
        if (changed && currentPageListener != null) {
            currentPageListener.onPageSelected(currentPage);
        }
    }

    public SliderPager getSliderPager() {
        return sliderPager;
    }

    public PagerIndicator getPagerIndicator() {
        return pagerIndicator;
    }

    private void renderCurrentPage() {
        removeAllViews();
        if (adapter == null || adapter.getCount() == 0) {
            return;
        }
        SliderViewAdapter.ViewHolder holder = adapter.onCreateViewHolder(this);
        adapter.onBindViewHolder(holder, currentPage);
        addView(holder.itemView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public interface CurrentPageListener {
        void onPageSelected(int position);
    }

    public class SliderPager {
        public void setCurrentItem(int position, boolean smoothScroll) {
            setCurrentPagePosition(position);
        }
    }

    public static class PagerIndicator {
        public void setSelection(int position) {
            // Indicator drawing is intentionally omitted from this compatibility layer.
        }
    }
}
