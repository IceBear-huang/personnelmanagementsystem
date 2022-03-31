package com.controller;

import com.pojo.Log;
import com.pojo.Memo;
import com.service.LogService;
import com.service.MemoService;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: LogController
 * Description:
 * date: 2021/11/30 22:40
 *
 * @author WhiteBear
 */
@RestController
@RequestMapping("/Log")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/queryAll/{username}")
    public List<Log> queryAllLogs(@PathVariable String username) {
        return logService.queryAllLogs(username);
    }

    @PostMapping("/add")
    public ResponseUtils addLog(@RequestBody Log log) {
        return logService.addLog(log);
    }

    @PutMapping("/update")
    public boolean updateLog(@RequestBody Log log) {
        return logService.updateLog(log);
    }

    @DeleteMapping("/delete")
    public boolean deleteLog(@RequestBody Log log) {
        return logService.deleteLog(log);
    }
}
