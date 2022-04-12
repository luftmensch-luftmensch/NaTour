/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.utils.handler;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
  private OnItemClickListener onItemClickListener;

  public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onLongItemClick(View view, int position);
  }

  GestureDetector mGestureDetector;

  public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
    onItemClickListener = listener;
    mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && onItemClickListener != null) {
                onItemClickListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
            }
        }
    });
}

  @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
    View childView = view.findChildViewUnder(e.getX(), e.getY());
    if (childView != null && onItemClickListener != null && mGestureDetector.onTouchEvent(e)) {
      onItemClickListener.onItemClick(childView, view.getChildAdapterPosition(childView));
      return true;
    }
    return false;
  }

  @Override public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) { }

  @Override
  public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
}
