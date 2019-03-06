package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname);

    Route findOne(String rid);
    //分页查询收藏信息
    PageBean<Route> myFavorite(int uid, int currentPage, int pageSize);
}
