package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;

import java.util.List;


public interface FavoriteDao {
    Favorite findByRidAndUid(int rid, int uid);

    int findCountByRid(int rid);

    void add(int rid, int uid);

    List<Integer> myFavoriteRid(int uid);

    Route favoriteRoute(int rid);
    //查询人气线路
    List<Route> findHot();
}
