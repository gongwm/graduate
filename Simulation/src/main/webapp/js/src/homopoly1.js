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
		'<defs id="MathJax_SVG_glyphs">'+
		'  <path stroke-width="1" id="homopoly1-thi-61" d="M33 157Q33 258 109 349T280 441Q331 441 370 392Q386 422 416 422Q429 422 439 414T449 394Q449 381 412 234T374 68Q374 43 381 35T402 26Q411 27 422 35Q443 55 463 131Q469 151 473 152Q475 153 483 153H487Q506 153 506 144Q506 138 501 117T481 63T449 13Q436 0 417 -8Q409 -10 393 -10Q359 -10 336 5T306 36L300 51Q299 52 296 50Q294 48 292 46Q233 -10 172 -10Q117 -10 75 30T33 157ZM351 328Q351 334 346 350T323 385T277 405Q242 405 210 374T160 293Q131 214 119 129Q119 126 119 118T118 106Q118 61 136 44T179 26Q217 26 254 59T298 110Q300 114 325 217T351 328Z"/>'+
		'  <path stroke-width="1" id="homopoly1-thi-73" d="M131 289Q131 321 147 354T203 415T300 442Q362 442 390 415T419 355Q419 323 402 308T364 292Q351 292 340 300T328 326Q328 342 337 354T354 372T367 378Q368 378 368 379Q368 382 361 388T336 399T297 405Q249 405 227 379T204 326Q204 301 223 291T278 274T330 259Q396 230 396 163Q396 135 385 107T352 51T289 7T195 -10Q118 -10 86 19T53 87Q53 126 74 143T118 160Q133 160 146 151T160 120Q160 94 142 76T111 58Q109 57 108 57T107 55Q108 52 115 47T146 34T201 27Q237 27 263 38T301 66T318 97T323 122Q323 150 302 164T254 181T195 196T148 231Q131 256 131 289Z"/>'+
		'  <path stroke-width="1" id="homopoly1-in-31" d="M213 578L200 573Q186 568 160 563T102 556H83V602H102Q149 604 189 617T245 641T273 663Q275 666 285 666Q294 666 302 660V361L303 61Q310 54 315 52T339 48T401 46H427V0H416Q395 3 257 3Q121 3 100 0H88V46H114Q136 46 152 46T177 47T193 50T201 52T207 57T213 61V578Z"/>'+
		'  <path stroke-width="1" id="homopoly1-in-2B" d="M56 237T56 250T70 270H369V420L370 570Q380 583 389 583Q402 583 409 568V270H707Q722 262 722 250T707 230H409V-68Q401 -82 391 -82H389H387Q375 -82 369 -68V230H70Q56 237 56 250Z"/>'+
		'  <path stroke-width="1" id="homopoly1-thi-62" d="M73 647Q73 657 77 670T89 683Q90 683 161 688T234 694Q246 694 246 685T212 542Q204 508 195 472T180 418L176 399Q176 396 182 402Q231 442 283 442Q345 442 383 396T422 280Q422 169 343 79T173 -11Q123 -11 82 27T40 150V159Q40 180 48 217T97 414Q147 611 147 623T109 637Q104 637 101 637H96Q86 637 83 637T76 640T73 647ZM336 325V331Q336 405 275 405Q258 405 240 397T207 376T181 352T163 330L157 322L136 236Q114 150 114 114Q114 66 138 42Q154 26 178 26Q211 26 245 58Q270 81 285 114T318 219Q336 291 336 325Z"/>'+
		'</defs>';

	var paths=Snap.parse(block_defs).selectAll("path");
	paths.forEach(function(path){
		svg.append(path);
		path.toDefs();
	});

	var homopoly1Svg=''+
		'<g>'+
		'  <rect x="0" y="0" width="52" height="35" style="" fill="#ffffff" stroke="#000000" stroke-width="2"/>'+
		'  <svg transform="scale(0.75) translate(3,3)" width="6.927ex" height="4.758ex" style="vertical-align: -1.974ex;" viewBox="0 -1198.9 2982.4 2048.7" role="img" focusable="false" aria-hidden="true">'+
		'    <g stroke="currentColor" fill="currentColor" stroke-width="0" transform="matrix(1 0 0 -1 0 0)">'+
		'      <g transform="translate(120,0)">'+
		'        <rect stroke="none" width="2742" height="60" x="0" y="220"/>'+
		'        <g transform="translate(871,676)">'+
		'          <use xlink:href="#homopoly1-thi-61" x="0" y="0"/>'+
		'          <use xlink:href="#homopoly1-thi-73" x="529" y="0"/>'+
		'        </g>'+
		'        <g transform="translate(60,-686)">'+
		'          <use xlink:href="#homopoly1-in-31" x="0" y="0"/>'+
		'          <use xlink:href="#homopoly1-in-2B" x="722" y="0"/>'+
		'          <use xlink:href="#homopoly1-thi-62" x="1723" y="0"/>'+
		'          <use xlink:href="#homopoly1-thi-73" x="2152" y="0"/>'+
		'        </g>'+
		'      </g>'+
		'    </g>'+
		'  </svg>'+
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