var Integrator=Block.plugin(function(Block,Snap){
function Integrator(){
	this.id="int"+(++_idx);
	this.block=Block.integrator.use().attr({id:this.id});
	this._rect=Block.integrator.select("rect");
	
	this._k=1.0;
	
	this.type='integrator';
	this.lines=[];
}

Integrator.prototype=new RectangleBase;
Integrator.prototype.constructor=Integrator;

var _idx=0,
	proto=Integrator.prototype,
	config=Block._.config;

Block.createIntegrator=function(){
	return new Integrator;
};

Block.integrator=null;

Block._predefIntegrator=function(svg){
	var block_defs=''+
		'<defs>'+
		'  <path stroke-width="1" id="MJMATHI-6B" d="M121 647Q121 657 125 670T137 683Q138 683 209 688T282 694Q294 694 294 686Q294 679 244 477Q194 279 194 272Q213 282 223 291Q247 309 292 354T362 415Q402 442 438 442Q468 442 485 423T503 369Q503 344 496 327T477 302T456 291T438 288Q418 288 406 299T394 328Q394 353 410 369T442 390L458 393Q446 405 434 405H430Q398 402 367 380T294 316T228 255Q230 254 243 252T267 246T293 238T320 224T342 206T359 180T365 147Q365 130 360 106T354 66Q354 26 381 26Q429 26 459 145Q461 153 479 153H483Q499 153 499 144Q499 139 496 130Q455 -11 378 -11Q333 -11 305 15T277 90Q277 108 280 121T283 145Q283 167 269 183T234 206T200 217T182 220H180Q168 178 159 139T145 81T136 44T129 20T122 7T111 -2Q98 -11 83 -11Q66 -11 57 -1T48 16Q48 26 85 176T158 471L195 616Q196 629 188 632T149 637H144Q134 637 131 637T124 640T121 647Z"/>'+
		'  <path stroke-width="1" id="MJMATHI-73" d="M131 289Q131 321 147 354T203 415T300 442Q362 442 390 415T419 355Q419 323 402 308T364 292Q351 292 340 300T328 326Q328 342 337 354T354 372T367 378Q368 378 368 379Q368 382 361 388T336 399T297 405Q249 405 227 379T204 326Q204 301 223 291T278 274T330 259Q396 230 396 163Q396 135 385 107T352 51T289 7T195 -10Q118 -10 86 19T53 87Q53 126 74 143T118 160Q133 160 146 151T160 120Q160 94 142 76T111 58Q109 57 108 57T107 55Q108 52 115 47T146 34T201 27Q237 27 263 38T301 66T318 97T323 122Q323 150 302 164T254 181T195 196T148 231Q131 256 131 289Z"/>'+
		'</defs>';
	var paths=Snap.parse(block_defs).selectAll("path");
	paths.forEach(function(path){
		svg.append(path);
		path.toDefs();
	});

	var integratorSvg=''+
		'<g>'+
		'  <rect x="0" y="0" width="30" height="35" style="" fill="#ffffff" stroke="#000000" stroke-width="2"/>'+
		'  <svg transform="scale(0.72) translate(10,1)" width="2.047ex" height="5.106ex" style="vertical-align: -1.742ex;" viewBox="0 -1448.3 881.5 2198.3" role="img" focusable="false" aria-hidden="true">'+
		'    <g stroke="currentColor" fill="currentColor" stroke-width="0" transform="matrix(1 0 0 -1 0 0)">'+
		'      <g transform="translate(120,0)">'+
		'        <rect stroke="none" width="641" height="60" x="0" y="220"/>'+
		'        <use xlink:href="#MJMATHI-6B" x="60" y="676"/>'+
		'        <use xlink:href="#MJMATHI-73" x="86" y="-686"/>'+
		'      </g>'+
		'    </g>'+
		'  </svg>'+
		'</g>',
		integrator=Snap.parse(integratorSvg).select("g");
	svg.append(integrator);
	Block.integrator=integrator.toDefs();
};

proto.toModel=function(){
	return {type:this.type,k: this._k};
};

proto.getConfig=function(){
	// config: id, name, value, type
	var configs=[];
	configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));
	configs.push(config('_k','k',this._k,configTypes.INPUT_TYPE));
	return configs;
};

proto.updateConfig=function(){
	var configs=this.config.configs;
	this._k=configs[1].value;
};

return Integrator;
});