package com.bameng.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.model.ArticleModel;
import com.bameng.model.ListModel;
import com.bameng.model.TopArticleIdModel;
import com.bameng.utils.DensityUtils;
import com.bameng.widgets.custom.FrescoDraweeController;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 47483 on 2016.11.07.
 */

public class ArticleAdapter extends BaseAdapter {

    private List<ListModel> Articles;
    private List<ListModel> TopArticles;
    private Context mContext;
    private Activity aty;
    public ArticleModel Article;
    private Handler mHandler;
    public long index;

    public ArticleAdapter(List<ListModel> Articles, List<ListModel> TopArticles, Context mContext, Activity aty)
    {
        this.Articles = Articles;
        this.TopArticles = TopArticles;
        this.mContext = mContext;
        this.aty = aty;
    }



    @Override
    public int getCount() {
        return  TopArticles.size()+Articles.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= TopArticles.size()) {
            return Articles.get(position - TopArticles.size());
        } else {
            return TopArticles.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ViewHolderTop viewHolderTop = null;
        if (convertView == null){
            if (position>=TopArticles.size()) {
                convertView = View.inflate(mContext, R.layout.article_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
                ListModel Article = Articles.get(position-TopArticles.size());
                DraweeController draweeController= Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true).build();
                holder.image.setController(draweeController);

                holder.image.setImageURI(Article.getArticleCover());
                holder.articleTitle.setText(Article.getArticleTitle());
                holder.articleIntro.setText(Article.getArticleIntro());
                holder.browseAmount.setText(String.valueOf(Article.getBrowseAmount()));
                holder.time.setText(Article.getPublishTimeText());
            }else {
                convertView = View.inflate(mContext, R.layout.article_item_top, null);
                viewHolderTop = new ViewHolderTop(convertView);
                convertView.setTag(viewHolderTop);
                ListModel topArticleIdModel = TopArticles.get(position);

                DraweeController draweeController= Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true).build();
                viewHolderTop.topImage.setController(draweeController);

                viewHolderTop.topImage.setImageURI(topArticleIdModel.getArticleCover());
                viewHolderTop.topArticleTitle.setText(topArticleIdModel.getArticleTitle());
                viewHolderTop.topArticleIntro.setText(topArticleIdModel.getArticleIntro());
                viewHolderTop.topBrowseAmount.setText(String.valueOf(topArticleIdModel.getBrowseAmount()));
                viewHolderTop.topTime.setText(topArticleIdModel.getPublishTimeText());
            }

        }else
        {


        }




        return convertView;
    }
    class ViewHolder
    {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.articleTitle) TextView articleTitle;
        @Bind(R.id.articleIntro) TextView articleIntro;
        @Bind(R.id.browseAmount) TextView browseAmount;
        @Bind(R.id.time)
        TextView time;



        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    class ViewHolderTop{
        @Bind(R.id.Top_image) SimpleDraweeView topImage;
        @Bind(R.id.Top_articleTitle) TextView topArticleTitle;
        @Bind(R.id.Top_articleIntro) TextView topArticleIntro;
        @Bind(R.id.Top_browseAmount) TextView topBrowseAmount;
        @Bind(R.id.Top_time) TextView topTime;
        public ViewHolderTop(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
