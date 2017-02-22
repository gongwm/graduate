var Model=(function(Block,Line){
	var toJson=JSON.stringify;
	
	function Model(svg){
		this.svg=svg;
		this.config={};
		this.components={};
		this.lines={};
		this.b_idx=0;
		this.l_idx=0;
		Block._predefs(svg);
	}
	
	var proto=Model.prototype;
	
	proto.exsitBlock=function(id){
		return this.components[id]!=null;
	}
	
	proto.addBlock=function(x,y){
		var block=Block.createInertia();
		this.components[block.id]=block;
		block.attachToSvg(this.svg);
		block.moveTo(x,y);
		var model=this;
		block.attachToModel(model);
		return block;
	}
	
	proto.removeBlock=function(id){
		var block=components[id],
			relatedLines=relatedLinesOf(block);
		block.detach();
		components[id]=null;
		for(idx in relatedLines){
			var lineId=relatedLines[idx]._id;
			this.removeLine(lineId);
		}
	}
	
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
	
	proto.addLine=function(fromId,toId){
		var line=new Line(this.components[fromId],this.components[toId]);
		this.lines[line.id]=line;
		line.attachToSvg(this.svg);
		return line;
	}
	
	proto.removeLine=function(id){
		var line=this.lines[id];
		line.detach();
	}
	
	proto.valid=function(){
		// TO-DO
	}
	
	proto.toJsonModel=function(){
		var model={'config':config,'components':components,lines:'lines'};
		return toJson(model);
	}
	
	proto.toJsonPersist=function(){
		var ret={svg:this.svg.toString(),model:toJsonModel()};
		return ret;
	}
		
	return Model;
})(Block,Line);