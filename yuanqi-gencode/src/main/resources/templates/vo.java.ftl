package ${basePackage}.vo;

<#list table.importPackages as pkg>
import ${pkg};
</#list>

import lombok.Data;

import java.io.Serializable;

/**
* ${table.comment!} VO
*/
@Data
public class ${table.entityName}Vo implements Serializable {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>

    /**
    * ${field.comment}
    */
    private ${field.propertyType} ${field.propertyName};
</#list>

}