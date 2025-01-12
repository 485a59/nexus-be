package cn.edu.nwafu.nexus.infrastructure.config.i18n;

import cn.edu.nwafu.nexus.common.api.IErrorCode;
import cn.edu.nwafu.nexus.common.api.ResultCode;
import cn.edu.nwafu.nexus.common.util.i18n.MessageUtils;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 检测未添加到 i18n 文件(messages.properties) 中的 message。
 *
 * @author Huang Z.Y.
 */
@Component
@Slf4j
public class MessageI18nCheckerRunner implements ApplicationRunner {

    public static Object[] allErrorCodes = ArrayUtil.addAll(
            ResultCode.Http.values(),
            ResultCode.Internal.values(),
            ResultCode.External.values(),
            ResultCode.Client.values(),
            ResultCode.Business.values());
    @Value("nexus.checkI18nKey")
    private String checkI18nKey;

    @Override
    public void run(ApplicationArguments args) {
        if (Convert.toBool(checkI18nKey)) {
            checkEveryMessage();
        }
    }

    /**
     * 如果想支持i18n, 请把对应的错误码描述填到 /resources/i18n/messages.properties 文件中
     */
    public void checkEveryMessage() {
        for (Object errorCode : allErrorCodes) {
            IErrorCode errorInterface = (IErrorCode) errorCode;
            try {
                MessageUtils.message(errorInterface.i18nKey());
            } catch (Exception e) {
                log.warn("could not find i18n message for:{}  in the file /resources/i18n/messages.properties.",
                        errorInterface.i18nKey());
            }
        }
    }
}
