package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.CharactersMapper;
import com.mapper.LogMapper;
import com.pojo.Characters;
import com.pojo.Log;
import com.pojo.User;
import com.service.CharactersService;
import com.service.LogService;
import com.service.UserService;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
        implements LogService {

    @Autowired
    @Qualifier(value = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CharactersMapper charactersMapper;

    @Autowired
    private CharactersService charactersService;

    @Override
    public List<Log> queryAllLogs (String username) {
        Object o = redisTemplate.opsForValue().get(username + "Log");

        if (o == null) {
            //查询mysql
            List<Log> logs = logMapper.selectList(
                    new QueryWrapper<Log>()
                            .lambda()
                            .eq(Log::getUserId, userService.findUserByUserName(username).getId())
                            .orderByAsc(Log::getTime));
            logs.forEach(log -> {
                List<Characters> charactersList = charactersMapper.selectList(
                        new QueryWrapper<Characters>()
                                .lambda()
                                .eq(Characters::getLogId, log.getId()));

                log.setCharactersList(charactersList);
            });

            redisTemplate.opsForValue().set(username + "Log", logs);
            return logs;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(o), List.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseUtils addLog (Log log) {
        User user = userService.getById(log.getUserId());
        int count = this.count(new QueryWrapper<Log>()
                .lambda()
                .eq(Log::getUserId, user.getId()));

        if (count >= user.getSize()) {
            return ResponseUtils.fail("日志容量不够,请去扩容，或者删除一些数据，再添加");
        } else {
            if (this.save(log) ) {
                Log queryLog = logMapper.selectOne(
                        new QueryWrapper<Log>()
                                .lambda()
                                .eq(Log::getTime,log.getTime()));

                List<Characters> charactersList = log.getCharactersList();
                charactersList.forEach(characters -> {
                    characters.setLogId(queryLog.getId());
                });

                charactersService.saveBatch(log.getCharactersList());
                updateRedis(user);
                return ResponseUtils.success("添加成功");
            }
            return ResponseUtils.fail("添加失败，请联系管理员");
        }
    }

    /**
     * 更新日志
     *
     * @param log 日志
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateLog (Log log) {
        User user = userService.getById(log.getUserId());
        boolean flag = false;
        if (this.updateById(log)
                && charactersService.updateBatchById(log.getCharactersList())) {
            flag = true;
        }

        if (flag) {
            updateRedis(user);
        }
        return flag;
    }


    /**
     * 删除日志
     *
     * @param log 日志
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteLog (Log log) {
        User user = userService.getById(log.getUserId());
        boolean flag = false;
        List<Characters> charactersList = log.getCharactersList();
        List<Integer> idList = new ArrayList<>();
        charactersList.forEach(characters -> {
            idList.add(characters.getId());
        });

        if (this.removeById(log.getId())
                && charactersService.removeByIds(idList)) {
            flag = true;
        }

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
                .delete(user.getUsername() + "Log");

        List<Log> logs = logMapper.selectList(
                new QueryWrapper<Log>()
                        .lambda()
                        .eq(Log::getUserId, user.getId())
                        .orderByAsc(Log::getTime));

        logs.forEach(log1 -> {
            List<Characters> charactersList = charactersMapper.selectList(
                    new QueryWrapper<Characters>()
                            .lambda()
                            .eq(Characters::getLogId, log1.getId()));

            log1.setCharactersList(charactersList);
        });
        redisTemplate.opsForValue().set(user.getUsername() + "Log", logs);
    }
}




