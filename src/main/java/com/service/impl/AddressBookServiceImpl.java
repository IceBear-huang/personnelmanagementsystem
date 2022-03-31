package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pojo.AddressBook;
import com.pojo.User;
import com.service.AddressBookService;
import com.mapper.AddressBookMapper;
import com.service.UserService;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
implements AddressBookService{

    @Autowired
    @Qualifier(value = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private UserService userService;

    /**
     * 查询所有的通讯录通过id
     *
     * @param username 用户id
     * @return {@link List}<{@link AddressBook}>
     */
    @Override
    public List<AddressBook> queryAllAddressBookById (String username) {
        Object o = redisTemplate.opsForValue().get(username + "AddressBooks");

        if (o == null) {
            //查询mysql
            List<AddressBook> addressBooks = addressBookMapper.selectList(
                    new QueryWrapper<AddressBook>()
                            .lambda()
                            .eq(AddressBook::getUserId, userService.findUserByUserName(username).getId()));

            redisTemplate.opsForValue().set(username +"AddressBooks",addressBooks);
            return addressBooks;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(o),List.class);
    }


    /**
     * 添加地址簿
     *
     * @param addressBook 地址本
     * @return boolean
     */
    @Override
    public ResponseUtils addAddressBook (AddressBook addressBook) {
        User user = userService.getById(addressBook.getUserId());
        int count = this.count(new QueryWrapper<AddressBook>()
                .lambda()
                .eq(AddressBook::getUserId, user.getId()));

        if (count >= user.getSize()) {
            return ResponseUtils.fail("通讯录容量不够,请去扩容，或者删除一些数据，再添加");
        } else {
            if (this.save(addressBook)) {
                redisTemplate.opsForValue()
                        .getOperations()
                        .delete(user.getUsername() + "AddressBooks");

                //查询mysql
                List<AddressBook> addressBooks = addressBookMapper.selectList(
                        new QueryWrapper<AddressBook>()
                                .lambda()
                                .eq(AddressBook::getUserId, addressBook.getUserId()));

                redisTemplate.opsForValue().set(user.getUsername() + "AddressBooks", addressBooks);
                return ResponseUtils.success("添加成功");
            }
            return ResponseUtils.fail("添加失败，请联系管理员");
        }
    }

    /**
     * 更新通讯录
     *
     * @param addressBook 地址本
     * @return boolean
     */
    @Override
    public boolean updateAddressBook (AddressBook addressBook) {
        User user = userService.getById(addressBook.getUserId());
        boolean flag = this.updateById(addressBook);
        return updateRedis(addressBook, user, flag);
    }

    /**
     * 删除地址簿
     *
     * @param addressBook
     * @return boolean
     */
    @Override
    public boolean deleteAddressBook (AddressBook addressBook) {
        User user = userService.getById(addressBook.getUserId());
        boolean flag = this.removeById(addressBook.getId());
        return updateRedis(addressBook, user, flag);
    }

    /**
     * 更新redis缓存,
     *
     * @param addressBook 地址本
     * @param user        用户
     * @param flag        国旗
     * @return boolean
     */
    private boolean updateRedis (AddressBook addressBook, User user, boolean flag) {
        if (flag) {
            redisTemplate.opsForValue()
                    .getOperations()
                    .delete(user.getUsername()+ "AddressBooks");

            //查询mysql
            List<AddressBook> addressBooks = addressBookMapper.selectList(
                    new QueryWrapper<AddressBook>()
                            .lambda()
                            .eq(AddressBook::getUserId, addressBook.getUserId()));

            redisTemplate.opsForValue().set(user.getUsername() + "AddressBooks", addressBooks);
        }
        return flag;
    }
}




