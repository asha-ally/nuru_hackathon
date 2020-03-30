package com.openinstitute.nuru;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openinstitute.nuru.Database.DatabaseHelper;
import com.openinstitute.nuru.Database.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Externalizable;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.openinstitute.nuru.app.AppFunctions.func_formatDateFromString;
import static com.openinstitute.nuru.app.AppFunctions.func_stringpart;
import static java.lang.System.currentTimeMillis;


public class postsAdapter extends RecyclerView.Adapter<postsAdapter.myViewHandler> implements Filterable {
    private Context context;
    private ArrayList<Post> postList; //= new ArrayList<>();
    public static final String SHARE_DESCRIPTION = "Look at this new post";
    public static final String HASHTAG_CANDYCODED = " #Nuru";
    private List<Post> postListFiltered;
    private List<Post> animalListFiltered;


    public postsAdapter(Context mContext, ArrayList<Post> postList) {
        this.context = mContext;
        this.postList= postList;
        this.postListFiltered = postList;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    postListFiltered=postList;
                }else{
                    List<Post> filteredList = new ArrayList<>();
                    for(Post row:postList){
                        /* Rage Chips */
                        String desc_tag_combi = row.getPost_details().toLowerCase() + row.getPost_tag().toLowerCase();
//charSequence.toString().toLowerCase();
                        if(desc_tag_combi.contains(charSequence.toString().toLowerCase())){
                            filteredList.add(row);
                        }
                    }
                    postListFiltered=filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = postListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                postListFiltered = (ArrayList<Post> )filterResults.values;
                notifyDataSetChanged();

            }
        };}

    /*@NonNull*/
    @Override
    public postsAdapter.myViewHandler onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_row, parent, false);

        return new myViewHandler(itemView);


    }

    @Override
    public void onBindViewHolder( postsAdapter.myViewHandler holder, int position) {
        Post post = postListFiltered.get(position);


        String rec_details = func_stringpart(post.getPost_details(), 150);

        String tags = post.getPost_tag().replace("|", "; ");

        /*holder.title.setText(post.getPost_title());*/
        holder.title.setText(post.getRecord_date());
        holder.description.setText(rec_details);
        holder.date.setText(post.getRecord_date());
        holder.rec_project.setText(post.getPost_project());
        holder.rec_tag.setText(tags);
        holder.rec_post_id.setText( String.valueOf(post.getPost_Id()) );

        //RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) holder.card_guts.getLayoutParams();

        Log.d("post_imageUrl", String.valueOf(post.getPost_imageUrl().length()));

        if (!post.post_imageUrl.equals("")) {
            Glide.with(holder.itemView.getContext()).load(post.post_imageUrl).into(holder.thumbnail);
            holder.thumbnail.setVisibility(View.VISIBLE);

            //relativeParams.setMargins(70, 0, 0, 0);  // left, top, right, bottom
            //holder.card_guts.setLayoutParams(relativeParams);
        }
        else {
            holder.thumbnail.setVisibility(View.GONE);

            //relativeParams.setMargins(0, 0, 0, 0);  // left, top, right, bottom
            //holder.card_guts.setLayoutParams(relativeParams);
        }

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
//
            }

    });
    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_post, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }



    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_project:
                    Toast.makeText(context, "Add to project", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_share:
                    Toast.makeText(context, "share", Toast.LENGTH_SHORT).show();
                    createShareIntent();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        Log.d("waterlilies",String.valueOf(postList.size()));

//        return postList.size();
        return postListFiltered.size();
    }


    public class myViewHandler extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, description, date, rec_project, rec_tag, rec_post_id;
        public ImageView thumbnail, overflow;
        public RelativeLayout card_guts;

        public myViewHandler(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            overflow = itemView.findViewById(R.id.overflow);
            rec_project = itemView.findViewById(R.id.rec_project);
            rec_tag = itemView.findViewById(R.id.rec_tag);
            rec_post_id = itemView.findViewById(R.id.rec_post_id);

            //card_guts = itemView.findViewById(R.id.card_guts);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("position",String.valueOf(position));
            Post clickedPost = postList.get(position);

            JSONObject asha = clickedPost.getPostAll();
            String thePostData = String.valueOf(asha);
            String thePost_id = "";

            try {
                thePost_id = asha.getString("post_Id");

                Log.d("postList", String.valueOf(clickedPost));
                Log.d("postList asha", String.valueOf(asha));

                Intent intent = new Intent(v.getContext(),ViewPost.class);
                intent.putExtra("PostActivity", String.valueOf(clickedPost));
                intent.putExtra("PostData", thePostData);
                intent.putExtra("PostId", thePost_id);
                intent.putExtra("PostPosition", String.valueOf(position));

                v.getContext().startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

    private void  createShareIntent(){
        Intent shareIntent= new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String shareString= SHARE_DESCRIPTION + HASHTAG_CANDYCODED;
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareString);
        context.startActivity(shareIntent);

    }


}
