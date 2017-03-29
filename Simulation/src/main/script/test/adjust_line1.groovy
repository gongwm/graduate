import groovy.transform.TupleConstructor

@TupleConstructor
class Stage{
	Map remaining
	Map existing
	Map tails

	List<Stage> split(){
		def result=[]
		if(tails.size()<=1){
			result<<this
		}else{
			tails.each{
				result<<new Stage(remaining.clone(),existing.clone(),[:]<<it)
			}
		}
		result
	}

	def resolveToBranch(){
		if(tails.size()!=1){
			return
		}
		while(true){
			def count=tails.size()

			if(count==0){
				tails=[:]
				break
			}else if(count==1){
				def added=tails.find{true} // only one
				remaining.remove(added.key)
				existing<<added
			}else/* if(count>1) */{
				break
			}
			tails=remaining.findAll{it.value[0]==tails.find{true}.value[1]}
		}
	}

	Stage resolveMainChain(){
		List<Stage> branches=([]<<this)

		while(branches.any{it.tails}){
			branches.each{ it.resolveToBranch() }
			branches=(branches as Iterable<Range>).collectMany{it.split()}
		}
		return (branches as Iterable<Stage>).max{it.existing.size()}
	}
}

static def resumeIt(List<Stage> stages,List resolved){
	stages.each{
		if(!it.tails){
			resolved.add(it)
		}else{
			it.resolveToBranch()
			if(it.tails.size()>1){
				def found=it.split()
				resumeIt(found,resolved)
			}else if(it.tails.size()==0){
				resolved.add(it)
			}
		}
	}
	return resolved
}

static List<Stage> resumeStages(List<Stage> stages){
	def resolved=[]
	resumeIt(stages,resolved)
	resolved
}

static def resolveOnce(Stage initStage){
	initStage.resolveToBranch()
	def initChains=initStage.split()
	def chains=resumeStages(initChains)
	return (chains as Iterable<Stage>).max{it.existing.size()}
}

def origin=[
	l1:['s1', 'j1'],
	l10:['b6', 'j1'],
	l11:['b5', 'b7'],
	l2:['j1', 'b1'],
	l3:['b1', 'b2'],
	l4:['b2', 'j2'],
	l5:['j2', 'b3'],
	l6:['b3', 'b4'],
	l7:['b4', 'j2'],
	l8:['b3', 'b5'],
	l9:['b5', 'b6']]

def initStage=new Stage(origin,[:],origin.findAll{it.key=='l1'})
println initStage.tails

def mainChain=initStage.resolveMainChain()
println mainChain.existing
println mainChain.remaining

println initStage.tails






