package com.codetho.photocollage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codetho.photocollage.R;

import com.codetho.photocollage.adapter.StraggedAdapter;
import com.codetho.photocollage.adapter.TemplateAdapter;
import com.codetho.photocollage.adapter.TemplateViewHolder;
import com.codetho.photocollage.adapter.tagAdopter;
import com.codetho.photocollage.model.TemplateItem;
import com.codetho.photocollage.quickaction.QuickAction;
import com.codetho.photocollage.quickaction.QuickActionItem;
import com.codetho.photocollage.template.PhotoItem;

import com.codetho.photocollage.ui.custom.ItemDecorations;
import com.codetho.photocollage.ui.fragment.StaggeredGridLayout;
import com.codetho.photocollage.utils.TemplateImageUtils;
import com.codetho.photocollage.utils.frame.FrameImageUtils;
import com.google.firebase.crash.FirebaseCrash;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;

/**
 * Created by vanhu_000 on 3/25/2016.
 */
public class TemplateActivity extends AdsFragmentActivity implements TemplateViewHolder.OnTemplateItemClickListener,tagAdopter.onTagSelected {
    @Override
    public void onTagSelected(tagAdopter.tagType TagType) {
        switch (TagType) {
            case MOVIES:
                Toast.makeText(getApplicationContext(), "movies selected", Toast.LENGTH_SHORT).show();
                break;

            case TRENDINGS:
                Toast.makeText(getApplicationContext(), "trending selected", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class ViewHolder {
        private final RecyclerView mRecyclerView;

        public ViewHolder(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        public void initViews(LinearLayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }

        public void scrollToPosition(int position) {
            mRecyclerView.scrollToPosition(position);
        }

        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            mRecyclerView.setAdapter(adapter);
        }

        public void smoothScrollToPosition(int position) {
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    public static final String EXTRA_IMAGE_PATHS = "imagePaths";
    public static final String EXTRA_IMAGE_IN_TEMPLATE_COUNT = "imageInTemplateCount";
    public static final String EXTRA_SELECTED_TEMPLATE_INDEX = "selectedTemplateIndex";
    public static final String EXTRA_IS_FRAME_IMAGE = "frameImage";

    private static final int REQUEST_SELECT_PHOTO = 789;

    private static final String KEY_HEADER_POSITIONING = "key_header_mode";

    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";

    private ViewHolder mViews;

    private TemplateAdapter mAdapter;

    private int mHeaderDisplay;

    private boolean mAreMarginsFixed;
    private ArrayList<Integer> count = new ArrayList<>();
    //Template views
    private ArrayList<TemplateItem> mTemplateItemList = new ArrayList<TemplateItem>();
    private ArrayList<TemplateItem> mAllTemplateItemList = new ArrayList<TemplateItem>();
    private boolean mFrameImages = false;
    //Frame filter Quick Action
    private QuickAction mQuickAction;
    private TextView mFilterView;
    private RecyclerView Tags,Recycler;
    private int mImageInTemplateCount = 0;
    private int mSelectedTemplateIndex = 0;
    private StraggedAdapter myAdapter;
    private boolean isLeft = true;
    @Override
    protected void preCreateAdsHelper() {
        mLoadedData = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Tags= (RecyclerView) findViewById(R.id.tags);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        Button edit_btn= (Button) findViewById(R.id.edit_img);
        edit_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PhotoCollageActivity.class);
                intent.putExtra(PhotoCollageActivity.EXTRA_CREATED_METHOD_TYPE, PhotoCollageActivity.PHOTO_TYPE);
                startActivity(intent);

            }
        });

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle(R.string.app_name);
        }

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        } else {
            mHeaderDisplay = getResources().getInteger(R.integer.default_header_display);
            mAreMarginsFixed = getResources().getBoolean(R.bool.default_margins_fixed);
        }
        //add ads view
        //addAdsView(R.id.adsLayout);
        //inflate widgets
        mViews = new ViewHolder((RecyclerView) findViewById(R.id.recycler_view));
        //mViews.initViews(new LayoutManager(this));
        mViews.initViews(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        //show templates
        mFrameImages = getIntent().getBooleanExtra(EXTRA_IS_FRAME_IMAGE, false);
        if (mFrameImages) {
        loadFrameImages(false);
        } else {
          loadFrameImages(true);
        }
        mAdapter = new TemplateAdapter(this, mHeaderDisplay, mTemplateItemList, this);
        mAdapter.setMarginsFixed(mAreMarginsFixed);
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        mViews.setAdapter(mAdapter);
        //Frame count filter
        createFilterQuickAction();
        Tags.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        Tags.setAdapter(new tagAdopter(this));


        RecyclerView Straggedimg = (RecyclerView) findViewById(R.id.straggedview);

        count.add(1);
        final StaggeredGridLayout mLayoutManager = new StaggeredGridLayout(
                new StaggeredGridLayout.GridSpanLookup() {
                    @Override
                    public StaggeredGridLayout.SpanInfo getSpanInfo(int position) {
                        if (myAdapter.getItemViewType(position) == 2) {
                            return new StaggeredGridLayout.SpanInfo(3, 2);
                        }
                        if (checkPositionForSpan(position)) {
                            return new StaggeredGridLayout.SpanInfo(2, 2);
                        } else {
                            return new StaggeredGridLayout.SpanInfo(1, 1);
                        }
                    }
                },
                3, // number of columns
                1f // default size of item
        );
        Straggedimg.setLayoutManager(mLayoutManager);
        Straggedimg.addItemDecoration(new ItemDecorations(getResources().getDimensionPixelSize(R.dimen.gridspacing), 3));



         myAdapter = new StraggedAdapter(getApplicationContext(), getData());
        Straggedimg.setAdapter(myAdapter);


    }




