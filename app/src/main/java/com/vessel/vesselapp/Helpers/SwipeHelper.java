package com.example.besoin.Helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vessel.example.vessel.Helpers.MyButtonClickListener;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {
  int buttonWidth;
  List<MyButton> buttons;
  GestureDetector gestureDetector;
  RecyclerView recyclerView;
  Map<Integer,MyButton> Button_buffer;
  MyButtonClickListener listener;
  int swipe_position;
  float swipe_threshold = 0.5f;

  GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){

      @Override
      public boolean onSingleTapUp(@NonNull MotionEvent e) {

          for(MyButton button :buttons){
            if(button.OnClick(e.getX(),e.getY())){
                    break;
            }
          }
          return true;
      }
  };
    public SwipeHelper(Context context,RecyclerView recyclerView , int buttonWidth) {
        super(0, ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.buttons = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context,gestureListener);
        recyclerView.setOnTouchListener(onTouchListener);
        Button_buffer = new HashMap<>();
        this.buttonWidth = buttonWidth;
        attachSwiper();

    }

    private void attachSwiper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {

          if(swipe_position<0){
              return false;
          }
          Point point = new Point((int) event.getRawX(), (int)event.getRawY());
          RecyclerView.ViewHolder swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipe_position);
          View swipedItem = swipeViewHolder.itemView;
          Rect rect = new Rect();
          swipedItem.getGlobalVisibleRect(rect);

          if(event.getAction() == MotionEvent.ACTION_DOWN
          || event.getAction() == MotionEvent.ACTION_UP
          || event.getAction() == MotionEvent.ACTION_UP){

              if(rect.top <point.y && rect.bottom>point.y){
                  gestureDetector.onTouchEvent(event);
              }/*else{

              }*/
          }
            return false;

      }
  };




    private class MyButton {

        String text;
        int ImageResID , color , pos,textSize;
        RectF clickRegion;
        Context context;
        MyButtonClickListener listener;
        Resources resources;

        public MyButton(Context context ,String text, int imageResID,int textSize, int color, int pos, RectF clickRegion,MyButtonClickListener listener, Resources resources) {
            this.ImageResID = imageResID;
            this.text = text;
            this.color = color;
            this.pos = pos;
            this.listener = listener;
            this.clickRegion = clickRegion;
            this.context = context;
            this.textSize = textSize;
            resources = context.getResources();
        }

        public boolean OnClick(float x , float y){

            if(clickRegion != null && clickRegion.contains(x,y)){

                listener.OnClick();
                return true;
            }
            return false;
        }
        public void onDrawButton(Canvas canvas , RectF rectF,int position){
            Paint paint = new Paint();
            paint.setColor(color);
            canvas.drawRect(rectF,paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize((float) textSize);

            Rect r = new Rect();
            float cHeight = rectF.height();
            float cWidth = rectF.width();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds(text,0,text.length(),r);
            float x=0,y=0;

            if (ImageResID == 0){
                x = cWidth/2f - r.width()/2f - r.left;
                y = cHeight/2f + r.height()/2f -r.bottom;

                canvas.drawText(text,rectF.left+x,rectF.top+y,paint);
            }else{
                Drawable drawable = ContextCompat.getDrawable(context,ImageResID);
                Bitmap bitmap = drawableToBitMap(drawable);
                canvas.drawBitmap(bitmap,(rectF.left+rectF.right)/2,(rectF.top+rectF.bottom)/2,paint);

            }
            clickRegion = rectF;
            this.pos = position;
        }
    }

    private Bitmap drawableToBitMap(Drawable drawable) {

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight()
        ,Bitmap.Config.ARGB_8888);
        Canvas canvas =new Canvas(bitmap);
        drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
