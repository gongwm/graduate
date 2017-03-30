package hust.hx.simulation.demo.block

def model=ExciterModel.exciter

def lines=model.lines.clone()

def stage=new Stage(lines,[:],[:]<<lines.find{it.value[0]=='s1'})


def chain=Stage.resolve(stage)

println chain

assert chain.size()==model.lines.size()
