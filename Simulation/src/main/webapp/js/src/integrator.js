var Integrator=Block.plugin(function(Block,Snap){
function Integrator(){
	this.id="int"+(++_idx);
	this.block=Block.integrator.use().attr({id:this.id});
	this._rect=Block.integrator.select("rect");
	
	this._k=1.0;
	
	this.type='integrator';
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
		'<defs id="MathJax_SVG_glyphs">'+
		'  <path stroke-width="1" id="intn-31" d="M213 578L200 573Q186 568 160 563T102 556H83V602H102Q149 604 189 617T245 641T273 663Q275 666 285 666Q294 666 302 660V361L303 61Q310 54 315 52T339 48T401 46H427V0H416Q395 3 257 3Q121 3 100 0H88V46H114Q136 46 152 46T177 47T193 50T201 52T207 57T213 61V578Z"></path>'+
		'  <path stroke-width="1" id="inthi-73" d="M131 289Q131 321 147 354T203 415T300 442Q362 442 390 415T419 355Q419 323 402 308T364 292Q351 292 340 300T328 326Q328 342 337 354T354 372T367 378Q368 378 368 379Q368 382 361 388T336 399T297 405Q249 405 227 379T204 326Q204 301 223 291T278 274T330 259Q396 230 396 163Q396 135 385 107T352 51T289 7T195 -10Q118 -10 86 19T53 87Q53 126 74 143T118 160Q133 160 146 151T160 120Q160 94 142 76T111 58Q109 57 108 57T107 55Q108 52 115 47T146 34T201 27Q237 27 263 38T301 66T318 97T323 122Q323 150 302 164T254 181T195 196T148 231Q131 256 131 289Z"></path>'+
		'</defs>';
	var paths=Snap.parse(block_defs).selectAll("path");
	paths.forEach(function(path){
		svg.append(path);
		path.toDefs();
	});

	var integratorSvg=''+
		'<g>'+
		'  <rect x="0" y="0" width="30" height="35" style="" fill="#ffffff" stroke="#000000" stroke-width="2"/>'+
		'  <svg transform="scale(0.73) translate(10,2)" width="1.999ex" height="4.99ex" style="vertical-align: -1.742ex;" viewbox="0 -1398.4 860.5 2148.5" role="img" focusable="false" aria-hidden="true">'+
		'    <g stroke="currentColor" fill="currentColor" stroke-width="0" transform="matrix(1 0 0 -1 0 0)">'+
		'      <g transform="translate(120,0)">'+
		'        <rect stroke="none" width="620" height="60" x="0" y="220"></rect>'+
		'        <use xlink:href="#intn-31" x="60" y="676"></use>'+
		'        <use xlink:href="#inthi-73" x="75" y="-686"></use>'+
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