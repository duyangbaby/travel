package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    //根据rid查询线路的图片集合
    List<RouteImg> findByRid(int rid);
}
