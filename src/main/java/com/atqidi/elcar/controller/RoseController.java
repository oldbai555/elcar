package com.atqidi.elcar.controller;


import com.atqidi.elcar.entity.Rose;
import com.atqidi.elcar.service.RoseService;
import com.atqidi.elcar.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Api(description = "角色")
@RestController
@RequestMapping("/elcar/rose")
@CrossOrigin
public class RoseController {

    @Autowired
    RoseService roseService;

    @ApiOperation("添加角色")
    @PostMapping("/rose")
    public Result addRose(@RequestBody Rose rose) {
        return roseService.addRose(rose);
    }

    @ApiOperation("修改角色信息通过ID")
    @PutMapping("/rose")
    public Result updateRose(@RequestBody Rose rose) {
        return roseService.updateRose(rose);
    }

    @ApiOperation("通过ID删除角色")
    @DeleteMapping("/rose/{id}")
    public Result deleteRose(@PathVariable("id") Integer id) {
        return roseService.deleteRose(id);
    }

    @ApiOperation("通过ID查找角色")
    @GetMapping("/rose/{id}")
    public Result getRoseById(@PathVariable("id") Integer id) {
        return roseService.getRoseById(id);
    }

    @ApiOperation("通过角色名称查找角色")
    @GetMapping("/name/{name}")
    public Result getRoseByName(@PathVariable("name") String name) {
        return roseService.getRoseByName(name);
    }

    @ApiOperation("分页查询查找角色列表")
    @GetMapping("/list/{page}/{size}")
    public Result getRoseById(@PathVariable("page") Integer page,
                              @PathVariable("size") Integer size) {
        return roseService.getRoseList(page, size);
    }

}

