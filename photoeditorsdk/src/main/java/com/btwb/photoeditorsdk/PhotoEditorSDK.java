package com.btwb.photoeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Adel on 02/06/2017.
 */

public class PhotoEditorSDK implements MultiTouchListener.OnMultiTouchListener {

    private Context context;
    private ConstraintLayout parentView;
    private LinearLayout activeView;
    private ImageView imageView;
    private View deleteView, leftLineView, rightLineView, bottomHorizontalLine, topHorizontalLine, verticalLine, horizontalLine, stickerHorizontalLine;
    private BrushDrawingView brushDrawingView;
    private List<View> addedViews;
    private OnPhotoEditorSDKListener onPhotoEditorSDKListener;
    private View addTextRootView;

    private PhotoEditorSDK(PhotoEditorSDKBuilder photoEditorSDKBuilder) {
        this.context = photoEditorSDKBuilder.context;
        this.parentView = photoEditorSDKBuilder.parentView;
        this.activeView = photoEditorSDKBuilder.activeView;
        this.leftLineView = photoEditorSDKBuilder.leftLineView;
        this.stickerHorizontalLine = photoEditorSDKBuilder.stickerHorizontalLine;
        this.rightLineView = photoEditorSDKBuilder.rightLineView;
        this.bottomHorizontalLine = photoEditorSDKBuilder.bottomHorizontalLine;
        this.topHorizontalLine = photoEditorSDKBuilder.topHorizontalLine;
        this.verticalLine = photoEditorSDKBuilder.verticalLine;
        this.horizontalLine = photoEditorSDKBuilder.horizontalLine;
        this.imageView = photoEditorSDKBuilder.imageView;
        this.deleteView = photoEditorSDKBuilder.deleteView;
        this.brushDrawingView = photoEditorSDKBuilder.brushDrawingView;
        addedViews = new ArrayList<>();
    }

