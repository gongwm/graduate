def start=System.currentTimeMillis()

def dt=0.1
def totalTime=10 //seconds

def n=totalTime/dt

def time=[]
def u=[]

(0..<n).each{
	time+=it*dt
	u+=1.0
}

def K=1.0 // K/(1+T*s)
def T=1.0
def out=[]
out[0]=0

(1..<n).each{k->
	double d=T/(T+dt)*out[k-1]+
			K*dt/(T+dt)*u[k]
	out+=d
}

def end=System.currentTimeMillis()
println "it costs ${end-start} ms"

println out[1..10]

//输出结果
def path='C:\\inertiaOut.txt'

def pw=new PrintWriter(path)
(0..<n).each{k->
	pw.println("${time[k]} ${out[k]}")
}
pw.flush()
pw.close()
