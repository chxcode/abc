package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.domain.Post;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IPostService;
import com.abc.cx.service.impl.PostServiceImpl;
import com.abc.cx.vo.Ret;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ChangXuan
 * @Decription: 职位管理
 * @Date: 11:10 2021/11/7
 **/
@Api(tags = "职位管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/post")
public class PostController {

    private IPostService postService;

    @Autowired
    public void setPostServiceImpl(PostServiceImpl postService) {
        this.postService = postService;
    }

    @ApiOperation("获取职位列表")
    @GetMapping
    public Ret listAll() {
        return postService.listAll();
    }

    @ApiOperation("获取职位分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "职位名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query"),
    })
    @GetMapping(value = "/pages")
    public Ret list(@RequestParam(defaultValue = "") String name,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return postService.pagePosts(name, page, pageSize);
    }

    @ApiOperation("获取职位详情")
    @ApiImplicitParam(name = "id", value = "职位id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id}")
    public Ret get(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @Log(title = "职位管理", operateType = OperateTypeEnum.ADD)
    @ApiOperation("增加职位")
    @ApiImplicitParam(name = "post", value = "职位对象", required = true, dataType = "Post", paramType = "body")
    @PostMapping
    public Ret add(@RequestBody Post post) {
        return postService.insertPost(post);
    }

    @Log(title = "职位管理", operateType = OperateTypeEnum.UPDATE)
    @ApiOperation("修改职位")
    @ApiImplicitParam(name = "post", value = "职位对象", required = true, dataType = "Post", paramType = "body")
    @PutMapping
    public Ret update(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @Log(title = "职位管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除职位")
    @ApiImplicitParam(name = "id", value = "职位id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return postService.deletePost(id);
    }

}
