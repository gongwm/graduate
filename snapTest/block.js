var Block=(function(root,Snap){
function Block(rect){
	this._rect=rect;
	this._paper=rect.paper;
	this._k=rect.data("k");
	this._t=rect.data("t");
}

var inertiaSvg='';
var inertia=Block.inertia=Snap.parse(inertiaSvg).select("g");
inertia.drag(function(){
	
});



var proto=Block.prototype,
	eve=['move','drag','select'];

proto._central=function(){
	var r=_xywh();
	return {x:r.x+r.w/2,y:r.y+r.h/2};
}

proto._leftMid=function(){
	var r=_xywh();
	return {x:r.x,y:r.y+r.height/2};
}

proto._rightMid=function(){
	var r=_xywh();
	return {x:r.x+r.width,y:r.y+r.height/2}
}

proto._topMid=function(){
	var r=_xywh();
	return {x:r.x+r.width/2,y:r.y};
}

proto._bottomMid=function(){
	var r=_xywh();
	return {x:r.x+r.width/2,y:r.y+r.height};
}

proto._xywh=function(){
return {x:+_rect.attr('x'),
        y:+_rect.attr('y'),
		width:+_rect.attr('width'),
		height:+_rect.attr('height')}
}

function changeFormula(){} // TO-DO

proto.set=function(k,t){
_k=k;
_t=t;
}

proto._attr=function(attrs){
	_rect.attr(attrs);
}

proto.dash=function(){
	_attr({'stroke-dasharray':'3,3'});
}

proto.solid=function(){
	_attr({'stroke-dasharray':null});
}

proto.setTK(t,k){
	_rect.data("k",k);
	_rect.data("t",t);
}

proto.toModel=function(){
	return {type:"inertia",k: _rect.data('k'),t: _rect.data('t')};
}

Block.wrap=function(model,rect){
	rect.data("k",model.k);
	rect.data("t",model.t);
	var res=new Block(rect);
	return res;
}


root.Block=Block;
return Block;	
})(window || this,Snap);

