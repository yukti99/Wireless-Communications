/* QUESTION: 
Write separate programs to compute the median path loss for outdoor propagation for 
A Free Space, 
B Two-Ray,
C Log Distance PL models

YUKTI KHURANA 
PART-C

This program implements two Models -  Log Distance Path Loss Model and Log Normal Shadow Model to calculate Median Path loss

*/
import java.util.*;
import java.io.*;
import java.lang.*; 

/* The Input has following units by default: distances in feet, frequency in MegaHertz, Antenna Gain in db*/

class LogDistancePL{
    /* DEFAULT VALUES FOR PARAMETERS REQUIRED FOR THE MODEL */
    // speed of light in meter/sec
    public Double c = 299792458.0; 
    public Double PL0 = 0.0; 
    public Double dist = 1.0;
    public Double N = 2.0;
    public Double close_in_dist = 1.0;
    public Double binary_change = 1.0;
   
    //constructor to initialize values
    public LogDistancePL(Double PL0, Double n, Double d0,Double d, Double b){
        this.PL0  = PL0;
        this.N = n;        
        this.close_in_dist = d0;
        this.dist = d;
        this.binary_change = b;
    }
    // function to calculate log distance path loss
    public Double LogDistance_PathLoss(){  
        Double x = this.dist/this.close_in_dist;
        Double y = 10 * this.N * Math.log10(x);
        Double path_loss = this.PL0 + y;
        //System.out.println("n="+this.N+"pl0="+this.PL0+"path_loss="+path_loss+"y="+y);
        return path_loss;
    }
    // function to calculate Log Normal Shadow Path loss
    public Double LogNormalShadow_PathLoss(){  
        Double x = this.dist/this.close_in_dist;
        Double y = 10 * this.N * Math.log10(x);
        Double path_loss = this.PL0 + y + this.binary_change;
        return path_loss;
    }
    public static void main(String[] args){
        System.out.println("The output of the Log Distance model can be seen in File Name - log_dist_output.text....");
        try{
            PrintStream fileOut = new PrintStream("log_dist_output.txt");
            System.setOut(fileOut);
        }catch(Exception e) {
            // in case any exception is thrown, especially while reading the file input
            System.out.println("An error occurred while creating output stream....");
            e.printStackTrace();
        }        
        System.out.println("\n------------------------------LOG DISTANCE PATH LOSS MODELS ---------------------------------------\n");

        // Taking Input from the file for computing median path loss           
        String data="";
        try{
            File f = new File("log_dist_input.txt");
            Scanner reader = new Scanner(f);
            int cnt=0;
            while(reader.hasNextLine()){
                cnt++;
                data = reader.nextLine();
                String[] a = data.split(" ");
                int l =  a.length;
                Double[] d = new Double[l];
                for (int i = 0; i < l; i++) {
                    String num = a[i];
                    d[i] = Double.parseDouble(num);
                }
                // LOG DISTANCE MODEL PATH LOSS CALCULATION
                // file input type = N d0 dist freq trans_grain receiver_gain loss_factor binary_change
                FreeSpaceModel freeSpaceObj = new FreeSpaceModel(d[1],d[3],d[4],d[5],d[6]); 
                Double PL0 = freeSpaceObj.calculate_PathLoss();  
                //converting the value in decibels
                PL0 = 10 * Math.log10(PL0);
                LogDistancePL logDistObj = new LogDistancePL(PL0,d[0],d[1],d[2],d[7]);  
                System.out.println(cnt+". Model parameters: \n");
                System.out.println("N = "+d[0]);
                if (d[0]==2){
                    System.out.println("Environment = Free Space");
                }else if (d[0]<=3.5 && d[0]>=2.7){
                    System.out.println("Environment = Urban Cellular Radio");
                }else if (d[0]<=6 && d[0]>= 4){
                    System.out.println("Environment = Obstructed Building");
                }
                System.out.println("Close-in distance = "+d[1]);
                System.out.println("Distance = "+d[2]+" ft");
                System.out.println("Frequency = "+d[3]+" MHz");
                System.out.println("Transmitter Gain = "+d[4]+" dBi");
                System.out.println("Receiver Gain = "+d[5]+" dBi");
                System.out.println("Loss Factor = "+d[6]);      
                System.out.println("Binary Change factor = "+d[7]);            
                System.out.println("\nLOG-DISTANCE MODEL MEDIAN PATH LOSS in Decibels = "+logDistObj.LogDistance_PathLoss());
                System.out.println("\nLOG-NORMAL SHADOWING MODEL MEDIAN PATH LOSS in Decibels = "+logDistObj.LogNormalShadow_PathLoss());
                System.out.println("\n***************************************************************************************");
            }      
            
        }catch(Exception e) {
            System.out.println("An error occurred while reading file...");
            e.printStackTrace();
        }        
           
    }       
    
}
// Free space model to calculate PL0 required to computer median path loss in log distance models 
class FreeSpaceModel{
    /* DEFAULT VALUES FOR PARAMETERS REQUIRED FOR THE MODEL */
    // speed of light in meter/sec
    public double c = 299792458.0;  
    public Double dist = 1.0;
    public Double trans_gain = 0.0;
    public Double receiver_gain = 0.0;
    public Double freq = 1.0;
    public Double loss_factor = 1.0;

    
    // FREE SPACE MODEL PATH LOSS CALCULATION
    public FreeSpaceModel(Double d, Double f, Double tg, Double rg, Double l){
        this.dist = d;
        this.freq = f;
        this.trans_gain = tg;
        this.receiver_gain = rg;      
        this.loss_factor = l;
    }

    public FreeSpaceModel(Double d, Double f){
        this.dist = d;
        this.freq = f;
    }

    public Double convert_fromDecibel(Double G){
        // given decibel values this code fragment will convert it to corresponding values 
        Double t = G/10.0;
        return Math.pow(10,t);
    }
    public Double wavelength_fromFreq(Double freq){
        // converting megahertz frequency to wavelength in feet
        Double w = 984.0/freq;
        return w;
    }

    public Double calculate_PathLoss(){
        this.trans_gain  = this.convert_fromDecibel(this.trans_gain);
        this.receiver_gain  = this.convert_fromDecibel(this.receiver_gain);
        Double w = wavelength_fromFreq(this.freq);
        Double x = (4 * Math.PI * this.dist)/w;
        x = Math.pow(x,2);       
        Double path_loss = (x*this.loss_factor)/(this.trans_gain*this.receiver_gain);
        return path_loss;
    }
  
    
}








 


