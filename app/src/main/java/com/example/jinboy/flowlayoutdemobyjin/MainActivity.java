package com.example.jinboy.flowlayoutdemobyjin;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

    Flowlayout mTagLayout;
    private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();

    private EditText inputLabel;
    private Button btnSure;

    // 存放标签数据的数组
    String[] mTextStr = { "A渠道", "B渠道", "TCL空调部", "TCL家电", "天猫", "京东", "淘宝" };

    ArrayList<String>  list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputLabel = (EditText) LayoutInflater.from(this).inflate(R.layout.my_edit_text, null);

        btnSure = (Button) findViewById(R.id.btn_sure);

        mTagLayout = (Flowlayout) findViewById(R.id.tag_layout);

        initList();

        initLayout(list);

        initBtnListener();
    }

    private void initList() {
        for(int i=0;i<mTextStr.length;i++){
            list.add(mTextStr[i]);
        }
    }

    private void initBtnListener() {
        /**
         * 初始化  单击事件：
         */
        btnSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String label = inputLabel.getText().toString().trim();

                String[] newStr = new String[mTagLayout.getChildCount()];

                /**
                 * 获取  子view的数量   并添加进去
                 */
                if(label!=null&&!label.equals("")){
                    for(int m = 0;m < mTagLayout.getChildCount()-1;m++){
                        newStr[m] =((TextView)mTagLayout.getChildAt(m).
                                findViewById(R.id.text)).getText().toString();//根据  当前   位置查找到 当前    textView中标签  内容
                    }
                    list.add(label);
                    initLayout(list);
                    inputLabel.setText("");
                }
            }
        });
    }


    private void initLayout(final ArrayList<String> arr) {

        mTagLayout.removeAllViewsInLayout();
        /**
         * 创建 textView数组
         */
        final TextView[] textViews = new TextView[arr.size()];
        final TextView[] icons = new TextView[arr.size()];

        for (int i = 0; i < arr.size(); i++) {

            final int pos = i;

            final View view = (View) LayoutInflater.from(MainActivity.this).inflate(R.layout.text_view, mTagLayout, false);

            final TextView text = (TextView) view.findViewById(R.id.text);  //查找  到当前     textView
            final TextView icon = (TextView) view.findViewById(R.id.delete_icon);  //查找  到当前  删除小图标

            // 将     已有标签设置成      可选标签
            text.setText(list.get(i));
            /**
             * 将当前  textView  赋值给    textView数组
             */
            textViews[i] = text;
            icons[i] = icon;

            //设置    单击事件：
            icon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //遍历  图标  删除 当前  被点击项
                    for(int j = 0; j < icons.length;j++){
                        if(icon.equals(icons[j])){  //获取   当前  点击删除图标的位置：
                            mTagLayout.removeViewAt(j);
                            list.remove(j);
                            initLayout(list);
                        }
                    }
                }
            });

            text.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    text.setActivated(!text.isActivated()); // true是激活的

                    if (text.isActivated()) {
                        boolean bResult = doAddText(list.get(pos), false, pos);
                        text.setActivated(bResult);
                        //遍历   数据    将图标设置为可见：
                        for(int j = 0;j< textViews.length;j++){
                            if(text.equals(textViews[j])){//非当前  textView
                                icons[j].setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        for(int j = 0;j< textViews.length;j++){
                            icons[j].setVisibility(View.GONE);
                        }
                    }

                    /**
                     * 遍历  textView  满足   已经被选中     并且不是   当前对象的textView   则置为  不选
                     */
                    for(int j = 0;j< textViews.length;j++){
                        if(!text.equals(textViews[j])){//非当前  textView
                            textViews[j].setActivated(false); // true是激活的
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                }
            });

            mTagLayout.addView(view);
        }

        mTagLayout.addView(inputLabel);
    }

    // 标签索引文本
    protected int idxTextTag(String text) {
        int mTagCnt = mAddTags.size(); // 添加标签的条数
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTags.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }

    // 标签添加文本状态
    private boolean doAddText(final String str, boolean bCustom, int idx) {
        int tempIdx = idxTextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTags.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;
            return true;
        }
        int tagCnt = mAddTags.size(); // 添加标签的条数
        TagItem item = new TagItem();
        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTags.add(item);
        tagCnt++;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}