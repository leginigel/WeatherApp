package j.com.weatherapp;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

public class CityRecyclerItemTouch extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;
    private CityListAdapter adapter;

    public CityRecyclerItemTouch(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener, CityListAdapter adapter) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
        this.adapter = adapter;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View bottomShadow = ((CityListAdapter.ViewHolder) viewHolder).BottomShadow;
            final View foregroundView = ((CityListAdapter.ViewHolder) viewHolder).CityCard;
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
                bottomShadow.setAlpha(1.0f);
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((CityListAdapter.ViewHolder) viewHolder).CityCard;
        getDefaultUIUtil().clearView(foregroundView);

        final View bottomShadow = ((CityListAdapter.ViewHolder) viewHolder).BottomShadow;
        bottomShadow.setAlpha(0f);
        getDefaultUIUtil().clearView(viewHolder.itemView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if (adapter.getItemCount() > 1) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                Log.d("onChildDraw", "SWIPE" + recyclerView.getAdapter().getItemCount());
                final View foregroundView = ((CityListAdapter.ViewHolder) viewHolder).CityCard;
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                Log.d("onChildDraw", "DRAG"+adapter.getItemCount());


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        if (viewHolder.getItemViewType() != viewHolder1.getItemViewType() || adapter.getItemCount() == 1) {
            return false;
        }
        listener.onMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (adapter.getItemCount() > 1)
            listener.onSwiped(viewHolder, i, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

        void onMove(int fromPosition, int toPosition);
    }
}
