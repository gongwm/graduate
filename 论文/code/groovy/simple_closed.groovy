def T=0.01//config
def e=Math.E
def totalTime=20
def n=totalTime/T;

def t=[]
def u=[]
(0..n).eachWithIndex{v,k->
t+=k*T
u+=1.0
}

def x1=[]
def x2=[]
def err=[]
def y=[]

x1[0]=0//initial value
x2[0]=0
err[0]=u[0]-x2[0]
y[0]=x2[0]

def c11=e**(-10*T)//buffered coefficient
def c12=1-e**(-10*T)
def c21=2*T

(1..n).each{//simulation
def k=it
x1[k]=c11*x1[k-1]+c12*err[k-1]
x2[k]=x2[k-1]+c21*x1[k-1]
err[k]=u[k]-x2[k]
y[k]=x2[k]
}

def pw=new PrintWriter('out.txt')
(0..n).each{
def k=it
pw.println("${t[k]} ${y[k]}")
}
pw.flush()
pw.close()