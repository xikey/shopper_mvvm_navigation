package com.zikey.android.razanstoreapp.ui.custome_view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import android.widget.TextView;

import com.razanpardazesh.com.resturantapp.tools.FontChanger;
import com.zikey.android.razanstoreapp.R;


public class CustomPrintLayout extends LinearLayout {

    LinearLayout lyRoot;


    public CustomPrintLayout(Context context) {
        super(context);
        init();
    }

    public CustomPrintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPrintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomPrintLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {

        if (isInEditMode())
            return;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup customView = (ViewGroup) inflater.inflate(R.layout.factor_print_custom_layout, null);

        if (customView == null)
            return;

        lyRoot = (LinearLayout) customView.findViewById(R.id.lyRoot);


        LayoutParams rootLayoutParams = (LayoutParams) lyRoot.getLayoutParams();
        addView(customView);
        new FontChanger().applyTitleFont(lyRoot);

    }

    public void addTableFiveRow(String one, String two, String three, String four, String five) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.table_five_line, null);
        TextView txtOne = (TextView) vg.findViewById(R.id.txtOne);
        TextView txtTwo = (TextView) vg.findViewById(R.id.txtTwo);
        TextView txtThree = (TextView) vg.findViewById(R.id.txtThree);
        TextView txtFour = (TextView) vg.findViewById(R.id.txtFour);
        TextView txtFive = (TextView) vg.findViewById(R.id.txtFive);

        try {
            txtOne.setText(one);
            txtTwo.setText(two);
            txtThree.setText(three);
            txtFour.setText(four);
            txtFive.setText(five);

            lyRoot.addView(vg);
            new FontChanger().applyTitleFont(lyRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addTableFiveRow_black(String one, String two, String three, String four, String five) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.table_five_line_white_on_black, null);
        TextView txtOne = (TextView) vg.findViewById(R.id.txtOne);
        TextView txtTwo = (TextView) vg.findViewById(R.id.txtTwo);
        TextView txtThree = (TextView) vg.findViewById(R.id.txtThree);
        TextView txtFour = (TextView) vg.findViewById(R.id.txtFour);
        TextView txtFive = (TextView) vg.findViewById(R.id.txtFive);
        LinearLayout lyBack =   vg.findViewById(R.id.lyBackground);

        try {
            txtOne.setText(one);
            txtTwo.setText(two);
            txtThree.setText(three);
            txtFour.setText(four);
            txtFive.setText(five);

            txtOne.setTextColor(Color.parseColor("#ffffff"));
            txtTwo.setTextColor(Color.parseColor("#ffffff"));
            txtThree.setTextColor(Color.parseColor("#ffffff"));
            txtFour.setTextColor(Color.parseColor("#ffffff"));
            txtFive.setTextColor(Color.parseColor("#ffffff"));
            lyBack.setBackgroundColor(Color.parseColor("#000000"));



            lyRoot.addView(vg);
            new FontChanger().applyTitleFont(lyRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void addTableFourRow(String one, String two, String three, String four) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.table_four_line, null);
        TextView txtOne = (TextView) vg.findViewById(R.id.txtOne);
        TextView txtTwo = (TextView) vg.findViewById(R.id.txtTwo);
        TextView txtThree = (TextView) vg.findViewById(R.id.txtThree);
        TextView txtFour = (TextView) vg.findViewById(R.id.txtFour);

        try {
            txtOne.setText(one);
            txtTwo.setText(two);
            txtThree.setText(three);
            txtFour.setText(four);


            lyRoot.addView(vg);
            new FontChanger().applyTitleFont(lyRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void addTableTwoRow(String one, String two) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.table_two_line, null);
        TextView txtOne = (TextView) vg.findViewById(R.id.txtOne);
        TextView txtTwo = (TextView) vg.findViewById(R.id.txtTwo);

        try {
            txtOne.setText(one);
            txtTwo.setText(two);

            lyRoot.addView(vg);
            new FontChanger().applyTitleFont(lyRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addTableOneRow(String one) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.table_one_line, null);
        TextView txtOne = (TextView) vg.findViewById(R.id.txtOne);
        try {
            txtOne.setText(one);
            lyRoot.addView(vg);
            new FontChanger().applyTitleFont(lyRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void addTableOneRow_StarBox(String one) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.table_one_line_star_box, null);
        TextView txtOne = (TextView) vg.findViewById(R.id.txtOne);
        try {
            txtOne.setText(one);
            lyRoot.addView(vg);

            new FontChanger().applyTitleFont(lyRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void addLeftAndRightTextRow(String one, String two) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.left_right_text, null);

        TextView txtOne = (TextView) vg.findViewById(R.id.txtOne);
        TextView txtTwo = (TextView) vg.findViewById(R.id.txtTwo);

        try {
            txtOne.setText(one);
            txtTwo.setText(two);

            lyRoot.addView(vg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addTextView(String s, String color, int gravity, int maxline, int textSize, String backgroundColor, boolean bold) {

        if (TextUtils.isEmpty(s))
            return;

        TextView tv = new TextView(getContext());


        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        tv.setGravity(gravity);
        tv.setTextSize(textSize);
        tv.setMaxLines(maxline);
        if (bold)
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

        tv.setEllipsize(TextUtils.TruncateAt.END);

        if (!TextUtils.isEmpty(color) && color.contains("#"))
            tv.setTextColor(Color.parseColor(color));

        if (!TextUtils.isEmpty(backgroundColor) && backgroundColor.contains("#"))
            tv.setBackgroundColor(Color.parseColor(backgroundColor));


        tv.setText(s);
        tv.setPadding(5, 5, 5, 5);


        if (lyRoot != null)
            lyRoot.addView(tv);

      new FontChanger().applyTitleFont(lyRoot);

    }

    public void addImage(int resID) {

        ImageView iv = new ImageView(getContext());

        iv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        iv.setImageResource(resID);

        if (lyRoot != null)
            lyRoot.addView(iv);


    }

    public void addEmptyRow(int marginTop) {

        ImageView v = new ImageView(getContext());
        ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1, 1);
        ((LayoutParams) params).setMargins(0, marginTop, 0, 0);
        v.setLayoutParams(params);

        v.setLayoutParams(params);

        lyRoot.addView(v);
      new  FontChanger().applyTitleFont(lyRoot);

    }

    public void addLine(int marginTop) {

        View iv = new View(getContext());

        ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1, 1);
        ((LayoutParams) params).setMargins(0, marginTop, 0, 0);

        iv.setLayoutParams(params);

        iv.setBackgroundColor(Color.parseColor("#000000"));

        if (lyRoot != null)
            lyRoot.addView(iv);

       new FontChanger().applyTitleFont(lyRoot);

    }


}
