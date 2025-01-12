package cn.edu.nwafu.nexus.common.exception;

import cn.edu.nwafu.nexus.common.api.IErrorCode;
import cn.edu.nwafu.nexus.common.util.i18n.MessageUtils;
import cn.hutool.core.util.StrUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * 统一异常类
 *
 * @author Huang Z.Y.
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class ApiException extends RuntimeException {
    @Getter
    protected IErrorCode errorCode;

    @Setter
    protected String message;

    @Getter
    @Setter
    protected String i18nMessage;

    /**
     * 存储特殊数据
     */
    protected HashMap<String, Object> payload;

    public ApiException(IErrorCode errorCode) {
        setErrorCode(errorCode);
    }

    public ApiException(IErrorCode errorCode, Object... args) {
        setErrorCode(errorCode, args);
    }

    /**
     * 注意  如果是try catch的情况下捕获异常 并转为ApiException的话  一定要填入Throwable e
     *
     * @param e         捕获到的原始异常
     * @param errorCode 错误码
     * @param args      错误详细信息参数
     */
    public ApiException(Throwable e, IErrorCode errorCode, Object... args) {
        super(e);
        setErrorCode(errorCode, args);
    }

    private void setErrorCode(IErrorCode errorCode, Object... args) {
        this.errorCode = errorCode;
        this.message = StrUtil.format(errorCode.message(), args);

        try {
            this.i18nMessage = MessageUtils.message(errorCode.i18nKey(), args);
        } catch (Exception e) {
            log.error("could not found i18nMessage entry for key: " + errorCode.i18nKey());
        }
    }

    @Override
    public String getMessage() {
        return i18nMessage != null ? i18nMessage : message;
    }

    @Override
    public String getLocalizedMessage() {
        return i18nMessage != null ? i18nMessage : message;
    }
}
