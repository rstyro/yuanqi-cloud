package ${basePackage}.dto;

<#list table.importPackages as pkg>
import ${pkg};
</#list>
import lombok.Builder;
import lombok.Data;

/**
* ${table.comment!} 参数DTO
*/
@Builder
@Data
public class ${table.entityName}Dto  {
    <#-- ----------  BEGIN 字段循环遍历  ---------->
    <#list table.fields as field>

        /**
        * ${field.comment}

        */
        private ${field.propertyType} ${field.propertyName};
    </#list>
}