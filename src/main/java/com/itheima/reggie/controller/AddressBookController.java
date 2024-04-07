package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;


    @PostMapping
    public R<String> addAddressBook(@RequestBody AddressBook addressBook) {
        Long userId = BaseContext.getId();
        addressBook.setUserId(userId);
        addressBook.setIsDefault(0);
        addressBookService.save(addressBook);
        return R.success("添加成功");
    }

    @PutMapping
    public R<String> updateAddressBook(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("更新成功");
    }

    @DeleteMapping
    public R<String> deleteAddressBook(Long ids) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId,ids);
        addressBookService.remove(queryWrapper);
        return R.success("删除成功");
    }

    @GetMapping("/{id}")
    public R<AddressBook> getAddressBookById(@PathVariable Long id) {
        System.out.println(id);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId,id);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook!=null) {
            return R.success(addressBook);
        }
        return R.error("没有找到该对象");
    }

    @PutMapping("/default")
    public R<String> changeStatus(@RequestBody AddressBook addressBook) {
        Long addressBookId = addressBook.getId();
        Long userId = BaseContext.getId();
        // 先将 UserId 下的全部设置为 0
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId,userId);
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(updateWrapper);

        updateWrapper.eq(AddressBook::getId,addressBookId);
        updateWrapper.set(AddressBook::getIsDefault,1);
        addressBookService.update(updateWrapper);
        return R.success("设置成功");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddressBook() {
        Long userId = BaseContext.getId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId);
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> listAddressBook() {
        Long userId = BaseContext.getId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != userId, AddressBook::getUserId,userId);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);
    }
}
