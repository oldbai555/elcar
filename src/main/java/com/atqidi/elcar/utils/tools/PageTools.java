package com.atqidi.elcar.utils.tools;

import com.atqidi.elcar.utils.Constants;

/**
 * 分页判断
 *
 * @author 老白
 */
public class PageTools {


    public static Integer getPage(Integer page) {
        return page < Constants.Page.DEFAULT_PAGE ? Constants.Page.DEFAULT_PAGE : page;
    }

    public static Integer getSize(Integer size) {
        return size < Constants.Page.MIN_SIZE ? Constants.Page.MIN_SIZE : size;
    }
}
