package com.easytoolsoft.easyreport.membership.domain.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Wujun
 * @date 2017-03-25
 */
public class ModuleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ModuleExample() {
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

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria)this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria)this;
        }

        public Criteria andCodeEqualTo(final String value) {
            addCriterion("code =", value, "code");
            return (Criteria)this;
        }

        public Criteria andCodeNotEqualTo(final String value) {
            addCriterion("code <>", value, "code");
            return (Criteria)this;
        }

        public Criteria andCodeGreaterThan(final String value) {
            addCriterion("code >", value, "code");
            return (Criteria)this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(final String value) {
            addCriterion("code >=", value, "code");
            return (Criteria)this;
        }

        public Criteria andCodeLessThan(final String value) {
            addCriterion("code <", value, "code");
            return (Criteria)this;
        }

        public Criteria andCodeLessThanOrEqualTo(final String value) {
            addCriterion("code <=", value, "code");
            return (Criteria)this;
        }

        public Criteria andCodeLike(final String value) {
            addCriterion("code like", value, "code");
            return (Criteria)this;
        }

        public Criteria andCodeNotLike(final String value) {
            addCriterion("code not like", value, "code");
            return (Criteria)this;
        }

        public Criteria andCodeIn(final List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria)this;
        }

        public Criteria andCodeNotIn(final List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria)this;
        }

        public Criteria andCodeBetween(final String value1, final String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria)this;
        }

        public Criteria andCodeNotBetween(final String value1, final String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria)this;
        }

        public Criteria andIconIsNull() {
            addCriterion("icon is null");
            return (Criteria)this;
        }

        public Criteria andIconIsNotNull() {
            addCriterion("icon is not null");
            return (Criteria)this;
        }

        public Criteria andIconEqualTo(final String value) {
            addCriterion("icon =", value, "icon");
            return (Criteria)this;
        }

        public Criteria andIconNotEqualTo(final String value) {
            addCriterion("icon <>", value, "icon");
            return (Criteria)this;
        }

        public Criteria andIconGreaterThan(final String value) {
            addCriterion("icon >", value, "icon");
            return (Criteria)this;
        }

        public Criteria andIconGreaterThanOrEqualTo(final String value) {
            addCriterion("icon >=", value, "icon");
            return (Criteria)this;
        }

        public Criteria andIconLessThan(final String value) {
            addCriterion("icon <", value, "icon");
            return (Criteria)this;
        }

        public Criteria andIconLessThanOrEqualTo(final String value) {
            addCriterion("icon <=", value, "icon");
            return (Criteria)this;
        }

        public Criteria andIconLike(final String value) {
            addCriterion("icon like", value, "icon");
            return (Criteria)this;
        }

        public Criteria andIconNotLike(final String value) {
            addCriterion("icon not like", value, "icon");
            return (Criteria)this;
        }

        public Criteria andIconIn(final List<String> values) {
            addCriterion("icon in", values, "icon");
            return (Criteria)this;
        }

        public Criteria andIconNotIn(final List<String> values) {
            addCriterion("icon not in", values, "icon");
            return (Criteria)this;
        }

        public Criteria andIconBetween(final String value1, final String value2) {
            addCriterion("icon between", value1, value2, "icon");
            return (Criteria)this;
        }

        public Criteria andIconNotBetween(final String value1, final String value2) {
            addCriterion("icon not between", value1, value2, "icon");
            return (Criteria)this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria)this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria)this;
        }

        public Criteria andUrlEqualTo(final String value) {
            addCriterion("url =", value, "url");
            return (Criteria)this;
        }

        public Criteria andUrlNotEqualTo(final String value) {
            addCriterion("url <>", value, "url");
            return (Criteria)this;
        }

        public Criteria andUrlGreaterThan(final String value) {
            addCriterion("url >", value, "url");
            return (Criteria)this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(final String value) {
            addCriterion("url >=", value, "url");
            return (Criteria)this;
        }

        public Criteria andUrlLessThan(final String value) {
            addCriterion("url <", value, "url");
            return (Criteria)this;
        }

        public Criteria andUrlLessThanOrEqualTo(final String value) {
            addCriterion("url <=", value, "url");
            return (Criteria)this;
        }

        public Criteria andUrlLike(final String value) {
            addCriterion("url like", value, "url");
            return (Criteria)this;
        }

        public Criteria andUrlNotLike(final String value) {
            addCriterion("url not like", value, "url");
            return (Criteria)this;
        }

        public Criteria andUrlIn(final List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria)this;
        }

        public Criteria andUrlNotIn(final List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria)this;
        }

        public Criteria andUrlBetween(final String value1, final String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria)this;
        }

        public Criteria andUrlNotBetween(final String value1, final String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria)this;
        }

        public Criteria andPathIsNull() {
            addCriterion("path is null");
            return (Criteria)this;
        }

        public Criteria andPathIsNotNull() {
            addCriterion("path is not null");
            return (Criteria)this;
        }

        public Criteria andPathEqualTo(final String value) {
            addCriterion("path =", value, "path");
            return (Criteria)this;
        }

        public Criteria andPathNotEqualTo(final String value) {
            addCriterion("path <>", value, "path");
            return (Criteria)this;
        }

        public Criteria andPathGreaterThan(final String value) {
            addCriterion("path >", value, "path");
            return (Criteria)this;
        }

        public Criteria andPathGreaterThanOrEqualTo(final String value) {
            addCriterion("path >=", value, "path");
            return (Criteria)this;
        }

        public Criteria andPathLessThan(final String value) {
            addCriterion("path <", value, "path");
            return (Criteria)this;
        }

        public Criteria andPathLessThanOrEqualTo(final String value) {
            addCriterion("path <=", value, "path");
            return (Criteria)this;
        }

        public Criteria andPathLike(final String value) {
            addCriterion("path like", value, "path");
            return (Criteria)this;
        }

        public Criteria andPathNotLike(final String value) {
            addCriterion("path not like", value, "path");
            return (Criteria)this;
        }

        public Criteria andPathIn(final List<String> values) {
            addCriterion("path in", values, "path");
            return (Criteria)this;
        }

        public Criteria andPathNotIn(final List<String> values) {
            addCriterion("path not in", values, "path");
            return (Criteria)this;
        }

        public Criteria andPathBetween(final String value1, final String value2) {
            addCriterion("path between", value1, value2, "path");
            return (Criteria)this;
        }

        public Criteria andPathNotBetween(final String value1, final String value2) {
            addCriterion("path not between", value1, value2, "path");
            return (Criteria)this;
        }

        public Criteria andHasChildIsNull() {
            addCriterion("has_child is null");
            return (Criteria)this;
        }

        public Criteria andHasChildIsNotNull() {
            addCriterion("has_child is not null");
            return (Criteria)this;
        }

        public Criteria andHasChildEqualTo(final Byte value) {
            addCriterion("has_child =", value, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildNotEqualTo(final Byte value) {
            addCriterion("has_child <>", value, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildGreaterThan(final Byte value) {
            addCriterion("has_child >", value, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildGreaterThanOrEqualTo(final Byte value) {
            addCriterion("has_child >=", value, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildLessThan(final Byte value) {
            addCriterion("has_child <", value, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildLessThanOrEqualTo(final Byte value) {
            addCriterion("has_child <=", value, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildIn(final List<Byte> values) {
            addCriterion("has_child in", values, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildNotIn(final List<Byte> values) {
            addCriterion("has_child not in", values, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildBetween(final Byte value1, final Byte value2) {
            addCriterion("has_child between", value1, value2, "hasChild");
            return (Criteria)this;
        }

        public Criteria andHasChildNotBetween(final Byte value1, final Byte value2) {
            addCriterion("has_child not between", value1, value2, "hasChild");
            return (Criteria)this;
        }

        public Criteria andLinkTypeIsNull() {
            addCriterion("link_type is null");
            return (Criteria)this;
        }

        public Criteria andLinkTypeIsNotNull() {
            addCriterion("link_type is not null");
            return (Criteria)this;
        }

        public Criteria andLinkTypeEqualTo(final Byte value) {
            addCriterion("link_type =", value, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeNotEqualTo(final Byte value) {
            addCriterion("link_type <>", value, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeGreaterThan(final Byte value) {
            addCriterion("link_type >", value, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeGreaterThanOrEqualTo(final Byte value) {
            addCriterion("link_type >=", value, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeLessThan(final Byte value) {
            addCriterion("link_type <", value, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeLessThanOrEqualTo(final Byte value) {
            addCriterion("link_type <=", value, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeIn(final List<Byte> values) {
            addCriterion("link_type in", values, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeNotIn(final List<Byte> values) {
            addCriterion("link_type not in", values, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeBetween(final Byte value1, final Byte value2) {
            addCriterion("link_type between", value1, value2, "linkType");
            return (Criteria)this;
        }

        public Criteria andLinkTypeNotBetween(final Byte value1, final Byte value2) {
            addCriterion("link_type not between", value1, value2, "linkType");
            return (Criteria)this;
        }

        public Criteria andTargetIsNull() {
            addCriterion("target is null");
            return (Criteria)this;
        }

        public Criteria andTargetIsNotNull() {
            addCriterion("target is not null");
            return (Criteria)this;
        }

        public Criteria andTargetEqualTo(final String value) {
            addCriterion("target =", value, "target");
            return (Criteria)this;
        }

        public Criteria andTargetNotEqualTo(final String value) {
            addCriterion("target <>", value, "target");
            return (Criteria)this;
        }

        public Criteria andTargetGreaterThan(final String value) {
            addCriterion("target >", value, "target");
            return (Criteria)this;
        }

        public Criteria andTargetGreaterThanOrEqualTo(final String value) {
            addCriterion("target >=", value, "target");
            return (Criteria)this;
        }

        public Criteria andTargetLessThan(final String value) {
            addCriterion("target <", value, "target");
            return (Criteria)this;
        }

        public Criteria andTargetLessThanOrEqualTo(final String value) {
            addCriterion("target <=", value, "target");
            return (Criteria)this;
        }

        public Criteria andTargetLike(final String value) {
            addCriterion("target like", value, "target");
            return (Criteria)this;
        }

        public Criteria andTargetNotLike(final String value) {
            addCriterion("target not like", value, "target");
            return (Criteria)this;
        }

        public Criteria andTargetIn(final List<String> values) {
            addCriterion("target in", values, "target");
            return (Criteria)this;
        }

        public Criteria andTargetNotIn(final List<String> values) {
            addCriterion("target not in", values, "target");
            return (Criteria)this;
        }

        public Criteria andTargetBetween(final String value1, final String value2) {
            addCriterion("target between", value1, value2, "target");
            return (Criteria)this;
        }

        public Criteria andTargetNotBetween(final String value1, final String value2) {
            addCriterion("target not between", value1, value2, "target");
            return (Criteria)this;
        }

        public Criteria andParamsIsNull() {
            addCriterion("params is null");
            return (Criteria)this;
        }

        public Criteria andParamsIsNotNull() {
            addCriterion("params is not null");
            return (Criteria)this;
        }

        public Criteria andParamsEqualTo(final String value) {
            addCriterion("params =", value, "params");
            return (Criteria)this;
        }

        public Criteria andParamsNotEqualTo(final String value) {
            addCriterion("params <>", value, "params");
            return (Criteria)this;
        }

        public Criteria andParamsGreaterThan(final String value) {
            addCriterion("params >", value, "params");
            return (Criteria)this;
        }

        public Criteria andParamsGreaterThanOrEqualTo(final String value) {
            addCriterion("params >=", value, "params");
            return (Criteria)this;
        }

        public Criteria andParamsLessThan(final String value) {
            addCriterion("params <", value, "params");
            return (Criteria)this;
        }

        public Criteria andParamsLessThanOrEqualTo(final String value) {
            addCriterion("params <=", value, "params");
            return (Criteria)this;
        }

        public Criteria andParamsLike(final String value) {
            addCriterion("params like", value, "params");
            return (Criteria)this;
        }

        public Criteria andParamsNotLike(final String value) {
            addCriterion("params not like", value, "params");
            return (Criteria)this;
        }

        public Criteria andParamsIn(final List<String> values) {
            addCriterion("params in", values, "params");
            return (Criteria)this;
        }

        public Criteria andParamsNotIn(final List<String> values) {
            addCriterion("params not in", values, "params");
            return (Criteria)this;
        }

        public Criteria andParamsBetween(final String value1, final String value2) {
            addCriterion("params between", value1, value2, "params");
            return (Criteria)this;
        }

        public Criteria andParamsNotBetween(final String value1, final String value2) {
            addCriterion("params not between", value1, value2, "params");
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

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria)this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria)this;
        }

        public Criteria andStatusEqualTo(final Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotEqualTo(final Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusGreaterThan(final Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(final Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLessThan(final Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLessThanOrEqualTo(final Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusIn(final List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotIn(final List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria)this;
        }

        public Criteria andStatusBetween(final Byte value1, final Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotBetween(final Byte value1, final Byte value2) {
            addCriterion("status not between", value1, value2, "status");
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