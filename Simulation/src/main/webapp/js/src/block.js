var Block=(function(root,Snap,Line){
function Block(){
	this._svg=null;
	this._model=null;
	this.lines=[];
	this.config=null;
}

Block._={};

var proto=Block.prototype,
	allTypes=['Inertia','Joint','Step','Scope','Homopoly','Amplifier','Integrator','Homopoly1'],
	options={PREDEF:'_predef',CREATE:'create'},
	types={INERTIA:'Inertia',JOINT:'Joint',STEP:'Step',SCOPE:'Scope',HOMOPOLY:'Homopoly',AMPLIFIER:'Amplifier',INTEGRATOR:'Integrator',
		HOMOPOLY1:'Homopoly1'},
	_=Block._,
	configTypes={
		TEXT_TYPE:'text',
		INPUT_TYPE:'input',
		SELECT_TYPE:'select'
	};

Block.options=options;
Block.types=types;
Block.configTypes=configTypes;

function performOption(opt,type,args){
	if(!type){
		type="";
	}
	var m=Block[opt+type];
	if(!m){
		throw new Error('function Block.'+prop+' undefined');
	}
	return m(args);
}

Block.perform=performOption;

function config(id,name,value,type,data){ // object 'config'.
	assert(contains(configTypes,type),'no such type');
	return {
		id:id,
		name:name,
		value:value,
		type:type,
		data:data||{}
	};
}
_.config=config;

function assert(bool,msg){
	if(!bool){
		if(msg){
			console.log(msg);
		}
		throw new Error("assertion error");
	}
}
_.assert=assert;

function contains(array,item){
	if(!item){
		return false;
	}
	for(var i in array){
		if(array[i]==item){
			return true;
		}
	}
	return false;
}
_.contains=contains;

function format(str){
	var args=arguments;
	return str.replace(/\{(\d+)\}/g, function(m,i){
		return args[i];
	});
}
_.format=format;
	
function throwNotImplementedError(){
	throw new Error("should be implemented by sub-class");
}

function throwOperationNotSupportedError(){
	throw new Error("operation not supported");
}

_.methodNotImplemented=throwNotImplementedError;

_.operationNotSupported=throwOperationNotSupportedError;

Block._predefs=function(svg){
	allTypes.forEach(function(type){
		performOption(options.PREDEF,type,svg);
	});
};

proto._redrawLines=function(){
	var lines=this.lines;
	for(var idx in lines){
		lines[idx].redraw();
	}
};

proto.addLine=function(line){
	this.lines.push(line);
};

proto.removeLine=function(line){
	var lines=this.lines,
		idx=lines.indexOf(line);
	if(idx!=-1){
		return lines.splice(idx,1);
	}
	return null;
};

proto.attachToSvg=function(svg){
	this._svg=svg;
	svg.append(this.block);
};

proto.attachToModel=function(model){
	this._model=model;
};

proto.detach=function(){
	this._svg=null;
	this._model=null;
	this.lines=[];
	this.block.remove();
};

proto.removeMode=function(){
	var block=this.block,
		id=this.id,
		model=this._model;
	block.dblclick(function(){
		model.removeBlock(id);
	});
};

proto.moveMode=function(){
	var block=this.block,
		m=null,
		m0=null,
		_this=this;
		
	block.undrag();
	block.drag(function onmove(dx,dy,x,y,e){
		m.translate(m0.e+dx-m.e,m0.f+dy-m.f);
		block.transform(m);
		_this._redrawLines();
	},function onstart(x,y,e){
		m=block.transform().totalMatrix;
		m0=m.clone();
	},function onend(e){
		m=block.transform().totalMatrix;
	});
	return this;
};

proto.connectMode=function(){
	var center=this.basePoint(),
		block=this.block,
		line=Snap.parse("<line></line>").select("line")
			.attr({stroke:'black','stroke-dasharray':'3,3',x1:center.x,y1:center.y}),
		svg=this._svg,
		container=svg.node.parentElement,
		_this=this;
	block.undrag();
	
	block.drag(function onmove(dx,dy,x,y,e){
		line.attr({x2:x-container.offsetLeft,y2:y-container.offsetTop});
	},function onstart(x,y,e){
		svg.append(line);
		line.attr({x2:x,y2:y});
	},function onend(e){
		var fromBlock=_this.block,
			toElement=e.target,
			model=_this._model,
			id=toElement.hasAttribute('id')?toElement.attributes['id'].value:null;
		line.remove();
		if(id in model.components){
			var l=model.addLine(_this.id,id);
		}
	});
	return this;
};
	
proto.moveTo=function(x,y){
	var m=new Snap.Matrix,
		bp=this.basePoint();
	m.translate(x-bp.x,y-bp.y);
	this.block.transform(m.toTransformString());
};

proto.flip=function(){
	var block=this.block,
		m=block.transform().totalMatrix,
		bp=this.basePoint();
	m.rotate(180,bp.x,bp.y);
	block.transform(m);
	this._redrawLines();
};

proto.getId=function(){
	var id=this.id;
	assert(id);
	return id;
};

proto.basePoint=throwNotImplementedError;

proto.lineAdded=function(line){
	// invoked when a line is connected to a block.
};

proto.inPoint=throwNotImplementedError;
proto.outPoint=throwNotImplementedError;
proto.toModel=throwNotImplementedError;

Block.plugin=function(f){
	return f(Block,Snap);
};

root.Block=Block;
return Block;
})(window || this,Snap,Line);


