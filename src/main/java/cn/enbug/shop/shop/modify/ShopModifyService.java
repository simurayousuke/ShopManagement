package cn.enbug.shop.shop.modify;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.ShopService;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
class ShopModifyService {

    static final ShopModifyService ME = new ShopModifyService();

    Ret modifyName(String token, String name, String ip) {
        if (ShopService.ME.modifyName(token, name, ip)) {
            return Ret.succeed();
        } else {
            return Ret.fail("Fail.");
        }
    }

    Ret modifyDescription(String token,String description,String ip){
        if (ShopService.ME.modifyDescription(token, description, ip)) {
            return Ret.succeed();
        } else {
            return Ret.fail("Fail.");
        }
    }

    Ret transfer(String token,String username,String password,String ip){
        if(ShopService.ME.transfer(token,password,username,ip)){
            return Ret.succeed();
        }else{
            return Ret.fail("Fail");
        }
    }

}
