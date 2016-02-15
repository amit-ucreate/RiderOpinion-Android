package com.nutsuser.ridersdomain.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.nutsuser.ridersdomain.R;

public class BetterPopupWindow {

    /**
     * The anchor.
     */
    protected final View anchor;

    /**
     * The window.
     */
    private final PopupWindow window;
    /**
     * The window manager.
     */
    private final WindowManager windowManager;
    /**
     * The root.
     */
    private View root;
    /**
     * The background.
     */
    private Drawable background = null;
    /**
     * The background.
     */
    private Color color = null;

    /**
     * Create a BetterPopupWindow.
     *
     * @param anchor the view that the BetterPopupWindow will be displaying 'from'
     */
    public BetterPopupWindow(View anchor) {
        this.anchor = anchor;
        this.window = new PopupWindow(anchor.getContext());

        // when a touch even happens outside of the window
        // make the window go away
        this.window.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    BetterPopupWindow.this.window.dismiss();
                    // v.clearAnimation();
                    return true;
                }
                return false;
            }
        });

        this.windowManager = (WindowManager) this.anchor.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        onCreate();
    }

    /**
     * Anything you want to have happen when created. Probably should create a
     * view and setup the event listeners on child views.
     */
    protected void onCreate() {
    }

    /**
     * In case there is stuff to do right before displaying.
     */
    protected void onShow() {
    }

    /**
     * Pre show.
     */
    private void preShow() {
        if (this.root == null) {
            throw new IllegalStateException(
                    "setContentView was not called with a view to display.");
        }
        onShow();

        if (this.background == null) {
            this.window.setBackgroundDrawable(new BitmapDrawable());
        } else {
            this.window.setBackgroundDrawable(this.background);
        }

        // if using PopupWindow#setBackgroundDrawable this is the only values of
        // the width and hight that make it work
        // otherwise you need to set the background of the root viewgroup
        // and set the popupwindow background to an empty BitmapDrawable
        this.window.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        this.window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.window.setTouchable(true);
        this.window.setFocusable(true);
        this.window.setOutsideTouchable(true);

        this.window.setContentView(this.root);
    }

    /**
     * Sets the background drawable.
     *
     * @param background the new background drawable
     */
    public void setBackgroundDrawable(Drawable background) {
        this.background = background;
    }

    /**
     * Sets the background drawable.
     *
     * @param background the new background drawable
     */
    public void setBackgroundColor(Color background) {
        this.color = background;
    }

    /**
     * Sets the content view. Probably should be called from {@link }
     *
     * @param root the view the popup will display
     */
    public void setContentView(View root) {
        this.root = root;
        // this.window.setBackgroundDrawable(background);
        this.window.setContentView(root);

    }

    /**
     * Will inflate and set the view from a resource id.
     *
     * @param layoutResID the new content view
     */
    public void setContentView(int layoutResID) {
        LayoutInflater inflator = (LayoutInflater) this.anchor.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setContentView(inflator.inflate(layoutResID, null));
    }

    /**
     * If you want to do anything when {@link } is called.
     *
     * @param listener the new on dismiss listener
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.window.setOnDismissListener(listener);
    }

    /**
     * Displays like a popdown menu from the anchor view.
     */
    public void showLikePopDownMenu() {
        this.showLikePopDownMenu(0, 0);
    }

    /**
     * Displays like a popdown menu from the anchor view.
     *
     * @param xOffset offset in X direction
     * @param yOffset offset in Y direction
     */
    public void showLikePopDownMenu(int xOffset, int yOffset) {
        this.preShow();

        this.window.setAnimationStyle(R.style.Animations_GrowFromBottom);

        this.window.showAsDropDown(this.anchor, xOffset, yOffset);
    }

    /**
     * Displays like a QuickAction from the anchor view.
     */
    public void showLikeQuickAction() {
        this.showLikeQuickAction(0, 0);
    }

    /**
     * Displays like a QuickAction from the anchor view.
     *
     * @param xOffset offset in the X direction
     * @param yOffset offset in the Y direction
     */
    public void showLikeQuickAction(int xOffset, int yOffset) {
        this.preShow();

        this.window.setAnimationStyle(R.style.Animations_GrowFromBottom);

        int[] location = new int[2];
        this.anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0]
                + this.anchor.getWidth(), location[1] + this.anchor.getHeight());

        this.root.measure(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        int rootWidth = this.root.getMeasuredWidth();
        int rootHeight = this.root.getMeasuredHeight();

        int screenWidth = this.windowManager.getDefaultDisplay().getWidth();
        int screenHeight = this.windowManager.getDefaultDisplay().getHeight();

        // System.out.println(rootWidth);
        // System.out.println(rootHeight);
        // System.out.println(screenWidth);
        // System.out.println(screenHeight);

        // Log.d("hunting", String.valueOf(rootWidth));
        // Log.d("hunting", String.valueOf(rootHeight));
        // Log.d("hunting", String.valueOf(screenWidth));
        // Log.d("hunting", String.valueOf(screenHeight));
        // Log.d("hunting", String.valueOf(anchorRect.top));
        int xPos = ((screenWidth - rootWidth) / 2) + xOffset;
        int yPos = screenHeight;

        // display on bottom
        if (rootHeight > anchorRect.top) {
            yPos = screenHeight;
            this.window.setAnimationStyle(R.style.Animations_GrowFromBottom);
        }

        this.window.showAtLocation(this.anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    /**
     * Dismiss.
     */
    public void dismiss() {
        this.window.dismiss();
    }

}
