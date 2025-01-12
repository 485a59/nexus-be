package cn.edu.nwafu.nexus.common.excel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 错误信息
 *
 * @author Huang Z.Y.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    /**
     * 行号
     */
    private Long lineNum;

    /**
     * 错误信息
     */
    private Set<String> errors = new HashSet<>();

    public ErrorMessage(Set<String> errors) {
        this.errors = errors;
    }

    public ErrorMessage(String error) {
        Set<String> objects = new HashSet<>();
        objects.add(error);
        this.errors = objects;
    }
}
