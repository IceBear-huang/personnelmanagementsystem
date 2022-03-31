package com.service;

import com.pojo.Expend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.util.ResponseUtils;

import java.util.List;

/**
 *
 */
public interface ExpendService extends IService<Expend> {

    List<Expend> queryAllExpend (String username);

    ResponseUtils addExpend (Expend expend);

    boolean updateExpend (Expend expend);

    boolean deleteExpend (Expend expend);

}
