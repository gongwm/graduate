var Model=(function(Block,Line){
	function Model(svg){
		this.svg=svg;
		this.config={type:'fixed',T:0.01,t:0.0,tt:10.0};
		this.components={};
		this.lines={};
		this.b_idx=0;
		this.l_idx=0;
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
		var block=Block.perform(options.CREATE,type,x,y);
		this.components[block.id]=block;
		block.attachToSvg(this.svg);
		block.moveTo(x,y);
		var model=this;
		block.attachToModel(model);
		block.moveMode();
		return block;
	};
	
	proto.removeBlock=function(id){
		var block=components[id],
			relatedLines=relatedLinesOf(block);
		block.detach();
		components[id]=null;
		for(idx in relatedLines){
			var lineId=relatedLines[idx]._id;
			this.removeLine(lineId);
		}
	};
	
	function relatedLinesOf(block){
		var lines=[];
		for(id in lines){
			var line=lines[id];
			if(line._fromBlock==block||line._toBlock==block){
				lines.push(line);
			}
		}
		return lines;
	}
	
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
		return line;
	};
	
	proto.connect=proto.addLine;
	
	proto.removeLine=function(id){
		var line=this.lines[id];
		line.detach();
	};
	
	proto.connectMode=function(){
		var comps=this.components;
		for(key in comps){
			var comp=comps[key];
			comp.connectMode();
		}
	};
	
	proto.moveMode=function(){
		var comps=this.components;
		for(key in comps){
			var comp=comps[key];
			comp.moveMode();
		}
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