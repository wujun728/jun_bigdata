package com.shujia.Controller;

import com.shujia.Entity.Student;
import com.shujia.Service.StudentService;
import com.shujia.common.Result;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController // 将数据以JSON格式返回
@RequestMapping("/stu")
public class StudentController {
    @Resource
    private StudentService studentService;

    @GetMapping
    public Result<List<Student>> findAll() {
        List<Student> list = studentService.findAll();
        return Result.success(list);
    }

    // /stu/page?pageSize=10&pageNum=1
//    @GetMapping("/page")
//    public Result<Page<Student>> findPage(@RequestParam(name = "pageSize") Integer pageSize,
//                                          @RequestParam(name = "pageNum") Integer pageNum) {
//        System.out.println(pageSize);
//        System.out.println(pageNum);
//        Page<Student> page = studentService.findPage(pageSize, pageNum);
//        return Result.success(page);
//
//    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id) {
        System.out.println(id);
        studentService.deleteById(id);
        return Result.success();
    }

    @PostMapping
    public Result saveStu(@RequestBody Student stu) {
        studentService.save(stu);
        return Result.success();
    }

    @GetMapping("/pageOrsearch")
    public Result<Page<Student>> searchByClazz(@RequestParam(name = "pageSize") Integer pageSize,
                                               @RequestParam(name = "pageNum") Integer pageNum,
                                               @RequestParam(name = "clazz") String clazz) {
        System.out.println(clazz);
        Page<Student> students = studentService.searchByClazz(pageSize, pageNum, clazz);
        return Result.success(students);
    }

}
