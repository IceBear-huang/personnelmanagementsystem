package com.controller;

import com.pojo.Expend;
import com.pojo.Income;
import com.service.IncomeService;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: IncomeController
 * Description:
 * date: 2021/12/1 17:17
 *
 * @author WhiteBear
 */
@RestController
@RequestMapping("/Income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @GetMapping("/queryAll/{username}")
    public List<Income> queryAllIncome (@PathVariable String username) {
        return incomeService.queryAllIncome(username);
    }

    @PostMapping("/add")
    public ResponseUtils addIncome (@RequestBody Income income) {
        return incomeService.addIncome(income);
    }

    @PutMapping("/update")
    public boolean updateIncome (@RequestBody Income income) {
        return incomeService.updateIncome(income);
    }

    @DeleteMapping("/delete")
    public boolean deleteIncome (@RequestBody Income income) {
        return incomeService.deleteIncome(income);
    }
}
