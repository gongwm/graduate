var Line=(function(Snap){
	var idx=0,
		proto=Line.prototype;
	
	Line.lineEnd=null;
			
	Line._predefs=function(svg){
		var m=svg.paper.path("M2,2 L2,11 L10,6 L2,2")
			.attr({fill: 'black'});
		Line.lineEnd=m.marker(0,0,13,13,10,6);
	}
	
	function Line(fromBlock,toBlock){
		this._id=replace("l{1}",++idx);
		this._fromBlock=fromBlock;
		this._toBlock=toBlock;
		this._path=draw(fromBlock,toBlock);
		
		fromBlock.lines.push(this);
		toBlock.lines.push(this);
	}
	
	function draw(fromBlock,toBlock){
		var p=Snap.parse("<path></path>").select("path");
		var ps=resolvePathString(fromBlock,toBlock);
		p.attr({stroke:'black',d:ps,'marker-end':Line.lineEnd});
		return p;
	}
		
	function resolvePathString(b1,b2){
		var p1=b1._rightMid();
		var p2=b2._leftMid();
		return replace("M{1},{2}L{3},{4}",p1.x,p1.y,p2.x,p2.y);
	}
	
	function replace(str/*...*/){
		var args=arguments;
		return str.replace(/\{(\d+)\}/g, function(m,i){
			return args[i];
		});
	}
	
	proto.attachToSvg=function(svg){
		svg.append(this._path);
	}
	
	proto.detach=function(){
		this._path.remove();
		this._path=null;
		this._fromBlock=null;
		this._toBlock=null;
	}
	
	proto.redraw=function(){
		var ps=resolvePathString(this._fromBlock,this._toBlock);
		this._path.attr({d:ps});
	}
	
	proto.toModel=function(){
		return [this._fromBlock.id,this._toBlock.id];
	}
	
	return Line;
})(Snap);