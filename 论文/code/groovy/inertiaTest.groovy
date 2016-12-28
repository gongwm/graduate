def start=System.currentTimeMillis()

def dt=0.01 //������
def totalTime=10 //seconds
def e=Math.E;

def n=totalTime/dt

def time=[] //����ʱ��
def u=[] //��Ծ����
(0..<n).each{k->
    time+=k*dt
    u+=1.0
}

def K=1.0 //���Ի���1/(1+0.1*s)
def T=0.1
def out=[]
out[0]=0

def c1=e**(-dt/T)
def c2=K*(1-e**(-dt/T))
(1..<n).each{k->
    //����
    double d= c1*out[k-1]+c2*u[k]
    out+=d
}

def end=System.currentTimeMillis()
println "it costs ${end-start} ms"

println out[1..<10] //��������ӡ
def path='C:\\out.txt'
def pw=new PrintWriter(path)
(0..<n).each{k->
    pw.println("${time[k]} ${out[k]}")
}
pw.flush()
pw.close()
