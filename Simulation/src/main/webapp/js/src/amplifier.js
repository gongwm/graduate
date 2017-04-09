var Amplifier=Block.plugin(function(Block,Snap){
function Amplifier(){
	this.id="amp"+(++_idx);
	this.block=Block.amplifier.use().attr({id:this.id});
	this._rect=Block.amplifier.select("rect");
	
	this._k=1.0;
	
	this.type='amplifier';
}

Amplifier.prototype=new RectangleBase;
Amplifier.prototype.constructor=Amplifier;

var _idx=0,
	proto=Amplifier.prototype,
	config=Block._.config;

Block.createAmplifier=function(){
	return new Amplifier;
};

Block.amplifier=null;

Block._predefAmplifier=function(svg){
	var paper=svg.paper;
	var rect=paper.rect(0,0,40,32).attr({stroke:'black',fill:'white',strokeWidth:2}).drag();
	var txt=paper.text(13,20,'A');
	var g=paper.g(rect,txt);
	Block.amplifier=g.toDefs();
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

return Amplifier;
});