/*
 * Copyright (c) 2018 Filippo Engidashet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dalol.chipsinputlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import org.dalol.chipsinputlayout.adapter.BaseChipsHintAdapter;
import org.dalol.chipsinputlayout.adapter.BaseChipsInputAdapter;
import org.dalol.chipsinputlayout.common.AbstractOnclickListener;
import org.dalol.chipsinputlayout.holders.ChipsInputViewHolder;
import org.dalol.chipsinputlayout.model.ChipInput;
import org.dalol.chipsinputlayout.utilities.ViewUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 03/02/2018.
 */

public class ChipsInputLayout extends ViewGroup {

    private final static int DEFAULT_MAX_CHIPS_COUNT = -1;
    private final static int DEFAULT_INPUT_FIELD_HINT_TEXT_COLOR = 0xFF000000;

    private final List<ChipInput> mPinnedChipsList = new LinkedList<>();
    private BaseChipsHintAdapter<? extends ChipInput, ? extends ChipsInputViewHolder> mChipsHintAdapter;
    private BaseChipsInputAdapter<? extends ChipInput> mChipsInputAdapter;
    private LayoutInflater mLayoutInflater;
    private AppCompatAutoCompleteTextView mAutoCompeteEditText;
    private int mMaxChipsCount;
    private String mInputFieldHintText;
    private int mGapSize;

    public ChipsInputLayout(Context context) {
        super(context);
        initialize(context, null);
    }

