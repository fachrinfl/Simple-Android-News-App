package me.fachrinfl.androidnewsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.fachrinfl.androidnewsapp.Common.Common;
import me.fachrinfl.androidnewsapp.Interface.ItemClickListener;
import me.fachrinfl.androidnewsapp.ListNews;
import me.fachrinfl.androidnewsapp.Model.WebSite;
import me.fachrinfl.androidnewsapp.R;
import me.fachrinfl.androidnewsapp.SplashActivity;

import static maes.tech.intentanim.CustomIntent.customType;

/**
 * Created by fachrinfl on 27/03/18.
 */

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemClickListener itemClickListener;

    @BindView(R.id.source_title)
    TextView source_title;
    @BindView(R.id.source_description)
    TextView source_description;

    public ListSourceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder> {

    private Context context;
    private WebSite webSite;

    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;

    }

    @Override
    public ListSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, parent, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListSourceViewHolder holder, int position) {
        String fontTitle = "fonts/Ubuntu-Medium.ttf";
        String fontDescription = "fonts/Ubuntu-Light.ttf";

        holder.source_title.setText(webSite.getSources().get(position).getName());
        holder.source_title.setTypeface(Typeface.createFromAsset(context.getAssets(), fontTitle));

        holder.source_description.setText(webSite.getSources().get(position).getDescription());
        holder.source_description.setTypeface(Typeface.createFromAsset(context.getAssets(), fontDescription));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source",webSite.getSources().get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return webSite.getSources().size();
    }

}