    public ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();

        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16999?alt=media&token=0fa8c578-72b1-40f9-80d3-86ab0c4cf88f");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16919?alt=media&token=fa6a7010-4b90-4cca-aacd-4bed672058ea");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16551?alt=media&token=781a9c1c-4482-4e9d-a85f-eb258252394a");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16918?alt=media&token=040ba5b3-3e46-48d1-aa50-e9578a72af04");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A17055?alt=media&token=6d4792d8-ee96-4948-85da-ac55f6c20a95");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16999?alt=media&token=0fa8c578-72b1-40f9-80d3-86ab0c4cf88f");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16919?alt=media&token=fa6a7010-4b90-4cca-aacd-4bed672058ea");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16551?alt=media&token=781a9c1c-4482-4e9d-a85f-eb258252394a");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16918?alt=media&token=040ba5b3-3e46-48d1-aa50-e9578a72af04");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A17055?alt=media&token=6d4792d8-ee96-4948-85da-ac55f6c20a95");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16999?alt=media&token=0fa8c578-72b1-40f9-80d3-86ab0c4cf88f");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16919?alt=media&token=fa6a7010-4b90-4cca-aacd-4bed672058ea");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16551?alt=media&token=781a9c1c-4482-4e9d-a85f-eb258252394a");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16918?alt=media&token=040ba5b3-3e46-48d1-aa50-e9578a72af04");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A17055?alt=media&token=6d4792d8-ee96-4948-85da-ac55f6c20a95");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16999?alt=media&token=0fa8c578-72b1-40f9-80d3-86ab0c4cf88f");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16919?alt=media&token=fa6a7010-4b90-4cca-aacd-4bed672058ea");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16551?alt=media&token=781a9c1c-4482-4e9d-a85f-eb258252394a");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16918?alt=media&token=040ba5b3-3e46-48d1-aa50-e9578a72af04");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A17055?alt=media&token=6d4792d8-ee96-4948-85da-ac55f6c20a95");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16999?alt=media&token=0fa8c578-72b1-40f9-80d3-86ab0c4cf88f");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16919?alt=media&token=fa6a7010-4b90-4cca-aacd-4bed672058ea");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16551?alt=media&token=781a9c1c-4482-4e9d-a85f-eb258252394a");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A16918?alt=media&token=040ba5b3-3e46-48d1-aa50-e9578a72af04");
        data.add("https://firebasestorage.googleapis.com/v0/b/upload-photo-33327.appspot.com/o/ImageFolder%2Fimage%2Fimage%3A17055?alt=media&token=6d4792d8-ee96-4948-85da-ac55f6c20a95");

        return data;
    }

    public boolean checkPositionForSpan(int position) {
        int tempCount = 1;
        int startIndex = 0;

        if (count != null && count.size() >= 1) {
            if (count.get(count.size() - 1) < position) {
                tempCount = count.get(count.size() - 1);
                if (count.get(count.size() - 1) > 1)
                    startIndex = count.get(count.size() - 1);
                for (int i = startIndex; i < startIndex + 100; i++) {
                    int temp = tempCount + (isLeft ? 8 : 10);
                    if (temp == i) {
                        tempCount = temp;
                        count.add(temp);
                        isLeft = !isLeft;
                    }
                }
            }

            if (count.contains(position)) {
                return true;
            }
        }
        return false;
    }





    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_HEADER_POSITIONING, mHeaderDisplay);
        outState.putBoolean(KEY_MARGINS_FIXED, mAreMarginsFixed);
        outState.putBoolean(EXTRA_IS_FRAME_IMAGE, mFrameImages);
        outState.putInt("mImageInTemplateCount", mImageInTemplateCount);
        outState.putInt("mSelectedTemplateIndex", mSelectedTemplateIndex);
    }

    protected void restoreInstanceState(Bundle savedInstanceState) {
        mHeaderDisplay = savedInstanceState
                .getInt(KEY_HEADER_POSITIONING,
                        getResources().getInteger(R.integer.default_header_display));
        mAreMarginsFixed = savedInstanceState
                .getBoolean(KEY_MARGINS_FIXED,
                        getResources().getBoolean(R.bool.default_margins_fixed));
        mFrameImages = savedInstanceState.getBoolean(EXTRA_IS_FRAME_IMAGE, false);
        mImageInTemplateCount = savedInstanceState.getInt("mImageInTemplateCount");
        mSelectedTemplateIndex = savedInstanceState.getInt("mSelectedTemplateIndex");
    }

    private void loadFrameImages(boolean template) {
        mAllTemplateItemList.clear();
        if (template) {
         mAllTemplateItemList.addAll(TemplateImageUtils.loadTemplates());
       } else {
            mAllTemplateItemList.addAll(FrameImageUtils.loadFrameImages(this));
        }
        mTemplateItemList.clear();
        if (mImageInTemplateCount > 0) {
            for (TemplateItem item : mAllTemplateItemList)
                if (item.getPhotoItemList().size() == mImageInTemplateCount) {
                    mTemplateItemList.add(item);
                }
        } else {
            mTemplateItemList.addAll(mAllTemplateItemList);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_frame_count, menu);
        MenuItem item = menu.findItem(R.id.action_filter);
       // MenuItem item1 = menu.findItem(R.id.action_edit);
       // CardView cd=(CardView) item1.getActionView().findViewById(R.id.card1);
        mFilterView = (TextView) item.getActionView().findViewById(R.id.frameCountView);
        mFilterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickAction.show(mFilterView);
            }
        });
        return true;
    }

  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_frame, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            clickDoneButton();
        } else if (id == R.id.action_add) {
            clickAddButton();
        }

        return super.onOptionsItemSelected(item);
    }
