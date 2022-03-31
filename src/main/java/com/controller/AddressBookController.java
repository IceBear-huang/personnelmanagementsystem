package com.controller;

import com.pojo.AddressBook;
import com.service.AddressBookService;
import com.util.RedisUtils;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: AddressBookController
 * Description:
 * date: 2021/11/29 22:07
 *
 * @author WhiteBear
 */
@RestController
@RequestMapping("/AddressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/queryAll/{username}")
    public List<AddressBook> queryAllAddressBook(@PathVariable String username) {
        return addressBookService.queryAllAddressBookById(username);
    }

    @PostMapping("/add")
    public ResponseUtils addAddressBook(@RequestBody AddressBook addAddressBook) {
        return addressBookService.addAddressBook(addAddressBook);
    }

    @PutMapping("/update")
    public boolean updateAddressBook(@RequestBody AddressBook addressBook) {
        return addressBookService.updateAddressBook(addressBook);
    }

    @DeleteMapping("/delete")
    public boolean deleteAddressBook(@RequestBody AddressBook addressBook) {
        return addressBookService.deleteAddressBook(addressBook);
    }
}
