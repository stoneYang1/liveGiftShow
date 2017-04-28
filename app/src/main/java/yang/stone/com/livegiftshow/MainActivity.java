package yang.stone.com.livegiftshow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

import yang.stone.com.livegiftshow.bean.Gift;
import yang.stone.com.livegiftshow.ui.LiveShowGiftView;

public class MainActivity extends AppCompatActivity {

    private LiveShowGiftView gift_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
       gift_show = (LiveShowGiftView) findViewById(R.id.gift_show);
    }

    public void onSendGiftClick(View view){
        Gift gift = new Gift();
        int randomNum = new Random().nextInt(5);
        gift.giftId = randomNum;
        gift.giftName = "礼物"+randomNum;
        gift.giftIco = R.mipmap.gift_666;
        gift.sendUserId = "123";
        gift.headUrl = R.mipmap.face;
        gift.sendName = "发送者"+randomNum;
        gift.showNum = 1;
        gift_show.addGift(gift,false);
    }
}
