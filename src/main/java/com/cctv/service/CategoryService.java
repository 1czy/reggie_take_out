package com.cctv.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cctv.common.R;
import com.cctv.entity.Category;

public interface CategoryService extends IService<Category> {
    R<Page> selectpage(int page, int pageSize);

    void removeWithId(Long ids);
}
