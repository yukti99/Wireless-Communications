/* QUESTION: 
Write separate programs to compute the median path loss for outdoor propagation for 
A Free Space, 
B Two-Ray,
C Log Distance PL models

YUKTI KHURANA 
2017UCP1234
PART-A

*/
import java.util.*;
import java.io.*;
import java.lang.*; 

/* The Input has following units by default: distances in feet, frequency in Mz, Antenna Gains in dBi*/

class FreeSpaceModel{
    /* DEFAULT VALUES FOR PARAMETERS REQUIRED FOR THE MODEL */
    // speed of light in meter/sec
    public double c = 299792458.0;  
    public Double dist = 1.0;
    public Double trans_gain = 0.0;
    public Double receiver_gain = 0.0;
    public Double freq = 1.0;
    public Double loss_factor = 1.0;

    
    // FREE SPACE MODEL PATH LOSS CALCULATION constructor
    public FreeSpaceModel(Double d, Double f, Double tg, Double rg, Double l){
        this.dist = d;
        this.freq = f;
        this.trans_gain = tg;
        this.receiver_gain = rg;      
        this.loss_factor = l;
    }
   // constructor to be called when loss factor and Antenna Gains are to be assumed as unity
    public FreeSpaceModel(Double d, Double f){
        this.dist = d;
        this.freq = f;
    }
    // function to get ratio from decibel value convert ratio to decibels
    public Double convert_fromDecibel(Double G){
        // given decibel values this code fragment will convert it to corresponding values 
        Double t = G/10.0;
        return Math.pow(10,t);
    }
    // convert distance to feet
    public Double convert_feet(Double dist){
        return (dist*0.3048);
    }
    // function to convert to frequency in MHz to wavelength in ft
    public Double wavelength_fromFreq(Double freq){
        // converting megahertz frequency to wavelength in feet
        Double w = 984.0/freq;
        return w;
    }
    // Function to calculate Path-loss for the Free-space model
    public Double calculate_PathLoss(){
        this.trans_gain  = this.convert_fromDecibel(this.trans_gain);
        this.receiver_gain  = this.convert_fromDecibel(this.receiver_gain);
        Double w = wavelength_fromFreq(this.freq);
        Double x = (4 * Math.PI * this.dist)/w;
        x = Math.pow(x,2);       
        Double path_loss = (x*this.loss_factor)/(this.trans_gain*this.receiver_gain);
        return path_loss;
    }
    public static void main(String[] args){
        System.out.println("The output of the free Space model can be seen in File Name - free_space_output.text....");
        try{
            PrintStream fileOut = new PrintStream("free_space_output.txt");
            System.setOut(fileOut);
        }catch(Exception e) {
            // in case any exception is thrown, especially while reading the file input
            System.out.println("An error occurred while creating output stream....");
            e.printStackTrace();
        }        
        System.out.println("\n------------------------------FREE SPACE MODEL ---------------------------------------\n");

        // Taking Input from the file for computing median path loss            
        String data="";
        try{
            File f = new File("free_space_input.txt");
            //File fout = new File("free_space_output.txt","w");
            //fout.createNewFile();
            Scanner reader = new Scanner(f);
            int cnt=0;
            // Reading file input for model parameters line by line
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
                // FREE SPACE MODEL PATH LOSS CALCULATION
                // file input type = dist freq trans_gain receiver_gain loss_factor
                // E.g. 5000.0 881.52 8.0 0.0 1.0

                // creating the path loss model object
                FreeSpaceModel freeSpaceObj;               
                System.out.println(cnt+". Model parameters: \n");
                if (l==2){
                    freeSpaceObj =  new FreeSpaceModel(d[0],d[1]);
                    System.out.println("Distance = "+d[0]+" ft");
                    System.out.println("Frequency = "+d[1]+" MHz");
                    System.out.println("Transmitter Gain = 1 dBi");
                    System.out.println("Receiver Gain = 1 dBi");
                    System.out.println("Loss Factor = 1");
                    System.out.println("\nMEDIAN PATH LOSS = "+freeSpaceObj.calculate_PathLoss());

                }else{                    
                    freeSpaceObj = new FreeSpaceModel(d[0],d[1],d[2],d[3],d[4]);                    
                    System.out.println("Distance = "+d[0]+" ft");
                    System.out.println("Frequency = "+d[1]+" MHz");
                    System.out.println("Transmitter Gain = "+d[2]+ "dBi");
                    System.out.println("Receiver Gain = "+d[3]+" dBi");
                    System.out.println("Loss Factor = "+d[4]);
                    System.out.println("\nMEDIAN PATH LOSS = "+freeSpaceObj.calculate_PathLoss());
                                  
                }     
                System.out.println("***************************************************************************");
                              
                
            } 
           
            
        }
        catch(Exception e) {
            // in case any exception is thrown, especially while reading the file input
            System.out.println("An error occurred....");
            e.printStackTrace();
        }        
           
    }         
    
    
}








 