    public void addImage(Bitmap desiredImage) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageRootView = inflater.inflate(R.layout.photo_editor_sdk_image_item_list, null);
        ImageView imageView = (ImageView) imageRootView.findViewById(R.id.photo_editor_sdk_image_iv);
        imageView.setImageBitmap(desiredImage);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
                parentView,activeView, this.imageView, onPhotoEditorSDKListener, 0,false, "", this.leftLineView, this.rightLineView, this.bottomHorizontalLine, this.topHorizontalLine, this.verticalLine, this.horizontalLine, this.stickerHorizontalLine);
        multiTouchListener.setOnMultiTouchListener(this);
        imageRootView.setOnTouchListener(multiTouchListener);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomToBottom = ConstraintSet.PARENT_ID;
        params.endToEnd = ConstraintSet.PARENT_ID;
        params.startToStart = ConstraintSet.PARENT_ID;
        params.topToTop = ConstraintSet.PARENT_ID;
        imageRootView.setLayoutParams(params);
        parentView.addView(imageRootView, params);
        addedViews.add(imageRootView);
        if (onPhotoEditorSDKListener != null)
            onPhotoEditorSDKListener.onAddViewListener(ViewType.IMAGE, addedViews.size());
    }

    public void addView(final int x, int y, int height, int width, boolean center) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRootView = inflater.inflate(R.layout.photo_editor_sdk_view_item_list, null);
        final View view = (View) viewRootView.findViewById(R.id.photo_editor_sdk_view_test__iv);
        view.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        view.setBackgroundColor(Color.WHITE);
        if(!center){
            viewRootView.setY(y);
            viewRootView.setX(x <= width ? x : x - width);
        }


        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
                parentView,activeView, this.imageView, onPhotoEditorSDKListener, 0,false, "",this.leftLineView, this.rightLineView, this.bottomHorizontalLine, this.topHorizontalLine, this.verticalLine, this.horizontalLine, this.stickerHorizontalLine);
        multiTouchListener.setOnMultiTouchListener(this);
        viewRootView.setOnTouchListener(multiTouchListener);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(center){
            params = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomToBottom = ConstraintSet.PARENT_ID;
            params.endToEnd = ConstraintSet.PARENT_ID;
            params.startToStart = ConstraintSet.PARENT_ID;
            params.topToTop = ConstraintSet.PARENT_ID;
            viewRootView.setLayoutParams(params);
        }
        parentView.addView(viewRootView, params);
        addedViews.add(viewRootView);
        if (onPhotoEditorSDKListener != null)
            onPhotoEditorSDKListener.onAddViewListener(ViewType.VIEW, addedViews.size());
    }


    public void addText(TextView textView, String text, int color, Typeface fontTextView, int width, String type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addTextRootView = inflater.inflate(R.layout.photo_editor_sdk_text_item_list, null);
        TextView addTextView = (TextView) addTextRootView.findViewById(R.id.photo_editor_sdk_text_tv);
        addTextView.setTextColor(Color.WHITE);
        if(textView != null){
            float sp = textView.getTextSize() / context.getResources().getDisplayMetrics().scaledDensity;
            addTextView.setTypeface(textView.getTypeface());
            addTextView.setTextSize(sp);
            addTextView.setTextColor(textView.getTextColors());
            addTextView.setGravity(Gravity.CENTER_VERTICAL);
            addTextView.setText(text);
            if (width > 0){
                addTextView.setWidth(width);
            }
            addTextView.setPadding(textView.getPaddingLeft(),textView.getPaddingTop(),textView.getPaddingRight(), textView.getPaddingBottom());
            if (textView.getBackground() instanceof ColorDrawable) {
                ColorDrawable cd = (ColorDrawable) textView.getBackground();
                int colorCode = cd.getColor();
                addTextView.setBackgroundColor(colorCode);
            }
            Drawable icon = (Drawable) textView.getCompoundDrawables()[0];
            if(icon != null){
                int h = icon.getIntrinsicHeight();
                int w = icon.getIntrinsicWidth();
                icon.setBounds( 0, 0, w, h );
                addTextView.setCompoundDrawables(icon, null, null, null );
            }

        }

        addTextView.setText(text);
        if (color != -1)
            addTextView.setTextColor(color);
        if(fontTextView != null)
            addTextView.setTypeface(fontTextView);


        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
                parentView,activeView, this.imageView, onPhotoEditorSDKListener,width, true, type, this.leftLineView, this.rightLineView, this.bottomHorizontalLine, this.topHorizontalLine, this.verticalLine, this.horizontalLine, this.stickerHorizontalLine);
        multiTouchListener.setOnMultiTouchListener(this);
        addTextRootView.setOnTouchListener(multiTouchListener);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomToBottom = ConstraintSet.PARENT_ID;
        params.endToEnd = ConstraintSet.PARENT_ID;
        params.startToStart = ConstraintSet.PARENT_ID;
        params.topToTop = ConstraintSet.PARENT_ID;
        addTextRootView.setLayoutParams(params);
        parentView.addView(addTextRootView, params);
        addedViews.add(addTextRootView);
        if (onPhotoEditorSDKListener != null)
            onPhotoEditorSDKListener.onAddViewListener(ViewType.TEXT, addedViews.size());
    }

    public void addStickers(String text,
                            Typeface font,
                            String type,
                            Drawable icon,
                            int iconHorizontal,
                            int fontSize,
                            final int x,
                            final int y,
                            String textColor,
                            String backgroundColor,
                            int width,
                            int paddingVertical,
                            int paddingHorizontal,
                            boolean editable,
                            boolean center,
                            String rightText,
                            int numberOfLines
    ) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addTextRootView = inflater.inflate(R.layout.photo_editor_sdk_text_item_list, null);
        final TextView addTextView = (TextView) addTextRootView.findViewById(R.id.photo_editor_sdk_text_tv);
        addTextView.setText(text);
        addTextView.setTypeface(font);
        addTextView.setTextSize(fontSize);
        addTextView.setPadding(paddingHorizontal,paddingVertical,paddingHorizontal,paddingVertical);
        addTextView.setTextColor(Color.parseColor(textColor));
        addTextView.setBackgroundColor(Color.parseColor(backgroundColor));
        addTextView.setMaxLines(numberOfLines);

        if(rightText != null){
            Spannable word = new SpannableString(text);

            word.setSpan(new ForegroundColorSpan(Color.WHITE), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            addTextView.setText(word);
            Spannable wordTwo = new SpannableString("  " + rightText);

            wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorGreen)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            addTextView.append(wordTwo);
        }

        if (width > 0) {
            addTextView.setWidth(width);
        }
        addTextView.setGravity(Gravity.CENTER_VERTICAL);
        if(icon != null){
            int h = icon.getIntrinsicHeight();
            int w = icon.getIntrinsicWidth();
            icon.setBounds( 0, 0, w, h );
            addTextView.setCompoundDrawables(icon, null , null, null);
            addTextView.setCompoundDrawablePadding(convertDpToPixel(iconHorizontal));
        }
        if(!center){
            addTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    addTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int viewWidth = addTextView.getWidth();
                    addTextView.setX(x <= viewWidth ? x : x - viewWidth);
                    addTextView.setY(y);
                }
            });
        }



        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomToBottom = ConstraintSet.PARENT_ID;
        params.endToEnd = ConstraintSet.PARENT_ID;
        params.startToStart = ConstraintSet.PARENT_ID;
        params.topToTop = ConstraintSet.PARENT_ID;
        addTextView.setLayoutParams(params);
        parentView.addView(addTextView, params);
        addedViews.add(addTextView);
        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
                    parentView,activeView, this.imageView, onPhotoEditorSDKListener, width,editable, type, this.leftLineView, this.rightLineView, this.bottomHorizontalLine, this.topHorizontalLine, this.verticalLine, this.horizontalLine, this.stickerHorizontalLine);
            multiTouchListener.setOnMultiTouchListener(this);
            addTextView.setOnTouchListener(multiTouchListener);
            if (onPhotoEditorSDKListener != null)
                onPhotoEditorSDKListener.onAddViewListener(ViewType.TEXT, addedViews.size());

    }

    public void addEmoji(String emojiName, Typeface emojiFont) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View emojiRootView = inflater.inflate(R.layout.photo_editor_sdk_text_item_list, null);
        TextView emojiTextView = (TextView) emojiRootView.findViewById(R.id.photo_editor_sdk_text_tv);
        emojiTextView.setTypeface(emojiFont);
        emojiTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        emojiTextView.setText(convertEmoji(emojiName));
        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
                parentView,activeView ,this.imageView, onPhotoEditorSDKListener, 0,false, "", this.leftLineView, this.rightLineView, this.bottomHorizontalLine, this.topHorizontalLine, this.verticalLine, this.horizontalLine, this.stickerHorizontalLine);
        multiTouchListener.setOnMultiTouchListener(this);
        emojiRootView.setOnTouchListener(multiTouchListener);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomToBottom = ConstraintSet.PARENT_ID;
        params.endToEnd = ConstraintSet.PARENT_ID;
        params.startToStart = ConstraintSet.PARENT_ID;
        params.topToTop = ConstraintSet.PARENT_ID;
        emojiRootView.setLayoutParams(params);
        parentView.addView(emojiRootView, params);
        addedViews.add(emojiRootView);
        if (onPhotoEditorSDKListener != null)
            onPhotoEditorSDKListener.onAddViewListener(ViewType.EMOJI, addedViews.size());
    }

    public  int convertDpToPixel(float dp){
        return Math.round(dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void setBrushDrawingMode(boolean brushDrawingMode) {
        if (brushDrawingView != null)
            brushDrawingView.bringToFront();
            brushDrawingView.setBrushDrawingMode(brushDrawingMode);
    }

    public void setBrushSize(float size) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushSize(size);
    }

    public void setBrushColor(@ColorInt int color) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushColor(color);
    }

    public void setBrushEraserSize(float brushEraserSize) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushEraserSize(brushEraserSize);
    }

    public void setBrushEraserColor(@ColorInt int color) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushEraserColor(color);
    }

    public float getEraserSize() {
        if (brushDrawingView != null)
            return brushDrawingView.getEraserSize();
        return 0;
    }

    public float getBrushSize() {
        if (brushDrawingView != null)
            return brushDrawingView.getBrushSize();
        return 0;
    }

    public int getBrushColor() {
        if (brushDrawingView != null)
            return brushDrawingView.getBrushColor();
        return 0;
    }

    public void brushEraser() {
        if (brushDrawingView != null)
            brushDrawingView.brushEraser();
    }

    public void viewUndo() {
        if (addedViews.size() > 0) {
            parentView.removeView(addedViews.remove(addedViews.size() - 1));
            if (onPhotoEditorSDKListener != null)
                onPhotoEditorSDKListener.onRemoveViewListener(addedViews.size());
        }
    }

    private void viewUndo(View removedView) {
        if (addedViews.size() > 0) {
            if (addedViews.contains(removedView)) {
                parentView.removeView(removedView);
                addedViews.remove(removedView);
                if (onPhotoEditorSDKListener != null)
                    onPhotoEditorSDKListener.onRemoveViewListener(addedViews.size());
            }
        }
    }

    public void clearBrushAllViews() {
        if (brushDrawingView != null)
            brushDrawingView.clearAll();
    }

    public void clearAllViews() {
        for (int i = 0; i < addedViews.size(); i++) {
            parentView.removeView(addedViews.get(i));
        }
        if (brushDrawingView != null)
            brushDrawingView.clearAll();
    }

    public String saveImage(String folderName, String imageName) {
        String selectedOutputPath = "";
        if (isSDCARDMounted()) {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);
            // Create a storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("PhotoEditorSDK", "Failed to create directory");
                }
            }
            // Create a media file name
            selectedOutputPath = mediaStorageDir.getPath() + File.separator + imageName;
            Log.d("PhotoEditorSDK", "selected camera path " + selectedOutputPath);
            File file = new File(selectedOutputPath);
            try {
                FileOutputStream out = new FileOutputStream(file);
                if (parentView != null) {
                    parentView.setDrawingCacheEnabled(true);
                    parentView.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 80, out);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return selectedOutputPath;
    }

    private boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    private String convertEmoji(String emoji) {
        String returnedEmoji = "";
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public void setOnPhotoEditorSDKListener(OnPhotoEditorSDKListener onPhotoEditorSDKListener) {
        this.onPhotoEditorSDKListener = onPhotoEditorSDKListener;
        brushDrawingView.setOnPhotoEditorSDKListener(onPhotoEditorSDKListener);
    }

    @Override
    public void onEditTextClickListener(TextView view) {
        if (addTextRootView != null) {
            parentView.removeView(view);
            addedViews.remove(view);
        }
    }

    @Override
    public void onRemoveViewListener(View removedView) {
        viewUndo(removedView);
    }

    public static class PhotoEditorSDKBuilder {

        private Context context;
        private ConstraintLayout parentView;
        private LinearLayout activeView;
        private ImageView imageView;
        private View deleteView, leftLineView, rightLineView, bottomHorizontalLine, topHorizontalLine, verticalLine, horizontalLine, stickerHorizontalLine;
        private BrushDrawingView brushDrawingView;

        public PhotoEditorSDKBuilder(Context context) {
            this.context = context;
        }

        public PhotoEditorSDKBuilder parentView(ConstraintLayout parentView) {
            this.parentView = parentView;
            return this;
        }

        public PhotoEditorSDKBuilder line(View left, View right,View bottom, View top, View vertical, View horizontal, View stickerHorizontalLine) {
            this.leftLineView = left;
            this.rightLineView = right;
            this.bottomHorizontalLine = bottom;
            this.topHorizontalLine = top;
            this.verticalLine = vertical;
            this.horizontalLine = horizontal;
            this.stickerHorizontalLine = stickerHorizontalLine;
            return this;
        }

        public PhotoEditorSDKBuilder activeView(LinearLayout activeView) {
            this.activeView = activeView;
            return this;
        }

        public PhotoEditorSDKBuilder childView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public PhotoEditorSDKBuilder deleteView(View deleteView) {
            this.deleteView = deleteView;
            return this;
        }

        public PhotoEditorSDKBuilder brushDrawingView(BrushDrawingView brushDrawingView) {
            this.brushDrawingView = brushDrawingView;
            return this;
        }

        public PhotoEditorSDK buildPhotoEditorSDK() {
            return new PhotoEditorSDK(this);
        }
    }
}
