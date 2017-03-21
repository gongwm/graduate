package hust.hx.simulation.demo.block

class Simulator {
	Config config
	Map<String,Block> components=[:]
	Map<String,Line> lines=[:]

	def initSystem(Map model){
		config=new Config()
		config.config(model.config.T,model.config.t,model.config.tt)

		model.components.each{
			components[it.key]=BlockFactory.create(it.value)
		}

		model.lines.each{
			def start=components[it.value[0]]
			def end=components[it.value[1]]
			def line=new Line(start,end)
			lines[it.key]=line
		}
		model.components.each{
			if(it.value.type=='joint'){
				def joint=components[it.key] as Adder
				it.value.lines.each{
					joint.addLine(lines[it.key],it.value as char)
				}
			}
		}

		components.each{
			if(it instanceof Inertia){
				it.setConfig(config)
			}
		}
	}

	def simulate(){
		config.iterate{i,k->
			lines.each{
				it.value.push(i,k)
			}
			components.each{
				it.value.moveOn()
			}
		}
	}

	private def checkValidation(){
	}

	private def adjustLine(){
	}

	def findOutputs(){
		return components.findAll { return it.value instanceof Scope }
	}

	def getTime(){
		return config.getTime();
	}
}

class BlockFactory{
	static def create(Map info){
		def b
		switch(info.type){
			case 'step':
					b=new StepSource();break;
			case 'joint':
					b=new Adder();break;
			case 'inertia':
					b=new Inertia().config(info.k as double,info.t as double);break;
			case 'amplifier':
					b=new Amplifier(info.k as double);break;
			case 'limiter':
					b=new Limiter(info.upper as double,info.lower as double);break;
			case 'scope':
					b=new Scope();break;
			default: throw new IllegalArgumentException('no such model');break;
		}
		return b;
	}
}
