package com.controller;

import com.pojo.AddressBook;
import com.pojo.Memo;
import com.service.MemoService;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: MemoController
 * Description:
 * date: 2021/11/30 22:05
 *
 * @author WhiteBear
 */
@RestController
@RequestMapping("/Memo")
public class MemoController {

    @Autowired
    private MemoService memoService;

    @GetMapping("/queryAll/{username}")
    public List<Memo> queryAllMemos(@PathVariable String username) {
        return memoService.queryAllMemos(username);
    }

    @PostMapping("/add")
    public ResponseUtils addMeos(@RequestBody Memo memo) {
        return memoService.addAddMemo(memo);
    }

    @PutMapping("/update")
    public boolean updateMeos(@RequestBody Memo memo) {
        return memoService.updateMemo(memo);
    }

    @DeleteMapping("/delete")
    public boolean deleteMeos(@RequestBody Memo memo) {
        return memoService.deleteMemo(memo);
    }
}
