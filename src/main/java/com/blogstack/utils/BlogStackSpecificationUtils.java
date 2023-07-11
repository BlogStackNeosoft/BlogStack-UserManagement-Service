package com.blogstack.utils;

import com.blogstack.commons.BlogStackCommonConstants;
import com.blogstack.commons.BlogStackSpecificationConstants;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum BlogStackSpecificationUtils {
    INSTANCE;

    public String convertFilterCriteriaToEntityFilterCriteria(String filterCriteria, String prefix) {
        StringBuffer stringBuffer = new StringBuffer();
        Matcher matcher = Pattern.compile(BlogStackSpecificationConstants.INSTANCE.SPLIT_PATTERN_STRING + BlogStackSpecificationConstants.INSTANCE.PIPE_STRING + BlogStackSpecificationConstants.INSTANCE.SPLIT_PATTERN_NUMBER).matcher(filterCriteria);
        while(matcher.find()) {
            String field = matcher.group(BigInteger.ONE.intValue());
            stringBuffer.append(BlogStackCommonConstants.INSTANCE.BACKQUOTE_STRING);
            stringBuffer.append(prefix.concat(StringUtils.capitalize(field)));

            String operator = matcher.group(BigInteger.TWO.intValue());
            stringBuffer.append(BlogStackCommonConstants.INSTANCE.LESSTHAN_STRING);
            stringBuffer.append(operator);
            stringBuffer.append(BlogStackCommonConstants.INSTANCE.GREATERTHAN_STRING);

            String value = matcher.group(BlogStackCommonConstants.INSTANCE.THREE);
            stringBuffer.append(value);
        }
        return stringBuffer.toString();
    }

    public Sort convertSortCriteriaToEntitySortCriteria(String sortCriteria, String prefix) {
        String[] sortCriteriaArray = sortCriteria.split(BlogStackCommonConstants.INSTANCE.COLON_STRING);
        Sort sort = Sort.by(prefix.concat(StringUtils.capitalize(sortCriteriaArray[BigInteger.ZERO.intValue()])));
        return sortCriteriaArray.length > BigInteger.ONE.intValue() && sortCriteriaArray[BigInteger.ONE.intValue()].equalsIgnoreCase(Sort.Direction.DESC.name()) ? sort.descending() : sort.ascending();
    }

    public <T> Specification<T> buildSpecificaton(String filterCriteria, List<Expression<? extends Comparable<?>>> externalExpressionList){
        List<Expression<? extends Comparable<?>>> expressionList = new ArrayList<Expression<? extends Comparable<?>>>();
        if(externalExpressionList != null)
            expressionList.addAll(externalExpressionList);

        expressionList.addAll(parseQuery(filterCriteria));
        return new CustomSpecification<>(expressionList);
    }

    public static class CustomSpecification<T> implements Specification<T> {

        private List<Expression<? extends Comparable<?>>> expressions;

        public CustomSpecification(List<Expression<? extends Comparable<?>>> expressions) {
            super();
            this.expressions = expressions;
        }

        public List<Expression<? extends Comparable<?>>> getExpressions() {
            return expressions;
        }

        public void setExpressions(List<Expression<? extends Comparable<?>>> expressions) {
            this.expressions = expressions;
        }


        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            Predicate predicate = criteriaBuilder.conjunction();
            for(Expression<? extends Comparable<?>> expression: expressions) {
                if(expression != null && expression.getField() != null && expression.getOperator() != null && (expression.getValue() != null || expression.getValues() != null)) {
                    Predicate tempPredicate = handleExpression(root, criteriaBuilder, expression);
                    if(BlogStackSpecificationConstants.INSTANCE.JOIN_AND.equalsIgnoreCase(expression.getJoin()))
                        predicate = criteriaBuilder.and(predicate, tempPredicate);
                    else if(BlogStackSpecificationConstants.INSTANCE.JOIN_OR.equalsIgnoreCase(expression.getJoin()))
                        predicate = criteriaBuilder.or(predicate, tempPredicate);
                }
            }
            return predicate;
        }
    }

    private static <T,E extends Comparable<E>> Predicate handleExpression(Root<T> root, CriteriaBuilder criteriaBuilder, Expression<E> expression) {
        return switch(expression.getOperator()) {
            case BlogStackSpecificationConstants.OPERATOR_EQUAL -> criteriaBuilder.equal(criteriaBuilder.lower(root.get(expression.getField())), expression.getValue());
            case BlogStackSpecificationConstants.OPERATOR_NOTEQUAL -> criteriaBuilder.notEqual(criteriaBuilder.lower(root.get(expression.getField())), expression.getValue());
            case BlogStackSpecificationConstants.OPERATOR_CONTAIN -> criteriaBuilder.like(criteriaBuilder.lower(root.get(expression.getField())).as(String.class), BlogStackCommonConstants.INSTANCE.PERCENTAGE_STRING + expression.getValue() + BlogStackCommonConstants.INSTANCE.PERCENTAGE_STRING);
            case BlogStackSpecificationConstants.OPERATOR_START_WITH -> criteriaBuilder.like(criteriaBuilder.lower(root.get(expression.getField())).as(String.class), expression.getValue() + BlogStackCommonConstants.INSTANCE.PERCENTAGE_STRING);
            case BlogStackSpecificationConstants.OPERATOR_ENDS_WITH -> criteriaBuilder.like(criteriaBuilder.lower(root.get(expression.getField())).as(String.class), BlogStackCommonConstants.INSTANCE.PERCENTAGE_STRING + expression.getValue());
            case BlogStackSpecificationConstants.OPERATOR_GREATER_THAN -> criteriaBuilder.greaterThan(root.get(expression.getField()), expression.getValue());
            case BlogStackSpecificationConstants.OPERATOR_LESS_THAN -> criteriaBuilder.lessThan(root.get(expression.getField()), expression.getValue());
            case BlogStackSpecificationConstants.OPERATOR_GREATER_THAN_EQUAL -> criteriaBuilder.greaterThanOrEqualTo(root.get(expression.getField()), expression.getValue());
            case BlogStackSpecificationConstants.OPERATOR_LESS_THAN_EQUAL -> criteriaBuilder.lessThanOrEqualTo(root.get(expression.getField()), expression.getValue());
            case BlogStackSpecificationConstants.OPERATOR_IN -> criteriaBuilder.in(criteriaBuilder.lower(root.get(expression.getField())).in(expression.getValues()));
            default -> throw new IllegalArgumentException("Unexpected value :: " + expression.getOperator());
        };
    }

    private static List<Expression<? extends Comparable<?>>> parseQuery(String query) {
        List<Expression<? extends Comparable<?>>> expressionList = new ArrayList<>();
        query = (null == query || query.isEmpty()) ? BlogStackCommonConstants.INSTANCE.BLANK_STRING : query;
        Matcher matcher = Pattern.compile(BlogStackSpecificationConstants.INSTANCE.SPLIT_PATTERN_STRING + BlogStackCommonConstants.INSTANCE.PIPE_STRING + BlogStackSpecificationConstants.INSTANCE.SPLIT_PATTERN_NUMBER).matcher(query);
        while(matcher.find()) {
            String group = matcher.group();
            String field = matcher.group(BigInteger.ONE.intValue());
            String operator = matcher.group(BigInteger.TWO.intValue());
            String value = matcher.group(BlogStackCommonConstants.INSTANCE.THREE);
            String join = (operator.contains(BlogStackCommonConstants.INSTANCE.COLON_STRING)) ? operator.split(BlogStackCommonConstants.INSTANCE.COLON_STRING)[BigInteger.ONE.intValue()] : BlogStackSpecificationConstants.INSTANCE.JOIN_AND;
            operator = (operator.contains(BlogStackCommonConstants.INSTANCE.COLON_STRING)) ? operator.split(BlogStackCommonConstants.INSTANCE.COLON_STRING)[BigInteger.ZERO.intValue()] : operator;

            List<String> values = null;
            if(operator.equalsIgnoreCase(BlogStackSpecificationConstants.OPERATOR_IN))
                values = Arrays.stream(value.split(BlogStackCommonConstants.INSTANCE.DOUBLE_BACKSLASH_CARET_STRING)).toList();

            if(group.equals(BlogStackSpecificationConstants.INSTANCE.SPLIT_PATTERN_NUMBER))
                expressionList.add(new Expression<Double>(field, operator, join, Double.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Double.class::cast).collect(Collectors.toList()) : null));
            else {
                if(field.contains(BlogStackCommonConstants.INSTANCE.COLON_STRING)) {
                    String[] fieldArray = field.split(BlogStackCommonConstants.INSTANCE.COLON_STRING);
                    String fieldName = fieldArray[BigInteger.ZERO.intValue()];
                    String fieldType = fieldArray[BigInteger.ONE.intValue()];
                    try {
                        if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.BOOLEAN))
                            expressionList.add(new Expression<Boolean>(fieldName, operator, join, Boolean.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Boolean.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.BYTE))
                            expressionList.add(new Expression<Byte>(fieldName, operator, join, Byte.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Byte.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.CHARACTER))
                            expressionList.add(new Expression<Character>(fieldName, operator, join, Character.valueOf(value.charAt(BigInteger.ZERO.intValue())), (values != null && !values.isEmpty()) ? values.stream().map(Character.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.SHORT))
                            expressionList.add(new Expression<Short>(fieldName, operator, join, Short.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Short.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.INTEGER))
                            expressionList.add(new Expression<Integer>(fieldName, operator, join, Integer.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Integer.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.LONG))
                            expressionList.add(new Expression<Long>(fieldName, operator, join, Long.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Long.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.FLOAT))
                            expressionList.add(new Expression<Float>(fieldName, operator, join, Float.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Float.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.DOUBLE))
                            expressionList.add(new Expression<Double>(fieldName, operator, join, Double.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Double.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.BIGINTEGER))
                            expressionList.add(new Expression<BigInteger>(fieldName, operator, join, new BigInteger(value), (values != null && !values.isEmpty()) ? values.stream().map(BigInteger.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(BlogStackSpecificationConstants.INSTANCE.BIGDECIMAL))
                            expressionList.add(new Expression<BigDecimal>(fieldName, operator, join, new BigDecimal(value), (values != null && !values.isEmpty()) ? values.stream().map(BigDecimal.class::cast).collect(Collectors.toList()) : null));
                        else
                            expressionList.add(new Expression<String>(fieldName, operator, join, value.toLowerCase(), (values != null && !values.isEmpty()) ? values.stream().map(String::toLowerCase).collect(Collectors.toList()) : null));
                    }
                    catch(Exception ex) {
                        expressionList.add(new Expression<String>(field, operator, join, value.toLowerCase(), (values != null && !values.isEmpty()) ? values.stream().map(String::toLowerCase).collect(Collectors.toList()) : null));
                    }
                }
                else {
                    expressionList.add(new Expression<String>(field, operator, join, value.toLowerCase(), (values != null && !values.isEmpty()) ? values.stream().map(String::toLowerCase).collect(Collectors.toList()) : null));
                }
            }
        }
        return expressionList;
    }

    public String manipulateFilterCriteria(String filterCriteria, Set<String> dateFields) {
        StringBuffer stringBuffer = new StringBuffer();
        Matcher matcher = Pattern.compile(BlogStackSpecificationConstants.INSTANCE.SPLIT_PATTERN_STRING + BlogStackCommonConstants.INSTANCE.PIPE_STRING + BlogStackSpecificationConstants.INSTANCE.SPLIT_PATTERN_NUMBER).matcher(filterCriteria);
        while(matcher.find()) {
            String field = matcher.group(BigInteger.ONE.intValue());

            stringBuffer.append(BlogStackCommonConstants.INSTANCE.BACKQUOTE_STRING);
            stringBuffer.append(field);

            String operator = matcher.group(BigInteger.TWO.intValue());
            stringBuffer.append(BlogStackCommonConstants.INSTANCE.LESSTHAN_STRING);
            stringBuffer.append(operator);
            stringBuffer.append(BlogStackCommonConstants.INSTANCE.GREATERTHAN_STRING);

            String value = matcher.group(BlogStackCommonConstants.INSTANCE.THREE);
            if(dateFields != null && dateFields.contains(field)) {
                String[] splittedDateArray = value.split(BlogStackCommonConstants.INSTANCE.FORWARD_SLASH_STRING, -BigInteger.ONE.intValue());

                StringBuilder stringBuilder = new StringBuilder();
                if(splittedDateArray.length == BlogStackCommonConstants.INSTANCE.THREE) {
                    if(splittedDateArray[BigInteger.TWO.intValue()].length() == BlogStackCommonConstants.INSTANCE.FOUR)
                        stringBuilder.append(splittedDateArray[BigInteger.TWO.intValue()]);

                    stringBuilder.append(BlogStackCommonConstants.INSTANCE.HYPHEN_STRING)
                            .append(splittedDateArray[BigInteger.ONE.intValue()])
                            .append(BlogStackCommonConstants.INSTANCE.HYPHEN_STRING)
                            .append(splittedDateArray[BigInteger.ZERO.intValue()]);
                } else if(splittedDateArray.length == BigInteger.TWO.intValue()) {
                    if(splittedDateArray[BigInteger.ONE.intValue()].length() >= BigInteger.TWO.intValue())
                        stringBuilder.append(splittedDateArray[BigInteger.ONE.intValue()]);

                    stringBuilder.append(BlogStackCommonConstants.INSTANCE.HYPHEN_STRING)
                            .append(splittedDateArray[BigInteger.ZERO.intValue()]);
                } else {
                    stringBuilder.append(splittedDateArray[BigInteger.ZERO.intValue()]);
                }
                value = stringBuilder.toString();
            }
            stringBuffer.append(value);
        }
        return stringBuffer.toString();
    }


    @Data
    public static class Expression<E extends Comparable<E>> {

        private String field;

        private String operator;

        private String join;

        private E value;

        private List<E> values;

        public Expression() {
            super();
        }

        public Expression(String field, String operator, String join, E value, List<E> values) {
            super();
            this.field = field;
            this.operator = operator;
            this.join = join;
            this.value = value;
            this.values = values;
        }
    }
}