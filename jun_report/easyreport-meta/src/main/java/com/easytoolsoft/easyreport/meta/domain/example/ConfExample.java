package com.easytoolsoft.easyreport.meta.domain.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Wujun
 * @date 2017-03-25
 */
public class ConfExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ConfExample() {
        this.oredCriteria = new ArrayList<>();
    }

    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setOrderByClause(final String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return this.distinct;
    }

    public void setDistinct(final boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getOredCriteria() {
        return this.oredCriteria;
    }

    public void or(final Criteria criteria) {
        this.oredCriteria.add(criteria);
    }

    public Criteria or() {
        final Criteria criteria = createCriteriaInternal();
        this.oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        final Criteria criteria = createCriteriaInternal();
        if (this.oredCriteria.size() == 0) {
            this.oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        return new Criteria();
    }

    public void clear() {
        this.oredCriteria.clear();
        this.orderByClause = null;
        this.distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            this.criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return this.criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return this.criteria;
        }

        public List<Criterion> getCriteria() {
            return this.criteria;
        }

        protected void addCriterion(final String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            this.criteria.add(new Criterion(condition));
        }

        protected void addCriterion(final String condition, final Object value, final String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            this.criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(final String condition, final Object value1, final Object value2,
                                    final String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            this.criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria)this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria)this;
        }

        public Criteria andIdEqualTo(final Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotEqualTo(final Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdGreaterThan(final Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdGreaterThanOrEqualTo(final Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLessThan(final Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLessThanOrEqualTo(final Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdIn(final List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotIn(final List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria)this;
        }

        public Criteria andIdBetween(final Integer value1, final Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotBetween(final Integer value1, final Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria)this;
        }

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return (Criteria)this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return (Criteria)this;
        }

        public Criteria andParentIdEqualTo(final Integer value) {
            addCriterion("parent_id =", value, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdNotEqualTo(final Integer value) {
            addCriterion("parent_id <>", value, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdGreaterThan(final Integer value) {
            addCriterion("parent_id >", value, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(final Integer value) {
            addCriterion("parent_id >=", value, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdLessThan(final Integer value) {
            addCriterion("parent_id <", value, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdLessThanOrEqualTo(final Integer value) {
            addCriterion("parent_id <=", value, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdIn(final List<Integer> values) {
            addCriterion("parent_id in", values, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdNotIn(final List<Integer> values) {
            addCriterion("parent_id not in", values, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdBetween(final Integer value1, final Integer value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return (Criteria)this;
        }

        public Criteria andParentIdNotBetween(final Integer value1, final Integer value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
            return (Criteria)this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria)this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria)this;
        }

        public Criteria andNameEqualTo(final String value) {
            addCriterion("name =", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameNotEqualTo(final String value) {
            addCriterion("name <>", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameGreaterThan(final String value) {
            addCriterion("name >", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameGreaterThanOrEqualTo(final String value) {
            addCriterion("name >=", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameLessThan(final String value) {
            addCriterion("name <", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameLessThanOrEqualTo(final String value) {
            addCriterion("name <=", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameLike(final String value) {
            addCriterion("name like", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameNotLike(final String value) {
            addCriterion("name not like", value, "name");
            return (Criteria)this;
        }

        public Criteria andNameIn(final List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria)this;
        }

        public Criteria andNameNotIn(final List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria)this;
        }

        public Criteria andNameBetween(final String value1, final String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria)this;
        }

        public Criteria andNameNotBetween(final String value1, final String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria)this;
        }

        public Criteria andKeyIsNull() {
            addCriterion("key is null");
            return (Criteria)this;
        }

        public Criteria andKeyIsNotNull() {
            addCriterion("key is not null");
            return (Criteria)this;
        }

        public Criteria andKeyEqualTo(final String value) {
            addCriterion("key =", value, "key");
            return (Criteria)this;
        }

        public Criteria andKeyNotEqualTo(final String value) {
            addCriterion("key <>", value, "key");
            return (Criteria)this;
        }

        public Criteria andKeyGreaterThan(final String value) {
            addCriterion("key >", value, "key");
            return (Criteria)this;
        }

        public Criteria andKeyGreaterThanOrEqualTo(final String value) {
            addCriterion("key >=", value, "key");
            return (Criteria)this;
        }

        public Criteria andKeyLessThan(final String value) {
            addCriterion("key <", value, "key");
            return (Criteria)this;
        }

        public Criteria andKeyLessThanOrEqualTo(final String value) {
            addCriterion("key <=", value, "key");
            return (Criteria)this;
        }

        public Criteria andKeyLike(final String value) {
            addCriterion("key like", value, "key");
            return (Criteria)this;
        }

        public Criteria andKeyNotLike(final String value) {
            addCriterion("key not like", value, "key");
            return (Criteria)this;
        }

        public Criteria andKeyIn(final List<String> values) {
            addCriterion("key in", values, "key");
            return (Criteria)this;
        }

        public Criteria andKeyNotIn(final List<String> values) {
            addCriterion("key not in", values, "key");
            return (Criteria)this;
        }

        public Criteria andKeyBetween(final String value1, final String value2) {
            addCriterion("key between", value1, value2, "key");
            return (Criteria)this;
        }

        public Criteria andKeyNotBetween(final String value1, final String value2) {
            addCriterion("key not between", value1, value2, "key");
            return (Criteria)this;
        }

        public Criteria andValueIsNull() {
            addCriterion("value is null");
            return (Criteria)this;
        }

        public Criteria andValueIsNotNull() {
            addCriterion("value is not null");
            return (Criteria)this;
        }

        public Criteria andValueEqualTo(final String value) {
            addCriterion("value =", value, "value");
            return (Criteria)this;
        }

        public Criteria andValueNotEqualTo(final String value) {
            addCriterion("value <>", value, "value");
            return (Criteria)this;
        }

        public Criteria andValueGreaterThan(final String value) {
            addCriterion("value >", value, "value");
            return (Criteria)this;
        }

        public Criteria andValueGreaterThanOrEqualTo(final String value) {
            addCriterion("value >=", value, "value");
            return (Criteria)this;
        }

        public Criteria andValueLessThan(final String value) {
            addCriterion("value <", value, "value");
            return (Criteria)this;
        }

        public Criteria andValueLessThanOrEqualTo(final String value) {
            addCriterion("value <=", value, "value");
            return (Criteria)this;
        }

        public Criteria andValueLike(final String value) {
            addCriterion("value like", value, "value");
            return (Criteria)this;
        }

        public Criteria andValueNotLike(final String value) {
            addCriterion("value not like", value, "value");
            return (Criteria)this;
        }

        public Criteria andValueIn(final List<String> values) {
            addCriterion("value in", values, "value");
            return (Criteria)this;
        }

        public Criteria andValueNotIn(final List<String> values) {
            addCriterion("value not in", values, "value");
            return (Criteria)this;
        }

        public Criteria andValueBetween(final String value1, final String value2) {
            addCriterion("value between", value1, value2, "value");
            return (Criteria)this;
        }

        public Criteria andValueNotBetween(final String value1, final String value2) {
            addCriterion("value not between", value1, value2, "value");
            return (Criteria)this;
        }

        public Criteria andSequenceIsNull() {
            addCriterion("sequence is null");
            return (Criteria)this;
        }

        public Criteria andSequenceIsNotNull() {
            addCriterion("sequence is not null");
            return (Criteria)this;
        }

        public Criteria andSequenceEqualTo(final Integer value) {
            addCriterion("sequence =", value, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceNotEqualTo(final Integer value) {
            addCriterion("sequence <>", value, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceGreaterThan(final Integer value) {
            addCriterion("sequence >", value, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceGreaterThanOrEqualTo(final Integer value) {
            addCriterion("sequence >=", value, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceLessThan(final Integer value) {
            addCriterion("sequence <", value, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceLessThanOrEqualTo(final Integer value) {
            addCriterion("sequence <=", value, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceIn(final List<Integer> values) {
            addCriterion("sequence in", values, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceNotIn(final List<Integer> values) {
            addCriterion("sequence not in", values, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceBetween(final Integer value1, final Integer value2) {
            addCriterion("sequence between", value1, value2, "sequence");
            return (Criteria)this;
        }

        public Criteria andSequenceNotBetween(final Integer value1, final Integer value2) {
            addCriterion("sequence not between", value1, value2, "sequence");
            return (Criteria)this;
        }

        public Criteria andCommentIsNull() {
            addCriterion("comment is null");
            return (Criteria)this;
        }

        public Criteria andCommentIsNotNull() {
            addCriterion("comment is not null");
            return (Criteria)this;
        }

        public Criteria andCommentEqualTo(final String value) {
            addCriterion("comment =", value, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentNotEqualTo(final String value) {
            addCriterion("comment <>", value, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentGreaterThan(final String value) {
            addCriterion("comment >", value, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentGreaterThanOrEqualTo(final String value) {
            addCriterion("comment >=", value, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentLessThan(final String value) {
            addCriterion("comment <", value, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentLessThanOrEqualTo(final String value) {
            addCriterion("comment <=", value, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentLike(final String value) {
            addCriterion("comment like", value, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentNotLike(final String value) {
            addCriterion("comment not like", value, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentIn(final List<String> values) {
            addCriterion("comment in", values, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentNotIn(final List<String> values) {
            addCriterion("comment not in", values, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentBetween(final String value1, final String value2) {
            addCriterion("comment between", value1, value2, "comment");
            return (Criteria)this;
        }

        public Criteria andCommentNotBetween(final String value1, final String value2) {
            addCriterion("comment not between", value1, value2, "comment");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedIsNull() {
            addCriterion("gmt_created is null");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedIsNotNull() {
            addCriterion("gmt_created is not null");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedEqualTo(final Date value) {
            addCriterion("gmt_created =", value, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedNotEqualTo(final Date value) {
            addCriterion("gmt_created <>", value, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedGreaterThan(final Date value) {
            addCriterion("gmt_created >", value, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedGreaterThanOrEqualTo(final Date value) {
            addCriterion("gmt_created >=", value, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedLessThan(final Date value) {
            addCriterion("gmt_created <", value, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedLessThanOrEqualTo(final Date value) {
            addCriterion("gmt_created <=", value, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedIn(final List<Date> values) {
            addCriterion("gmt_created in", values, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedNotIn(final List<Date> values) {
            addCriterion("gmt_created not in", values, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedBetween(final Date value1, final Date value2) {
            addCriterion("gmt_created between", value1, value2, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtCreatedNotBetween(final Date value1, final Date value2) {
            addCriterion("gmt_created not between", value1, value2, "gmtCreated");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedIsNull() {
            addCriterion("gmt_modified is null");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedIsNotNull() {
            addCriterion("gmt_modified is not null");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedEqualTo(final Date value) {
            addCriterion("gmt_modified =", value, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedNotEqualTo(final Date value) {
            addCriterion("gmt_modified <>", value, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedGreaterThan(final Date value) {
            addCriterion("gmt_modified >", value, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedGreaterThanOrEqualTo(final Date value) {
            addCriterion("gmt_modified >=", value, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedLessThan(final Date value) {
            addCriterion("gmt_modified <", value, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedLessThanOrEqualTo(final Date value) {
            addCriterion("gmt_modified <=", value, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedIn(final List<Date> values) {
            addCriterion("gmt_modified in", values, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedNotIn(final List<Date> values) {
            addCriterion("gmt_modified not in", values, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedBetween(final Date value1, final Date value2) {
            addCriterion("gmt_modified between", value1, value2, "gmtModified");
            return (Criteria)this;
        }

        public Criteria andGmtModifiedNotBetween(final Date value1, final Date value2) {
            addCriterion("gmt_modified not between", value1, value2, "gmtModified");
            return (Criteria)this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }

        public Criteria andFieldLike(final String fieldName, final String keyword) {
            addCriterion(fieldName + " like ", keyword, fieldName);
            return this;
        }
    }

    public static class Criterion {
        private final String condition;
        private final String typeHandler;
        private Object value;
        private Object secondValue;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;

        protected Criterion(final String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(final String condition, final Object value, final String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(final String condition, final Object value) {
            this(condition, value, null);
        }

        protected Criterion(final String condition, final Object value, final Object secondValue,
                            final String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(final String condition, final Object value, final Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return this.condition;
        }

        public Object getValue() {
            return this.value;
        }

        public Object getSecondValue() {
            return this.secondValue;
        }

        public boolean isNoValue() {
            return this.noValue;
        }

        public boolean isSingleValue() {
            return this.singleValue;
        }

        public boolean isBetweenValue() {
            return this.betweenValue;
        }

        public boolean isListValue() {
            return this.listValue;
        }

        public String getTypeHandler() {
            return this.typeHandler;
        }
    }
}