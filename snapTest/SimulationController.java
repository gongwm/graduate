package demo;

public class SimulationController{
	private LineData line;
	
	public void calculate(String jsonModel){
		Claculator c=new Calculator(jsonModel);
		
		c.calculate();
		
		line=c.getResult();
	}
	
	public LineData getLine(){
		return line;
	}
}
