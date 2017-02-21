var Line=(function(Model,svg){
	var toJson=JSON.stringify;
	
	function Line(m1,m2){
		this._fromId=m1._id;
		this._toId=m2._id;
		this._line=draw(m1,m2);
	}
	
	var proto=Line.prototype;
	
	function draw(fromModel,toModel){
		var ltm=leftTopModel(fromModel,toModel);
		var rbm=rightBottomModel(fromModel,toModel);
		
		var p1=ltm._rightMid();
		var p2=rbm._leftMid();
		
		return svg.paper.line(p1.x,p1.y,p2.x,p2.y);
	}
	
	proto.redraw=function(){
		_line.attr({x1:p1.x,y1:p1.y,x2:p2:x,y2:p2.y});
	}
			
	function leftTopModel(m1,m2){
		
	}
	
	proto.toJson=function(){
		return toJson([fromId,toId]);
	}
	
	return Line;
})(Model,svg);