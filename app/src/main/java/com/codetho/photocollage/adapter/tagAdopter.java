package com.codetho.photocollage.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.codetho.photocollage.R;

import java.util.ArrayList;
import java.util.List;

public class tagAdopter extends RecyclerView.Adapter<tagAdopter.ViewHolder> {

    private List<tagModel> taglist = new ArrayList<tagModel>();
    private tagAdopter.onTagSelected mOnItemSelected;

    public tagAdopter(tagAdopter.onTagSelected onItemSelected) {
        mOnItemSelected = onItemSelected;
//        mToolList.add(new collageToolAdopter.collageModel(R.drawable.one, collageToolAdopter.collageToolType.ONE));
        taglist.add(new tagModel("#movies", tagType.MOVIES));
        taglist.add(new tagModel("#trending", tagType.TRENDINGS));

//        mToolList.add(new collageModel(R.drawable., collageToolType.FBCOVER));
//        mToolList.add(new collageModel( R.drawable.four, collageToolType.INSTA));
//       mToolList.add(new collageModel(R.drawable., collageToolType.FIVE));
//        mToolList.add(new collageModel( R.drawable., collageToolType.SIX));
    }

    public interface onTagSelected {
        void onTagSelected(tagType TagType);
    }

    class tagModel {
        //        private int mToolIcon;
        private String Stxt;
        private tagType tagtype;

        tagModel( String txt, tagType toolType) {

            Stxt = txt;
            tagtype = toolType;
        }

    }

    @NonNull
    @Override
    public tagAdopter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tags_layout, parent, false);
        return new tagAdopter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tagAdopter.ViewHolder holder, int position) {
        tagModel item = taglist.get(position);
        holder.txtTool.setText(item.Stxt);

//        holder.imgToolIcon.setImageResource(item.mToolIcon);

    }



    @Override
    public int getItemCount() {
        return taglist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View itemView) {
            super(itemView);
            txtTool = (TextView) itemView.findViewById(R.id.tags_txt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelected.onTagSelected(taglist.get(getLayoutPosition()).tagtype);
                }
            });
        }
    }
    public enum tagType {
        MOVIES,
        TRENDINGS;
    }

}