*/

    private void createFilterQuickAction() {
        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        mQuickAction = new QuickAction(this, QuickAction.VERTICAL);
        mQuickAction.setPopupBackgroundColor(getResources().getColor(R.color.primaryColor));
        //add action items into QuickAction
        String[] filterTexts = getResources().getStringArray(R.array.frame_count);
        if (mFrameImages) {
            for (int idx = 0; idx < filterTexts.length; idx++) {
                QuickActionItem item = new QuickActionItem(idx, filterTexts[idx]);
                mQuickAction.addActionItem(item);
            }
        } else {
            for (int idx = 0; idx < 4; idx++) {
                QuickActionItem item = new QuickActionItem(idx, filterTexts[idx]);
                mQuickAction.addActionItem(item);
            }
        }
        //Set listener for action item clicked
        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                QuickActionItem quickActionItem = mQuickAction.getActionItem(pos);
                mQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                mFilterView.setText(quickActionItem.getTitle());
                if (quickActionItem.getActionId() == 0) {
                    mTemplateItemList.clear();
                    mTemplateItemList.addAll(mAllTemplateItemList);
                    mImageInTemplateCount = 0;
                } else {
                    mTemplateItemList.clear();
                    mImageInTemplateCount = quickActionItem.getActionId();
                    for (TemplateItem item : mAllTemplateItemList)
                        if (item.getPhotoItemList().size() == quickActionItem.getActionId()) {
                            mTemplateItemList.add(item);
                        }
                }
                mAdapter.setData(mTemplateItemList);
            }
        });

        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
        //by clicking the area outside the dialog.
        mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    @Override
    public void onTemplateItemClick(TemplateItem templateItem) {
        if (!templateItem.isAds()) {
            mSelectedTemplateIndex = mTemplateItemList.indexOf(templateItem);
            Intent data = new Intent(this, SelectPhotoActivity.class);
            data.putExtra(SelectPhotoActivity.EXTRA_IMAGE_COUNT, templateItem.getPhotoItemList().size());
            startActivityForResult(data, REQUEST_SELECT_PHOTO);
        } else {
            //TODO:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHOTO && resultCode == RESULT_OK) {
            try {
                ArrayList<String> mSelectedImages = data.getStringArrayListExtra(SelectPhotoActivity.EXTRA_SELECTED_IMAGES);
                final TemplateItem selectedTemplateItem = mTemplateItemList.get(mSelectedTemplateIndex);
                int itemSize = selectedTemplateItem.getPhotoItemList().size();
                int size = Math.min(itemSize, mSelectedImages.size());
                for (int idx = 0; idx < size; idx++) {
                    selectedTemplateItem.getPhotoItemList().get(idx).imagePath = mSelectedImages.get(idx);
                }
                Intent intent;

                if (mFrameImages) {
                    intent = new Intent(this, FrameDetailActivity.class);
                } else {
                   intent = new Intent(this, TemplateDetailActivity.class);
                }

                intent.putExtra(EXTRA_IMAGE_IN_TEMPLATE_COUNT, selectedTemplateItem.getPhotoItemList().size());
                intent.putExtra(EXTRA_IS_FRAME_IMAGE, mFrameImages);
                if (mImageInTemplateCount == 0) {
                    ArrayList<TemplateItem> tmp = new ArrayList<>();
                    for (TemplateItem item : mTemplateItemList)
                        if (item.getPhotoItemList().size() == selectedTemplateItem.getPhotoItemList().size()) {
                            tmp.add(item);
                        }
                    intent.putExtra(EXTRA_SELECTED_TEMPLATE_INDEX, tmp.indexOf(selectedTemplateItem));
                } else {
                    intent.putExtra(EXTRA_SELECTED_TEMPLATE_INDEX, mSelectedTemplateIndex);
                }
                ArrayList<String> imagePaths = new ArrayList<>();
                for (PhotoItem item : selectedTemplateItem.getPhotoItemList()) {
                    if (item.imagePath == null) item.imagePath = "";
                    imagePaths.add(item.imagePath);
                }
                intent.putExtra(EXTRA_IMAGE_PATHS, imagePaths);
                startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
                FirebaseCrash.report(ex);
            }
        }
    }
}
