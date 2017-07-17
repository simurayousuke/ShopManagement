/*
 * Copyright (c) 2017 EnBug Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.enbug.shop.common.service;

import cn.enbug.shop.common.exception.*;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.*;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * order status:
 * -2 closed
 * -1 refunding
 * 0 waiting payment
 * 1 waiting send
 * 2 sending
 * 3 checked
 * 4 commented
 * -2 or 3/4 means finished
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.1.3
 * @since 1.0.0
 */
public class OrderService {

    public static final OrderService ME = Duang.duang(OrderService.class);
    private static final Config CONFIG_DAO = new Config().dao();
    private static final Order ORDER_DAO = new Order().dao();
    private static final OrderNumber ORDER_NUMBER_DAO = new OrderNumber().dao();

    private Config getConfig() {
        return CONFIG_DAO.findFirst(CONFIG_DAO.getSql("order.config"));
    }

    private String getCurrentOrder() {
        long orderNum = RedisKit.getCurrentOrderId();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return date + String.format("%08d", orderNum);
    }

    /**
     * getOrderListByOrderNumber
     *
     * @param number order number
     * @return List
     */
    public List<Order> getOrderListByOrderNumber(String number) {
        return null == number ? new ArrayList<>() : ORDER_DAO.find(ORDER_DAO.getSqlPara("order.findByOrderNumber", number));
    }

    /**
     * getOrderListByTokenAndStatusForBuyer
     *
     * @param token  buyer token
     * @param status order status
     * @return List
     */
    public List<Order> getOrderListByTokenAndStatusForBuyer(String token, int status) {
        User user = RedisKit.getUserByToken(token);
        return null == user ? new ArrayList<>() : ORDER_DAO.find(ORDER_DAO.getSqlPara("order.findByUserIdAndStatus", user.getId(), status));
    }

    /**
     * getOrderListByTokenAndStatusForSeller
     *
     * @param token  seller token
     * @param status order status
     * @return List
     */
    public List<Order> getOrderListByTokenAndStatusForSeller(String token, int status) {
        User user = RedisKit.getUserByToken(token);
        return null == user ? new ArrayList<>() : ORDER_DAO.find(ORDER_DAO.getSqlPara("order.findByOwnerIdAndStatus", user.getId(), status));
    }

    /**
     * getOrderByOrderNumAndGoodId
     *
     * @param number order number
     * @param id     good id
     * @return Order Object
     */
    public Order getOrderByOrderNumAndGoodId(String number, int id) {
        return null == number ? null : ORDER_DAO.findFirst(ORDER_DAO.getSqlPara("order.findByOrderNumberAndGoodId", number, id));
    }

    /**
     * createOrderFromShopCar
     *
     * @param token     buyer token
     * @param addressId address id
     * @return boolean
     */
    @Before(Tx.class)
    public boolean createOrderFromShopCar(String token, int addressId) {
        User user = RedisKit.getUserByToken(token);
        List<ShopCar> arrayList = ShopCarService.ME.getShopCarListByUser(user);
        if (null == arrayList) {
            return false;
        }
        if (!AddressService.ME.verifyAddress(user, addressId)) {
            return false;
        }
        String orderNum = getCurrentOrder();
        OrderNumber orderNumber = new OrderNumber();
        orderNumber.setOrderNumber(orderNum);
        orderNumber.setUserId(user.getId());
        orderNumber.setStatus(0);
        if (!orderNumber.save()) {
            throw new CreateOrderException("储存订单失败！");
        }
        for (ShopCar o : arrayList) {
            Shop shop = ShopService.ME.findShopById(o.getShopId());
            Good good = GoodService.ME.findGoodById(o.getGoodId());
            int number = good.getNumber();
            if (number < o.getCount()) {
                throw new NoEnoughGoodException("商品库存不足！");
            }
            good.setNumber(number - o.getCount());
            good.setSaleCount(good.getSaleCount() + o.getCount());
            if (!good.update()) {
                throw new ModifyGoodException("更新货物信息失败！");
            }
            HashMap<String, Object> update = new HashMap<>();
            update.put("id", good.getId());
            update.put("number", good.getNumber());
            update.put("sale_count", good.getSaleCount());
            if (!OpenSearchService.ME.update(update)) {
                throw new OpenSearchException("同步搜索引擎失败！");
            }
            Order order = new Order();
            order.setUserId(user.getId());
            order.setShopId(shop.getId());
            order.setOwnerId(shop.getOwnerUserId());
            order.setGoodId(good.getId());
            order.setCount(o.getCount());
            order.setPrice(good.getPrice().multiply(new BigDecimal(o.getCount())));
            order.setOrderNumber(orderNum);
            order.setOrderStatus(0);
            order.setAddressId(addressId);
            order.setShopName(shop.getShopName());
            order.setGoodName(good.getGoodName());
            order.setAvator(good.getAvator());
            order.setShopUuid(shop.getUuid());
            order.setGoodUuid(good.getUuid());
            if (!order.save()) {
                throw new CreateOrderException("储存订单详情失败！");
            }
        }
        if (!ShopCarService.ME.clean(token)) {
            throw new CreateOrderException("清空购物车失败！");
        }
        return true;
    }