var RectangleBase=Block.plugin(function(Block,Snap){
	function RectangleBase(){}
	
	RectangleBase.prototype=new Block;
	RectangleBase.prototype.constructor=RectangleBase;
	
	var proto=RectangleBase.prototype;
	
proto._xywh=function(){
var _rect=this._rect,
    t=this.block.transform().totalMatrix;
return {x:t.e,
        y:t.f,
		width:+_rect.attr('width'),
		height:+_rect.attr('height')}
};
	
proto._central=function(){
	var r=this._xywh();
	return {x:r.x+r.width/2,y:r.y+r.height/2};
};

proto._leftMid=function(){
	var r=this._xywh();
	return {x:r.x,y:r.y+r.height/2};
};

proto._rightMid=function(){
	var r=this._xywh();
	return {x:r.x+r.width,y:r.y+r.height/2}
};

proto._topMid=function(){
	var r=this._xywh();
	return {x:r.x+r.width/2,y:r.y};
};

proto._bottomMid=function(){
	var r=this._xywh();
	return {x:r.x+r.width/2,y:r.y+r.height};
};

proto.basePoint=proto._central;
proto.inPoint=proto._leftMid;
proto.outPoint=proto._rightMid;

return RectangleBase;
	
});

var Inertia=Block.plugin(function(Block,Snap){
function Inertia(){
	this.id="b"+(++_idx);
	this.block=Block.inertia.use().attr({id:this.id});
	this._rect=Block.inertia.select("rect");
	
	this._k=1.0;
	this._t=1.0;
	
	this.type='inertia';
	this.lines=[];
}

Inertia.prototype=new RectangleBase;
Inertia.prototype.constructor=Inertia;

var _idx=0,
	proto=Inertia.prototype,
	config=Block._.config;

Block.createInertia=function(){
	return new Inertia;
};

Block.inertia=null;

Block._predefInertia=function(svg){
	var block_defs=''+
		'<defs>'+
		'  <path stroke-width="1" id="inertia-thi-6B" d="M121 647Q121 657 125 670T137 683Q138 683 209 688T282 694Q294 694 294 686Q294 679 244 477Q194 279 194 272Q213 282 223 291Q247 309 292 354T362 415Q402 442 438 442Q468 442 485 423T503 369Q503 344 496 327T477 302T456 291T438 288Q418 288 406 299T394 328Q394 353 410 369T442 390L458 393Q446 405 434 405H430Q398 402 367 380T294 316T228 255Q230 254 243 252T267 246T293 238T320 224T342 206T359 180T365 147Q365 130 360 106T354 66Q354 26 381 26Q429 26 459 145Q461 153 479 153H483Q499 153 499 144Q499 139 496 130Q455 -11 378 -11Q333 -11 305 15T277 90Q277 108 280 121T283 145Q283 167 269 183T234 206T200 217T182 220H180Q168 178 159 139T145 81T136 44T129 20T122 7T111 -2Q98 -11 83 -11Q66 -11 57 -1T48 16Q48 26 85 176T158 471L195 616Q196 629 188 632T149 637H144Q134 637 131 637T124 640T121 647Z"></path>'+
		'  <path stroke-width="1" id="inertia-in-31" d="M213 578L200 573Q186 568 160 563T102 556H83V602H102Q149 604 189 617T245 641T273 663Q275 666 285 666Q294 666 302 660V361L303 61Q310 54 315 52T339 48T401 46H427V0H416Q395 3 257 3Q121 3 100 0H88V46H114Q136 46 152 46T177 47T193 50T201 52T207 57T213 61V578Z"></path>'+
		'  <path stroke-width="1" id="inertia-in-2B" d="M56 237T56 250T70 270H369V420L370 570Q380 583 389 583Q402 583 409 568V270H707Q722 262 722 250T707 230H409V-68Q401 -82 391 -82H389H387Q375 -82 369 -68V230H70Q56 237 56 250Z"></path>'+
		'  <path stroke-width="1" id="inertia-thi-54" d="M40 437Q21 437 21 445Q21 450 37 501T71 602L88 651Q93 669 101 677H569H659Q691 677 697 676T704 667Q704 661 687 553T668 444Q668 437 649 437Q640 437 637 437T631 442L629 445Q629 451 635 490T641 551Q641 586 628 604T573 629Q568 630 515 631Q469 631 457 630T439 622Q438 621 368 343T298 60Q298 48 386 46Q418 46 427 45T436 36Q436 31 433 22Q429 4 424 1L422 0Q419 0 415 0Q410 0 363 1T228 2Q99 2 64 0H49Q43 6 43 9T45 27Q49 40 55 46H83H94Q174 46 189 55Q190 56 191 56Q196 59 201 76T241 233Q258 301 269 344Q339 619 339 625Q339 630 310 630H279Q212 630 191 624Q146 614 121 583T67 467Q60 445 57 441T43 437H40Z"></path>'+
		'  <path stroke-width="1" id="inertia-thi-73" d="M131 289Q131 321 147 354T203 415T300 442Q362 442 390 415T419 355Q419 323 402 308T364 292Q351 292 340 300T328 326Q328 342 337 354T354 372T367 378Q368 378 368 379Q368 382 361 388T336 399T297 405Q249 405 227 379T204 326Q204 301 223 291T278 274T330 259Q396 230 396 163Q396 135 385 107T352 51T289 7T195 -10Q118 -10 86 19T53 87Q53 126 74 143T118 160Q133 160 146 151T160 120Q160 94 142 76T111 58Q109 57 108 57T107 55Q108 52 115 47T146 34T201 27Q237 27 263 38T301 66T318 97T323 122Q323 150 302 164T254 181T195 196T148 231Q131 256 131 289Z"></path>'+
		'</defs>';
	var paths=Snap.parse(block_defs).selectAll("path");
	paths.forEach(function(path){
		svg.append(path);
		path.toDefs();
	});

	var inertiaSvg=''+
		'<g>'+
		'  <rect x="0" y="0" width="47" height="35" style="" fill="#ffffff" stroke="#000000" stroke-width="2"/>'+
		'  <svg transform="scale(0.65) translate(3,3)" width="7.566ex" height="5.337ex" style="vertical-align: -1.974ex;" viewbox="0 -1448.3 3257.4 2298.1" role="img" focusable="false" aria-hidden="true">'+
		'    <g stroke="currentColor" fill="currentColor" stroke-width="0" transform="matrix(1 0 0 -1 0 0)">'+
		'      <g transform="translate(120,0)">'+
		'        <rect stroke="none" width="3017" height="60" x="0" y="220"></rect>'+
		'        <use xlink:href="#inertia-thi-6B" x="1247" y="676"></use>'+
		'        <g transform="translate(60,-686)">'+
		'          <use xlink:href="#inertia-in-31" x="0" y="0"></use>'+
		'          <use xlink:href="#inertia-in-2B" x="722" y="0"></use>'+
		'          <use xlink:href="#inertia-thi-54" x="1723" y="0"></use>'+
		'          <use xlink:href="#inertia-thi-73" x="2427" y="0"></use>'+
		'        </g>'+
		'      </g>'+
		'    </g>'+
		'  </svg>'+
		'</g>',
		inertia=Snap.parse(inertiaSvg).select("g");
	svg.append(inertia);
	Block.inertia=inertia.toDefs();
};

proto.set=function(k,t){
this._k=k;
this._t=t;
};

proto._attr=function(attrs){
	this._rect.attr(attrs);
};

proto.dash=function(){
	this._attr({'stroke-dasharray':'3,3'});
};

proto.solid=function(){
	this._attr({'stroke-dasharray':null,strokeWidth:1});
};

proto.selected=function(){
	this._attr({strokeWidth:3})
};

proto.setKT=function(k,t){
	this._t=t;
	this._k=k;
};

proto.toModel=function(){
	return {type:this.type,k: this._k,t:this._t};
};


proto.getConfig=function(){
	// config: id, name, value, type
	var configs=[];
	configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));
	configs.push(config('_k','K',this._k,configTypes.INPUT_TYPE));
	configs.push(config('_t','T',this._t,configTypes.INPUT_TYPE));
	return configs;
};

