package com.shujia.Service;

import com.shujia.Dao.StudentRepository;
import com.shujia.Entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentService {
    @Resource
    private StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Page<Student> findPage(Integer pageSize, Integer pageNum) {
        PageRequest pg = PageRequest.of(pageNum - 1, pageSize);
        Page<Student> pageStu = studentRepository.findAll(pg);
        return pageStu;
    }

    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
    }

    public void save(Student stu) {
        studentRepository.save(stu);
    }

    public Page<Student> searchByClazz(Integer pageSize, Integer pageNum, String clazz) {
        Sort sort = new Sort(Sort.Direction.DESC, "sum_score");
        PageRequest pg = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Student> stuLikeClazz = studentRepository.findByClazz(clazz, pg);
        return stuLikeClazz;
    }

}
