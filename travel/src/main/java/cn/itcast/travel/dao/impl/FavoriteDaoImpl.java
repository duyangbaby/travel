package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    //根据rid和uid查询线路收藏信息
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite =null;
        try {
            String sql = "select * from tab_favorite where rid = ? and uid =?";
            favorite = template.queryForObject(sql,new BeanPropertyRowMapper<>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {
        }
        return favorite;
    }
    //查询线路被收藏次数
    @Override
    public int findCountByRid(int rid) {
        String sql = "select count from tab_route where rid = ?";
        return template.queryForObject(sql,Integer.class,rid);
    }

    //添加收藏
    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?, ?, ?)";
        template.update(sql,rid,new Date(),uid);
        int i = template.queryForInt("select count from tab_route where rid = "+rid+"")+1;
        template.update("UPDATE tab_route SET COUNT ="+i+" where rid = "+rid+"");
    }

    //根据uid查询rid
    @Override
    public List<Integer> myFavoriteRid(int uid) {
        String sql = "select rid from tab_favorite where uid = ?";
        List<Integer> list = new ArrayList<>();
        List<Map<String, Object>> maps = template.queryForList(sql, uid);
        for (Map<String, Object> map : maps) {
            for (Object value : map.values()) {
                list.add((Integer) value);
            }
        }
        return list;
    }

    //根据rid查询Route对象
    @Override
    public Route favoriteRoute(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<>(Route.class),rid);
    }
    //查询人气线路
    public List<Route> findHot() {
        String sql = "SELECT * FROM tab_route ORDER BY COUNT DESC;";
        return template.query(sql, new BeanPropertyRowMapper<>(Route.class));
    }


}
