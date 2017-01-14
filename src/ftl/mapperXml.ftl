<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${packageMapper}.${className}Mapper" >
  <resultMap id="BaseResultMap" type="${packageBean}.${className}" >
    <#list tableEntity.primaries as columnField>
    <id column="${columnField.column}" property="${columnField.field}" jdbcType="${columnField.jdbcType}" />
    </#list>
    <#list tableEntity.columnFields as columnField>
    <result column="${columnField.column}" property="${columnField.field}" jdbcType="${columnField.jdbcType}" />
    </#list>
  </resultMap>
  <sql id="Base_Column_List" >
    <#list tableEntity.primaries as columnField>${columnField.column},</#list><#list tableEntity.columnFields as columnField><#if columnField_index%8==0>${'\r\n'}    </#if><#if columnField_has_next>${columnField.column},</#if><#if !columnField_has_next>${columnField.column}</#if></#list>
  </sql>
  
  <!-- 根据对象主键查询 -->
  <select id="load" resultMap="BaseResultMap" >
    select 
    <include refid="Base_Column_List" />
    from ${tableEntity.tableName}
    where
  	<#list tableEntity.primaries as columnField>
  	<#if columnField_has_next>
    ${columnField.column} = ${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"} and
    </#if>
  	<#if !columnField_has_next>
    ${columnField.column} = ${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"} 
    </#if>
    </#list>
  </select>
  
  <!-- 根据条件分页查询 -->
  <select id="find${className}Page" resultMap="BaseResultMap" >
    select * from ${tableEntity.tableName} where 1 = 1
    <if test='pd.keyword != null and pd.keyword != ""'>
    	<!-- and 字段 like concat('%', ${r"#{pd.keyword},'%')"} -->
    </if>
  </select>
  
  <!-- 根据条件查询全部 -->
  <select id="find${className}" resultMap="BaseResultMap" >
    select * from ${tableEntity.tableName} where 1 = 1
    <if test='keyword != null and keyword != ""'>
    </if>
  </select>
  
  <!-- 根据对象主键删除 -->
  <delete id="delete" >
    delete from ${tableEntity.tableName}
    where
  	<#list tableEntity.primaries as columnField>
  	<#if columnField_has_next>
    ${columnField.column} = ${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"} and
    </#if>
  	<#if !columnField_has_next>
    ${columnField.column} = ${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"} 
    </#if>
    </#list>
  </delete>
  
  <!-- 插入对象全部属性的字段 -->
  <insert id="insert" parameterType="${packageBean}.${className}" >
    insert into ${tableEntity.tableName} (<include refid="Base_Column_List" />)
    values( 
  		<#list tableEntity.primaries as columnField>
    	${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"},
    	</#list>
  		<#list tableEntity.columnFields as columnField>
  		<#if columnField_has_next>
    	${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"},
    	</#if>
  		<#if !columnField_has_next>
    	${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"}
    	</#if>
    	</#list>
  	)
  </insert>
  
  <!-- 根据主键更新对象不为空属性的字段 -->
  <update id="update" parameterType="${packageBean}.${className}" >
    update ${tableEntity.tableName}
    <set>
    	<#list tableEntity.columnFields as columnField>
    	<#if columnField_has_next>
      	<if test="${columnField.field} != null" >
        	${columnField.column} = ${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"},
      	</if>
      	</#if>
    	<#if !columnField_has_next>
      	<if test="${columnField.field} != null" >
        	${columnField.column} = ${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"}
      	</if>
      	</#if>
      	</#list>
    </set>
    where
  	<#list tableEntity.primaries as columnField>
  	<#if columnField_has_next>
    ${columnField.column} = ${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"} and
    </#if>
  	<#if !columnField_has_next>
    ${columnField.column} = ${r"#{"}${columnField.field},jdbcType=${columnField.jdbcType}${r"}"} 
    </#if>
    </#list>
  </update>
</mapper>
