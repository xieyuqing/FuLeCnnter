package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.NetUtilRS;

/**
 * Created by clawpo on 16/3/19.
 */
public class BoutiqueAdapterRS extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = BoutiqueAdapterRS.class.getName();

    Context mContext;
    ArrayList<BoutiqueBean> boutiqueList;
    ImageLoader imageLoader;
    /** 还有更多的数据可供下载*/
    boolean misMore;

    /**RecyclerView*/
    ViewGroup parent;
    String footerText;
    BoutiqueViewHolder boutiqueViewHolder;
    FooterViewHolder footerHolder;

    public BoutiqueAdapterRS(Context mContext, ArrayList<BoutiqueBean> boutiqueList) {
        this.mContext = mContext;
        this.boutiqueList = boutiqueList;
        this.imageLoader = new ImageLoader(FuLiCenterApplication.getInstance().getRequestQueue(),
                new NetUtilRS.BitmapCaches(mContext));

    }

    /** 设置底部用于提示上拉刷新的文本*/
    public void setFooterText(String text) {
        footerText=text;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return misMore;
    }

    public void setMore(boolean isMore) {
        this.misMore = isMore;
    }

    public void initItems(ArrayList<BoutiqueBean> list){
        if(boutiqueList!=null){
            boutiqueList.clear();
        }
        boutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<BoutiqueBean> list){
        boutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case I.TYPE_ITEM:
                holder = new BoutiqueViewHolder(inflater.inflate(R.layout.item_boutique, parent,false));
                break;
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(inflater.inflate(R.layout.item_footer, parent,false));
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FooterViewHolder){
            footerHolder  = (FooterViewHolder) holder;
            footerHolder.tvFooter.setText(footerText);
            footerHolder.tvFooter.setVisibility(View.VISIBLE);
            return;
        }
        if(position == boutiqueList.size()){
            return;
        }
        if(holder instanceof BoutiqueViewHolder){
            boutiqueViewHolder = (BoutiqueViewHolder) holder;
            final BoutiqueBean boutique = boutiqueList.get(position);
            boutiqueViewHolder.tvDescription.setText(boutique.getDescription());
            boutiqueViewHolder.tvName.setText(boutique.getName());
            boutiqueViewHolder.tvTitle.setText(boutique.getTitle());
            String url = I.DOWNLOAD_BOUTIQUE_IMG_URL+boutique.getImageurl();
            boutiqueViewHolder.iv.setDefaultImageResId(R.drawable.nopic);
            boutiqueViewHolder.iv.setErrorImageResId(R.drawable.nopic);
            boutiqueViewHolder.iv.setImageUrl(url,imageLoader);

            boutiqueViewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("main",boutique.toString());
                    Intent intent=new Intent(mContext, BoutiqueChildActivity.class);
                    intent.putExtra(I.Boutique.ID, boutique.getId());
                    intent.putExtra(I.Boutique.NAME, boutique.getName());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()){
            return I.TYPE_FOOTER;
        }else{
            return I.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return boutiqueList==null?0:boutiqueList.size();
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layoutItem;
        NetworkImageView iv;
        TextView tvTitle;
        TextView tvName;
        TextView tvDescription;

        public BoutiqueViewHolder(View itemView) {
            super(itemView);
            layoutItem=(RelativeLayout) itemView.findViewById(R.id.layout_boutique_item);
            iv=(NetworkImageView) itemView.findViewById(R.id.ivBoutiqueImg);
            tvDescription=(TextView) itemView.findViewById(R.id.tvBoutiqueDescription);
            tvName=(TextView) itemView.findViewById(R.id.tvBoutiqueName);
            tvTitle=(TextView) itemView.findViewById(R.id.tvBoutiqueTitle);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder{
        TextView tvFooter;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tvFooter);
        }
    }
}
