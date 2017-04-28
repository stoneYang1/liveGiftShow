package yang.stone.com.livegiftshow.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;

import yang.stone.com.livegiftshow.R;
import yang.stone.com.livegiftshow.bean.Gift;
import yang.stone.com.livegiftshow.utils.NumAnimUtils;

/**
 * Created by StoneYang on 2017/3/23.
 */

public class LiveShowGiftView extends LinearLayout {

    private List<View> giftViewCollection = new LinkedList<>();
    private SparseArray<Gift> giftList = new SparseArray<>();//等待显示的礼物
    private SparseArray<Gift> giftShowList = new SparseArray<>();//正在显示的礼物
    private NumAnimUtils giftNumAnim;
    private TranslateAnimation inAnim;
    private ObjectAnimator outAnim;
    private int GiftShowTime = 3000;//礼物显示的时间 默认3秒
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int uuid = msg.what;
            updateGitView(uuid);
        }
    };



    public LiveShowGiftView(Context context) {
        super(context);
        init(context);
    }

    public LiveShowGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LiveShowGiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        giftNumAnim = new NumAnimUtils();
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.gift_in);
    }






    private void showGift( Gift gift){
        View view = this.findViewWithTag(gift.uuid);
        if(view == null){//不再礼物列表
            view = addGiftView();
            final GiftViewHolder holder = (GiftViewHolder)view.getTag(R.id.gift_holder_id);
            holder.bindView(gift);
            view.setTag(gift.uuid);
            view.setTranslationY(0f);
            this.addView(view);/*将礼物的View添加到礼物的ViewGroup中*/
            this.invalidate();/*刷新该view*/
            view.startAnimation(inAnim);/*开始执行显示礼物的动画*/
            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(holder.giftNum);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mHandler.sendEmptyMessageDelayed(gift.uuid,GiftShowTime);
        }else{//礼物已经在显示
            GiftViewHolder holder = (GiftViewHolder)view.getTag(R.id.gift_holder_id);
            holder.giftNum.setText("x" + gift.showNum);
           // gift.start_time = System.currentTimeMillis();
            giftNumAnim.start(holder.giftNum);
            mHandler.removeMessages(gift.uuid);
            mHandler.sendEmptyMessageDelayed(gift.uuid,GiftShowTime);
        }

    }


    /**
     * 添加礼物
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_gift_message, null);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            GiftViewHolder holder = new GiftViewHolder(view);
            view.setTag(R.id.gift_holder_id,holder);

        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }


    /**
     * 删除礼物view
     */
    private void removeGiftView(final View removeView) {


        outAnim = ObjectAnimator.ofFloat(removeView,"translationY",0,-getHeight());
        outAnim.setDuration(300);
        outAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                removeView.clearAnimation();
               // removeView.setVisibility(INVISIBLE);
                removeView(removeView);
                giftViewCollection.add(removeView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                removeView.clearAnimation();
               // removeView.setVisibility(INVISIBLE);
                removeView(removeView);
                giftViewCollection.add(removeView);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        outAnim.start();

    }




    public static class GiftViewHolder{

        public ImageView headImg;
        public ImageView giftImg;
        public MagicTextView giftNum;
        public TextView name;
        public TextView giftName;
        public   GiftViewHolder(View view){
            headImg = (ImageView) view.findViewById(R.id.img_avatar);
            giftImg = (ImageView) view.findViewById(R.id.gift_type);
            giftNum = (MagicTextView) view.findViewById(R.id.gift_num);/*找到数量控件*/
            name = (TextView) view.findViewById(R.id.name);
            giftName = (TextView) view.findViewById(R.id.gift_name);


        }
        public void bindView(Gift gift){
            giftNum.setText("x"+gift.showNum);/*设置礼物数量*/
            giftImg.setImageResource(gift.giftIco);
            headImg.setImageResource(gift.headUrl);
         /*   Glide.with(giftImg.getContext()).load(gift.giftIco).centerCrop().into(giftImg);
            Glide.with(headImg.getContext()).load(gift.headUrl).asBitmap().into(headImg);*/
            name.setText(gift.sendName);
            giftName.setText(" 送 讲师 " + gift.giftName);
        }
    }

    /**
     * 添加礼物
     * @param gift
     * @param immediatelyshow  是否立即展示 自己发送的礼物立即展示
     */
    public void addGift(Gift gift,boolean immediatelyshow){
        //gift = gift.clone();
        gift.uuid = (gift.sendUserId + gift.giftId).hashCode();
        View view = this.findViewWithTag(gift.uuid);
        if(view != null) {//VieW 在显示 直接加入
            Gift temp  = giftShowList.get(gift.uuid);
            //LogUtils.e("TAG","礼物数量ch："+temp.showNum);
            if(temp != null ) {
                temp.showNum += gift.showNum;
                showGift(temp);
            }
           // LogUtils.e("TAG","礼物数量："+temp.showNum);
        }else {
            if(immediatelyshow){//自己的礼物立即展示
                giftShowList.put(gift.uuid, gift);//加入到正在显示的队列
                showGift(gift);//显示礼物
                return;
            }
            Gift gift1 = giftList.get(gift.uuid);
            if (gift1 != null) {//在待显示的 队列 直接加计数
                gift1.showNum += gift.showNum;
            } else {
                if (getChildCount() < 5) {//在显示的礼物小于五个 直接展示
                    giftShowList.put(gift.uuid, gift);//加入到正在显示的队列
                    showGift(gift);//显示礼物
                }else{
                    giftList.put(gift.uuid, gift);
                    if(giftList.size() > 5){//超过 5个待显示 每个显示 1000秒
                        GiftShowTime = 1000;
                    }else if(giftList.size() > 3){//超过 3个待显示 每个显示 1500秒
                        GiftShowTime = 1500;
                    }else{
                        GiftShowTime = 3000;
                    }
                }
            }
        }
    }


    /**
     * 更新礼物列表
     */
    private void updateGitView(int uuid){
        View view =LiveShowGiftView.this.findViewWithTag(uuid);
        removeGiftView(view);//时间到 移除View
        giftShowList.remove(uuid);//从正在显示的列表移除
        if(giftList.size() > 0){//列表 中有等待显示的礼物
            Gift gift =  giftList.valueAt(0);
            if(gift != null) {
                giftList.removeAt(0);
                showGift(gift);
            }
        }
        if(giftList.size() > 5){//超过 5个待显示 每个显示 1000秒
            GiftShowTime = 1000;
        }else if(giftList.size() > 3){//超过 3个待显示 每个显示 1500秒
            GiftShowTime = 1500;
        }else{
            GiftShowTime = 3000;
        }
    }

}
