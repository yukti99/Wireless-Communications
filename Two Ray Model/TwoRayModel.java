/* QUESTION: 
Write separate programs to compute the median path loss for outdoor propagation for 
A Free Space, 
B Two-Ray,
C Log Distance PL models

YUKTI KHURANA 
PART-B

*/
import java.util.*;
import java.io.*;
import java.lang.*; 

/* The Input has following units by default: distances in feet, frequency in MegaHertz, Antenna Gain in db*/

class TwoRayModel{
    /* DEFAULT VALUES FOR PARAMETERS REQUIRED FOR THE MODEL */
    // speed of light in meter/sec
    public double c = 299792458.0;  
    public Double dist = 1.0;
    public Double trans_gain = 0.0;
    public Double receiver_gain = 0.0;
    public Double freq = 1.0;
    public Double loss_factor = 1.0;
    public double trans_height;
    public double receiver_height;
    public double direct_dist;
    public double refl_dist;
    // reflective coefficient is -1 by default
    public double refl_coef = -1;

    public TwoRayModel(Double d, Double f, Double tg, Double rg, Double l, Double th, Double rh, Double dd, Double rd, Double rc){
        this.dist = d;
        this.freq = f;
        this.trans_gain = tg;
        this.receiver_gain = rg;        
        this.loss_factor = l;
        this.trans_height = th;
        this.receiver_height = rh;
        this.direct_dist = dd;
        this.refl_dist = rd;
        this.refl_coef = rc;
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
        Double path_loss;
        this.trans_gain  = this.convert_fromDecibel(this.trans_gain);
        this.receiver_gain  = this.convert_fromDecibel(this.receiver_gain);
        Double w = wavelength_fromFreq(this.freq);
        Double d1 = (4 * this.receiver_height * this.trans_height)/w;
        Double d_alpha = 2 * Math.PI * Math.abs(this.direct_dist-this.refl_dist)/w;
        // IF D1 IS << dist, ONLY THEN THIS MODEL CAN BE APPLIED - 
        if (d1 < this.dist || d_alpha <= Math.PI/8){
            Double x = Math.pow(this.dist,2) / ( this.receiver_height*this.trans_height);
            x = Math.pow(x,2);
            path_loss = (x*this.loss_factor)/(this.trans_gain*this.receiver_gain);            
        }else{
            Double cos_alpha = Math.cos(d_alpha);
            path_loss = 1+Math.pow(this.refl_coef,2) - 2*this.refl_coef*cos_alpha;
            path_loss = Math.pow(path_loss,0.5);
        }  
        return path_loss;    
             

    }
    public static void main(String[] args){
        System.out.println("The output of the Two ray model can be seen in File Name - two_ray_output.text....");
        try{
            PrintStream fileOut = new PrintStream("two_ray_output.txt");
            System.setOut(fileOut);
        }catch(Exception e) {
            // in case any exception is thrown, especially while reading the file input
            System.out.println("An error occurred while creating output stream....");
            e.printStackTrace();
        }        
        System.out.println("\n------------------------------TWO RAY PATH LOSS MODEL ---------------------------------------\n");

        // Taking Input from the file for computing median path loss     
        String data="";
        try{
            File f = new File("two_ray_input.txt");
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
                // TWO RAY MODEL FOR GROUND RAY PROPAGATION -> PATH LOSS CALCULATION
                // file input type = dist freq tran_gain receiver_gain loss_factor trans_height receiver_height direct_dist refl_dist refl_coef
                TwoRayModel twoRayObj;
                twoRayObj = new TwoRayModel(d[0],d[1],d[2],d[3],d[4],d[5],d[6],d[7],d[8],d[9]);   
                System.out.println(cnt+". Model parameters: \n");
                System.out.println("Distance = "+d[0]+" ft");
                System.out.println("Frequency = "+d[1]+" MHz");
                System.out.println("Transmitter Gain = "+d[2]+ "dBi");
                System.out.println("Receiver Gain = "+d[3]+" dBi");
                System.out.println("Loss Factor = "+d[4]+" dBi");
                System.out.println("Transmitter Height = "+d[5]+" ft");      
                System.out.println("Receiver Height = "+d[6]+" ft");      
                System.out.println("Direct Distance = "+d[7]+" ft");      
                System.out.println("Reflected Distance = "+d[8]+" ft");    
                System.out.println("Reflection Coefficient = "+d[9]);              
                System.out.println("\nTWO RAY MODEL MEDIAN PATH LOSS = "+twoRayObj.calculate_PathLoss());
                System.out.println("\n***************************************************************************");
            }      
            
        }catch(Exception e) {
            System.out.println("An error occurred....");
            e.printStackTrace();
        }        
           
    }         
    
    
}








 


