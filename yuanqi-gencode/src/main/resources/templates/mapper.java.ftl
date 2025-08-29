package ${package.Mapper};

<#list importMapperFrameworkPackages as pkg>
import ${pkg};
</#list>
<#if importMapperJavaPackages?size !=0>

  <#list importMapperJavaPackages as pkg>
import ${pkg};
   </#list>
</#if>
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${basePackage}.vo.${table.entityName}Vo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if mapperAnnotationClass??>
@${mapperAnnotationClass.simpleName}
</#if>
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}> {
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {
</#if>

    IPage<${table.entityName}Vo> selectPageVo(IPage<?> page, @Param("dto") PageDto dto);

<#list mapperMethodList as m>
    /**
     * generate by ${m.indexName}
     *
    <#list m.tableFieldList as f>
     * @param ${f.propertyName} ${f.comment}
    </#list>
     */
    ${m.method}
</#list>
}
