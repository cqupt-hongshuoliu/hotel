package com.example.xiaosage.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.controller.dto.PasswordDTO;
import com.example.xiaosage.controller.dto.UserDTO;
import com.example.xiaosage.entity.SysUser;

import com.example.xiaosage.service.ISysUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2023-03-08
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;

    /**
     * 登录接口
     * @param userDTO   user参数接收类
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (!StrUtil.isBlank(username) && !StrUtil.isBlank(password)) {
            Result result = sysUserService.login(userDTO);
            return result;
        } else {
            return Result.error("400", "参数错误");
        }
    }

    /**
     * 注册接口
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        if (StrUtil.isBlank(userDTO.getUsername()) || StrUtil.isBlank(userDTO.getPassword()) || StrUtil.isBlank(userDTO.getNickname())){
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return sysUserService.register(userDTO);
    }

    @PostMapping({"/modifyPassword/{username}"})
    public Result modifyPassword(@PathVariable String username, @RequestBody PasswordDTO passwordDTO) {
        return sysUserService.modifyPassword(username, passwordDTO);
    }

    /**
     * 依据 username 查找用户接口
     * @param username
     * @return
     */
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        SysUser result = sysUserService.findOne(username);
        if (result != null){
            return Result.success(result,"查找成功");
        }else {
            return Result.error(Constants.CODE_500, "查找失败");
        }
    }

    /**
     * 插入或更新接口
     * @param sysUser
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser){
        //新增或更新
        return sysUserService.saveUser(sysUser);
    }

    /**
     * 依据 id 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        if (sysUserService.removeById(id)){
            return Result.success(null, "删除成功");
        }else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/list")
    public Result findAll(){
        return Result.success(sysUserService.list(),"");
    }

    /**
     * 分页实现
     * @param pageNum
     * @param pageSize
     * @param username
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username){
        IPage<SysUser> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username",username);
        queryWrapper.orderByDesc("id");
        IPage<SysUser> sysUserIPage = sysUserService.page(page, queryWrapper);
        return Result.success(sysUserIPage,"");
    }

    /**
     * 导入数据
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws Exception {
        List<SysUser> list = sysUserService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        return Result.success();
    }

    /**
     * 导入数据
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<SysUser> list = reader.readAll(SysUser.class);
        if (sysUserService.saveBatch(list)){
            return Result.success();
        } else {
            return Result.error(Constants.CODE_500,"导入失败");
        }
    }
}
