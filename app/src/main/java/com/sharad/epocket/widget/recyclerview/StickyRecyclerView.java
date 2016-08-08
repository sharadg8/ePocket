package com.sharad.epocket.widget.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Implements a ListView class with a sticky header at the top. The header is
 * per section and it is pinned to the top as long as its section is at the top
 * of the view. If it is not, the header slides up or down (depending on the
 * scroll movement) and the header of the current section slides to the top.
 * Notes:
 * 1. The class uses the first available child ListView as the working
 *    ListView. If no ListView child exists, the class will create a default one.
 * 2. The ListView's adapter must be passed to this class using the 'setAdapter'
 *    method. The adapter must implement the HeaderIndexer interface. If no adapter
 *    is specified, the class will try to extract it from the ListView
 * 3. The class registers itself as a listener to scroll events (OnScrollListener), if the
 *    ListView needs to receive scroll events, it must register its listener using
 *    this class' setOnScrollListener method.
 * 4. Headers for the list view must be added before using the StickyRecyclerView
 */
public class StickyRecyclerView extends FrameLayout {
    private static final String TAG = "StickyRecyclerView";
    protected boolean mChildViewsCreated = false;
    protected boolean mDoHeaderReset = false;

    protected Context mContext = null;
    protected Adapter mAdapter = null;
    protected HeaderIndexer mIndexer = null;
    protected HeaderHeightListener mHeaderHeightListener = null;
    protected View mStickyHeader = null;
    protected View mDummyHeader = null; // A invisible header used when a section has no header
    protected RecyclerView mRecyclerView = null;
    protected LinearLayoutManager mLayoutManager = null;

    private int mLastStickyHeaderHeight = 0;
    protected int mCurrentSectionPos = -1; // Position of section that has its header on the
                                           // top of the view
    protected int mNextSectionPosition = -1; // Position of next section's header
    private boolean refreshHeader = true;

    /**
     * Interface that must be implemented by the ListView adapter to provide headers locations
     * and number of items under each header.
     *
     */
    public interface HeaderIndexer {
        /**
         * Calculates the position of the header of a specific item in the adapter's data set.
         * For example: Assuming you have a list with albums and songs names:
         * Album A, song 1, song 2, ...., song 10, Album B, song 1, ..., song 7. A call to
         * this method with the position of song 5 in Album B, should return  the position
         * of Album B.
         * @param position - Position of the item in the RecyclerView dataset
         * @return Position of header. -1 if the is no header
         */

        int getHeaderPositionFromItemPosition(int position);

        /**
         * Calculates the number of items in the section defined by the header (not including
         * the header).
         * For example: A list with albums and songs, the method should return
         * the number of songs names (without the album name).
         *
         * @param headerPosition - the value returned by 'getHeaderPositionFromItemPosition'
         * @return Number of items. -1 on error.
         */
        int getHeaderItemsNumber(int headerPosition);

        /**
         * Provide a header view, with content set to the header position.
         *
         * @param headerPosition - the value returned by 'getHeaderPositionFromItemPosition'
         * @return View view to be displayed as header.
         */
        View getHeaderView(int headerPosition);
    }

    /***
    *
    * Interface that is used to update the sticky header's height
    *
    */
   public interface HeaderHeightListener {

       /***
        * Updated a change in the sticky header's size
        *
        * @param height - new height of sticky header
        */
       void OnHeaderHeightChanged(int height);
   }

    /**
     * Sets the adapter to be used by the class to get views of headers
     *
     * @param adapter - The adapter.
     */
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mAdapter = adapter;
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * Sets the indexer object (that implements the HeaderIndexer interface).
     *
     * @param indexer - The indexer.
     */

    public void setIndexer(HeaderIndexer indexer) {
        mIndexer = indexer;
    }

    /**
     * Sets the list view that is displayed
     * @param rv - The list view.
     */
    public void setRecyclerView(RecyclerView rv) {
        mRecyclerView = rv;
        mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                updateStickyHeader(mLayoutManager.findFirstVisibleItemPosition());
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    /**
     * Constructor
     *
     * @param context - application context.
     * @param attrs - layout attributes.
     */
    public StickyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
     }

