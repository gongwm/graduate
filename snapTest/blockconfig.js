var BlockConfig=(function(jQuery){
	
	return Block.plugin(function(Block,Snap){

	function BlockConfig(id){
		this.id=id+DIV_POSTFIX;
		this.configs=[];
	}
	
	BlockConfig.configContainerId='config';
	
	var types=Block.configTypes;
		divhtml='<div id="{1}">{2}</div>',
		labelhtml='<label style="margin-right:10px">{1}</label>',
		texthtml='{1}',
		inputhtml='<input type="{1}" value="{2}" />',
		selecthtml='<select required="true">{1}</select>',
		optionhtml='<option value="{1}"{3}>{2}</option>',
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
		return format(inputhtml,'text',value);
	}
	
	function makeSelect(options){
		return format(selecthtml,options);
	}
	
	function makeOption(value,txt,selected){
		var attr='';
		if(selected){
			attr=' selected="selected"';
		}
		return format(optionhtml,value,txt,attr);
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
			value=config.value,
			data=config.data;
		
		var l=makeLabel(name);
		for(var i in data){
			var d=data[i];
			options+=makeOption(d,d,d==value);
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
	
	function resolveConfig(config){
		var id=config.id,
			selector=format('#{1} {2}',id,config.type);
			value=$(selector).val();
		config.value=value;
	}
	
	function update(configs){
		for(var i in configs){
			var config=configs[i];
			if(config.type!=types.TEXT_TYPE){
				resolveConfig(config);
			}
		}
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
		var configs=this.configs;
		if(configs){
			update(configs);
		}
		return configs;
	};
		
	blockproto.getConfig=methodNotImplemented; // return configs
	blockproto.updateConfig=function(){
		console.log('default update config called');
	}; // update by configs
	
	blockproto.showConfig=function(){
		var config=this.config=new BlockConfig(this.getId()),
			cs=this.getConfig(),
			containerId=BlockConfig.configContainerId;
		for(var k in cs){
			config.addConfig(cs[k]);
		}
		config.build(containerId);
	};
	
	blockproto.update=function(){
		var config=this.config,
			block=this;
		config.update();
		block.updateConfig();
	};
	
	
	blockproto.configMode=function(){
		var block=this.block,
			model=this._model,
			_this=this;
		block.undrag();
		block.unclick();
		block.click(function(){
			var lastComp=model.selectedComponent;
			model.selectedComponent=_this;
			lastComp&&lastComp.update();
			_this.showConfig();
		});
	};

	return BlockConfig;
	
	});
	
	
})($);