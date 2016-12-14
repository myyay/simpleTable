package com.yay.controller;

import com.yay.common.Page;
import com.yay.domain.CommonProblem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 模板Controller
 * @author Yay
 * @version 1.0
 * @since 2016/11/29 10:30
 */
@Controller
@RequestMapping("template")
public class TemplateController {

    private final Logger logger = LogManager.getLogger(TemplateController.class);


    @RequestMapping("list")
    public String list(Model model) {


        return "commonProblem/list";
    }

    @RequestMapping(value = "listLoad")
    @ResponseBody
    public Page listLoad(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                         CommonProblem condition) {

        logger.info("查询条件:" + (null == condition ? "无" : condition.toString()));
        List<CommonProblem> queryResult = new ArrayList<CommonProblem>();
        for (int i = 0; i < 35; i++) {
            CommonProblem problem = createProblem();
            problem.setId((long) i);
            queryResult.add(problem);
        }


        List<CommonProblem> retList = new ArrayList<CommonProblem>();

        for (int i = (pageNum - 1) * pageSize; i < ((pageNum - 1) * pageSize + pageSize) && i < queryResult.size(); i++) {
            retList.add(queryResult.get(i));
        }
        //throw new RuntimeException("测试");

        return new Page<CommonProblem>(pageNum, pageSize, 35, retList);

    }

    @RequestMapping("exception")
    public String exception(Model model) {
        throw new RuntimeException("测试异常");
    }

    private CommonProblem createProblem() {
        CommonProblem cp = new CommonProblem();
        cp.setId(1L);
        cp.setCreateOperator("我");
        cp.setProblemClass(1);
        cp.setProblemTitle("title");
        cp.setProblemContent("内容");
        cp.setProblemType(2);
        cp.setCreateTime(new Date());
        return cp;
    }


}
