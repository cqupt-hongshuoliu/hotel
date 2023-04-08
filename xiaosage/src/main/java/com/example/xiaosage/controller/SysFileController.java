package com.example.xiaosage.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.config.AuthAccess;
import com.example.xiaosage.entity.SysFile;
import com.example.xiaosage.mapper.SysFileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/file")
public class SysFileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private SysFileMapper sysFileMapper;

    /**
     * 上传文件
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file, @RequestParam String purpose) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + "." + type;
        File uploadFile = new File(this.fileUploadPath + fileUUID);
        File uploadParentFile = uploadFile.getParentFile();
        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdir();
        }

        file.transferTo(uploadFile);
        String md5 = SecureUtil.md5(uploadFile);
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper();
        queryWrapper.eq("md5", md5);
        List<SysFile> sysFileList = this.sysFileMapper.selectList(queryWrapper);
        String url;
        SysFile saveFile;
        if (sysFileList.size() != 0) {
            saveFile = (SysFile)sysFileList.get(0);
            url = saveFile.getUrl();
            uploadFile.delete();
        } else {
            url = "http://localhost:9091/file/" + fileUUID;
        }

        saveFile = new SysFile();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024L);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        if (purpose.length() == 0) {
            purpose = "头像";
        }

        saveFile.setPurpose(purpose);
        this.sysFileMapper.insert(saveFile);
        return url;
    }

    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse httpServletResponse) throws IOException{
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        //设置输出流的格式
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        httpServletResponse.setContentType("application/octet-stream");
        //读取文件字节流
        outputStream.write(FileUtil.readBytes(uploadFile));
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping({"/save"})
    public Result save(@RequestBody SysFile sysFile) {
        int i = this.sysFileMapper.updateById(sysFile);
        return i == 1 ? Result.success((Object)null, "保存成功") : Result.error("500", "保存失败");
    }

    @DeleteMapping({"/{id}"})
    public Result delete(@PathVariable Integer id) {
        SysFile sysFile = (SysFile)this.sysFileMapper.selectById(id);
        sysFile.setIsDelete(true);
        int i = this.sysFileMapper.updateById(sysFile);
        return i == 1 ? Result.success((Object)null, "删除成功") : Result.error("500", "删除失败");
    }

    @DeleteMapping({"/del/batch"})
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", ids);
        List<SysFile> sysFiles = this.sysFileMapper.selectList(queryWrapper);
        Iterator var4 = sysFiles.iterator();

        while(var4.hasNext()) {
            SysFile sysFile = (SysFile)var4.next();
            sysFile.setIsDelete(true);
            this.sysFileMapper.updateById(sysFile);
        }

        return Result.success();
    }

    @GetMapping({"/list"})
    public Result findAll() {
        return Result.success(this.sysFileMapper.selectList((Wrapper)null), "查询成功");
    }

    @GetMapping({"/page"})
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String name) {
        IPage<SysFile> page = new Page((long)pageNum, (long)pageSize);
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_delete", false);
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        IPage<SysFile> sysFileIPage = this.sysFileMapper.selectPage(page, queryWrapper);
        return Result.success(sysFileIPage, "");
    }

    @AuthAccess
    @GetMapping({"/getZoumadeng"})
    public Result getZoumadeng() {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper();
        queryWrapper.eq("purpose", "走马灯");
        queryWrapper.eq("is_delete", 0);
        queryWrapper.eq("enable", 1);
        List<SysFile> sysFiles = this.sysFileMapper.selectList(queryWrapper);
        return Result.success(sysFiles, "");
    }

}
