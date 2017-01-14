package ${packageBean};

${tableEntity.importPackage}
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @desc ${objectName}实体类
 * @author zong
 * @date ${nowDate?string("yyyy年MM月dd日")}
 */
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ${className} implements Serializable {
<#list tableEntity.primaries as columnField>	
	private ${columnField.type} ${columnField.field};
</#list>	
<#list tableEntity.columnFields as columnField>
	<#if columnField.type=='Date'>
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	</#if>
	private ${columnField.type} ${columnField.field};
</#list>
	
	public ${className}(){
	}
	
<#list tableEntity.primaries as columnField>
	public ${columnField.type} get${columnField.fieldUpper}(){
		return ${columnField.field};
	}
	
	public void set${columnField.fieldUpper}(${columnField.type} ${columnField.field}){
		this.${columnField.field} = ${columnField.field};
	}
	
</#list>
<#list tableEntity.columnFields as columnField>
	public ${columnField.type} get${columnField.fieldUpper}(){
		return ${columnField.field};
	}
	
	public void set${columnField.fieldUpper}(${columnField.type} ${columnField.field}){
		this.${columnField.field} = ${columnField.field};
	}
	
</#list>
}
