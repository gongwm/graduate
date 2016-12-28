def dt=0.01
def totalTime=10

def e=Math.E

def n=totalTime/dt

def time=[]
def input=[]
(1..n).eachWithIndex{a,idx->
	time+=idx*dt
	input+=1
}

def ty1=0.1

def ty=0.5

def x1=[]
x1[0]=0

def x2=[]
x2[0]=0

def c11=e**(-10*dt),c12=1-c11
def c2=2*dt

(1..n).eachWithIndex{a,k->
	x1[k]=c11*x1[k-1]+c12*input[k];
	x1[k]=x1[k].toDouble().trunc(4)
	x2[k]=(x2[k-1]+c2*x1[k]).trunc(4)
}

def pw=new PrintWriter(new File('C:\\out.txt'))
time.eachWithIndex{a,k->
	pw.println("${time[k]} ${x2[k]}")
}
pw.flush()
pw.close()

