var Block=(function(root,Snap,Line){
function Block(){
	this._svg=null;
	this._model=null;
	this.lines=[];
}

Block._={};

var proto=Block.prototype;

function hasParent(elem,id){
	var e=elem;
	do{
		if(e.hasAttribute("id")&&e.attributes["id"].value==id){
			return true;
		}
		e=e.parentElement;
	}while(e&&e.tagName.toLowerCase()!="body");
	return false;
}
Block._.hasParent=hasParent;

proto.addLine=function(line){
	this.lines.push(line);
}

proto.attachToSvg=function(svg){
	this._svg=svg;
	svg.append(this.block);
}

proto.attachToModel=function(model){
	this._model=model;
}

proto.detach=function(){
	this._svg=null;
	this._model=null;
	this.lines=[];
}
	
proto.moveMode=function(){
	var block=this.block,
		lines=this.lines,
		m=block.attr("transform").totalMatrix,
		m0=null;
		
	block.undrag();
	block.drag(function onmove(dx,dy,x,y,e){
		var trans= m.translate(m0.e+dx-m.e,m0.f+dy-m.f);
		block.transform(trans);
		for(var idx in lines){
			lines[idx].redraw();
		}
	},function onstart(x,y,e){
		m0=m.clone();
	});
}
	
proto.moveTo=function(x,y){
	var m=new Snap.Matrix;
	m.translate(x,y);
	this.block.transform(m.toTransformString());
}

function changeFormula(){
	// TODO
} 

Block.plugin=function(f){
	f(Block);
}

root.Block=Block;
return Block;
})(window || this,Snap,Line);


