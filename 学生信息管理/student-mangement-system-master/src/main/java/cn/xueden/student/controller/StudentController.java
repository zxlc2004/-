package cn.xueden.student.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.exception.BadRequestException;
import cn.xueden.student.domain.GradeClass;
import cn.xueden.student.domain.Student;
import cn.xueden.student.domain.StudentTwo;
import cn.xueden.student.service.IGradeClassService;
import cn.xueden.student.service.IStudentService;

import cn.xueden.student.service.dto.StudentQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**功能描述：学生信息前端控制器
 * @author:梁志杰
 * @date:2022/12/11
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("student")
public class StudentController {

    private final IStudentService studentService;

    private final IGradeClassService gradeClassService;

    public StudentController(IStudentService studentService, IGradeClassService gradeClassService) {
        this.studentService = studentService;
        this.gradeClassService = gradeClassService;
    }

    /**
     * 获取学生列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(StudentQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC, "id");
        return new ResponseEntity<>(studentService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加学生信息
     * @param student
     * @return
     */
    @PostMapping
    public BaseResult addStudent(@RequestBody StudentTwo student){
        Student student1 = new Student();
        student1.setId(student.getId());
        student1.setQq(student.getQq());
        student1.setName(student.getName());
        student1.setPhone(student.getPhone());
        student1.setSex(student.getSex());
        student1.setStuno(student.getStuno());
        student1.setRemarks(student.getRemarks());
        student1.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        student1.setCreateBy(1L);
        GradeClass byId = gradeClassService.getById(2L);
        student1.setGradeClass(byId);
        studentService.editStudent1(student1);
        boolean result= studentService.addStudent(student1);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取学生详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        Student dbStudent = studentService.getById(id);
        return BaseResult.success(dbStudent);
    }

    /**
     * 更新学生信息
     * @param student
     * @return
     */
    @PutMapping
    public BaseResult editStudent(@RequestBody StudentTwo student){
        Student student1 = new Student();
        student1.setId(student.getId());
        student1.setQq(student.getQq());
        student1.setName(student.getName());
        student1.setPhone(student.getPhone());
        student1.setSex(student.getSex());
        student1.setStuno(student.getStuno());
        student1.setRemarks(student.getRemarks());
        // 直接设置 gradeClass 属性
        student1.setGradeClass(studentService.getById(student.getId()).getGradeClass());
        student1.setCreateTime(studentService.getById(student.getId()).getCreateTime());
        studentService.editStudent1(student1);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据ID删除学生信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        studentService.deleteById(id);
        return BaseResult.success("删除成功");
    }

}
