package cn.oreo.common.core.validator;

import cn.oreo.common.core.annotation.IsMobile;
import cn.oreo.common.core.entity.constant.RegexpConstant;
import cn.oreo.common.core.utils.OreoUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
public class MobileValidator implements ConstraintValidator<IsMobile, String> {

    @Override
    public void initialize(IsMobile isMobile) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (StringUtils.isBlank(s)) {
                return true;
            } else {
                String regex = RegexpConstant.MOBILE;
                return OreoUtil.match(regex, s);
            }
        } catch (Exception e) {
            return false;
        }
    }
}