proto.updateConfig=function(){
	var configs=this.config.configs;
	this._k=configs[1].value;
	this._t=configs[2].value;
};

return Inertia;
});

var Joint=Block.plugin(function(Block,Snap){
	function Joint(){
		this.id='j'+(++_idx);
		this.block=Block.joint.use().attr({id:this.id});
		this.lineMode={};
		this.type='joint';
		this.lines=[];
	}

	Block.joint=null;

	var _idx=0;
		RADIUS=15,
		CENTER_X=15,
		CENTER_Y=15,
		LINE_MODE_PLUS='+',
		LINE_MODE_MINUS='-',
		config=Block._.config,
		configTypes=Block.configTypes;
	
	Block._predefJoint=function(svg){
		var cx=CENTER_X,
			cy=CENTER_Y,
			r=RADIUS,
			c=svg.paper.circle(cx,cy,r)
			   .attr({fill:'white',stroke:'black',strokeWidth:2}),
			sin=Snap.sin,
			cos=Snap.cos,
			p1x=cx-r*cos(45),
			p1y=cy-r*sin(45),
			p2x=cx+r*cos(45),
			p2y=cy+r*sin(45);
			
		var l1=svg.paper.line(p1x,p1y,p2x,p2y);
		l1.attr({stroke:'black'});

		p1x=cx+r*cos(45);
		p1y=cy-r*sin(45);
		p2x=cx-r*cos(45);
		p2y=cy+r*sin(45);
		var l2=svg.paper.line(p1x,p1y,p2x,p2y);
		l2.attr({stroke:'black'});
		
		var g=svg.g(c,l1,l2);
		Block.joint=g.toDefs();
	};

	Joint.prototype=new Block;
	Joint.prototype.constructor=Joint;
	
	Block.createJoint=function(cx,cy){
		var j=new Joint;
		j.moveTo(cx,cy);
		return j;
	};

	var proto=Joint.prototype;

	proto.center=function(){
		var m=this.block.transform().localMatrix,
			x=CENTER_X+m.e,
			y=CENTER_Y+m.f;
		return {x:x,y:y};
	};
	
	proto.leftPoint=function(){
		var m=this.block.transform().localMatrix,
			p=this.center();
		return {x:p.x-RADIUS,y:p.y};
	};
	
	proto.rightPoint=function(){
		var m=this.block.transform().localMatrix,
			p=this.center();
		return {x:p.x+RADIUS,y:p.y};
	};
	
	proto.basePoint=proto.center;
	proto.inPoint=proto.leftPoint;
	proto.outPoint=proto.rightPoint;
	
	proto.lineAdded=function(line){
		this.lineMode[line._id]=LINE_MODE_PLUS;
	};
	
	proto.toModel=function(){
		var lines={},
			lineMode=this.lineMode;
		for(var prop in lineMode){
			lines[prop]=lineMode[prop];
		}
		return {type:this.type,lines:lines};
	};
	
	proto.getConfig=function(){
		// config: id, name, value, type, data
		var configs=[],
			lineMode=this.lineMode,
			optionValues=[LINE_MODE_PLUS,LINE_MODE_MINUS];
		configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));
		for(var i in lineMode){
			var mode=lineMode[i];
			configs.push(config(i,'line '+i,mode,configTypes.SELECT_TYPE,optionValues));
		}
		return configs;
	};
	
	proto.updateConfig=function(){
		var configs=this.config.configs.slice(1),
			lineMode=this.lineMode;
		for(var i in configs){
			var config=configs[i];
			lineMode[config.id]=config.value;
		}
	};

	return Joint;
});

