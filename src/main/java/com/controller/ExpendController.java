package com.controller;

import com.pojo.Expend;
import com.service.ExpendService;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: FinanceController
 * Description:
 * date: 2021/12/1 15:14
 *
 * @author WhiteBear
 */
@RestController
@RequestMapping("/Expend")
public class ExpendController {

    @Autowired
    private ExpendService expendService;

    @GetMapping("/queryAll/{username}")
    public List<Expend> queryAllExpend (@PathVariable String username) {
        return expendService.queryAllExpend(username);
    }

    @PostMapping("/add")
    public ResponseUtils addExpend (@RequestBody Expend expend) {
        return expendService.addExpend(expend);
    }

    @PutMapping("/update")
    public boolean updateExpend (@RequestBody Expend expend) {
        return expendService.updateExpend(expend);
    }

    @DeleteMapping("/delete")
    public boolean deleteExpend (@RequestBody Expend expend) {
        return expendService.deleteExpend(expend);
    }
}
