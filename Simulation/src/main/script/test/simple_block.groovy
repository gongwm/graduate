class Config{
	double T=0.01
	double totalTime=10

	int n=totalTime/T
	double currentTime=0.0
	int i=0

	void next(){
		++i
		currentTime+=T
	}
}

class Inertia{
	Config config
	def e=Math.E

	double k=1
	double t=0.1

	double out=0.0 //initial value

	double c1
	double c2

	def init(){
		c1=e**(-config.T/t)
		c2=k*(1-e**(-config.T/t))
	}

	double next(double input){
		out=c1*out+c2*input
		return out
	}
}

class Line{
	def start
	def end
	
	void next(){
		end.next(start.next())
	}
}

class StepSource{ // simple step source
	double next(){
		return 1.0
	}
}

class Simulation{
	def simulate(){
		def config=new Config() //仿真配置

		def source=new StepSource() //元件设置

		def inertia=new Inertia()
		inertia.config=config
		inertia.init()

		def line1=new Line() // 连接设置
		line1.start=source
		line1.end=inertia

		def y=[]
		y[0]=inertia.out

		(1..config.n).each{
			config.next()
			line1.next()
			y[it]=inertia.out
		}
		return y
	}
}

def simu=new Simulation()
def out=simu.simulate()
println out[0..10]
