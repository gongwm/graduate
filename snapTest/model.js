var Model=(function(Block,Line){
	function Model(svg){
		this.svg=svg;
		this.config={type:'fixed',T:0.01,t:0.0,tt:10.0};
		this.components={};
		this.lines={};
		this.selectedComponent=null;
	}
	
	var proto=Model.prototype,
		options=Block.options,
		types=Block.types,
		toJson=JSON.stringify;
		
	function predef(svg){
		Block._predefs(svg);
		Line._predefs(svg);
	}
	Model.predef=predef;
	
	proto.exsitBlock=function(id){
		return this.components[id]!=null;
	};
	
	proto.addBlock=function(x,y,type){
		if(!type){
			type=types.INERTIA;
		}
		var block=Block.perform(options.CREATE,type,x,y),
			model=this,
			components=this.components;
		components[block.id]=block;
		block.attachToSvg(this.svg);
		block.moveTo(x,y);
		block.attachToModel(model);
		block.moveMode();
		block.removeMode();
		return block;
	};
	
	proto.removeBlock=function(id){
		var components=this.components,
			block=components[id],
			relatedLines=block.lines;
		block.detach();
		delete components[id];
		for(idx in relatedLines){
			var lineId=relatedLines[idx]._id;
			this.removeLine(lineId);
		}
	};
	
	proto.find=function(id){
		return model.components[id];
	};
	
	proto.addLine=function(fromId,toId){
		if(fromId==toId){
			return;
		}
		for(var key in this.lines){ // avoid repeat add
			var line=this.lines[key];
			if(line._fromBlock.id==fromId&&line._toBlock.id==toId){
				return;
			}
		}
		try{
			var line=new Line(this.components[fromId],this.components[toId]);
			
		}catch(e){
			alert(e.message);
			return;
		}
		this.lines[line._id]=line;
		line.attachToSvg(this.svg);
		line.removeMode();
		return line;
	};
	
	proto.connect=proto.addLine;
	
	proto.removeLine=function(id){
		var lines=this.lines,
			line=lines[id];
		line.detach();
		delete lines[id];
	};
	
	proto.changeMode=function(mode){
		var comps=this.components;
		for(key in comps){
			var comp=comps[key];
			comp[mode]();
		}
	};

	proto.connectMode=function(){
		this.changeMode('connectMode');
	};
	
	proto.moveMode=function(){
		this.changeMode('moveMode');
	};
	
	proto.configMode=function(){
		this.changeMode('configMode');
	};
	
	proto.valid=function(){
		// TO-DO
	};
	
	proto.toJsonModel=function(){
		var blocks={},
			components=this.components,
			lines=this.lines,
			lineModels={};
		for(var key in components){
			blocks[key]=components[key].toModel();
		}
		for(key in lines){
			lineModels[key]=lines[key].toModel();
		}

		var model={'config':this.config,'components':blocks,'lines':lineModels};
		return toJson(model);
	};
	
	proto.toJsonPersist=function(){
		var ret={svg:this.svg.toString(),model:toJsonModel()};
		return ret;
	};
		
	return Model;
})(Block,Line);