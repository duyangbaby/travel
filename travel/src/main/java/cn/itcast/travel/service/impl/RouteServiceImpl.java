package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.ArrayList;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao dao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        int totalCount = dao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        int start = (currentPage - 1) * pageSize;
        List<Route> list = dao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }

    //查询一个线路的详细信息
    @Override
    public Route findOne(String rid) {
        Route route = dao.findOne(Integer.parseInt(rid));
        //根据rote的rid查询图片集合
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        route.setRouteImgList(routeImgList);
        //根据route的sid查询商家信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }

    //分页查询收藏的线路
    @Override
    public PageBean<Route> myFavorite(int uid,int currentPage, int pageSize) {
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        List<Integer> rids = favoriteDao.myFavoriteRid(uid);
        int totalCount = rids.size();
        pb.setTotalCount(totalCount);
        int start = (currentPage - 1) * pageSize;
        List<Route> list = new ArrayList<Route>();
        for (int i = 0; i < rids.size(); i++) {
            Route route = favoriteDao.favoriteRoute(rids.get(i));
            list.add(route);
        }
        pb.setList(list);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }



}
