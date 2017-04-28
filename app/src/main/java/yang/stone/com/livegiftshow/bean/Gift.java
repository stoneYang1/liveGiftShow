package yang.stone.com.livegiftshow.bean;

/**
 * Created by WZH on 2016/12/25.
 */

public class Gift {
    public int giftId;
    public String giftName;
    public int giftIco;
    public String giftAvatar;


    public int uuid;
    public String sendUserId;//
    public int headUrl;//发送者 头像
    public String sendName;//发送者名称
    public int showNum;//显示的数字

    public  Gift clone(){
        Gift gift = new Gift();
        gift.giftId = this.giftId;
        gift.giftName = this.giftName;
        gift.giftIco = this.giftIco;
        gift.giftAvatar = this.giftAvatar;
        gift.uuid = this.uuid;
        gift.sendUserId = this.sendUserId;
        gift.headUrl = this.headUrl;
        gift.sendName = this.sendName;
        gift.showNum = this.showNum;
        return gift;
    }



}
