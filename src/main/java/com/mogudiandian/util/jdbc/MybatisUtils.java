package com.mogudiandian.util.jdbc;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collections;
import java.util.List;

/**
 * Mybatis的工具类
 *
 * @author Joshua Sun
 * @since 1.0.24
 */
public final class MybatisUtils {

    private MybatisUtils() {
    }

    /**
     * 批量更新
     *
     * @param sqlSessionFactory mybatis 的 SqlSessionFactory ，通常从 Spring 中获取
     * @param batchUpdateMethod 批量更新方法
     * @param batchSize         每次更新批量大小
     * @param list              数据集合
     * @param throwExceptionIfEffectedRowsLessThanBatchSize 如果更新行数少于参数列表数量是否抛出异常
     * @return 更新的总行数
     */
    public static <T> int batchUpdate(SqlSessionFactory sqlSessionFactory, String batchUpdateMethod, int batchSize, List<T> list, boolean throwExceptionIfEffectedRowsLessThanBatchSize) {
        if (sqlSessionFactory == null) {
            throw new IllegalArgumentException("sqlSessionFactory must not be null");
        }
        if (StringUtils.isBlank(batchUpdateMethod)) {
            throw new IllegalArgumentException("batchUpdateMethod must not be blank");
        }
        if (batchSize <= 0) {
            throw new IllegalArgumentException("batchSize must be greater than 0");
        }
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        List<List<T>> parts = batchSize >= list.size() ? Collections.singletonList(list) : Lists.partition(list, batchSize);

        int totalRows = 0;

        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            for (List<T> part : parts) {
                int rows = sqlSession.update(batchUpdateMethod, part);
                if (rows < part.size()) {
                    if (throwExceptionIfEffectedRowsLessThanBatchSize) {
                        throw new RuntimeException("batch update failed, expect " + part.size() + " rows, but only " + rows + " rows updated");
                    }
                }
                totalRows += rows;
            }
            sqlSession.commit();
        } catch (Throwable e) {
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }

        return totalRows;
    }

}
