package hust.hx.simulation.demo.block

class RegularSystem{
	Integrator int1
	Homopoly1 homo1_1
	Inertia inertia1
	def components,lines
	def scope

	Config config=Config.of(0.01,30)

	RegularSystem(double bt,double td,double ty,double ti){
		init(bt,td,ty,ti)
	}

	private def init(bt,td,ty,ti){
		def step=new StepSource()
		def adder1=new Adder()
		def homo1=new Homopoly(1.0,1.0,1.0)
		def adder2=new Adder()

		int1=new Integrator(1/ti)
		homo1_1=new Homopoly1(bt*td,td)

		def amp1=new Amplifier(0.0)
		def adder3=new Adder()

		inertia1=new Inertia(1,ty)

		def homo2=new Homopoly(1,-1,0.5)
		def inertia2=new Inertia(1,5)

		scope=new Scope();

		def l1=Line.of(step,adder1)
		def l2=Line.of(adder1,homo1)
		def l3=Line.of(homo1,adder2)
		def l4=Line.of(adder2,int1)
		def l5=Line.of(int1,inertia1)
		def l6=Line.of(inertia1,homo2)
		def l7=Line.of(homo2,inertia2)
		def l8=Line.of(inertia2,scope)

		def l9=Line.of(int1,homo1_1)
		def l10=Line.of(homo1_1,adder3)
		def l11=Line.of(int1,amp1)
		def l12=Line.of(amp1,adder3)
		def l13=Line.of(adder3,adder2)
		def l14=Line.of(inertia2,adder1)

		([inertia1, inertia2] as List<Inertia>).each{it.setConfig(config)}
		(int1 as Integrator).setConfig(config)
		([homo1, homo2] as List<Homopoly>).each{it.setConfig(config)}
		(homo1_1 as Homopoly1).setConfig(config)

		def add1=adder1 as Adder
		add1.addLine(l1,Adder.ADD)
		add1.addLine(l14,Adder.SUB)

		def add2=adder2 as Adder
		add2.addLine(l3,Adder.ADD)
		add2.addLine(l13,Adder.SUB)

		def add3=adder3 as Adder
		add3.addLine(l10,Adder.ADD)
		add3.addLine(l12,Adder.ADD)

		components=[step, adder1, homo1, adder2, int1, homo1_1, amp1, adder3, inertia1, homo2, inertia2, scope]
		lines=[l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14]
	}

	def reset(bt,td,ty,ti){
		int1.config(1/ti)
		homo1_1.config(bt*td,td)
		inertia1.config(1,ty)
		components.each{(it as Block).setInitValue(0.0)}
		lines.each{(it as Line).init()}
		config.reset();
	}

	def simulate(){
		config.iterate{
			lines.each{(it as Line).push()}
			components.each{(it as Block).moveOn()}
		}
	}

	def getTime(){
		config.time as List
	}

	def getOutput(){
		(scope as Scope).getData()
	}
}
