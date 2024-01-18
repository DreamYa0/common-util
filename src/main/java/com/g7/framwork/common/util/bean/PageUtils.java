package com.g7.framwork.common.util.bean;

import com.g7.framework.common.dto.PagedResult;
import com.g7.framework.common.dto.Result;
import com.g7.framework.common.dto.pager.PagerData;
import com.github.pagehelper.Page;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dreamyao
 * @title
 * @date 2018/8/25 下午10:53
 * @since 1.0.0
 */
public class PageUtils {

    public static <T> Result<T> buildSuccess(T data) {
        return Result.<T>create().success(data);
    }

    /**
     * PagerData 对象转换为 PagedResult
     * @param pagerData PagerData
     * @param <T>       类型
     * @return PagedResult
     */
    public static <T> PagedResult<T> toPagedResult(PagerData<T> pagerData) {

        Assert.notNull(pagerData, "pageData is not null.");

        List<T> data = pagerData.getDataList();

        Assert.notNull(data, "page data is not null.");
        PagedResult.Builder<T> builder = new PagedResult.Builder<>();

        Integer total = pagerData.getTotal();
        Integer pageNum = pagerData.getPageNum();
        Integer pageSize = pagerData.getPageSize();
        if (total == 0 && pageNum == 0) {
            pageNum = 1;
        }

        builder.total(total).pageSize(pageSize).currentPage(pageNum).data(data);
        return builder.buildForSuccess();
    }

    /**
     * List集合转换为出参PagedResult对象
     * @param list  source
     * @param clazz 源对象类型
     * @param <E>   类型
     * @return 目标对象
     */
    public static <E> PagedResult<E> toPagedResult(List<?> list, Class<E> clazz, String... ignoreProperties) {

        Assert.notNull(list, "list param is not null.");
        Assert.notNull(clazz, "clazz param is not null.");

        int size = list.size();
        List<E> dest = EnhanceBeanUtils.convert(list, clazz, ignoreProperties);
        PagedResult.Builder<E> b = new PagedResult.Builder<>();
        if (Boolean.FALSE.equals(list instanceof Page)) {
            //不分页
            b.total(size).pageSize(size == 0 ? 10 : size).currentPage(1).data(dest);
            b.fetchAll(true);
        } else if (list instanceof Page) {
            Page<?> p = (Page<?>) list;
            int pageNum = p.getPageNum();
            int total = (int) p.getTotal();
            if (total == 0 && pageNum == 0) {
                pageNum = 1;
            }
            b.total(total).pageSize(p.getPageSize()).currentPage(pageNum).data(dest);
        }
        return b.buildForSuccess();
    }

    /**
     * mybatis PageHelper插件Page对象转换为出参PagedResult对象
     * @param p   Page对象
     * @param <E> 类型
     * @return 目标对象
     */
    public static <E> PagedResult<E> toPagedResult(Page<E> p) {

        Assert.notNull(p, "Page is not null.");

        List<E> dest = new ArrayList<>(p.size());
        dest.addAll(p);
        PagedResult.Builder<E> builder = new PagedResult.Builder<>();
        int pageNum = p.getPageNum();
        int total = (int) p.getTotal();
        if (total == 0 && pageNum == 0) {
            pageNum = 1;
        }
        builder.total(total).pageSize(p.getPageSize()).currentPage(pageNum).data(dest);
        return builder.buildForSuccess();
    }
}
