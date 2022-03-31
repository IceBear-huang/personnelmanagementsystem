package com.service;

import com.pojo.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;
import com.util.ResponseUtils;

import java.util.List;

/**
 *
 */
public interface AddressBookService extends IService<AddressBook> {
    List<AddressBook> queryAllAddressBookById (String username);

    ResponseUtils addAddressBook (AddressBook addressBook);

    boolean updateAddressBook (AddressBook addressBook);

    boolean deleteAddressBook(AddressBook addressBook);
}
