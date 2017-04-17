var Homopoly1=Block.plugin(function(Block,Snap){
function Homopoly1(){
	this.id="hm1_"+(++_idx);
	this.block=Block.homopoly1.use().attr({id:this.id});
	this._rect=Block.homopoly1.select("rect");
	
	this._a=1.0;
	this._b=1.0;

	this.type='homopoly1';
	this.lines=[];
}

Homopoly1.prototype=new RectangleBase;
Homopoly1.prototype.constructor=Homopoly1;

var _idx=0,
	proto=Homopoly1.prototype,
	config=Block._.config;

Block.createHomopoly1=function(){
	return new Homopoly1;
};

Block.homopoly1=null;

Block._predefHomopoly1=function(svg){
	var block_defs=''+
		'<defs>'+
		'	<path stroke-width="1" id="homopoly1-thi-61" d="M33 157Q33 258 109 349T280 441Q331 441 370 392Q386 422 416 422Q429 422 439 414T449 394Q449 381 412 234T374 68Q374 43 381 35T402 26Q411 27 422 35Q443 55 463 131Q469 151 473 152Q475 153 483 153H487Q506 153 506 144Q506 138 501 117T481 63T449 13Q436 0 417 -8Q409 -10 393 -10Q359 -10 336 5T306 36L300 51Q299 52 296 50Q294 48 292 46Q233 -10 172 -10Q117 -10 75 30T33 157ZM351 328Q351 334 346 350T323 385T277 405Q242 405 210 374T160 293Q131 214 119 129Q119 126 119 118T118 106Q118 61 136 44T179 26Q217 26 254 59T298 110Q300 114 325 217T351 328Z"></path>'+
		'	<path stroke-width="1" id="homopoly1-thi-78" d="M52 289Q59 331 106 386T222 442Q257 442 286 424T329 379Q371 442 430 442Q467 442 494 420T522 361Q522 332 508 314T481 292T458 288Q439 288 427 299T415 328Q415 374 465 391Q454 404 425 404Q412 404 406 402Q368 386 350 336Q290 115 290 78Q290 50 306 38T341 26Q378 26 414 59T463 140Q466 150 469 151T485 153H489Q504 153 504 145Q504 144 502 134Q486 77 440 33T333 -11Q263 -11 227 52Q186 -10 133 -10H127Q78 -10 57 16T35 71Q35 103 54 123T99 143Q142 143 142 101Q142 81 130 66T107 46T94 41L91 40Q91 39 97 36T113 29T132 26Q168 26 194 71Q203 87 217 139T245 247T261 313Q266 340 266 352Q266 380 251 392T217 404Q177 404 142 372T93 290Q91 281 88 280T72 278H58Q52 284 52 289Z"></path>'+
		'	<path stroke-width="1" id="homopoly1-in-31" d="M213 578L200 573Q186 568 160 563T102 556H83V602H102Q149 604 189 617T245 641T273 663Q275 666 285 666Q294 666 302 660V361L303 61Q310 54 315 52T339 48T401 46H427V0H416Q395 3 257 3Q121 3 100 0H88V46H114Q136 46 152 46T177 47T193 50T201 52T207 57T213 61V578Z"></path>'+
		'	<path stroke-width="1" id="homopoly1-in-2B" d="M56 237T56 250T70 270H369V420L370 570Q380 583 389 583Q402 583 409 568V270H707Q722 262 722 250T707 230H409V-68Q401 -82 391 -82H389H387Q375 -82 369 -68V230H70Q56 237 56 250Z"></path>'+
		'	<path stroke-width="1" id="homopoly1-thi-62" d="M73 647Q73 657 77 670T89 683Q90 683 161 688T234 694Q246 694 246 685T212 542Q204 508 195 472T180 418L176 399Q176 396 182 402Q231 442 283 442Q345 442 383 396T422 280Q422 169 343 79T173 -11Q123 -11 82 27T40 150V159Q40 180 48 217T97 414Q147 611 147 623T109 637Q104 637 101 637H96Q86 637 83 637T76 640T73 647ZM336 325V331Q336 405 275 405Q258 405 240 397T207 376T181 352T163 330L157 322L136 236Q114 150 114 114Q114 66 138 42Q154 26 178 26Q211 26 245 58Q270 81 285 114T318 219Q336 291 336 325Z"></path>'+
		'</defs>';

	var paths=Snap.parse(block_defs).selectAll("path");
	paths.forEach(function(path){
		svg.append(path);
		path.toDefs();
	});

	var homopoly1Svg=''+
		'<g>'+
		'  <rect x="0" y="0" width="55" height="35" style="" fill="#ffffff" stroke="#000000" stroke-width="2"/>'+
		'  <svg transform="scale(0.8) translate(3,2)" width="7.166ex" height="4.758ex" style="vertical-align: -1.974ex;" viewbox="0 -1198.9 3085.4 2048.7" role="img" focusable="false" aria-hidden="true">'+
		'    <g stroke="currentColor" fill="currentColor" stroke-width="0" transform="matrix(1 0 0 -1 0 0)">'+
		'      <g transform="translate(120,0)">'+
		'      <rect stroke="none" width="2845" height="60" x="0" y="220"></rect>'+
		'      <g transform="translate(871,676)">'+
		'        <use xlink:href="#homopoly1-thi-61" x="0" y="0"></use>'+
		'        <use xlink:href="#homopoly1-thi-78" x="529" y="0"></use>'+
		'      </g>'+
		'      <g transform="translate(60,-686)">'+
		'        <use xlink:href="#homopoly1-in-31" x="0" y="0"></use>'+
		'        <use xlink:href="#homopoly1-in-2B" x="722" y="0"></use>'+
		'        <use xlink:href="#homopoly1-thi-62" x="1723" y="0"></use>'+
		'        <use xlink:href="#homopoly1-thi-78" x="2152" y="0"></use>'+
		'      </g>'+
		'    </g>'+
		'    </g>'+
		' </svg>'+
		'</g>',
		homopoly1=Snap.parse(homopoly1Svg).select("g");
	svg.append(homopoly1);
	Block.homopoly1=homopoly1.toDefs();
};

proto.toModel=function(){
	return {type:this.type,a: this._a,b:this._b};
};

proto.getConfig=function(){
	// config: id, name, value, type
	var configs=[];
	configs.push(config('id','id',this.id,configTypes.TEXT_TYPE));
	configs.push(config('_a','a',this._a,configTypes.INPUT_TYPE));
	configs.push(config('_b','b',this._b,configTypes.INPUT_TYPE));
	return configs;
};

proto.updateConfig=function(){
	var configs=this.config.configs;
	this._a=configs[1].value;
	this._b=configs[2].value;
};

return Homopoly1;
});