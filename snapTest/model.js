var Model=(function(){
	var toJson=JSON.stringify;
	
	function Model(){
		this.config={};
		this.components={};
		this.lines={};
		this.b_idx=0;
		this.l_idx=0;
	}
	
	var proto=Model.prototype;
	
	proto.addBlock=function(block){
		components[block.id]=block;
	}
	
	proto.addLine=function(line){
		lines[line.id]=line;
	}
	
	proto.valid=function(){
		// TO-DO
	}
	
	proto.toJsonModel=function(){
		var model={'config':config,'components':components,lines:'lines'};
		return toJson(model);
	}
	
	proto.toJsonPersist=function(svg){
		var ret={svg:svg.toString(),model:toJsonModel()};
		return ret;
	}
		
	return Model;
})();