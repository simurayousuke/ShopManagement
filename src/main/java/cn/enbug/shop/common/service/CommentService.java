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

import cn.enbug.shop.common.model.Comment;
import cn.enbug.shop.common.model.User;
import com.jfinal.aop.Duang;

import java.util.List;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class CommentService {

    public static final CommentService ME = Duang.duang(CommentService.class);
    private static final Comment COMMENT_DAO = new Comment().dao();

    /**
     * @param id id
     * @return Comment
     */
    public Comment getCommentById(int id) {
        return COMMENT_DAO.findFirst(COMMENT_DAO.getSqlPara("comment.findById", id));
    }

    /**
     * @param goodId good id
     * @return List
     */
    public List<Comment> getListByGoodId(int goodId) {
        return COMMENT_DAO.find(COMMENT_DAO.getSqlPara("comment.findByGoodId", goodId));
    }

    /**
     * @param shopId shop id
     * @return List
     */
    public List<Comment> getListByShopId(int shopId) {
        return COMMENT_DAO.find(COMMENT_DAO.getSqlPara("comment.findByShopId", shopId));
    }

    /**
     * @param userId user id
     * @return List
     */
    public List<Comment> getListByUserId(int userId) {
        return COMMENT_DAO.find(COMMENT_DAO.getSqlPara("comment.findByUserId", userId));
    }

    /**
     * @param goodId good id
     * @return List
     */
    public List<Comment> getGoodListByGoodId(int goodId) {
        return COMMENT_DAO.find(COMMENT_DAO.getSqlPara("comment.findByGoodIdAndGood", goodId));
    }

    /**
     * @param shopId shop id
     * @return List
     */
    public List<Comment> getGoodListByShopId(int shopId) {
        return COMMENT_DAO.find(COMMENT_DAO.getSqlPara("comment.findByShopIdAndGood", shopId));
    }

    /**
     * @param goodId good id
     * @return List
     */
    public List<Comment> getBadListByGoodId(int goodId) {
        return COMMENT_DAO.find(COMMENT_DAO.getSqlPara("comment.findByGoodIdAndBad", goodId));
    }

    /**
     * @param shopId shop id
     * @return List
     */
    public List<Comment> getBadListByShopId(int shopId) {
        return COMMENT_DAO.find(COMMENT_DAO.getSqlPara("comment.findByShopIdAndBad", shopId));
    }

    /**
     * @param goodId good id
     * @return int
     */
    public int getGoodNumByGoodId(int goodId) {
        return getGoodListByGoodId(goodId).size();
    }

    /**
     * @param shopId shop id
     * @return int
     */
    public int getGoodNumByShopId(int shopId) {
        return getGoodListByShopId(shopId).size();
    }

    /**
     * @param goodId good id
     * @return int
     */
    public int getBadNumByGoodId(int goodId) {
        return getBadListByGoodId(goodId).size();
    }

    /**
     * @param shopId shop id
     * @return int
     */
    public int getBadNumByShopId(int shopId) {
        return getBadListByShopId(shopId).size();
    }

    /**
     * please invoke OrderService.ME.commentGood instead of this function.
     *
     * @param user        User Object (buyer)
     * @param goodId      good id
     * @param shopId      shop id
     * @param description description
     * @param isGood      is good?
     * @return boolean
     * @see cn.enbug.shop.common.service.OrderService#commentGood(String, String, int, String, boolean)
     */
    public boolean comment(User user, int goodId, int shopId, String description, boolean isGood) {
        Comment comment = new Comment();
        comment.setGoodId(goodId);
        comment.setBuyerId(user.getId());
        comment.setIsGood(isGood ? 1 : 0);
        comment.setDescription(description);
        comment.setShopId(shopId);
        return comment.save();
    }

}
