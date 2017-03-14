var Block=(function(root,Snap,Line){function Block(){this._svg=null;this._model=null;this.lines=[];this.config=null;}Block._={};var proto=Block.prototype,allTypes=['Inertia','Joint','Step','Scope'],options={PREDEF:'_predef',CREATE:'create'},types={INERTIA:'Inertia',JOINT:'Joint',STEP:'Step',SCOPE:'Scope'},_=Block._,configTypes={TEXT_TYPE:'text',INPUT_TYPE:'input',SELECT_TYPE:'select'};Block.options=options;Block.types=types;Block.configTypes=configTypes;function performOption(opt,type,args){if(!type){type="";}var m=Block[opt+type];if(!m){throw new Error('function Block.'+prop+' undefined');}return m(args);}Block.perform=performOption;function config(id,name,value,type,data){ assert(contains(configTypes,type),'no such type');return {id:id,name:name,value:value,type:type,data:data||{}};}_.config=config;function assert(bool,msg){if(!bool){if(msg){console.log(msg);}throw new Error("assertion error");}}_.assert=assert;function contains(array,item){if(!item){return false;}for(var i in array){if(array[i]==item){return true;}}return false;}_.contains=contains;function format(str){var args=arguments;return str.replace(/\{(\d+)\}/g, function(m,i){return args[i];});}_.format=format;function throwNotImplementedError(){throw new Error("should be implemented by sub-class");}function throwOperationNotSupportedError(){throw new Error("operation not supported");}_.methodNotImplemented=throwNotImplementedError;_.operationNotSupported=throwOperationNotSupportedError;Block._predefs=function(svg){allTypes.forEach(function(type){performOption(options.PREDEF,type,svg);})};proto._redrawLines=function(){var lines=this.lines;for(var idx in lines){lines[idx].redraw();}};proto.addLine=function(line){this.lines.push(line);};proto.removeLine=function(line){var lines=this.lines,idx=lines.indexOf(line);if(idx!=-1){return lines.splice(idx,1);}return null;};proto.attachToSvg=function(svg){this._svg=svg;svg.append(this.block);};proto.attachToModel=function(model){this._model=model;};proto.detach=function(){this._svg=null;this._model=null;this.lines=[];this.block.remove();};proto.removeMode=function(){var block=this.block,id=this.id,model=this._model;block.dblclick(function(){model.removeBlock(id);});};proto.moveMode=function(){var block=this.block,lines=this.lines,m=null,m0=null,_this=this;block.undrag();block.drag(function onmove(dx,dy,x,y,e){m.translate(m0.e+dx-m.e,m0.f+dy-m.f);block.transform(m);_this._redrawLines();},function onstart(x,y,e){m=block.transform().totalMatrix;m0=m.clone();},function onend(e){m=block.transform().totalMatrix;});return this;};proto.connectMode=function(){var center=this.basePoint(),block=this.block,line=Snap.parse("<line></line>").select("line").attr({stroke:'black','stroke-dasharray':'3,3',x1:center.x,y1:center.y}),svg=this._svg,container=svg.node.parentElement,_this=this;block.undrag();block.drag(function onmove(dx,dy,x,y,e){line.attr({x2:x-container.offsetLeft,y2:y-container.offsetTop});},function onstart(x,y,e){svg.append(line);line.attr({x2:x,y2:y});},function onend(e){var fromBlock=_this.block,toElement=e.target,model=_this._model,id=toElement.hasAttribute('id')?toElement.attributes['id'].value:null;line.remove();if(id in model.components){var l=model.addLine(_this.id,id);}});return this;};proto.moveTo=function(x,y){var m=new Snap.Matrix,bp=this.basePoint();m.translate(x-bp.x,y-bp.y);this.block.transform(m.toTransformString());};proto.flip=function(){var block=this.block,m=block.transform().totalMatrix,bp=this.basePoint();m.rotate(180,bp.x,bp.y);block.transform(m);this._redrawLines();};proto.getId=function(){var id=this.id;assert(id);return id;};proto.basePoint=throwNotImplementedError;proto.lineAdded=function(line){};proto.inPoint=throwNotImplementedError;proto.outPoint=throwNotImplementedError;proto.toModel=throwNotImplementedError;Block.plugin=function(f){return f(Block,Snap);};root.Block=Block;return Block;})(window || this,Snap,Line);var RectangleBase=Block.plugin(function(Block,Snap){function RectangleBase(){}RectangleBase.prototype=new Block;RectangleBase.prototype.constructor=RectangleBase;var proto=RectangleBase.prototype;proto._xywh=function(){var _rect=this._rect,t=this.block.transform().totalMatrix;return {x:t.e,y:t.f,width:+_rect.attr('width'),height:+_rect.attr('height')}};proto._central=function(){var r=this._xywh();return {x:r.x+r.width/2,y:r.y+r.height/2};};proto._leftMid=function(){var r=this._xywh();return {x:r.x,y:r.y+r.height/2};};proto._rightMid=function(){var r=this._xywh();return {x:r.x+r.width,y:r.y+r.height/2}};proto._topMid=function(){var r=this._xywh();return {x:r.x+r.width/2,y:r.y};};proto._bottomMid=function(){var r=this._xywh();return {x:r.x+r.width/2,y:r.y+r.height};};proto.basePoint=proto._central;proto.inPoint=proto._leftMid;proto.outPoint=proto._rightMid;return RectangleBase;});var Inertia=Block.plugin(function(Block,Snap){function Inertia(){this.id="b"+(++_idx);this.block=Block.inertia.use().attr({id:this.id});this._rect=Block.inertia.select("rect");this._k=1.0;this._t=1.0;this.type='inertia';}Inertia.prototype=new RectangleBase;Inertia.prototype.constructor=Inertia;var _idx=0,proto=Inertia.prototype,config=Block._.config;Block.createInertia=function(){return new Inertia;};Block.inertia=null;Block._predefInertia=function(svg){var block_defs=''+'<defs id="MathJax_SVG_glyphs">'+'  <path stroke-width="1" id="MJMAIN-31" d="M213 578L200 573Q186 568 160 563T102 556H83V602H102Q149 604 189 617T245 641T273 663Q275 666 285 666Q294 666 302 660V361L303 61Q310 54 315 52T339 48T401 46H427V0H416Q395 3 257 3Q121 3 100 0H88V46H114Q136 46 152 46T177 47T193 50T201 52T207 57T213 61V578Z"/>'+'  <path stroke-width="1" id="MJMAIN-2B" d="M56 237T56 250T70 270H369V420L370 570Q380 583 389 583Q402 583 409 568V270H707Q722 262 722 250T707 230H409V-68Q401 -82 391 -82H389H387Q375 -82 369 -68V230H70Q56 237 56 250Z"/>'+'  <path stroke-width="1" id="MJMATHI-78" d="M52 289Q59 331 106 386T222 442Q257 442 286 424T329 379Q371 442 430 442Q467 442 494 420T522 361Q522 332 508 314T481 292T458 288Q439 288 427 299T415 328Q415 374 465 391Q454 404 425 404Q412 404 406 402Q368 386 350 336Q290 115 290 78Q290 50 306 38T341 26Q378 26 414 59T463 140Q466 150 469 151T485 153H489Q504 153 504 145Q504 144 502 134Q486 77 440 33T333 -11Q263 -11 227 52Q186 -10 133 -10H127Q78 -10 57 16T35 71Q35 103 54 123T99 143Q142 143 142 101Q142 81 130 66T107 46T94 41L91 40Q91 39 97 36T113 29T132 26Q168 26 194 71Q203 87 217 139T245 247T261 313Q266 340 266 352Q266 380 251 392T217 404Q177 404 142 372T93 290Q91 281 88 280T72 278H58Q52 284 52 289Z"/>'+'</defs>';var paths=Snap.parse(block_defs).selectAll("path");paths.forEach(function(path){svg.append(path);path.toDefs();});var inertiaSvg=''+'<g>'+'  <rect x="0" y="0" width="38" height="32" style="" fill="#ffffff" stroke="#000000" stroke-width="2"/>'+'  <svg width="3.877ex" height="3.343ex" style="vertical-align: -1.116ex;" viewbox="0 -958.8 1669.2 1439.2" role="img" focusable="false" aria-hidden="true">'+'    <g stroke="currentColor" fill="currentColor" stroke-width="0" transform="matrix(1 0 0 -1 0 0)"> '+'      <g transform="translate(120,0)">'+'        <rect stroke="none" width="1429" height="60" x="0" y="220"/>'+'        <use transform="scale(0.707)" xlink:href="#MJMAIN-31" x="760" y="566"/>'+'        <g transform="translate(60,-372)">'+'          <use transform="scale(0.707)" xlink:href="#MJMAIN-31" x="0" y="0"/>'+'          <use transform="scale(0.707)" xlink:href="#MJMAIN-2B" x="500" y="0"/>'+'          <use transform="scale(0.707)" xlink:href="#MJMATHI-78" x="1279" y="0"/>'+'        </g>'+'      </g>'+'    </g>'+'    <desc>Created with Snap</desc>'+'    <defs/>'+'  </svg>'+'</g>',inertia=Snap.parse(inertiaSvg).select("g");svg.append(inertia);Block.inertia=inertia.toDefs();};proto.set=function(k,t){this._k=k;this._t=t;};proto._attr=function(attrs){this._rect.attr(attrs);};proto.dash=function(){this._attr({'stroke-dasharray':'3,3'});};proto.solid=function(){this._attr({'stroke-dasharray':null,strokeWidth:1});};proto.selected=function(){this._attr({strokeWidth:3})};proto.setKT=function(k,t){this._t=t;this._k=k;};proto.toModel=function(){return {type:this.type,k: this._k,t:this._t};};proto.getConfig=function(){var configs=[];configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));configs.push(config('_k','K',this._k,configTypes.INPUT_TYPE));configs.push(config('_t','T',this._t,configTypes.INPUT_TYPE));return configs;};proto.updateConfig=function(){var configs=this.config.configs;this._k=configs[1].value;this._t=configs[2].value;};return Inertia;});var Joint=Block.plugin(function(Block,Snap){function Joint(){this.id='j'+(++_idx);this.block=Block.joint.use().attr({id:this.id});this.lineMode={};this.type='joint';}Block.joint=null;var _idx=0;RADIUS=15,CENTER_X=15,CENTER_Y=15,LINE_MODE_PLUS='+',LINE_MODE_MINUS='-',config=Block._.config,configTypes=Block.configTypes;Block._predefJoint=function(svg){var cx=CENTER_X,cy=CENTER_Y,r=RADIUS,c=svg.paper.circle(cx,cy,r).attr({fill:'white',stroke:'black',strokeWidth:2}),sin=Snap.sin,cos=Snap.cos,p1x=cx-r*cos(45),p1y=cy-r*sin(45),p2x=cx+r*cos(45),p2y=cy+r*sin(45);var l1=svg.paper.line(p1x,p1y,p2x,p2y);l1.attr({stroke:'black'});p1x=cx+r*cos(45);p1y=cy-r*sin(45);p2x=cx-r*cos(45);p2y=cy+r*sin(45);var l2=svg.paper.line(p1x,p1y,p2x,p2y);l2.attr({stroke:'black'});var g=svg.g(c,l1,l2);Block.joint=g.toDefs();};Joint.prototype=new Block;Joint.prototype.constructor=Joint;Block.createJoint=function(cx,cy){var j=new Joint;j.moveTo(cx,cy);return j;};var proto=Joint.prototype;proto.center=function(){var m=this.block.transform().localMatrix,x=CENTER_X+m.e,y=CENTER_Y+m.f;return {x:x,y:y};};proto.leftPoint=function(){var m=this.block.transform().localMatrix,p=this.center();return {x:p.x-RADIUS,y:p.y};};proto.rightPoint=function(){var m=this.block.transform().localMatrix,p=this.center();return {x:p.x+RADIUS,y:p.y};};proto.basePoint=proto.center;proto.inPoint=proto.leftPoint;proto.outPoint=proto.rightPoint;proto.lineAdded=function(line){this.lineMode[line._id]=LINE_MODE_PLUS;};proto.toModel=function(){var lines={},lineMode=this.lineMode;for(var prop in lineMode){lines[prop]=lineMode[prop];}return {type:this.type,lines:lines};};proto.getConfig=function(){var configs=[],lineMode=this.lineMode,optionValues=[LINE_MODE_PLUS,LINE_MODE_MINUS];configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));for(var i in lineMode){var mode=lineMode[i];configs.push(config(i,'line '+i,mode,configTypes.SELECT_TYPE,optionValues));}return configs;};proto.updateConfig=function(){var configs=this.config.configs.slice(1),lineMode=this.lineMode;for(var i in configs){var config=configs[i];lineMode[config.id]=config.value;}};return Joint;});var Step=Block.plugin(function(Block,Snap){function Step(){this.id="s"+(++_idx);this.type="step";this.block=Block.step.use().attr({id:this.id});this._rect=Block.step.select("rect");}Step.prototype=new RectangleBase;Step.prototype.constructor=Step;var proto=Step.prototype,_idx=0,operationNotSupported=Block._.operationNotSupported,config=Block._.config,configTypes=Block.configTypes;Block.step=null;Block._predefStep=function(svg){var c1=svg.paper.rect(0,0,26,30).attr({stroke:'black',strokeWidth:2,fill:'white'});var p1=svg.paper.path("M2,25 H13 V5 H24").attr({fill:'none',stroke:'black'});var g=svg.g(c1,p1);Block.step=g.toDefs();};Block.createStep=function(x,y){var s=new Step;s.moveTo(x,y);return s;};proto.toModel=function(){return {type:this.type};};proto.inPoint=operationNotSupported;proto.getConfig=function(){var configs=[];configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));return configs;};return Step;});var Scope=Block.plugin(function(Block,Snap){function Scope(){this.id="sc"+(++_idx);this.type="scope";this.block=Block.scope.use().attr({id:this.id});this._rect=Block.scope.select("rect");}Scope.prototype=new RectangleBase;Scope.prototype.constructor=Scope;var proto=Scope.prototype,_idx=0,operationNotSupported=Block._.operationNotSupported,config=Block._.config,configTypes=Block.configTypes;Block.Scope=null;Block._predefScope=function(svg){var c1=svg.paper.rect(0,0,26,30).attr({stroke:'black',strokeWidth:2,fill:'white'});var p1=svg.paper.rect(5,3,16,15).attr({stroke:'black',strokeWidth:2,fill:'white'});var g=svg.g(c1,p1);Block.scope=g.toDefs();};Block.createScope=function(x,y){var s=new Scope;s.moveTo(x,y);return s;};proto.toModel=function(){return {type:this.type};};proto.outPoint=operationNotSupported;proto.getConfig=function(){var configs=[];configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));return configs;};return Scope;});var BlockConfig=(function(jQuery){return Block.plugin(function(Block,Snap){function BlockConfig(id){this.id=id+DIV_POSTFIX;this.configs=[];}BlockConfig.configContainerId='config';var types=Block.configTypes;divhtml='<div id="{1}">{2}</div>',labelhtml='<label style="margin-right:10px">{1}</label>',texthtml='{1}',inputhtml='<input type="{1}" value="{2}" />',selecthtml='<select required="true">{1}</select>',optionhtml='<option value="{1}"{3}>{2}</option>',DIV_POSTFIX='_config_div',proto=BlockConfig.prototype;blockproto=Block.prototype,methodNotImplemented=Block._.methodNotImplemented,createConfig=Block._.config,assert=Block._.assert,contains=Block._.contains,format=Block._.format;BlockConfig.types=types;function makeLabel(label){return format(labelhtml,label);}function makeText(value){return format(texthtml,value);}function makeInput(value){return format(inputhtml,'text',value);}function makeSelect(options){return format(selecthtml,options);}function makeOption(value,txt,selected){var attr='';if(selected){attr=' selected="selected"';}return format(optionhtml,value,txt,attr);}function makeDiv(id,content){return format(divhtml,id,content);}function pureText(config){var l=makeLabel(config.name);var t=makeText(config.value);return makeDiv(config.id,l+t);}function textInput(config){var l=makeLabel(config.name);var i=makeInput(config.value);return makeDiv(config.id,l+i);}function select(config){var options='',name=config.name,value=config.value,data=config.data;var l=makeLabel(name);for(var i in data){var d=data[i];options+=makeOption(d,d,d==value);}var s=makeSelect(options);return makeDiv(config.id,l+s);}function make(config){var type=config.type,result='';assert(contains(types,type),'no such type');switch(type){case types.TEXT_TYPE:fun=pureText;break;case types.INPUT_TYPE:fun=textInput;break;case types.SELECT_TYPE:fun=select;break;default:throw 'impossble';}return fun(config);}function resolveConfig(config){var id=config.id,selector=format('#{1} {2}',id,config.type);value=$(selector).val();config.value=value;}function update(configs){for(var i in configs){var config=configs[i];if(config.type!=types.TEXT_TYPE){resolveConfig(config);}}}proto.makeConfigDiv=function(){var configs=this.configs,result='',id=this.id;for(var i in configs){result+=make(configs[i]);}return makeDiv(id,result);};proto.addConfig=function(config){var configs=this.configs;configs.push(config);return this;};proto.appendTo=function(id){var container=$('#'+id);var configDiv=this.makeConfigDiv();container.empty();container.append(configDiv);return this;};proto.build=proto.appendTo; proto.update=function(){var configs=this.configs;if(configs){update(configs);}return configs;};blockproto.getConfig=methodNotImplemented; blockproto.updateConfig=function(){console.log('default update config called');}; blockproto.showConfig=function(){var config=this.config=new BlockConfig(this.getId()),cs=this.getConfig(),containerId=BlockConfig.configContainerId;for(var k in cs){config.addConfig(cs[k]);}config.build(containerId);};blockproto.update=function(){var config=this.config,block=this;config.update();block.updateConfig();};blockproto.configMode=function(){var block=this.block,model=this._model,_this=this;block.undrag();block.unclick();block.click(function(){var lastComp=model.selectedComponent;model.selectedComponent=_this;lastComp&&lastComp.update();_this.showConfig();});};return BlockConfig;});})($);var Line=(function(Snap){var idx=0,proto=Line.prototype;Line.lineEnd=null;Line._predefs=function(svg){var m=svg.paper.path("M2,2 L2,11 L10,6 L2,2").attr({fill: 'black'});Line.lineEnd=m.marker(0,0,13,13,10,6);};function Line(fromBlock,toBlock){try{toBlock.inPoint();fromBlock.outPoint();}catch(e){throw e;}this._id=replace("l{1}",++idx);this._fromBlock=fromBlock;this._toBlock=toBlock;this._path=draw(fromBlock,toBlock);this._model=fromBlock._model;fromBlock.lines.push(this);toBlock.lines.push(this);toBlock.lineAdded(this);}function draw(fromBlock,toBlock){var p=Snap.parse("<path></path>").select("path");var ps=resolvePathString(fromBlock,toBlock);p.attr({stroke:'black',d:ps,'marker-end':Line.lineEnd});return p;}function resolvePathString(b1,b2){var p1=b1.outPoint();var p2=b2.inPoint();return replace("M{1},{2}L{3},{4}",p1.x,p1.y,p2.x,p2.y);}function replace(str){var args=arguments;return str.replace(/\{(\d+)\}/g, function(m,i){return args[i];});}proto.attachToSvg=function(svg){svg.append(this._path);};proto.detach=function(){this._path.remove();this._path=null;this._fromBlock.removeLine(this);this._toBlock.removeLine(this);this._fromBlock=null;this._toBlock=null;};proto.removeMode=function(){var path=this._path,id=this._id,model=this._model;path.dblclick(function(){model.removeLine(id);});};proto.redraw=function(){var ps=resolvePathString(this._fromBlock,this._toBlock);this._path.attr({d:ps});};proto.toModel=function(){return [this._fromBlock.id,this._toBlock.id];};return Line;})(Snap);var Model=(function(Block,Line){function Model(svg){this.svg=svg;this.config={type:'fixed',T:0.01,t:0.0,tt:10.0};this.components={};this.lines={};this.selectedComponent=null;}var proto=Model.prototype,options=Block.options,types=Block.types,toJson=JSON.stringify;function predef(svg){Block._predefs(svg);Line._predefs(svg);}Model.predef=predef;proto.exsitBlock=function(id){return this.components[id]!=null;};proto.addBlock=function(x,y,type){if(!type){type=types.INERTIA;}var block=Block.perform(options.CREATE,type,x,y),model=this,components=this.components;components[block.id]=block;block.attachToSvg(this.svg);block.moveTo(x,y);block.attachToModel(model);block.moveMode();block.removeMode();return block;};proto.removeBlock=function(id){var components=this.components,block=components[id],relatedLines=block.lines;block.detach();delete components[id];for(idx in relatedLines){var lineId=relatedLines[idx]._id;this.removeLine(lineId);}};proto.find=function(id){return model.components[id];};proto.addLine=function(fromId,toId){if(fromId==toId){return;}for(var key in this.lines){ var line=this.lines[key];if(line._fromBlock.id==fromId&&line._toBlock.id==toId){return;}}try{var line=new Line(this.components[fromId],this.components[toId]);}catch(e){alert(e.message);return;}this.lines[line._id]=line;line.attachToSvg(this.svg);line.removeMode();return line;};proto.connect=proto.addLine;proto.removeLine=function(id){var lines=this.lines,line=lines[id];line.detach();delete lines[id];};proto.changeMode=function(mode){var comps=this.components;for(key in comps){var comp=comps[key];comp[mode]();}};proto.connectMode=function(){this.changeMode('connectMode');};proto.moveMode=function(){this.changeMode('moveMode');};proto.configMode=function(){this.changeMode('configMode');};proto.valid=function(){};proto.toJsonModel=function(){var blocks={},components=this.components,lines=this.lines,lineModels={};for(var key in components){blocks[key]=components[key].toModel();}for(key in lines){lineModels[key]=lines[key].toModel();}var model={'config':this.config,'components':blocks,'lines':lineModels};return toJson(model);};proto.toJsonPersist=function(){var ret={svg:this.svg.toString(),model:toJsonModel()};return ret;};return Model;})(Block,Line);