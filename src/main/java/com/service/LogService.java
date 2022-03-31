package com.service;

import com.pojo.Log;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pojo.Memo;
import com.util.ResponseUtils;

import java.util.List;

/**
 *
 */
public interface LogService extends IService<Log> {
    List<Log> queryAllLogs(String username);

    ResponseUtils addLog (Log log);

    boolean updateLog (Log log);

    boolean deleteLog (Log log);
}
