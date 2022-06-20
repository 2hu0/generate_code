package com.model;

import java.util.List;

/**
 * @author 2hu0
 */
public class TableClass {
    private String tableName;
    private String modelName;
    private String serviceName;
    private String mapperName;
    private String controllerName;
    private String packageName;
    private List<ColumnClass> columns;

    public TableClass() { }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setColumns(List<ColumnClass> columns) {
        this.columns = columns;
    }
}
