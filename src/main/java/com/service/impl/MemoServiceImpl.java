package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.MemoMapper;
import com.pojo.Memo;
import com.pojo.User;
import com.service.MemoService;
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
public class MemoServiceImpl extends ServiceImpl<MemoMapper, Memo>
        implements MemoService {

    @Autowired
    @Qualifier(value = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MemoMapper memoMapper;

    @Autowired
    private UserService userService;

    /**
     * 查询所有的备忘录
     *
     * @param username 用户名
     * @return {@link List}<{@link Memo}>
     */
    @Override
    public List<Memo> queryAllMemos (String username) {
        Object o = redisTemplate.opsForValue().get(username + "Memo");

        if (o == null) {
            //查询mysql
            List<Memo> addressBooks = memoMapper.selectList(
                    new QueryWrapper<Memo>()
                            .lambda()
                            .eq(Memo::getUserId, userService.findUserByUserName(username).getId()));

            redisTemplate.opsForValue().set(username + "Memo", addressBooks);
            return addressBooks;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(o), List.class);
    }

    /**
     * 添加添加备忘录
     *
     * @param memo 备忘录
     * @return {@link ResponseUtils}
     */
    @Override
    public ResponseUtils addAddMemo (Memo memo) {
        User user = userService.getById(memo.getUserId());
        int count = this.count(new QueryWrapper<Memo>()
                .lambda()
                .eq(Memo::getUserId, user.getId()));

        if (count >= user.getSize()) {
            return ResponseUtils.fail("备忘录容量不够,请去扩容，或者删除一些数据，再添加");
        } else {
            if (this.save(memo)) {
                updateRedis(user);
                return ResponseUtils.success("添加成功");
            }
            return ResponseUtils.fail("添加失败，请联系管理员");
        }
    }

    /**
     * 更新的备忘录
     *
     * @param memo 备忘录
     * @return boolean
     */
    @Override
    public boolean updateMemo (Memo memo) {
        User user = userService.getById(memo.getUserId());
        boolean flag = this.updateById(memo);
        if (flag) {
            updateRedis(user);
        }
        return flag;
    }

    /**
     * 删除备忘录
     *
     * @param memo 备忘录
     * @return boolean
     */
    @Override
    public boolean deleteMemo (Memo memo) {
        User user = userService.getById(memo.getUserId());
        boolean flag = this.removeById(memo.getId());
        if (flag) {
            updateRedis(user);
        }
        return flag;
    }


    /**
     * 更新复述,
     *
     * @param user 用户
     */
    private void updateRedis (User user) {
        redisTemplate.opsForValue()
                .getOperations()
                .delete(user.getUsername() + "Memo");

        //查询mysql
        List<Memo> memos = memoMapper.selectList(
                new QueryWrapper<Memo>()
                        .lambda()
                        .eq(Memo::getUserId, user.getId()));

        redisTemplate.opsForValue().set(user.getUsername() + "Memo", memos);
    }
}




