var Block=(function(root,Snap){
function Block(rect){
	this._id=rect.attr("id");
	this._rect=rect;
	this._paper=rect.paper;
	this._k=rect.data("k");
	this._t=rect.data("t");
}

/* predefined blocks */
var inertiaSvg='<g transform="matrix(1,0,0,1,225,89)"><rect x="0" y="0" width="40" height="30" style="" fill="#ffffff" stroke="#000000"></rect><svg xmlns:xlink="http://www.w3.org/1999/xlink" width="3.877ex" height="3.343ex" style="vertical-align: -1.116ex;" viewBox="0 -958.8 1669.2 1439.2" role="img" focusable="false" aria-hidden="true"><g stroke="currentColor" fill="currentColor" stroke-width="0" transform="matrix(1 0 0 -1 0 0)"><g transform="translate(120,0)"><rect stroke="none" width="1429" height="60" x="0" y="220"></rect><use transform="scale(0.707)" xlink:href="#MJMAIN-31" x="760" y="566"></use><g transform="translate(60,-372)"><use transform="scale(0.707)" xlink:href="#MJMAIN-31" x="0" y="0"></use><use transform="scale(0.707)" xlink:href="#MJMAIN-2B" x="500" y="0"></use><use transform="scale(0.707)" xlink:href="#MJMATHI-78" x="1279" y="0"></use></g></g></g><desc>Created with Snap</desc><defs></defs></svg></g>',
    inertia=Block.inertia=Snap.parse(inertiaSvg).select("g");

inertia.data({k:1.0,t:1.0});	
	
inertia.drag(function(){
	
});

/* export predefined blocks */
Block.inertia=inertia;

Block.predefs=function(svg){
	var block_defs='<defs id="MathJax_SVG_glyphs"><path stroke-width="1" id="MJMAIN-31" d="M213 578L200 573Q186 568 160 563T102 556H83V602H102Q149 604 189 617T245 641T273 663Q275 666 285 666Q294 666 302 660V361L303 61Q310 54 315 52T339 48T401 46H427V0H416Q395 3 257 3Q121 3 100 0H88V46H114Q136 46 152 46T177 47T193 50T201 52T207 57T213 61V578Z"></path><path stroke-width="1" id="MJMAIN-2B" d="M56 237T56 250T70 270H369V420L370 570Q380 583 389 583Q402 583 409 568V270H707Q722 262 722 250T707 230H409V-68Q401 -82 391 -82H389H387Q375 -82 369 -68V230H70Q56 237 56 250Z"></path><path stroke-width="1" id="MJMATHI-78" d="M52 289Q59 331 106 386T222 442Q257 442 286 424T329 379Q371 442 430 442Q467 442 494 420T522 361Q522 332 508 314T481 292T458 288Q439 288 427 299T415 328Q415 374 465 391Q454 404 425 404Q412 404 406 402Q368 386 350 336Q290 115 290 78Q290 50 306 38T341 26Q378 26 414 59T463 140Q466 150 469 151T485 153H489Q504 153 504 145Q504 144 502 134Q486 77 440 33T333 -11Q263 -11 227 52Q186 -10 133 -10H127Q78 -10 57 16T35 71Q35 103 54 123T99 143Q142 143 142 101Q142 81 130 66T107 46T94 41L91 40Q91 39 97 36T113 29T132 26Q168 26 194 71Q203 87 217 139T245 247T261 313Q266 340 266 352Q266 380 251 392T217 404Q177 404 142 372T93 290Q91 281 88 280T72 278H58Q52 284 52 289Z"></path></defs>'
	svg.append(Snap.parse(block_defs));
}

var _idx=0;

Block.createInertia=function(){
	var iner=inertia.clone();
	iner.attr({id:"b"+(++_idx)});
	iner.data(inertia.data());
	return new Block(iner);
}

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

proto.setTK=function(t,k){
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

