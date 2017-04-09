package hust.hx.simulation.demo.block

import groovy.json.JsonSlurper

class Simulator {
	Config config
	Map<String,Block> components=[:]
	Map<String,Line> lines=[:]

	def initJsonSystem(String jsonString){
		def model=new JsonSlurper().parseText(jsonString)
		initSystem(model)
	}

	def initSystem(Map model){
		config=new Config()
		config.config(model.config.T,model.config.t,model.config.tt)

		model.components.each{
			components[it.key]=BlockFactory.create(it.value)
		}

		components.each{
			def comp=it.value
			if(comp instanceof Inertia){
				comp.setConfig(config)
			}
		}

		adjustLine(model)
		println model.lines

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
	}

	private def adjustLine(Map model){
		def srcId=components.findResult{if(it.value instanceof Source) return it.key}
		def line=model.lines.find{it.value[0]==srcId}
		def stage=new Stage(model.lines.clone(),[:],[:] << line)
		model.lines=Stage.resolve(stage)
	}

	def simulate(){
		config.iterate{
			lines.each{
				it.value.push()
			}
			components.each{
				it.value.moveOn()
			}
		}
	}

	private def checkValidation(){
	}

	private def resolve(origin){
		def ol=[:]
		def lines=origin.clone()

		def init=lines.find{ true }
		if(!init) return [:]
		lines.remove(init.key)
		ol<<init

		def f=lines.find{it.value[0]==init.value[1]}
		while(f){
			lines.remove(f.key)
			ol<<f
			f=lines.find{it.value[0]==f.value[1]}
		}

		return [ol, lines]
	}

	private def resolveAll(origin){
		def result=[]
		def one=resolve(origin)
		result<<one[0]

		while(one[1]){
			one=resolve(one[1])
			result<<one[0]
		}

		def ret=result.inject{x,y->	x<<y}

		println ret
		return ret
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
					b=new Inertia(info.k as double,info.t as double);break;
			case 'amplifier':
					b=new Amplifier(info.k as double);break;
			case 'limiter':
					b=new Limiter(info.upper as double,info.lower as double);break;
			case 'scope':
					b=new Scope();break;
			case 'homopoly':
					b=new Homopoly(info.a as double,info.b as double,info.c as double);break;
			case 'integrator':
					b=new Integrator(info.k as double);break;
			default: throw new IllegalArgumentException('no such model');break;
		}
		return b;
	}
}