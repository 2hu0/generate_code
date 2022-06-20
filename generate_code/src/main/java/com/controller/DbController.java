package com.controller;

import com.google.common.base.CaseFormat;
import com.model.Db;
import com.model.ResponseBean;
import com.model.TableClass;
import com.utils.DBUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 2hu0
 */
@RestController
public class DbController {
    @PostMapping("/connect")
    public ResponseBean connect(@RequestBody Db db) {
        Connection connection = DBUtils.initDb(db);
        if (connection != null) {
            return ResponseBean.ok("数据库连接成功");
        }else {
            return ResponseBean.error("数据库连接失败");
        }
    }
 @PostMapping("/config")
    public ResponseBean config(@RequestBody Map<String, String> map) {
        String packageName = map.get("packageName");
        try {
            Connection connection = DBUtils.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, null);
            List<TableClass> tableClasses = new ArrayList<>();
            while (tables.next())  {
                TableClass tableClass = new TableClass();
                String table_name = tables.getString("TABLE_NAME");
                String modelName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,table_name);
                tableClass.setModelName(modelName);
                tableClass.setTableName(table_name);
                tableClass.setPackageName(packageName);
                tableClass.setControllerName(modelName+"Controller");
                tableClass.setServiceName(modelName + "Service");
                tableClass.setMapperName(modelName + "Mapper");
                tableClasses.add(tableClass);
            }
            return ResponseBean.ok("success",tableClasses);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseBean.error("false");
    }
}
