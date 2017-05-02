package hust.hx.simulation.demo.block

import hust.hx.simulation.util.PrintUtil

def config=Config.of(0.02,30)

def step=new StepSource()
def hp1=new Homopoly1(1.0,1.0)
def scope=new Scope()

hp1.setConfig(config)

def l1=Line.of(step,hp1)
def l2=Line.of(hp1,scope)

config.iterate{
	l1.push()
	l2.push()

	hp1.moveOn()
}

def time=config.time
def out=scope.data

print out[0..10]

PrintUtil.print{pw->
	time.eachWithIndex{t,idx->
		pw.println("$t ${out[idx]}")
	}
}