var Step=Block.plugin(function(Block,Snap){
function Step(){
	this.id="s"+(++_idx);
	this.type="step";
	this.block=Block.step.use().attr({id:this.id});
	this._rect=Block.step.select("rect");
	this.lines=[];
}

Step.prototype=new RectangleBase;
Step.prototype.constructor=Step;

var proto=Step.prototype,
	_idx=0,
	operationNotSupported=Block._.operationNotSupported,
	config=Block._.config,
	configTypes=Block.configTypes;

Block.step=null;

Block._predefStep=function(svg){
	var c1=svg.paper.rect(0,0,26,30).attr({stroke:'black',strokeWidth:2,fill:'white'});
	var p1=svg.paper.path("M2,25 H13 V5 H24").attr({fill:'none',stroke:'black'});
	
	var g=svg.g(c1,p1);
	Block.step=g.toDefs();
};

Block.createStep=function(x,y){
	var s=new Step;
	s.moveTo(x,y);
	return s;
};

proto.toModel=function(){
	return {type:this.type};
};

proto.inPoint=operationNotSupported;

proto.getConfig=function(){
	// config: id, name, value, type
	var configs=[];
	configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));
	return configs;
};

return Step;
});


var Scope=Block.plugin(function(Block,Snap){
function Scope(){
	this.id="sc"+(++_idx);
	this.type="scope";
	this.block=Block.scope.use().attr({id:this.id});
	this._rect=Block.scope.select("rect");
	this.lines=[];
}

Scope.prototype=new RectangleBase;
Scope.prototype.constructor=Scope;

var proto=Scope.prototype,
	_idx=0,
	operationNotSupported=Block._.operationNotSupported,
	config=Block._.config,
	configTypes=Block.configTypes;

Block.Scope=null;

Block._predefScope=function(svg){
	var c1=svg.paper.rect(0,0,26,30).attr({stroke:'black',strokeWidth:2,fill:'white'});
	var p1=svg.paper.rect(5,3,16,15).attr({stroke:'black',strokeWidth:2,fill:'white'});
	
	var g=svg.g(c1,p1);
	Block.scope=g.toDefs();
};

Block.createScope=function(x,y){
	var s=new Scope;
	s.moveTo(x,y);
	return s;
};

proto.toModel=function(){
	return {type:this.type};
};

proto.outPoint=operationNotSupported;

proto.getConfig=function(){
	// config: id, name, value, type
	var configs=[];
	configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));
	return configs;
};

return Scope;	
});


