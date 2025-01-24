package cn.edu.nwafu.nexus.infrastructure.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Huang Z.Y.
 */
@MappedTypes(Set.class)
public class SetTypeHandler extends BaseTypeHandler<Set<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<String> parameter, JdbcType jdbcType) throws SQLException {
        // 将 Set<String> 转换为逗号分隔的字符串
        String value = String.join(",", parameter);
        ps.setString(i, value);
    }

    @Override
    public Set<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 将数据库中的字符串转换为 Set<String>
        String value = rs.getString(columnName);
        return value == null ? new HashSet<>() : new HashSet<>(Arrays.asList(value.split(",")));
    }

    @Override
    public Set<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? new HashSet<>() : new HashSet<>(Arrays.asList(value.split(",")));
    }

    @Override
    public Set<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? new HashSet<>() : new HashSet<>(Arrays.asList(value.split(",")));
    }
}