    public ChipsInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public ChipsInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChipsInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ChipsInputLayout, 0, 0);
        if (attr == null) {
            return;
        }
        int inputFieldHintTextColor;
        try {
            mMaxChipsCount = attr.getInt(R.styleable.ChipsInputLayout_cil_max_chip_count, DEFAULT_MAX_CHIPS_COUNT);
            mInputFieldHintText = attr.getString(R.styleable.ChipsInputLayout_cil_input_text_hint);
            inputFieldHintTextColor = attr.getColor(R.styleable.ChipsInputLayout_cil_input_text_color, DEFAULT_INPUT_FIELD_HINT_TEXT_COLOR);
        } finally {
            attr.recycle();
        }

        mLayoutInflater = LayoutInflater.from(context);
        mGapSize = ViewUtils.getSize(context, 5);
        mAutoCompeteEditText = new ChipsAutoCompleteEditText(context, attrs);
        mAutoCompeteEditText.setThreshold(1);
        mAutoCompeteEditText.setGravity(Gravity.CENTER_VERTICAL);
        mAutoCompeteEditText.setHint(mInputFieldHintText);
        mAutoCompeteEditText.setHintTextColor(inputFieldHintTextColor);
        mAutoCompeteEditText.setPadding(0, 0, 0, 0);
        mAutoCompeteEditText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//        mAutoCompeteEditText.setBackgroundColor(Color.CYAN);
        mAutoCompeteEditText.setMinWidth(ViewUtils.getSize(context, 80));
        mAutoCompeteEditText.setBackgroundResource(android.R.color.transparent);
        mAutoCompeteEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mAutoCompeteEditText.setPrivateImeOptions("nm");
        mAutoCompeteEditText.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mAutoCompeteEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    int childIndex = getChildCount() - 2;
                    if (mAutoCompeteEditText.getText().toString().length() == 0 && childIndex >= 0) {
                        removeView(getChildAt(childIndex));
                        mPinnedChipsList.remove(childIndex);
                        handleHint();
                    }
                }
                return false;
            }
        });

        mAutoCompeteEditText.setDropDownVerticalOffset(0);
        mAutoCompeteEditText.setDropDownAnchor(getId());
        mAutoCompeteEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChipInput chipInput = (ChipInput) parent.getItemAtPosition(position);
                addChip(chipInput);
            }
        });
        addView(mAutoCompeteEditText);
    }

    public void setDropDownAnchor(int viewId) {
        mAutoCompeteEditText.setDropDownAnchor(viewId);
    }

    private void handleHint() {
        if (getChildCount() > 1) mAutoCompeteEditText.setHint("");
        else mAutoCompeteEditText.setHint(mInputFieldHintText);
        mAutoCompeteEditText.setText(null);
        mAutoCompeteEditText.setFocusable(true);
        mAutoCompeteEditText.setFocusableInTouchMode(true);
        mAutoCompeteEditText.requestFocus();
    }

    public void setFilterableAdapter(BaseChipsHintAdapter<? extends ChipInput, ? extends ChipsInputViewHolder> adapter) {
        mChipsHintAdapter = adapter;
        mAutoCompeteEditText.setAdapter(adapter);
    }

    public void setChipsAdapter(BaseChipsInputAdapter<? extends ChipInput> chipsAdapter) {
        mChipsInputAdapter = chipsAdapter;
    }

    public void setChipsGap(int chipsGap) {
        mGapSize = chipsGap;
    }

    public void setMaximumChipInputCount(int maxChipsCount) {
        mMaxChipsCount = maxChipsCount;
    }

    public void setChips(List<? extends ChipInput> chipInputs) {
        mPinnedChipsList.clear();
    }

    public void addChips(List<? extends ChipInput> chipInputs) {
        if (chipInputs != null) {
            for (ChipInput chipInput : chipInputs) {
                addChip(chipInput);
            }
        }
    }

    public <C extends ChipInput> void addChip(C chipInput) {

        if (mChipsInputAdapter == null) {
            return;
        }

        int chipPosition = getChildCount() - 1;

        View parentChipView = mChipsInputAdapter.onCreateChipView(mLayoutInflater, this);
        mChipsInputAdapter.onBindChipView(parentChipView, chipInput);

        View removeChipView = mChipsInputAdapter.getChipInputRemoverView(parentChipView);
        if (removeChipView != null) {
            removeChipView.setOnClickListener(new AbstractOnclickListener<View>(parentChipView) {
                @Override
                protected void onClick(View parentView, View childView) {
                    int indexOfChild = indexOfChild(parentView);
                    removeView(parentView);
                    mPinnedChipsList.remove(indexOfChild);
                    handleHint();
                }
            });
        }

        addView(parentChipView, chipPosition);
        mPinnedChipsList.add(chipInput);
        handleHint();
    }

    public <C extends ChipInput> List<C> getPinnedChipsList() {
        return (List<C>) mPinnedChipsList;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = resolveSize(0, widthMeasureSpec);

        int childLeft = getPaddingLeft() + mGapSize;
        int childTop = getPaddingTop();

        boolean isNewRow = true;

        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View view = getChildAt(i);

            if(mMaxChipsCount == DEFAULT_MAX_CHIPS_COUNT) {
                view.setVisibility(VISIBLE);
            } else if (mPinnedChipsList.size() >= mMaxChipsCount && (i == (childCount - 1))) {
                view.setVisibility(GONE);
                continue;
            } else {
                view.setVisibility(VISIBLE);
            }

            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();

            int childRight = childLeft + childWidth;
            if (childRight > parentWidth) {
                childLeft = getPaddingLeft() + mGapSize;
                isNewRow = true;
            }

            childLeft += childWidth + mGapSize;

            if (isNewRow) {
                childTop += childHeight + mGapSize;
                isNewRow = false;
            }
        }

        setMeasuredDimension(parentWidth, resolveSize(childTop + mGapSize, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int t, int right, int b) {

        int parentWidth = right - left;
        int childLeft = getPaddingLeft() + mGapSize;
        int childTop = getPaddingTop() + mGapSize;
        int currentRowMaxHeight = 0;
        int childCount = getChildCount();
        boolean isFirstRow = true;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if ((childWidth + childLeft) > parentWidth) {
                childLeft = getPaddingLeft() + mGapSize;
                childTop += currentRowMaxHeight + (isFirstRow ? 0 : mGapSize);
                currentRowMaxHeight = childHeight;
            }
            currentRowMaxHeight = Math.max(childHeight, currentRowMaxHeight);

            if (i == (childCount - 1)) {
                int top = (childTop + (currentRowMaxHeight - childHeight));
                child.layout(childLeft, top, right - mGapSize, top + childHeight);
            } else {
                int childSize = Math.min(childWidth, parentWidth - (mGapSize + mGapSize));
                if (childWidth >= parentWidth) {
                    child.measure(MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    childHeight = child.getMeasuredHeight();
                }
                child.layout(childLeft, childTop, childLeft + childSize, childTop + childHeight);
            }
            childLeft += childWidth + mGapSize;
            isFirstRow = false;
        }
    }

    public void setPinnedChipsInput(@NonNull List<? extends ChipInput> pinnedChipsInput) {
        for (int i = 0, size = pinnedChipsInput.size(); i < size; i++) {
            ChipInput chipInput = pinnedChipsInput.get(i);
            addChip(chipInput);
        }
    }

    class ChipsAutoCompleteEditText extends AppCompatAutoCompleteTextView {

        private boolean mWasShowing;

        public ChipsAutoCompleteEditText(Context context) {
            super(context);
        }

        public ChipsAutoCompleteEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ChipsAutoCompleteEditText(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void replaceText(CharSequence text) {
            // do nothing so that the text stays the same
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleFilterableList();
                    break;
            }
            return super.onTouchEvent(event);
        }

        public void handleFilterableList() {
            if (mWasShowing) {
                dismissDropDown();
            } else {
                if (mChipsHintAdapter != null) {
                    mChipsHintAdapter.updateChipsHint(mPinnedChipsList);
                }
                post(new Runnable() {
                    @Override
                    public void run() {
                        showDropDown();
                    }
                });
            }
        }

        @Override
        public void showDropDown() {
            super.showDropDown();
            mWasShowing = true;
        }

        @Override
        public void dismissDropDown() {
            super.dismissDropDown();
            mWasShowing = false;
        }
    }
}