package com.shujia.Entity;

import javax.persistence.*;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 描述id自增
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private String clazz;
    // 属性名一般同数据库中表的列名保持一致，不一致时可以使用@Column注解
    @Column(name = "sum_score")
    private Integer sumScore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Integer getSumScore() {
        return sumScore;
    }

    public void setSumScore(Integer sumScore) {
        this.sumScore = sumScore;
    }
}
