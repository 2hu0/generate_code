package com.service;

import com.google.common.base.CaseFormat;
import com.model.ColumnClass;
import com.model.ResponseBean;
import com.model.TableClass;
import com.utils.DBUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 2hu0
 */
@Service
public class GenerateCodeService {
    Configuration cfg = null;
    {
        cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setTemplateLoader(new ClassTemplateLoader(GenerateCodeService.class,"/templates"));
        cfg.setDefaultEncoding("UTF-8");
    }
    public ResponseBean generateCode(List<TableClass> tableClasses, String realPath) {
        try {
            Template modelTemplate = cfg.getTemplate("Model.java.ftl");
            Template serviceTemplate = cfg.getTemplate("Service.java.ftl");
            Template mapperTemplate = cfg.getTemplate("Mapper.java.ftl");
            Template mapperXmlTemplate = cfg.getTemplate("Mapper.xml.ftl");
            Template controllerTemplate = cfg.getTemplate("Controller.java.ftl");
            Connection connection = DBUtils.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            for (TableClass tableClass : tableClasses) {
                ResultSet columns = databaseMetaData.getColumns(connection.getCatalog(), null, tableClass.getTableName(), null);
                ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(connection.getCatalog(), null, tableClass.getTableName());
                List<ColumnClass> columnClasses = new ArrayList<>();
                while (columns.next()) {
                    String column_name = columns.getString("COLUMN_NAME");
                    String type_name = columns.getString("TYPE_NAME");
                    String remarks = columns.getString("REMARKS");
                    ColumnClass columnClass = new ColumnClass();
                    columnClass.setColumnName(column_name);
                    columnClass.setRemark(remarks);
                    columnClass.setType(type_name);
                    columnClass.setPropertyName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,column_name));
                    primaryKeys.first();
                    while (primaryKeys.next()) {
                        String pkName = primaryKeys.getString("COLUMN_NAME");
                        if (column_name.equals(pkName)) {
                            columnClass.setPrimary(true);
                        }
                    }
                    columnClasses.add(columnClass);
                }
                tableClass.setColumns(columnClasses);
                String path = realPath + "/" + tableClass.getPackageName().replace(".","/");
                generate(modelTemplate,tableClass,path+"/model/");
                generate(serviceTemplate,tableClass,path+"/service/");
                generate(controllerTemplate,tableClass,path+"/controller/");
                generate(mapperTemplate,tableClass,path+"/mapper/");
                generate(mapperXmlTemplate,tableClass,path+"/mapper/");
            }
            return ResponseBean.ok("代码已经生成",realPath);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseBean.error("代码生成失败! ");
    }
    private void generate(Template template,TableClass tableClass,String path) throws IOException, TemplateException {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = path + "/" + tableClass.getModelName() +template.getName().replace(".ftl","").replace("Model","");
        System.out.println(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        template.process(tableClass,outputStreamWriter);
        fileOutputStream.close();
        outputStreamWriter.close();
    }
}
