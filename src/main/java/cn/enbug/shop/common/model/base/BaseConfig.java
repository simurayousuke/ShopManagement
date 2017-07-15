package cn.enbug.shop.common.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseConfig<M extends BaseConfig<M>> extends Model<M> implements IBean {

    public java.lang.Integer getId() {
        return get("id");
    }

    public M setId(java.lang.Integer id) {
        set("id", id);
        return (M) this;
    }

    public java.lang.Integer getOrder() {
        return get("order");
    }

    public M setOrder(java.lang.Integer order) {
        set("order", order);
        return (M) this;
    }

}