Block.plugin(function(Block){
function Inertia(){
	this.id="b"+(++_idx);
	this.block=Block.inertia.use().attr({id:this.id});
	
	this._k=1.0;
	this._t=1.0;
}

Inertia.prototype=new Block;
Inertia.prototype.constructor=Inertia;

var _idx=0,
	proto=Inertia.prototype,
	hasParent=Block._.hasParent;

Block.createInertia=function(){
	return new Inertia();
}

/* predefined blocks */
Block.inertia=null;

Block._predefs=function(svg){
	var block_defs=''+
		'<defs id="MathJax_SVG_glyphs">'+
		'  <path stroke-width="1" id="MJMAIN-31" d="M213 578L200 573Q186 568 160 563T102 556H83V602H102Q149 604 189 617T245 641T273 663Q275 666 285 666Q294 666 302 660V361L303 61Q310 54 315 52T339 48T401 46H427V0H416Q395 3 257 3Q121 3 100 0H88V46H114Q136 46 152 46T177 47T193 50T201 52T207 57T213 61V578Z"/>'+
		'  <path stroke-width="1" id="MJMAIN-2B" d="M56 237T56 250T70 270H369V420L370 570Q380 583 389 583Q402 583 409 568V270H707Q722 262 722 250T707 230H409V-68Q401 -82 391 -82H389H387Q375 -82 369 -68V230H70Q56 237 56 250Z"/>'+
		'  <path stroke-width="1" id="MJMATHI-78" d="M52 289Q59 331 106 386T222 442Q257 442 286 424T329 379Q371 442 430 442Q467 442 494 420T522 361Q522 332 508 314T481 292T458 288Q439 288 427 299T415 328Q415 374 465 391Q454 404 425 404Q412 404 406 402Q368 386 350 336Q290 115 290 78Q290 50 306 38T341 26Q378 26 414 59T463 140Q466 150 469 151T485 153H489Q504 153 504 145Q504 144 502 134Q486 77 440 33T333 -11Q263 -11 227 52Q186 -10 133 -10H127Q78 -10 57 16T35 71Q35 103 54 123T99 143Q142 143 142 101Q142 81 130 66T107 46T94 41L91 40Q91 39 97 36T113 29T132 26Q168 26 194 71Q203 87 217 139T245 247T261 313Q266 340 266 352Q266 380 251 392T217 404Q177 404 142 372T93 290Q91 281 88 280T72 278H58Q52 284 52 289Z"/>'+
		'</defs>';
	var paths=Snap.parse(block_defs).selectAll("path");
	paths.forEach(function(path){
		svg.append(path);
		path.toDefs();
	});

	var inertiaSvg=''+
		'<g>'+
		'  <rect x="0" y="0" width="38" height="32" style="" fill="#ffffff" stroke="#000000" stroke-width="2"/>'+
		'  <svg width="3.877ex" height="3.343ex" style="vertical-align: -1.116ex;" viewbox="0 -958.8 1669.2 1439.2" role="img" focusable="false" aria-hidden="true">'+
		'    <g stroke="currentColor" fill="currentColor" stroke-width="0" transform="matrix(1 0 0 -1 0 0)"> '+
		'      <g transform="translate(120,0)">'+
		'        <rect stroke="none" width="1429" height="60" x="0" y="220"/>'+
		'        <use transform="scale(0.707)" xlink:href="#MJMAIN-31" x="760" y="566"/>'+
		'        <g transform="translate(60,-372)">'+
		'          <use transform="scale(0.707)" xlink:href="#MJMAIN-31" x="0" y="0"/>'+
		'          <use transform="scale(0.707)" xlink:href="#MJMAIN-2B" x="500" y="0"/>'+
		'          <use transform="scale(0.707)" xlink:href="#MJMATHI-78" x="1279" y="0"/>'+
		'        </g>'+
		'      </g>'+
		'    </g>'+
		'    <desc>Created with Snap</desc>'+
		'    <defs/>'+
		'  </svg>'+
		'</g>',
		inertia=Snap.parse(inertiaSvg).select("g");
	svg.append(inertia);
	Block.inertia=inertia.toDefs();
}

proto.connectMode=function(){
	var center=this._central(),
		block=this.block,
		line=Snap.parse("<line></line>").select("line")
			.attr({stroke:'black','stroke-dasharray':'3,3',x1:center.x,y1:center.y}),
		svg=this._svg,
		_this=this;
	block.undrag();
	
	block.drag(function onmove(dx,dy,x,y,e){
		line.attr({x2:x,y2:y});
	},function onstart(x,y,e){
		svg.append(line);
		line.attr({x2:x,y2:y});
	},function onend(e){
		var fromBlock=_this.block,
			toElement=e.target,
			model=_this._model,
			id=null;
		line.remove();
		for(var compId in model.components){
			if(hasParent(toElement,compId)){
				id=compId;
				break;
			}
		}
		if(id!=null){
			model.addLine(_this.id,id);
		}
	});
}

proto._central=function(){
	var r=this._xywh();
	return {x:r.x+r.width/2,y:r.y+r.height/2};
}

proto._leftMid=function(){
	var r=this._xywh();
	return {x:r.x,y:r.y+r.height/2};
}

proto._rightMid=function(){
	var r=this._xywh();
	return {x:r.x+r.width,y:r.y+r.height/2}
}

proto._topMid=function(){
	var r=this._xywh();
	return {x:r.x+r.width/2,y:r.y};
}

proto._bottomMid=function(){
	var r=this._xywh();
	return {x:r.x+r.width/2,y:r.y+r.height};
}

proto._xywh=function(){
var _rect=Block.inertia.select("rect"),
    t=this.block.transform().totalMatrix;
return {x:t.e,
        y:t.f,
		width:+_rect.attr('width'),
		height:+_rect.attr('height')}
}

proto.set=function(k,t){
this._k=k;
this._t=t;
}

proto._attr=function(attrs){
	this._rect.attr(attrs);
}

proto.dash=function(){
	this._attr({'stroke-dasharray':'3,3'});
}

proto.solid=function(){
	this._attr({'stroke-dasharray':null,strokeWidth:1});
}

proto.selected=function(){
	this._attr({strokeWidth:3})
}

proto.setKT=function(k,t){
	this._t=t;
	this._k=k;
}

proto.toModel=function(){
	return {type:"inertia",k: this._k,t:this._t};
}

});











var Joint=(function(Block,Snap){
	function Joint(){
		this.block=Joint.joint.use();
	}

	Joint.joint=null;
	
	Joint._predefs=function(svg){
		var c=svg.paper.circle(15,15,15);
		c.attr({fill:'white',stroke:'black',strokeWidth:2});
		
		var sin=Snap.sin,
			cos=Snap.cos;

		var p1x=15-15*cos(45),
			p1y=15-15*sin(45),
			p2x=15+15*cos(45),
			p2y=15+15*sin(45);
		var l1=svg.paper.line(p1x,p1y,p2x,p2y);
		l1.attr({stroke:'black'});

		p1x=15+15*cos(45);
		p1y=15-15*sin(45);
		p2x=15-15*cos(45);
		p2y=15+15*sin(45);
		var l2=svg.paper.line(p1x,p1y,p2x,p2y);
		l2.attr({stroke:'black'});
		
		var g=svg.g(c,l1,l2);
		Joint.joint=g.toDefs();
	}
	
	Joint.prototype=new Block;
	Joint.constructor=Joint;

	var proto=Joint.prototype;
	
	return Joint;
})(Block,Snap);

