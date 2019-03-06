package cn.itcast.travel.tset;


import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


public class Tset1 {
    public static void main(String[] args) {
       /* JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

            String sql = "SELECT * FROM tab_route ORDER BY COUNT DESC;";
        List<Route> list = template.query(sql, new BeanPropertyRowMapper<>(Route.class));
        for (int i = 0; i < 10; i++) {
            Route route = list.get(i);
            System.out.println(route);*/
        }

    }