    private List<Order> verify(String token, String orderNum) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return new ArrayList<>();
        }
        List<Order> orders = getOrderListByOrderNumber(orderNum);
        if (null == orders) {
            return new ArrayList<>();
        }
        Order first = orders.get(0);
        if (!Objects.equals(first.getUserId(), user.getId()) && !Objects.equals(first.getOwnerId(), user.getId())) {
            return new ArrayList<>();
        }
        return orders;
    }

    private List<Order> verifyBuyer(String token, String orderNum) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return new ArrayList<>();
        }
        List<Order> orders = getOrderListByOrderNumber(orderNum);
        if (null == orders) {
            return new ArrayList<>();
        }
        Order first = orders.get(0);
        if (!Objects.equals(first.getUserId(), user.getId())) {
            return new ArrayList<>();
        }
        return orders;
    }

    private List<Order> verifySeller(String token, String orderNum) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return new ArrayList<>();
        }
        List<Order> orders = getOrderListByOrderNumber(orderNum);
        if (null == orders) {
            return new ArrayList<>();
        }
        Order first = orders.get(0);
        if (!Objects.equals(first.getOwnerId(), user.getId())) {
            return new ArrayList<>();
        }
        return orders;
    }

    private OrderNumber getOrderNumberByOrderNumber(String number) {
        return null == number ? null : ORDER_NUMBER_DAO.findFirst((ORDER_NUMBER_DAO.getSqlPara("orderNumber.findByOrderNumber", number)));
    }

    private List<OrderNumber> getOrderNumberListByUserId(int id) {
        return ORDER_NUMBER_DAO.find(ORDER_NUMBER_DAO.getSqlPara("orderNumber.findByUserId", id));
    }

    private List<OrderNumber> getUnpayedOrderNumberListByUserId(int id) {
        return ORDER_NUMBER_DAO.find(ORDER_NUMBER_DAO.getSqlPara("orderNumber.findByUserIdAndStatus", id, 0));
    }

    /**
     * closeGood
     *
     * @param token    seller token
     * @param orderNum order number
     * @param goodId   good id
     * @return boolean
     */
    public boolean closeGood(String token, String orderNum, int goodId) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Order order = getOrderByOrderNumAndGoodId(orderNum, goodId);
        if (null == order) {
            return false;
        }
        if (!Objects.equals(user.getId(), order.getOwnerId())) {
            return false;
        }
        if (!UserService.ME.transfer(user, order.getUserId(), order.getPrice())) {
            return false;
        }
        if (order.getOrderStatus() != -1) {
            return false;
        }
        order.setOrderStatus(-2);
        order.setFinishTime(new Timestamp(System.currentTimeMillis()));
        if (!order.update()) {
            throw new ModifyOrderStatusException("Fail to close good.");
        }
        return true;
    }

    /**
     * refundGood
     *
     * @param token    buyer token
     * @param orderNum order number
     * @param goodId   good id
     * @return boolean
     */
    public boolean refundGood(String token, String orderNum, int goodId) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Order order = getOrderByOrderNumAndGoodId(orderNum, goodId);
        if (null == order) {
            return false;
        }
        if (!Objects.equals(user.getId(), order.getUserId())) {
            return false;
        }
        // not payed good can not be refunded
        if (order.getOrderStatus() <= 0) {
            return false;
        }
        order.setOrderStatus(-1);
        order.setDealTime(new Timestamp(System.currentTimeMillis()));
        if (!order.update()) {
            throw new ModifyOrderStatusException("Fail to refund good.");
        }
        return true;
    }

    /**
     * pay
     *
     * @param token    buyer token
     * @param orderNum order number
     * @return boolean
     */
    @Before(Tx.class)
    public boolean payOrder(String token, String orderNum) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        List<Order> orders = getOrderListByOrderNumber(orderNum);
        if (null == orders) {
            return false;
        }
        Order first = orders.get(0);
        if (!Objects.equals(first.getUserId(), user.getId())) {
            return false;
        }
        OrderNumber number = getOrderNumberByOrderNumber(orderNum);
        if (null == number) {
            return false;
        }
        if (!number.setStatus(1).update()) {
            throw new ModifyOrderStatusException("Fail to pay good.(1)");
        }
        Timestamp date = new Timestamp(System.currentTimeMillis());
        for (Order o : orders) {
            if (!UserService.ME.transfer(user, o.getOwnerId(), o.getPrice())) {
                throw new NoEnoughMoneyException("Fail to pay order.");
            }
            o.setOrderStatus(1);
            o.setDealTime(date);
            if (!o.update()) {
                throw new ModifyOrderStatusException("Fail to pay order.");
            }
        }
        return true;
    }

    /**
     * sendGood
     *
     * @param token    seller token
     * @param orderNum order number
     * @param goodId   good id
     * @return boolean
     */
    public boolean sendGood(String token, String orderNum, int goodId) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Order order = getOrderByOrderNumAndGoodId(orderNum, goodId);
        if (null == order) {
            return false;
        }
        if (!Objects.equals(user.getId(), order.getOwnerId())) {
            return false;
        }
        if (order.getOrderStatus() != 1) {
            return false;
        }
        order.setOrderStatus(2);
        order.setDealTime(new Timestamp(System.currentTimeMillis()));
        if (!order.update()) {
            throw new ModifyOrderStatusException("Fail to send good.");
        }
        return true;
    }

    /**
     * checkGood
     *
     * @param token    buyer token
     * @param orderNum order number
     * @param goodId   good id
     * @return boolean
     */
    public boolean checkGood(String token, String orderNum, int goodId) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Order order = getOrderByOrderNumAndGoodId(orderNum, goodId);
        if (null == order) {
            return false;
        }
        if (!Objects.equals(user.getId(), order.getUserId())) {
            return false;
        }
        if (order.getOrderStatus() != 2) {
            return false;
        }
        order.setOrderStatus(3);
        order.setFinishTime(new Timestamp(System.currentTimeMillis()));
        if (!order.update()) {
            throw new ModifyOrderStatusException("Fail to check good.");
        }
        return true;
    }

    /**
     * comment
     *
     * @param token    buyer token
     * @param orderNum order number
     * @param goodId   good id
     * @param comment  comment
     * @param isGood   is good?
     * @return boolean
     */
    @Before(Tx.class)
    public boolean commentGood(String token, String orderNum, int goodId, String comment, boolean isGood) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Order order = getOrderByOrderNumAndGoodId(orderNum, goodId);
        if (null == order) {
            return false;
        }
        if (!Objects.equals(user.getId(), order.getUserId())) {
            return false;
        }
        if (!CommentService.ME.comment(user, order.getGoodId(), order.getShopId(), comment, isGood)) {
            return false;
        }
        order.setOrderStatus(4);
        if (!order.update()) {
            throw new ModifyOrderStatusException("Fail to comment order.");
        }
        return true;
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<Order> getRefundingListByTokenForBuyer(String token) {
        return getOrderListByTokenAndStatusForBuyer(token, -1);
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<Order> getClosedListByTokenForBuyer(String token) {
        return getOrderListByTokenAndStatusForBuyer(token, -2);
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<Order> getUnPayedListByTokenForBuyer(String token) {
        return getOrderListByTokenAndStatusForBuyer(token, 0);
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<Order> getUnSentListByTokenForBuyer(String token) {
        return getOrderListByTokenAndStatusForBuyer(token, 1);
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<Order> getSendingListByTokenForBuyer(String token) {
        return getOrderListByTokenAndStatusForBuyer(token, 2);
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<Order> getCheckedListByTokenForBuyer(String token) {
        User user = RedisKit.getUserByToken(token);
        return null == user ? new ArrayList<>() : ORDER_DAO.find(ORDER_DAO.getSqlPara("order.findCheckedByUserId", user.getId()));
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<Order> getCheckedNotCommentedListByTokenForBuyer(String token) {
        return getOrderListByTokenAndStatusForBuyer(token, 3);
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<Order> getCommentedListByTokenForBuyer(String token) {
        return getOrderListByTokenAndStatusForBuyer(token, 4);
    }

    /**
     * @param token seller token
     * @return List
     */
    public List<Order> getRefundingListByTokenForSeller(String token) {
        return getOrderListByTokenAndStatusForSeller(token, -1);
    }

    /**
     * @param token seller token
     * @return List
     */
    public List<Order> getClosedListByTokenForSeller(String token) {
        return getOrderListByTokenAndStatusForSeller(token, -2);
    }

    /**
     * @param token seller token
     * @return List
     */
    public List<Order> getUnPayedListByTokenForSeller(String token) {
        return getOrderListByTokenAndStatusForSeller(token, 0);
    }

    /**
     * @param token seller token
     * @return List
     */
    public List<Order> getUnSentListByTokenForSeller(String token) {
        return getOrderListByTokenAndStatusForSeller(token, 1);
    }

    /**
     * @param token seller token
     * @return List
     */
    public List<Order> getSendingListByTokenForSeller(String token) {
        return getOrderListByTokenAndStatusForSeller(token, 2);
    }

    /**
     * @param token seller token
     * @return List
     */
    public List<Order> getCheckedListByTokenForSeller(String token) {
        User user = RedisKit.getUserByToken(token);
        return null == user ? new ArrayList<>() : ORDER_DAO.find(ORDER_DAO.getSqlPara("order.findCheckedByOwnerId", user.getId()));
    }

    /**
     * @param token seller token
     * @return List
     */
    public List<Order> getCheckedNotCommentedListByTokenForSeller(String token) {
        return getOrderListByTokenAndStatusForSeller(token, 3);
    }

    /**
     * @param token seller token
     * @return List
     */
    public List<Order> getCommentedListByTokenForSeller(String token) {
        return getOrderListByTokenAndStatusForSeller(token, 4);
    }

    /**
     * @param token buyer token
     * @return List
     */
    public List<OrderNumber> getUnpayedOrderNumberList(String token) {
        User user = RedisKit.getUserByToken(token);
        return null == user ? new ArrayList<>() : getUnpayedOrderNumberListByUserId(user.getId());
    }

}
