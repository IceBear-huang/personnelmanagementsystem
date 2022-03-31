package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.ExpendMapper;
import com.pojo.Expend;
import com.pojo.Income;
import com.pojo.User;
import com.service.IncomeService;
import com.mapper.IncomeMapper;
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
public class IncomeServiceImpl extends ServiceImpl<IncomeMapper, Income>
implements IncomeService{

    @Autowired
    @Qualifier(value = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IncomeMapper incomeMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<Income> queryAllIncome (String username) {
        Object o = redisTemplate.opsForValue().get(username + "Income");

        if (o == null) {
            //查询mysql
            List<Income> expendList = incomeMapper.selectList(
                    new QueryWrapper<Income>()
                            .lambda()
                            .eq(Income::getUserId, userService.findUserByUserName(username).getId())
                            .orderByAsc(Income::getTime));

            redisTemplate.opsForValue().set(username + "Income", expendList);
            return expendList;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(o), List.class);
    }

    @Override
    public ResponseUtils addIncome (Income income) {
        User user = userService.getById(income.getUserId());
        int count = this.count(new QueryWrapper<Income>()
                .lambda()
                .eq(Income::getUserId, user.getId()));

        if (count >= user.getSize()) {
            return ResponseUtils.fail("收入记录容量不够,请去扩容，或者删除一些数据，再添加");
        } else {
            if (this.save(income)) {
                updateRedis(user);
                return ResponseUtils.success("添加成功");
            }
            return ResponseUtils.fail("添加失败，请联系管理员");
        }
    }

    @Override
    public boolean updateIncome (Income income) {
        User user = userService.getById(income.getUserId());
        boolean flag = this.updateById(income);
        if (flag) {
            updateRedis(user);
        }
        return flag;
    }

    @Override
    public boolean deleteIncome (Income income) {
        User user = userService.getById(income.getUserId());
        boolean flag = this.removeById(income.getId());
        if (flag) {
            updateRedis(user);
        }
        return flag;
    }

    private void updateRedis (User user) {
        redisTemplate.opsForValue()
                .getOperations()
                .delete(user.getUsername() + "Income");

        //查询mysql
        List<Income> incomeList = incomeMapper.selectList(
                new QueryWrapper<Income>()
                        .lambda()
                        .eq(Income::getUserId, user.getId())
                        .orderByAsc(Income::getTime));

        redisTemplate.opsForValue().set(user.getUsername() + "Income", incomeList);
    }
}




