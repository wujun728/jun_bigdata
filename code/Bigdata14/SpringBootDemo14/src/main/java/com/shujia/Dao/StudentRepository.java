package com.shujia.Dao;

import com.shujia.Entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query(value = "select * from student where clazz like %?%", nativeQuery = true)
    public Page<Student> findByClazz(String clazz, PageRequest pg);
}
