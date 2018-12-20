package cn.stylefeng.guns.modular.spark.controller;

import cn.stylefeng.guns.config.properties.GunsProperties;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.modular.spark.util.StudentAuthentication;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import cn.stylefeng.guns.modular.spark.model.StudentUserDetail;
import cn.stylefeng.guns.modular.spark.service.IStudentUserDetailService;

import java.io.File;
import java.util.UUID;

/**
 * 学生认证控制器
 *
 * @author fengshuonan
 * @Date 2018-12-20 22:06:19
 */
@Controller
@RequestMapping("/spark/studentUserDetail")
public class StudentUserDetailController extends BaseController {

    private String PREFIX = "/spark/studentUserDetail/";

    @Autowired
    private IStudentUserDetailService studentUserDetailService;


    @Autowired
    private GunsProperties gunsProperties;

    @Autowired
    StudentAuthentication studentAuthentication;

    /**
     * 获取cookie信息
     */
    @RequestMapping("/getCookie")
    @ResponseBody
    public String getCookieInfo(){
        studentAuthentication.getCookie();
        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix("gif");
        try {
            String fileSavePath = gunsProperties.getFileUploadPath();
            studentAuthentication.SaveImg(fileSavePath,pictureName);
            return "kaptcha/"+pictureName;
        } catch (Exception e) {
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }

    }

    /**
     * 跳转到学生认证首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "studentUserDetail.html";
    }

    /**
     * 跳转到添加学生认证
     */
    @RequestMapping("/studentUserDetail_add")
    public String studentUserDetailAdd() {
        return PREFIX + "studentUserDetail_add.html";
    }

    /**
     * 跳转到修改学生认证
     */
    @RequestMapping("/studentUserDetail_update/{studentUserDetailId}")
    public String studentUserDetailUpdate(@PathVariable Integer studentUserDetailId, Model model) {
        StudentUserDetail studentUserDetail = studentUserDetailService.selectById(studentUserDetailId);
        model.addAttribute("item",studentUserDetail);
        LogObjectHolder.me().set(studentUserDetail);
        return PREFIX + "studentUserDetail_edit.html";
    }

    /**
     * 获取学生认证列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return studentUserDetailService.selectList(null);
    }

    /**
     * 新增学生认证
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(StudentUserDetail studentUserDetail) {
        studentUserDetailService.insert(studentUserDetail);
        return SUCCESS_TIP;
    }

    /**
     * 删除学生认证
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer studentUserDetailId) {
        studentUserDetailService.deleteById(studentUserDetailId);
        return SUCCESS_TIP;
    }

    /**
     * 修改学生认证
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(StudentUserDetail studentUserDetail) {
        studentUserDetailService.updateById(studentUserDetail);
        return SUCCESS_TIP;
    }

    /**
     * 学生认证详情
     */
    @RequestMapping(value = "/detail/{studentUserDetailId}")
    @ResponseBody
    public Object detail(@PathVariable("studentUserDetailId") Integer studentUserDetailId) {
        return studentUserDetailService.selectById(studentUserDetailId);
    }
}
