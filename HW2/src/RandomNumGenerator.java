import java.util.Random;
/**referenced text: Introduction to Programming- An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
**/
/** Generate 100 random numbers in the range 0..0.99. **/
public enum RandomNumGenerator{
	Exponential {
		@Override
        public double getRandom(Random r, double p) {
            return -(Math.log(r.nextDouble()) / p);
		}
	};
	
    public double getRandom(double p) throws IllegalArgumentException {        
        return getRandom(defaultR, p);
    }
    public double getRandom(Random r, double p) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
    public static final Random defaultR = new Random();
    
    public static final void main(String[] args){
    //Testing     
        RandomNumGenerator testStat = Exponential;
        double lambda = 4;
        System.out.println("Number of Random Values: 100 with lambda: " + lambda);
        for (int i = 0; i < 100; i++){
        	System.out.println(testStat.getRandom(lambda));
        }                
    } 
}