package com.controller;

import com.model.ResponseBean;
import com.model.TableClass;
import com.service.GenerateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 2hu0
 */
@RestController
public class GenerateController {
    @Autowired
    GenerateCodeService generateCodeService;

     @PostMapping("/generateCode")
    public ResponseBean generateCode(@RequestBody List<TableClass> tableClasses, HttpServletRequest request) {
         return generateCodeService.generateCode(tableClasses,request.getServletContext().getRealPath("/"));
    }
}
