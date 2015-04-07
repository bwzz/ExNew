/*
 * *
 *  * Copyright 2012 fenbi.com. All rights reserved.
 *  * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.bwzz.common.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author wanghb
 */
public class ViewHelper {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static View setChildOnClickListener(View parent, int cid,
            OnClickListener listener) {
        if (parent != null) {
            View v = parent.findViewById(cid);
            if (v != null) {
                v.setOnClickListener(listener);
            }
            return v;
        }
        return null;
    }

    public static void setChildOnClickListener(View parent, int[] cids,
            OnClickListener listener) {
        if (parent != null && cids != null) {
            for (int cid : cids) {
                View v = parent.findViewById(cid);
                if (v != null) {
                    v.setOnClickListener(listener);
                }
            }
        }
    }

    public static TextView setTextView(View parent, int id, CharSequence string) {
        TextView t = (TextView) parent.findViewById(id);
        if (t != null) {
            t.setText(string);
            setCursorToTextEnd(t);
        }
        return t;
    }

    public static TextView setTextView(View v, CharSequence string) {
        if (v != null && v instanceof TextView) {
            TextView t = (TextView)v;
            t.setText(string);
            setCursorToTextEnd(t);
            return t;
        }
        throw new IllegalArgumentException();
    }

    public static String getTextViewText(View parent, int id) {
        TextView t = (TextView) parent.findViewById(id);
        if (t != null) {
            return t.getText().toString();
        } else {
            return null;
        }
    }

    public static boolean getCheckBoxValue(View parent, int id) {
        CheckBox cb = (CheckBox) parent.findViewById(id);
        return cb == null ? false : cb.isChecked();
    }

    public static void setCheckBoxValue(View parent, int id, boolean checked) {
        CheckBox cb = (CheckBox) parent.findViewById(id);
        cb.setChecked(checked);
    }

    public static void showChild(View parent, int child) {
        showChild(parent, parent.findViewById(child));
    }

    public static void showChild(View parent, View child) {
        if (!(parent instanceof ViewGroup)) {
            return;
        }
        ViewGroup g = (ViewGroup) parent;
        for (int i = 0; i < g.getChildCount(); ++i) {
            View v = g.getChildAt(i);
            if (v == child) {
                showView(v, false);
                v.bringToFront();
            } else {
                goneView(v, false);
            }
        }
    }

    private static long ANIMATE_DURATION = 200;

    public static View showView(View v, boolean animate, long duration) {
        if (v != null) {
            v.setVisibility(View.VISIBLE);
            if (animate) {
                Animation ani = new AlphaAnimation(0f, 1f);
                ani.setDuration(duration);
                v.startAnimation(ani);
            }
        }
        return v;
    }

    public static View showView(View v, boolean animate) {
        return showView(v, animate, ANIMATE_DURATION);
    }

    public static View goneView(View v, boolean animate) {
        if (v != null) {
            v.setVisibility(View.GONE);
            if (animate) {
                Animation ani = new AlphaAnimation(1f, 0f);
                ani.setDuration(ANIMATE_DURATION);
                v.startAnimation(ani);
            }
        }
        return v;
    }

    public static View invisibleView(View v, boolean animate) {
        if (v != null) {
            v.setVisibility(View.INVISIBLE);
            if (animate) {
                Animation ani = new AlphaAnimation(1f, 0f);
                ani.setDuration(ANIMATE_DURATION);
                v.startAnimation(ani);
            }
        }
        return v;
    }

    public static void showAndGoneView(View visible, View... gones) {
        showView(visible, false);
        if (gones != null) {
            for (View gone : gones) {
                if (visible != gone) {
                    goneView(gone, false);
                }
            }
        }
    }

    public static void setBackground(View v, int resId) {
        if (v != null) {
            v.setBackgroundResource(resId);
        }
    }

    public static void setSelected(View v, int resId, boolean show) {
        if (v != null) {
            setSelected(v.findViewById(resId), show);
        }
    }

    public static void setSelected(View v, boolean show) {
        if (v != null) {
            v.setSelected(show);
        }
    }

    public static void setSelected(ViewGroup vg, boolean enable) {
        if (vg != null) {
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                child.setSelected(enable);
                if (child instanceof ViewGroup) {
                    setSelected((ViewGroup) child, enable);
                }
            }
        }
    }

    public static void setDicemalEditFilter(EditText inputEdit, final int length) {
        InputFilter lengthfilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                    Spanned dest, int dstart, int dend) {
                if ("".equals(source.toString())) {
                    return null;
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - length;
                    if (diff > 0 && start >= 0 && start < source.length()
                            && end - diff >= 0 && end - diff < source.length()
                            && start <= end - diff) {
                        return source.subSequence(start, end - diff);
                    } else {
                        return null;
                    }
                }
                return null;
            }
        };
        inputEdit.setFilters(new InputFilter[] {
                lengthfilter
        });
    }

    public static void setOnclikListenerOnViewWithId(View view, OnClickListener listener) {
        if (view == null) {
            return;
        }
        if (view.getId() != View.NO_ID) {
            view.setOnClickListener(listener);
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); ++i) {
                setOnclikListenerOnViewWithId(group.getChildAt(i), listener);
            }
        }
    }

    public static interface EmptyWatcher {
        public void onNotEmpty(boolean notEmpty);
    }

    public static void observerEditTextEmpty(final EmptyWatcher watcher,
            final TextView... editTexts) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean notEmpty = true;
                for (TextView editText : editTexts) {
                    notEmpty = notEmpty && !TextUtils.isEmpty(editText.getText());
                }
                if (watcher != null) {
                    watcher.onNotEmpty(notEmpty);
                }
            }
        };
        for (TextView editText : editTexts) {
            editText.addTextChangedListener(textWatcher);
        }
    }

    public static void setCursorToTextEnd(View v) {
        if (!(v instanceof EditText)) {
            return;
        }
        EditText et = (EditText) v;
        CharSequence charSequence = et.getText();
        et.setSelection(charSequence != null ? charSequence.length() : 0);
    }

    public static void setEnable(View v, boolean enable) {
        if (v != null) {
            v.setEnabled(enable);
        }
    }

    public static void setEnable(ViewGroup vg, boolean enable) {
        if (vg != null) {
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                child.setEnabled(enable);
                if (child instanceof ViewGroup) {
                    setEnable((ViewGroup) child, enable);
                }
            }
        }
    }

}
