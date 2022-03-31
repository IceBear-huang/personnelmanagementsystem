package com.service;

import com.pojo.AddressBook;
import com.pojo.Memo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.util.ResponseUtils;

import java.util.List;

/**
 *
 */
public interface MemoService extends IService<Memo> {

    List<Memo> queryAllMemos(String username);

    ResponseUtils addAddMemo (Memo memo);

    boolean updateMemo (Memo memo);

    boolean deleteMemo (Memo memo);
}
