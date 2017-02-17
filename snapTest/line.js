var Line=(function(){
	var toJson=JSON.stringify;
	
	function Line(fromId,toId){
		this.fromId=fromId;
		this.toId=toId;
	}
	
	var proto=Line.prototype;
	
	proto.update=function(model){
		
	}
	
	proto.toJson=function(){
		return toJson([fromId,toId]);
	}
	
	return Line;
})();