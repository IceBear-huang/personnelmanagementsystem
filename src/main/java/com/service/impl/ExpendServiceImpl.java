package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.ExpendMapper;
import com.pojo.Expend;
import com.pojo.User;
import com.service.ExpendService;
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
public class ExpendServiceImpl extends ServiceImpl<ExpendMapper, Expend>
        implements ExpendService {

    @Autowired
    @Qualifier(value = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ExpendMapper expendMapper;

    @Autowired
    private UserService userService;


    /**
     * 查询所有的花费
     *
     * @param username 用户名
     * @return {@link List}<{@link Expend}>
     */
    @Override
    public List<Expend> queryAllExpend (String username) {
        Object o = redisTemplate.opsForValue().get(username + "Expend");

        if (o == null) {
            //查询mysql
            List<Expend> expendList = expendMapper.selectList(
                    new QueryWrapper<Expend>()
                            .lambda()
                            .eq(Expend::getUserId, userService.findUserByUserName(username).getId()));

            redisTemplate.opsForValue().set(username + "Expend", expendList);
            return expendList;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(o), List.class);
    }

    /**
     * 增加花费
     *
     * @param expend 消耗
     * @return {@link ResponseUtils}
     */
    @Override
    public ResponseUtils addExpend (Expend expend) {
        User user = userService.getById(expend.getUserId());
        int count = this.count(new QueryWrapper<Expend>()
                .lambda()
                .eq(Expend::getUserId, user.getId()));

        if (count >= user.getSize()) {
            return ResponseUtils.fail("支出记录容量不够,请去扩容，或者删除一些数据，再添加");
        } else {
            if (this.save(expend)) {
                updateRedis(user);
                return ResponseUtils.success("添加成功");
            }
            return ResponseUtils.fail("添加失败，请联系管理员");
        }
    }

    /**
     * 更新消耗
     *
     * @param expend 消耗
     * @return boolean
     */
    @Override
    public boolean updateExpend (Expend expend) {
        User user = userService.getById(expend.getUserId());
        boolean flag = this.updateById(expend);
        if (flag) {
            updateRedis(user);
        }
        return flag;
    }

    /**
     * 删除消耗
     *
     * @param expend 消耗
     * @return boolean
     */
    @Override
    public boolean deleteExpend (Expend expend) {
        User user = userService.getById(expend.getUserId());
        boolean flag = this.removeById(expend.getId());
        if (flag) {
            updateRedis(user);
        }
        return flag;
    }

    /**
     * 更新redis
     *
     * @param user 用户
     */
    private void updateRedis (User user) {
        redisTemplate.opsForValue()
                .getOperations()
                .delete(user.getUsername() + "Expend");

        //查询mysql
        List<Expend> expendList = expendMapper.selectList(
                new QueryWrapper<Expend>()
                        .lambda()
                        .eq(Expend::getUserId, user.getId()));

        redisTemplate.opsForValue().set(user.getUsername() + "Expend", expendList);
    }

}




