import groovy.transform.TupleConstructor

interface Block{
	double e=Math.E
}

class Config{
	//basic config.
	double T=0.01

	double currentTime=0.0
	double totalTime=5
	int n=totalTime/T
	int i=0

	void nextStep(){
		++i
		currentTime+=T
	}
}

class Inertia implements Block{//inertia object model
	Config config=new Config()

	double k=1
	double t=0.1

	double out=0.0

	double c1
	double c2

	Inertia(){
		c1=e**(-config.T/t)
		c2=k*(1-e**(-config.T/t))
	}

	double next(double input){
		out=c1*out+c2*input
		return out
	}
}

class StepSource implements Block{//simple step source
	double next(){
		return 1.0
	}
}

class Simulation{
	def simulate(){
		def config=new Config()

		def source=new StepSource()
		def inertia=new Inertia()
		def line1=new Line(source,inertia)

		def y=[]
		y[0]=inertia.out

		(1..config.n).each{
			y+=line1.end.next(line1.start.next())
		}
		return y
	}
}

@TupleConstructor
class Line{
	Block start
	Block end
}

//joint point
class JointPoint{

}

def simu=new Simulation()
def out=simu.simulate()
println out[0..10]