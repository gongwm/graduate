var BlockConfig=(function(jQuery){
	
	return Block.plugin(function(Block,Snap){

	function BlockConfig(id){
		this.id=id+DIV_POSTFIX;
		this.configs=[];
	}
	
	var types=Block.configTypes;
		divhtml='<div id="{1}">{2}</div>',
		labelhtml='<label style="margin-right:10px">{1}</label>',
		texthtml='{1}',
		inputhtml='<input type="{1}" value="{2}" />',
		selecthtml='<select required="true">{1}</select>',
		optionhtml='<option value="{1}">{2}</option>',
		DIV_POSTFIX='_config_div',
		proto=BlockConfig.prototype;
		blockproto=Block.prototype,
		methodNotImplemented=Block._.methodNotImplemented,
		createConfig=Block._.config,
		assert=Block._.assert,
		contains=Block._.contains,
		format=Block._.format;
	
	BlockConfig.types=types;
		
	function makeLabel(label){
		return format(labelhtml,label);
	}
	
	function makeText(value){
		return format(texthtml,value);
	}
	
	function makeInput(value){
		return format(inputhtml,'input',value);
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
	
	// divs
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
		var options='',
			name=config.name,
			values=config.value;
		var l=makeLabel(name);
		for(var i in values){
			options+=makeOption(values[i],values[i]);
		}
		var s=makeSelect(options);
		return makeDiv(config.id,l+s);
	}
	
	function make(config){
		var type=config.type,
			result='';
		assert(contains(types,type),'no such type');
		switch(type){
			case types.TEXT_TYPE:
				fun=pureText;
				break;
			case types.INPUT_TYPE:
				fun=textInput;
				break;
			case types.SELECT_TYPE:
				fun=select;
				break;
			default:
				throw 'impossble';
		}
		return fun(config);
	}
	
	
	
	proto.makeConfigDiv=function(){
		var configs=this.configs,
			result='',
			id=this.id;
		for(var i in configs){
			result+=make(configs[i]);
		}
		return makeDiv(id,result);
	};
	
	proto.addConfig=function(config){
		var configs=this.configs;
		configs.push(config);
		return this;
	};
	
	proto.appendTo=function(id){
		var container=$('#'+id);
		var configDiv=this.makeConfigDiv();
		container.empty();
		container.append(configDiv);
		return this;
	};
	proto.build=proto.appendTo; // alias

	proto.update=function(){
		var divId=this.id,
			configs=this.configs;
		for(var i in configs){
			var config=configs[i],
				id=config.id,
				selector=format("#{1} {2}",divId,id);
			if(config.type==types.INPUT_TYPE||config.type==types.SELECT_TYPE){
				config.value=$(selector).val();
			}
		}
	}
	
	
	blockproto.getConfig=methodNotImplemented; // return configs
	blockproto.updateConfig=methodNotImplemented; // update by configs
	
	blockproto.showConfig=function(containerId){
		var config=this.config=new BlockConfig(this.getId());
		var cs=this.getConfig();
		for(var k in cs){
			var c=cs[k];
			config.addConfig(c);
		}
		config.build(containerId);
	}

	return BlockConfig;
		
		
	});
	
	
})($);