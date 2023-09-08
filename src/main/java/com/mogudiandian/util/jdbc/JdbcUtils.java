package com.mogudiandian.util.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.base.CaseFormat;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JDBC的工具类
 * 可以作为一个简易版的ORM使用
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class JdbcUtils {

    private JdbcUtils() {
        super();
    }

    /**
     * 内部查询方法
     * @see JdbcUtils#selectList(Connection, String, Object...)
     * @param dataSource 数据源
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return JSON对象集合
     */
    @SneakyThrows
    private static List<JSONObject> select0(DataSource dataSource, String preparedSql, Object... params) {
        try (Connection connection = dataSource.getConnection()) {
            return selectList(connection, preparedSql, params);
        }
    }

    /**
     * 数据源查询
     * @see JdbcUtils#select0(Connection, String, Object...)
     * @param dataSource 数据源
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return JSON对象集合
     * @deprecated 使用selectList或selectOne更好
     */
    @Deprecated
    public static List<JSONObject> select(DataSource dataSource, String preparedSql, Object... params) {
        return select0(dataSource, preparedSql, params);
    }

    /**
     * 数据源查询
     * @see JdbcUtils#select0(Connection, String, Object...)
     * @param dataSource 数据源
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return JSON对象集合
     */
    public static List<JSONObject> selectList(DataSource dataSource, String preparedSql, Object... params) {
        return select0(dataSource, preparedSql, params);
    }

    /**
     * 数据源查询
     * @see JdbcUtils#selectList(DataSource, String, Object...)
     * @param clazz clazz 返回对象类型
     * @param dataSource 数据源
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return Java对象集合
     */
    public static <T> List<T> selectList(Class<T> clazz, DataSource dataSource, String preparedSql, Object... params) {
        List<JSONObject> jsonObjects = selectList(dataSource, preparedSql, params);
        return jsonObjects.stream()
                          .peek(JdbcUtils::updateKeysToCamelCase)
                          .map(x -> TypeUtils.cast(x, clazz, null))
                          .collect(Collectors.toList());
    }

    /**
     * 数据源查询
     * @see JdbcUtils#selectList(DataSource, String, Object...)
     * @param dataSource 数据源
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return JSON对象
     */
    public static JSONObject selectOne(DataSource dataSource, String preparedSql, Object... params) {
        return selectList(dataSource, preparedSql, params).stream().findFirst().orElse(null);
    }

    /**
     * 数据源查询
     * @see JdbcUtils#selectList(Class, DataSource, String, Object...)
     * @param clazz clazz 返回对象类型
     * @param dataSource 数据源
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return Java对象
     */
    public static <T> T selectOne(Class<T> clazz, DataSource dataSource, String preparedSql, Object... params) {
        return selectList(clazz, dataSource, preparedSql, params).stream().findFirst().orElse(null);
    }

    /**
     * 内部查询方法
     * @param connection 连接
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return JSON对象集合
     */
    @SneakyThrows
    private static List<JSONObject> select0(Connection connection, String preparedSql, Object... params) {
        // 返回结果
        List<JSONObject> result = new ArrayList<>();

        // JDBC返回的数据
        ResultSet resultSet = null;

        // 进行数据查询
        try (PreparedStatement preparedStatement = connection.prepareStatement(preparedSql)) {
            // 填充?参数
            if (params != null && params.length > 0) {
                for (int i = 0, len = params.length; i < len; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            // 执行查询
            resultSet = preparedStatement.executeQuery();

            // 查询结果的元数据 包括返回了哪些字段
            ResultSetMetaData metaData = resultSet.getMetaData();

            // 缓存各个字段名 这样下面遍历时就不需要每一行都获取一次了
            // 这个地方之所以没有用Stream API 是因为jdbc所有操作都throws SQLException
            int columnCount = metaData.getColumnCount();
            String[] columnLabels = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnLabels[i] = metaData.getColumnLabel(i + 1);
            }

            // 遍历查询结果的每一行
            while (resultSet.next()) {
                JSONObject row = new JSONObject();
                // 根据字段名从查询结果的每一行获取该字段的指
                for (String columnLabel : columnLabels) {
                    Object columnValue = resultSet.getObject(columnLabel);
                    row.put(columnLabel, columnValue);
                }
                result.add(row);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return result;
    }

    /**
     * 内部查询方法
     * @see JdbcUtils#select0(Connection, String, Object...)
     * @param connection 连接
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return JSON对象集合
     */
    @Deprecated
    public static List<JSONObject> select(Connection connection, String preparedSql, Object... params) {
        return select0(connection, preparedSql, params);
    }

    /**
     * 连接查询
     * @see JdbcUtils#select0(Connection, String, Object...)
     * @param connection 连接
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return JSON对象集合
     */
    public static List<JSONObject> selectList(Connection connection, String preparedSql, Object... params) {
        return select0(connection, preparedSql, params);
    }

    /**
     * 连接查询
     * @see JdbcUtils#selectList(Connection, String, Object...)
     * @param connection 连接
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return Java对象集合
     */
    public static <T> List<T> selectList(Class<T> clazz, Connection connection, String preparedSql, Object... params) {
        List<JSONObject> jsonObjects = selectList(connection, preparedSql, params);
        return jsonObjects.stream()
                          .peek(JdbcUtils::updateKeysToCamelCase)
                          .map(x -> TypeUtils.cast(x, clazz, null))
                          .collect(Collectors.toList());
    }

    /**
     * 连接查询
     * @see JdbcUtils#selectList(Connection, String, Object...)
     * @param connection 连接
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return JSON对象
     */
    public static JSONObject selectOne(Connection connection, String preparedSql, Object... params) {
        return selectList(connection, preparedSql, params).stream().findFirst().orElse(null);
    }

    /**
     * 连接查询
     * @see JdbcUtils#selectList(Class, Connection, String, Object...) 
     * @param connection 连接
     * @param preparedSql 预处理SQL
     * @param params 参数
     * @return Java对象
     */
    public static <T> T selectOne(Class<T> clazz, Connection connection, String preparedSql, Object... params) {
        return selectList(clazz, connection, preparedSql, params).stream().findFirst().orElse(null);
    }

    /**
     * 将map中的key由匈牙利转换成小驼峰（column_name -> propertyName）
     * @param map column_name -> column_value
     */
    private static void updateKeysToCamelCase(Map<String, Object> map) {
        List<String> columns = new ArrayList<>(map.keySet());
        for (String column : columns) {
            String lowerCase = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
            String property = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, lowerCase);
            map.put(property, map.remove(column));
        }
    }

}
