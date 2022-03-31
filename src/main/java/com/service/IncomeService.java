package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pojo.Expend;
import com.pojo.Income;
import com.util.ResponseUtils;

import java.util.List;

/**
 *
 */
public interface IncomeService extends IService<Income> {
    List<Income> queryAllIncome (String username);

    ResponseUtils addIncome (Income income);

    boolean updateIncome (Income income);

    boolean deleteIncome (Income income);
}