    public void invalidateHeaderView() {
        refreshHeader = true;
        updateStickyHeader(mLayoutManager.findFirstVisibleItemPosition());
    }

    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    protected void updateStickyHeader(int firstVisibleItem) {
        // Try to make sure we have an adapter to work with (may not succeed).
        if (mAdapter == null && mRecyclerView != null) {
            setAdapter(mRecyclerView.getAdapter());
        }

        if (mAdapter != null && mIndexer != null && mDoHeaderReset) {
            // Get the section header position
            int sectionSize = 0;
            int sectionPos = mIndexer.getHeaderPositionFromItemPosition(firstVisibleItem);

            // New section - set it in the header view
            boolean newView = false;
            if ((sectionPos != mCurrentSectionPos) || refreshHeader){
                refreshHeader = false;
                // No header for current position , use the dummy invisible one, hide the separator
                if (sectionPos == -1) {
                    sectionSize = 0;
                    this.removeView(mStickyHeader);
                    mStickyHeader = mDummyHeader;
                    newView = true;
                } else {
                    // Create a copy of the header view to show on top
                    sectionSize = mIndexer.getHeaderItemsNumber(sectionPos);
                    View v = mIndexer.getHeaderView(sectionPos);
                    ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT, Gravity.TOP);
                    v.setLayoutParams(params);
                    this.removeView(mStickyHeader);
                    mStickyHeader = v;
                    newView = true;
                }
                mCurrentSectionPos = sectionPos;
                mNextSectionPosition = sectionSize + sectionPos + 1;
            }

            // Do transitions
            // If position of bottom of last item in a section is smaller than the height of the
            // sticky header - shift drawable of header.
            if (mStickyHeader != null) {
                int sectionLastItemPosition =  mNextSectionPosition - firstVisibleItem - 1;
                /* 10 subtracted to avoid the shadow visible from view behind */
                int stickyHeaderHeight = mStickyHeader.getHeight() - 10;
                if (stickyHeaderHeight == 0) {
                    stickyHeaderHeight = mStickyHeader.getMeasuredHeight();
                }

                // Update new header height
                if (mHeaderHeightListener != null &&
                        mLastStickyHeaderHeight != stickyHeaderHeight) {
                    mLastStickyHeaderHeight = stickyHeaderHeight;
                    mHeaderHeightListener.OnHeaderHeightChanged(stickyHeaderHeight);
                }

                View SectionLastView = mRecyclerView.getChildAt(sectionLastItemPosition);
                if (SectionLastView != null && SectionLastView.getBottom() <= stickyHeaderHeight) {
                    int lastViewBottom = SectionLastView.getBottom();
                    mStickyHeader.setTranslationY(lastViewBottom - stickyHeaderHeight);
                } else if (stickyHeaderHeight != 0) {
                    mStickyHeader.setTranslationY(0);
                }
                if (newView) {
                    mStickyHeader.setVisibility(View.INVISIBLE);
                    this.addView(mStickyHeader);
                    mStickyHeader.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!mChildViewsCreated) {
            setChildViews();
        }
        mDoHeaderReset = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mChildViewsCreated) {
            setChildViews();
        }
        mDoHeaderReset = true;
    }

    private void setChildViews() {
        // Find a child ListView (if any)
        int iChildNum = getChildCount();
        for (int i = 0; i < iChildNum; i++) {
            Object v = getChildAt(i);
            if (v instanceof RecyclerView) {
                setRecyclerView((RecyclerView) v);
            }
        }

        // No child ListView - add one
        if (mRecyclerView == null) {
            RecyclerView rv = new RecyclerView(mContext);
            ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, Gravity.FILL);
            rv.setLayoutParams(params);
            this.addView(rv, 0);
            setRecyclerView(rv);
        }

        // Create a dummy view , it will be used in case a section has no header
        mDummyHeader = new View (mContext);
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                1, Gravity.TOP);
        mDummyHeader.setLayoutParams(params);
        mDummyHeader.setBackgroundColor(Color.TRANSPARENT);

        mChildViewsCreated = true;
    }
}
