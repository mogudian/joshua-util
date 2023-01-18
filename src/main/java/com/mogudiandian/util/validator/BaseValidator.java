package com.mogudiandian.util.validator;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基础校验器
 * @author sunbo
 */
public final class BaseValidator {

    @Getter
    private static final Validator validator;

    static {
        validator = Validation.byProvider(HibernateValidator.class)
                              .configure()
                              .failFast(false)
                              .buildValidatorFactory()
                              .getValidator();
    }

    private BaseValidator() {
        super();
    }

    /**
     * 校验对象
     * @param obj 对象
     * @return 错误消息集合
     */
    public static List<String> validate(Object obj) {
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        return Optional.ofNullable(set)
                       .orElse(Collections.emptySet())
                       .stream()
                       .map(ConstraintViolation::getMessage)
                       .collect(Collectors.toList());
    }

    /**
     * 对象是否有效
     * @param obj 对象
     * @return 是否有效
     */
    public static boolean isValid(Object obj) {
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        return CollectionUtils.isEmpty(set);
    }

    /**
     * 对象是否无效
     * @param obj 对象
     * @return 是否无效
     */
    public static boolean isInvalid(Object obj) {
        return !isValid(obj);
    }

}
