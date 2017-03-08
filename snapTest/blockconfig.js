var BlockConfig=(function(jQuery){
	function BlockConfig(){
		
	}
	
	var types={
			TEXT_TYPE:'text',
			INPUT_TYPE:'input'
			SELECT_TYPE:'select'
		},
		divhtml='<div id={1}>{2}</div>',
		labelhtml='<label>{1}</label>',
		texthtml='<input type="{1}" value="{2}"/>',
		selecthtml='<select required="true">{2}</select>',
		optionhtml='<option value="{1}">{2}</option>';
		
	function makeLabel(label){
		return format(labelhtml,label);
	}
	
	function makeText(value){
		return format(texthtml,'text',value);
	}
	
	function makeInput(value){
		return format(texthtml,'input',value);
	}
	
	function makeSelect(options){
		return format(selecthtml,options);
	}
	
	function makeOption(value,txt){
		return format(optionhtml,value,txt);
	}
	
	function makeDiv(id,content){
		return format(divhtml,id,content);
	}
	
	function pureText(config){
		var l=makeLabel(config.name);
		var t=makeText(config.value);
		return makeDiv(config.id,l+t);
	}
	
	function textInput(config){
		var l=makeLabel(config.name);
		var i=makeInput(config.value);
		return makeDiv(config.id,l+i);
	}
	
	function select(config){
		assert(names.length==values.length);
		var options='',
			names=config.name,
			values=config.value;
		for(var i in names){
			options+=makeOption(names[i],values[i]);
		}
		var s=makeSelect(selecthtml,options);
		return makeDiv(config.id,s);
	}
		
	function assert(bool){
		if(!bool){
			throw "assertion error";
		}
	}
	
	function format(str){
		var args=arguments;
		return str.replace(/\{(\d+)\}/g, function(m,i){
			return args[i];
		});
	}
	
	function config(id,name,value,type){ // object 'config'.
		assert(type in types);
		return {
			id:id,
			name:name,
			value:value,
			type:type
		};
	}
	
	var proto=BlockConfig.prototype;
	
	return BlockConfig;
})($);